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


    @Override
    public List<SysProduct> selectProductList() {
        return productMapper.selectProductList();
    }

    @Override
    public SysProduct selectProductById(Long id) {
        return productMapper.selectProductById(id);
    }
}
