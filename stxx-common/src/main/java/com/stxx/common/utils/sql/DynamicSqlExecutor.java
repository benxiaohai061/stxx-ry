package com.stxx.common.utils.sql;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 动态SQL执行器
 * 支持动态切换数据源和执行任意MyBatis动态SQL
 *
 * @author wangcc
 */
@Slf4j
@Component
public class DynamicSqlExecutor {

    private final SqlSessionFactory sqlSessionFactory;

    protected MapperBuilderAssistant builderAssistant;

    private static final String DEFAULT_DS = "master";

    public DynamicSqlExecutor(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.builderAssistant = new MapperBuilderAssistant(sqlSessionFactory.getConfiguration(),"");
    }

    // 缓存MappedStatement ID，避免重复创建
    private final Set<String> statementIdCache = new CopyOnWriteArraySet<>();

    /**
     * 执行查询，返回List<Map<String, Object>>
     * 默认使用master数据源
     *
     * @param sql 动态SQL语句（MyBatis XML格式）
     * @param parameter 参数Map
     * @return 查询结果
     */
    public List<Map<String, Object>> queryForList(String sql, Map<String, Object> parameter) {
        return queryForList(sql, parameter, DEFAULT_DS);
    }

    /**
     * 执行查询，返回List<Map<String, Object>>，支持指定数据源
     *
     * @param sql 动态SQL语句（MyBatis XML格式）
     * @param parameter 参数Map
     * @param dataSource 数据源名称
     * @return 查询结果
     */
    public List<Map<String, Object>> queryForList(String sql, Map<String, Object> parameter, String dataSource) {
        if (sql == null || sql.trim().isEmpty()) {
            throw new IllegalArgumentException("SQL语句不能为空");
        }

        // 切换数据源
        String previousDataSource = DynamicDataSourceContextHolder.peek();
        try {
            DynamicDataSourceContextHolder.push(dataSource);

            try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
                // 获取或创建MappedStatement ID
                String statementId = getOrCreateStatementId(sqlSession.getConfiguration(), sql, SqlCommandType.SELECT);
                // 执行查询
                List<Map<String, Object>> result = sqlSession.selectList(statementId, parameter != null ? parameter : new HashMap<>());

                log.debug("执行动态SQL成功，数据源：{}，SQL：{}，结果条数：{}", dataSource, sql.trim(), result.size());
                return result;

            } catch (Exception e) {
                log.error("执行动态SQL失败，数据源：{}，SQL：{}，参数：{}", dataSource, sql, parameter, e);
                throw new RuntimeException("执行动态SQL失败: " + e.getMessage(), e);
            }

        } finally {
            // 恢复之前的数据源
            if (previousDataSource != null) {
                DynamicDataSourceContextHolder.push(previousDataSource);
            } else {
                DynamicDataSourceContextHolder.clear();
            }
        }
    }

    /**
     * 执行查询，返回单条记录 Map<String, Object>
     *
     * @param sql 动态SQL语句（MyBatis XML格式）
     * @param parameter 参数Map
     * @return 查询结果
     */
    public Map<String, Object> queryForMap(String sql, Map<String, Object> parameter) {
        return queryForMap(sql, parameter, DEFAULT_DS);
    }

    /**
     * 执行查询，返回单条记录 Map<String, Object>，支持指定数据源
     *
     * @param sql 动态SQL语句（MyBatis XML格式）
     * @param parameter 参数Map
     * @param dataSource 数据源名称
     * @return 查询结果
     */
    public Map<String, Object> queryForMap(String sql, Map<String, Object> parameter, String dataSource) {
        List<Map<String, Object>> results = queryForList(sql, parameter, dataSource);
        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * 执行更新操作
     *
     * @param sql 动态SQL语句（MyBatis XML格式）
     * @param parameter 参数Map
     * @return 影响的行数
     */
    public int executeUpdate(String sql, Map<String, Object> parameter) {
        return executeUpdate(sql, parameter, DEFAULT_DS);
    }

    /**
     * 执行更新操作，支持指定数据源
     *
     * @param sql 动态SQL语句（MyBatis XML格式）
     * @param parameter 参数Map
     * @param dataSource 数据源名称
     * @return 影响的行数
     */
    public int executeUpdate(String sql, Map<String, Object> parameter, String dataSource) {
        if (sql == null || sql.trim().isEmpty()) {
            throw new IllegalArgumentException("SQL语句不能为空");
        }

        // 切换数据源
        String previousDataSource = DynamicDataSourceContextHolder.peek();
        try {
            DynamicDataSourceContextHolder.push(dataSource);

            try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
                // 获取或创建MappedStatement ID
                String statementId = getOrCreateStatementId(sqlSession.getConfiguration(), sql, SqlCommandType.UPDATE);

                // 执行更新
                int result = sqlSession.update(statementId, parameter != null ? parameter : new HashMap<>());

                // 提交事务
                sqlSession.commit();

                log.debug("执行动态SQL更新成功，数据源：{}，SQL：{}，影响行数：{}", dataSource, sql.trim(), result);
                return result;

            } catch (Exception e) {
                log.error("执行动态SQL更新失败，数据源：{}，SQL：{}，参数：{}", dataSource, sql, parameter, e);
                throw new RuntimeException("执行动态SQL更新失败: " + e.getMessage(), e);
            }

        } finally {
            // 恢复之前的数据源
            if (previousDataSource != null) {
                DynamicDataSourceContextHolder.push(previousDataSource);
            } else {
                DynamicDataSourceContextHolder.clear();
            }
        }
    }


    /**
     * 创建动态SQL源
     */
    private SqlSource createDynamicSqlSource(Configuration configuration, String sql) {
        try {
            // 检查SQL是否已经包含<script>标签
            String trimmedSql = sql.trim();
            String xmlSql;

            if (trimmedSql.startsWith("<script>") && trimmedSql.endsWith("</script>")) {
                // 如果已经包含<script>标签，直接使用
                xmlSql = trimmedSql;
            } else {
                // 否则包装为完整的XML格式
                xmlSql = "<script>" + trimmedSql + "</script>";
            }

            // 使用XML语言驱动来解析动态SQL
            XMLLanguageDriver languageDriver = new XMLLanguageDriver();
            return languageDriver.createSqlSource(configuration, xmlSql, Map.class);

        } catch (Exception e) {
            log.warn("动态SQL解析失败，尝试作为静态SQL处理：{}", e.getMessage());
            // 如果动态SQL解析失败，回退到静态SQL（移除<script>标签）
            String staticSql = sql.replaceAll("<script>|</script>", "").trim();
            return new SqlSourceBuilder(configuration).parse(staticSql, Map.class, new HashMap<>());
        }
    }

    /**
     * 获取或创建MappedStatement ID
     */
    private String getOrCreateStatementId(Configuration configuration, String sql, SqlCommandType sqlCommandType) {
        // 生成标准化的StatementId，使用下划线替换点号，避免MyBatis命名冲突
        String statementId = "com_stxx__dynamicSqlByDefault_" + DigestUtils.md5Hex(sql.trim().getBytes(StandardCharsets.UTF_8));

        // 首先检查缓存
        if (statementIdCache.contains(statementId)) {
            return statementId;
        }

        // 检查Configuration是否已存在该Statement
        if (configuration.hasStatement(statementId, false)) {
            // 存入缓存
            statementIdCache.add(statementId);
            return statementId;
        }

        // 缓存中不存在，创建新的MappedStatement
        synchronized (this) {
            // 双重检查
            if (statementIdCache.contains(statementId) || configuration.hasStatement(statementId, false)) {
                return statementId;
            }

            MappedStatement mappedStatement = createMappedStatement(configuration, statementId, sql, sqlCommandType);
            // 存入缓存
            statementIdCache.add(statementId);

            log.debug("创建新的MappedStatement: {}", statementId);
            return statementId;
        }
    }


    /**
     * 创建MappedStatement (参考MyBatis Plus实现优化)
     */
    private MappedStatement createMappedStatement(Configuration configuration, String statementId, String sql, SqlCommandType sqlCommandType) {
        // 创建动态SQL源
        SqlSource sqlSource = createDynamicSqlSource(configuration, sql);

        // 参考MyBatis Plus的参数设置
        boolean isSelect = sqlCommandType == SqlCommandType.SELECT;
        Class<?> resultType = isSelect ? Map.class : Integer.class;

        // 调用addMappedStatement并返回创建的MappedStatement
        return builderAssistant.addMappedStatement(
            statementId,           // id
            sqlSource,             // sqlSource
            StatementType.PREPARED, // statementType
            sqlCommandType,        // sqlCommandType
            null,                  // fetchSize
            null,                  // timeout
            null,                  // parameterMap
            Map.class,            // parameterType (parameter类型)
            null,                  // resultMap
            resultType,           // resultType
            null,                  // resultSetType
            !isSelect,            // flushCache (非SELECT操作需要flush)
            isSelect,             // useCache (SELECT操作使用缓存)
            false,                 // resultOrdered
            NoKeyGenerator.INSTANCE, // keyGenerator
            null,                  // keyProperty
            null,                  // keyColumn
            configuration.getDatabaseId(), // databaseId
            configuration.getDefaultScriptingLanguageInstance(), // lang
            null                   // resultSets
        );
    }
}
