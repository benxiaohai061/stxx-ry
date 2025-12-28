package com.stxx.generator.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.stxx.generator.service.IGenTableColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stxx.common.core.text.Convert;
import com.stxx.generator.domain.GenTableColumn;
import com.stxx.generator.mapper.GenTableColumnMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * 业务字段 服务层实现
 *
 * @author wangcc
 */
@RequiredArgsConstructor
@Service
public class GenTableColumnServiceImpl extends ServiceImpl<GenTableColumnMapper, GenTableColumn> implements IGenTableColumnService
{
	private final GenTableColumnMapper genTableColumnMapper;

	/**
     * 查询业务字段列表
     *
     * @param tableId 业务字段编号
     * @return 业务字段集合
     */
	@Override
	public List<GenTableColumn> selectGenTableColumnListByTableId(Long tableId)
	{
	    QueryWrapper<GenTableColumn> queryWrapper = new QueryWrapper<>();
	    queryWrapper.eq("table_id", tableId)
	               .orderByAsc("sort");
	    return this.list(queryWrapper);
	}

    /**
     * 新增业务字段
     *
     * @param genTableColumn 业务字段信息
     * @return 结果
     */
	@Override
	public int insertGenTableColumn(GenTableColumn genTableColumn)
	{
	    return this.save(genTableColumn) ? 1 : 0;
	}

	/**
     * 修改业务字段
     *
     * @param genTableColumn 业务字段信息
     * @return 结果
     */
	@Override
	public int updateGenTableColumn(GenTableColumn genTableColumn)
	{
	    return this.updateById(genTableColumn) ? 1 : 0;
	}

	/**
     * 删除业务字段对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Transactional
	@Override
	public int deleteGenTableColumnByIds(String ids)
	{
		return this.removeByIds(Arrays.asList(Convert.toLongArray(ids))) ? 1 : 0;
	}

	/**
	 * 删除业务字段对象
	 *
	 * @param genTableColumns 业务字段集合
	 * @return 结果
	 */
	@Transactional
	@Override
	public int deleteGenTableColumns(List<GenTableColumn> genTableColumns)
	{
		List<Long> ids = genTableColumns.stream().map(GenTableColumn::getColumnId).collect(Collectors.toList());
		return this.removeByIds(ids) ? 1 : 0;
	}
}
