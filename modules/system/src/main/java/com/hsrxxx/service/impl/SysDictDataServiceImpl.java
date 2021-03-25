package com.hsrxxx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hsrxxx.common.core.utils.StringUtils;
import com.hsrxxx.entity.SysDictData;
import com.hsrxxx.mapper.SysDictDataMapper;
import com.hsrxxx.service.SysDictDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hsrxxx.utils.DictUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 字典数据表 服务实现类
 * </p>
 *
 * @author 松仁
 * @since 2021-03-21
 */
@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements SysDictDataService {

    @Autowired
    private SysDictDataMapper dictDataMapper;

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典类型信息
     * @return 字典类型集合信息
     */
    @Override
    public List<SysDictData> selectDictDataList(SysDictData dictData) {
        QueryWrapper<SysDictData> query = new QueryWrapper<>();
        query.like(StringUtils.isNotNull(dictData.getDictType()), "dict_type", dictData.getDictType());
        query.like(StringUtils.isNotNull(dictData.getDictLabel()), "dict_label", dictData.getDictLabel());
        query.like(StringUtils.isNotNull(dictData.getStatus()), "status", dictData.getStatus());
        return dictDataMapper.selectList(query);
    }

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     * @return 结果
     */
    @Override
    public int deleteDictDataByIds(List<Long> dictCodes) {
        int rows = dictDataMapper.deleteBatchIds(dictCodes);
        if (rows > 0){
            DictUtils.clearDictCache();
        }
        return rows;
    }

    /**
     * 新增保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    @Override
    public int insertDictData(SysDictData dictData) {
        int rows = dictDataMapper.insert(dictData);
        if (rows > 0){
            DictUtils.clearDictCache();
        }
        return rows;
    }

    /**
     * 修改保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    @Override
    public int updateDictData(SysDictData dictData) {
        int rows = dictDataMapper.updateById(dictData);
        if (rows > 0){
            DictUtils.clearDictCache();
        }
        return rows;
    }
}
