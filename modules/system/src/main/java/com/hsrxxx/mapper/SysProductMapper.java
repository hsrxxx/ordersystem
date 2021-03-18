package com.hsrxxx.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hsrxxx.entity.SysProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 松仁
 * @since 2021-03-08
 */
public interface SysProductMapper extends BaseMapper<SysProduct> {
    /**
     * 获取全部产品的详情信息
     *
     * @return List<SysProduct>
     */
    @Select("select p.id, p.name, p.price, p.flavor, p.remark, t.id as tid, t.name as type from sys_product p left join sys_type t on p.tid = t.id where p.del_flag = 0")
    List<SysProduct> selectProductList();

    /**
     * 根据 id 获取产品的详情信息
     * @param id 产品id
     * @return SysProduct
     */
    @Select("select p.id, p.name, p.price, p.flavor, p.remark, t.id as tid, t.name as type from sys_product p left join sys_type t on p.tid = t.id where p.id = #{id} and p.del_flag = 0")
    SysProduct selectProductById(Long id);
}
