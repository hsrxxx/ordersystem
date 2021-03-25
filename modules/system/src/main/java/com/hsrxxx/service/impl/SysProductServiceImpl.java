package com.hsrxxx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hsrxxx.common.core.utils.StringUtils;
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
    public List<SysProduct> selectProductList(SysProduct product) {
        QueryWrapper<SysProduct> query = new QueryWrapper<>();
        query.like(StringUtils.isNotNull(product.getName()), "p.name", product.getName());
        query.eq(StringUtils.isNotNull(product.getType()), "p.tid", product.getType());
        query.eq("p.del_flag", 0);
        if (!product.getParams().isEmpty()){
            query.between("p.create_time", product.getParams().get("beginTime"), product.getParams().get("endTime"));
        }
        return productMapper.selectProductList(query);
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
