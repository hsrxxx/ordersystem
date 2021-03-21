package com.hsrxxx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hsrxxx.common.core.constant.UserConstants;
import com.hsrxxx.common.core.exception.CustomException;
import com.hsrxxx.common.core.utils.StringUtils;
import com.hsrxxx.entity.SysDictData;
import com.hsrxxx.entity.SysDictType;
import com.hsrxxx.mapper.SysDictDataMapper;
import com.hsrxxx.mapper.SysDictTypeMapper;
import com.hsrxxx.service.SysDictTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hsrxxx.utils.DictUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * <p>
 * 字典类型表 服务实现类
 * 强制初始化 DictUtils 工具类，进行初始化，否则会出现空指针
 * </p>
 *
 * @author 松仁
 * @since 2021-03-21
 */
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements SysDictTypeService {

    @Autowired
    private SysDictTypeMapper dictTypeMapper;

    @Autowired
    private SysDictDataMapper dictDataMapper;

    /**
     * 项目启动时，初始化字典到缓存
     */
    @PostConstruct
    public void init()
    {
        List<SysDictType> dictTypeList = dictTypeMapper.selectList(null);
        QueryWrapper<SysDictData> query = new QueryWrapper<>();
        for (SysDictType dictType : dictTypeList)
        {
            query = new QueryWrapper<>();
            query.eq("dict_type", dictType.getDictType());
            List<SysDictData> dictDatas = dictDataMapper.selectList(query);
            DictUtils.setDictCache(dictType.getDictType(), dictDatas);
        }
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
        if (StringUtils.isNotEmpty(dictDatas))
        {
            return dictDatas;
        }
        QueryWrapper<SysDictData> query = new QueryWrapper<>();
        query.eq("dict_type", dictType);
        dictDatas = dictDataMapper.selectList(query);
        if (StringUtils.isNotEmpty(dictDatas))
        {
            DictUtils.setDictCache(dictType, dictDatas);
            return dictDatas;
        }
        return null;
    }

    /**
     * 批量删除字典信息
     *
     * @param dictIds 需要删除的字典ID
     * @return 结果
     */
    @Override
    public int deleteDictTypeByIds(List<Long> dictIds) {
        for (Long dictId : dictIds)
        {
            SysDictType dictType = getById(dictId);
            QueryWrapper<SysDictData> query = new QueryWrapper<>();
            query.eq("dict_type", dictType.getDictType());
            if (dictDataMapper.selectCount(query) > 0)
            {
                throw new CustomException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
            }
        }
        int count = dictTypeMapper.deleteBatchIds(dictIds);
        if (count > 0)
        {
            DictUtils.clearDictCache();
        }
        return count;
    }

    /**
     * 清空缓存数据
     */
    @Override
    public void clearCache() {
        DictUtils.clearDictCache();
    }

    /**
     * 新增保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    @Override
    public int insertDictType(SysDictType dictType) {
        int row = dictTypeMapper.insert(dictType);
        if (row > 0)
        {
            DictUtils.clearDictCache();
        }
        return row;
    }

    /**
     * 修改保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    @Override
    public int updateDictType(SysDictType dictType) {
        int row = dictTypeMapper.updateById(dictType);
        if (row > 0)
        {
            DictUtils.clearDictCache();
        }
        return row;
    }

    /**
     * 校验字典类型称是否唯一
     *
     * @param dict 字典类型
     * @return 结果
     */
    @Override
    public String checkDictTypeUnique(SysDictType dict)
    {
        Long dictId = StringUtils.isNull(dict.getDictId()) ? -1L : dict.getDictId();
        QueryWrapper<SysDictType> query = new QueryWrapper<>();
        query.eq("dict_type", dict.getDictType());
        SysDictType dictType = dictTypeMapper.selectOne(query);
        if (StringUtils.isNotNull(dictType) && dictType.getDictId().longValue() != dictId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
