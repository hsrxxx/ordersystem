package com.hsrxxx.service;

import com.hsrxxx.entity.SysProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 松仁
 * @since 2021-03-08
 */
public interface SysProductService extends IService<SysProduct> {

    /**
     * 获取全部产品的详情信息
     *
     * @return List<SysProduct>
     */
    List<SysProduct> selectProductList();

    /**
     * 根据 id 获取产品的详情信息
     * @param id 产品id
     * @return SysProduct
     */
    SysProduct selectProductById(Long id);
}
