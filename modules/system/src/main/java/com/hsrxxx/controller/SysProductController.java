package com.hsrxxx.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hsrxxx.common.core.utils.StringUtils;
import com.hsrxxx.common.core.web.controller.BaseController;
import com.hsrxxx.common.core.web.domain.AjaxResult;
import com.hsrxxx.common.core.web.page.TableDataInfo;
import com.hsrxxx.common.security.annotation.PreAuthorize;
import com.hsrxxx.entity.SysProduct;
import com.hsrxxx.service.SysProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 松仁
 * @since 2021-01-19
 */
@RestController
@RequestMapping("/product")
public class SysProductController extends BaseController {

    @Autowired
    private SysProductService productService;

    /**
     * 获取产品列表
     */
    @PreAuthorize(hasPermi = "system:product:list")
    @GetMapping("/list")
    public TableDataInfo list() {
        startPage();
        List<SysProduct> list = productService.selectProductList();
        return getDataTable(list);
    }

    /**
     * 根据产品编号获取详细信息
     */
    @PreAuthorize(hasPermi = "system:product:query")
    @GetMapping("/{productId}")
    public AjaxResult query(@PathVariable("productId") Long productId){
        // 请求成功
        AjaxResult ajax =AjaxResult.success();
        ajax.put(AjaxResult.DATA_TAG, productService.selectProductById(productId));
        return ajax;
    }

    /**
     * 新增产品
     */
    @PreAuthorize(hasAnyPermi = {"system:product:add", "system:product:edit"})
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysProduct product){
        // 判断产品名称是否重复
        QueryWrapper<SysProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", product.getName());
        if (StringUtils.isNotEmpty(product.getName())
                && StringUtils.isNotNull(productService.getOne(queryWrapper))){
            return AjaxResult.error("新增产品'" + product.getName() + "'失败，该产品已存在");
        }
        return toAjax(productService.save(product));
    }

    /**
     * 修改产品
     */
    @PreAuthorize(hasAnyPermi = {"system:product:add", "system:product:edit"})
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysProduct product){
        // 检查用户是否允许操作（如是超级管理员则禁止操作）
        // 判断产品名称是否重复
        QueryWrapper<SysProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", product.getName());
        SysProduct sysProduct = productService.getOne(queryWrapper);
        if (StringUtils.isNotEmpty(product.getName())
                && StringUtils.isNotNull(sysProduct)){
            if (!sysProduct.getId().equals(product.getId())){
                return AjaxResult.error("修改产品'" + product.getName() + "'失败，该产品已存在");
            }
        }
        return toAjax(productService.updateById(product));
    }

    /**
     * 删除产品
     */
    @PreAuthorize(hasPermi = "system:product:remove")
    @DeleteMapping
    public AjaxResult remove(@RequestParam("productIds") List<Long> productIds){
        return toAjax(productService.removeByIds(productIds));
    }
}