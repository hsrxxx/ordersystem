package com.hsrxxx.service;

import com.hsrxxx.entity.SysDictData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 字典数据表 服务类
 * </p>
 *
 * @author 松仁
 * @since 2021-03-21
 */
public interface SysDictDataService extends IService<SysDictData> {

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    List<SysDictData> selectDictDataList(SysDictData dictData);

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     * @return 结果
     */
    int deleteDictDataByIds(List<Long> dictCodes);

    /**
     * 新增保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    int insertDictData(SysDictData dictData);

    /**
     * 修改保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    int updateDictData(SysDictData dictData);
}
