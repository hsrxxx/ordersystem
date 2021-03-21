package com.hsrxxx.service.impl;

import com.hsrxxx.entity.SysProduct;
import com.hsrxxx.mapper.SysProductMapper;
import com.hsrxxx.service.SysProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 松仁
 * @since 2021-03-08
 */
@Service
public class SysProductServiceImpl extends ServiceImpl<SysProductMapper, SysProduct> implements SysProductService {

    @Autowired
    SysProductMapper productMapper;


    /**
     * 获取全部产品的详情信息
     *
     * @return List<SysProduct>
     */
    @Override
    public List<SysProduct> selectProductList() {
        return productMapper.selectProductList();
    }

    /**
     * 根据 id 获取产品的详情信息
     * @param id 产品id
     * @return SysProduct
     */
    @Override
    public SysProduct selectProductById(Long id) {
        return productMapper.selectProductById(id);
    }
}
