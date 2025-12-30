package com.stxx.web.controller.tool;

import com.stxx.common.core.controller.BaseController;
import com.stxx.common.core.domain.R;
import com.stxx.common.utils.sql.DynamicSqlExecutor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态SQL测试控制器
 *
 * @author wangcc
 */
@Tag(name = "动态SQL测试")
@RestController
@RequestMapping("/test/dynamic-sql")
@RequiredArgsConstructor
public class DynamicSqlController extends BaseController {

    private final DynamicSqlExecutor sqlExecutor;

    /**
     * 执行动态查询
     */
    @Operation(summary = "执行动态查询")
    @PostMapping("/query")
    public R<List<Map<String, Object>>> query(
            @Parameter(description = "SQL语句") @RequestParam String sql,
            @Parameter(description = "数据源") @RequestParam(defaultValue = "master") String dataSource,
            @Parameter(description = "参数") @RequestBody(required = false) Map<String, Object> parameters) {
        try {
            List<Map<String, Object>> results = sqlExecutor.queryForList(sql, parameters, dataSource);
            return R.ok(results);
        } catch (Exception e) {
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    /**
     * 执行动态更新
     */
    @Operation(summary = "执行动态更新")
    @PostMapping("/update")
    public R<Integer> update(
            @Parameter(description = "SQL语句") @RequestParam String sql,
            @Parameter(description = "数据源") @RequestParam(defaultValue = "master") String dataSource,
            @Parameter(description = "参数") @RequestBody(required = false) Map<String, Object> parameters) {
        try {
            int affectedRows = sqlExecutor.executeUpdate(sql, parameters, dataSource);
            return R.ok(affectedRows);
        } catch (Exception e) {
            return R.fail("更新失败: " + e.getMessage());
        }
    }

    /**
     * 查询用户列表（示例）
     */
    @Operation(summary = "查询用户列表示例")
    @GetMapping("/users")
    public R<List<Map<String, Object>>> getUsers(
            @Parameter(description = "用户名") @RequestParam(required = false) String userName,
            @Parameter(description = "状态") @RequestParam(required = false) String status,
            @Parameter(description = "数据源") @RequestParam(defaultValue = "master") String dataSource) {

        String sql = "<script>" +
                "SELECT user_id, user_name, nick_name, email, status, create_time " +
                "FROM sys_user " +
                "<where> " +
                "<if test=\"userName != null and userName != ''\"> " +
                "AND user_name LIKE CONCAT('%', #{userName}, '%') " +
                "</if> " +
                "<if test=\"status != null and status != ''\"> " +
                "AND status = #{status} " +
                "</if> " +
                "</where> " +
                "ORDER BY create_time DESC " +
                "LIMIT 10 " +
                "</script>";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userName", userName);
        parameters.put("status", status);

        try {
            List<Map<String, Object>> users = sqlExecutor.queryForList(sql,
                    parameters, dataSource);
            return R.ok(users);
        } catch (Exception e) {
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    /**
     * 测试数据源切换
     */
    @Operation(summary = "测试数据源切换")
    @GetMapping("/datasource-test")
    public R<String> testDataSource(
            @Parameter(description = "数据源") @RequestParam(defaultValue = "master") String dataSource) {

        String sql = "SELECT COUNT(*) as count FROM sys_user";
        try {
            Map<String, Object> result = sqlExecutor.queryForMap(sql, null, dataSource);
            return R.ok("数据源 " + dataSource + " 测试成功，数据条数: " + result.get("count"));
        } catch (Exception e) {
            return R.fail("数据源 " + dataSource + " 测试失败: " + e.getMessage());
        }
    }
}
