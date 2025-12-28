package com.stxx.common.utils;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Date;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * QueryWrapper工具类，用于简化时间范围查询条件构建
 *
 * @author wangcc
 */
public final class QueryWrapperUtils {

    private QueryWrapperUtils() {}

    private static final String BEGIN = "beginTime";
    private static final String END   = "endTime";

    /**
     * 普通 QueryWrapper（字段名字符串）
     *
     * @param wrapper QueryWrapper实例
     * @param column 数据库字段名
     * @param params 参数Map，包含beginTime和endTime
     */
    public static <T> void between(
            QueryWrapper<T> wrapper,
            String column,
            Map<String, Object> params
    ) {
        applyBetweenCondition(params, (begin, end) -> {
            if (begin != null) wrapper.ge(column, begin);
            if (end != null) wrapper.lt(column, end);
        });
    }

    /**
     * LambdaQueryWrapper（类型安全）
     *
     * @param wrapper LambdaQueryWrapper实例
     * @param column 字段引用函数
     * @param params 参数Map，包含beginTime和endTime
     */
    public static <T> void between(
            LambdaQueryWrapper<T> wrapper,
            SFunction<T, ?> column,
            Map<String, Object> params
    ) {
        applyBetweenCondition(params, (begin, end) -> {
            if (begin != null) wrapper.ge(column, begin);
            if (end != null) wrapper.lt(column, end);
        });
    }

    /**
     * 通用时间范围查询条件应用方法
     * 使用函数式接口简化重复代码
     *
     * @param params 参数Map
     * @param conditionAdder 条件添加函数，接收(begin, end)两个Date参数
     */
    private static void applyBetweenCondition(Map<String, Object> params, BiConsumer<Date, Date> conditionAdder) {
        if (params == null) {
            return;
        }

        Date begin = parseDateParam(params.get(BEGIN));
        Date end = parseDateParam(params.get(END));

        conditionAdder.accept(begin, end);
    }

    /**
     * 解析日期参数
     *
     * @param param 日期参数
     * @return 解析后的Date对象，如果参数为null则返回null
     */
    private static Date parseDateParam(Object param) {
        return param != null ? DateUtil.parse(String.valueOf(param)) : null;
    }

}
