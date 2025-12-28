package com.stxx.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stxx.common.constant.UserConstants;
import com.stxx.common.core.domain.entity.SysDictData;
import com.stxx.common.core.domain.entity.SysDictType;
import com.stxx.common.exception.ServiceException;
import com.stxx.common.utils.DictUtils;
import com.stxx.common.utils.StringUtils;
import com.stxx.system.mapper.SysDictTypeMapper;
import com.stxx.system.service.ISysDictDataService;
import com.stxx.system.service.ISysDictTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典 业务层处理
 *
 * @author ruoyi
 */
@RequiredArgsConstructor
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements ISysDictTypeService {

    private final ISysDictDataService sysDictDataService;

    /**
     * 项目启动时，初始化字典到缓存
     */
    @PostConstruct
    public void init() {
        loadingDictCache();
    }

    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    @Override
    public List<SysDictType> selectDictTypeList(SysDictType dictType) {
        QueryWrapper<SysDictType> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(dictType.getDictName()), "dict_name", dictType.getDictName())
                    .eq(StringUtils.isNotEmpty(dictType.getStatus()), "status", dictType.getStatus())
                    .like(StringUtils.isNotEmpty(dictType.getDictType()), "dict_type", dictType.getDictType());

        // 时间范围查询
        if (dictType.getParams() != null) {
            Object beginTime = dictType.getParams().get("beginTime");
            Object endTime = dictType.getParams().get("endTime");
            queryWrapper.ge(beginTime != null && StringUtils.isNotEmpty(beginTime.toString()), "create_time", beginTime)
                        .le(endTime != null && StringUtils.isNotEmpty(endTime.toString()), "create_time", endTime);
        }

        return this.list(queryWrapper);
    }

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    @Override
    public List<SysDictType> selectDictTypeAll() {
        return this.list();
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {
        List<SysDictData> dictDatas = DictUtils.getDictCache(dictType);
        if (StringUtils.isNotEmpty(dictDatas)) {
            return dictDatas;
        }
        dictDatas = sysDictDataService.selectDictDataByType(dictType);
        if (StringUtils.isNotEmpty(dictDatas)) {
            DictUtils.setDictCache(dictType, dictDatas);
        }
        return dictDatas;
    }

    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    @Override
    public SysDictType selectDictTypeById(Long dictId) {
        return this.getById(dictId);
    }

    /**
     * 根据字典类型查询信息
     *
     * @param dictType 字典类型
     * @return 字典类型
     */
    @Override
    public SysDictType selectDictTypeByType(String dictType) {
        QueryWrapper<SysDictType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_type", dictType);
        return this.getOne(queryWrapper);
    }

    /**
     * 批量删除字典类型信息
     *
     * @param dictIds 需要删除的字典ID
     */
    @Override
    public void deleteDictTypeByIds(Long[] dictIds) {
        // 预检查：确保没有字典数据在使用这些字典类型
        for (Long dictId : dictIds) {
            SysDictType dictType = this.getById(dictId);
            if (dictType != null && sysDictDataService.countDictDataByType(dictType.getDictType()) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
            }
        }

        // 批量删除
        this.removeByIds(Arrays.asList(dictIds));

        // 清理缓存
        for (Long dictId : dictIds) {
            SysDictType dictType = this.getById(dictId);
            if (dictType != null) {
                DictUtils.removeDictCache(dictType.getDictType());
            }
        }
    }

    /**
     * 加载字典缓存数据
     */
    @Override
    public void loadingDictCache() {
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "0");

        sysDictDataService.list(queryWrapper).stream()
                .collect(Collectors.groupingBy(SysDictData::getDictType))
                .forEach((dictType, dictDataList) -> {
                    List<SysDictData> sortedList = dictDataList.stream()
                            .sorted(Comparator.comparing(SysDictData::getDictSort))
                            .collect(Collectors.toList());
                    DictUtils.setDictCache(dictType, sortedList);
                });
    }

    /**
     * 清空字典缓存数据
     */
    @Override
    public void clearDictCache() {
        DictUtils.clearDictCache();
    }

    /**
     * 重置字典缓存数据
     */
    @Override
    public void resetDictCache() {
        clearDictCache();
        loadingDictCache();
    }

    /**
     * 新增保存字典类型信息
     *
     * @param dict 字典类型信息
     * @return 结果
     */
    @Override
    public int insertDictType(SysDictType dict) {
        boolean result = this.save(dict);
        if (result) {
            DictUtils.setDictCache(dict.getDictType(), null);
        }
        return result ? 1 : 0;
    }

    /**
     * 修改保存字典类型信息
     *
     * @param dict 字典类型信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateDictType(SysDictType dict) {
        SysDictType oldDict = this.getById(dict.getDictId());
        sysDictDataService.updateDictDataType(oldDict.getDictType(), dict.getDictType());
        boolean result = this.updateById(dict);
        if (result) {
            List<SysDictData> dictDatas = sysDictDataService.selectDictDataByType(dict.getDictType());
            DictUtils.setDictCache(dict.getDictType(), dictDatas);
        }
        return result ? 1 : 0;
    }

    /**
     * 校验字典类型称是否唯一
     *
     * @param dict 字典类型
     * @return 结果
     */
    @Override
    public boolean checkDictTypeUnique(SysDictType dict) {
        Long dictId = dict.getDictId() != null ? dict.getDictId() : -1L;
        QueryWrapper<SysDictType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_type", dict.getDictType());
        SysDictType dictType = this.getOne(queryWrapper);
        if (dictType != null && !dictType.getDictId().equals(dictId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
