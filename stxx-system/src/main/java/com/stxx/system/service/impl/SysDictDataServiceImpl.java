package com.stxx.system.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stxx.common.core.domain.entity.SysDictData;
import com.stxx.common.utils.DictUtils;
import com.stxx.system.mapper.SysDictDataMapper;
import com.stxx.system.service.ISysDictDataService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 字典 业务层处理
 *
 * @author wangcc
 */
@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements ISysDictDataService {

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> selectDictDataList(SysDictData dictData) {
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(dictData.getDictType() != null && !dictData.getDictType().isEmpty(), "dict_type", dictData.getDictType())
                    .like(dictData.getDictLabel() != null && !dictData.getDictLabel().isEmpty(), "dict_label", dictData.getDictLabel())
                    .eq(dictData.getStatus() != null && !dictData.getStatus().isEmpty(), "status", dictData.getStatus())
                    .orderByAsc("dict_sort");
        return this.list(queryWrapper);
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType 字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue) {
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_type", dictType)
                    .eq("dict_value", dictValue)
                    .select("dict_label");
        SysDictData dictData = this.getOne(queryWrapper);
        return dictData != null ? dictData.getDictLabel() : null;
    }

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Override
    public SysDictData selectDictDataById(Long dictCode) {
        return this.getById(dictCode);
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    public List<SysDictData> selectDictDataByType(String dictType) {
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "0")
                    .eq("dict_type", dictType)
                    .orderByAsc("dict_sort");
        return this.list(queryWrapper);
    }

    /**
     * 查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据数量
     */
    public int countDictDataByType(String dictType) {
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_type", dictType);
        return (int) this.count(queryWrapper);
    }

    /**
     * 同步修改字典类型
     *
     * @param oldDictType 旧字典类型
     * @param newDictType 新字典类型
     * @return 结果
     */
    public boolean updateDictDataType(String oldDictType, String newDictType) {
        SysDictData updateData = new SysDictData();
        updateData.setDictType(newDictType);
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_type", oldDictType);
        return this.update(updateData, queryWrapper);
    }

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */
    @Transactional
    @Override
    public void deleteDictDataByIds(Long[] dictCodes) {
        // 批量删除前先获取所有要删除的数据，用于更新缓存
        List<SysDictData> dictDataList = Arrays.stream(dictCodes)
                .map(this::getById)
                .collect(Collectors.toList());

        // 批量删除
        this.removeByIds(Arrays.asList(dictCodes));

        // 更新缓存（按字典类型分组更新）
        dictDataList.stream()
                .collect(Collectors.groupingBy(SysDictData::getDictType))
                .forEach((dictType, list) -> {
                    List<SysDictData> remainingData = selectDictDataByType(dictType);
                    DictUtils.setDictCache(dictType, remainingData);
                });
    }

    /**
     * 新增保存字典数据信息
     *
     * @param data 字典数据信息
     * @return 结果
     */
    @Override
    public int insertDictData(SysDictData data) {
        boolean result = this.save(data);
        if (result) {
            List<SysDictData> dictDatas = selectDictDataByType(data.getDictType());
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
        return result ? 1 : 0;
    }

    /**
     * 修改保存字典数据信息
     *
     * @param data 字典数据信息
     * @return 结果
     */
    @Override
    public int updateDictData(SysDictData data) {
        boolean result = this.updateById(data);
        if (result) {
            List<SysDictData> dictDatas = selectDictDataByType(data.getDictType());
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
        return result ? 1 : 0;
    }
}
