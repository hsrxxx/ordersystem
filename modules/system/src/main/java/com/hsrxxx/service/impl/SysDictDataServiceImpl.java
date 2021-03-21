package com.hsrxxx.service.impl;

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
