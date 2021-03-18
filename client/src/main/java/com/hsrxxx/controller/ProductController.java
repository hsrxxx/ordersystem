package com.hsrxxx.controller;

import com.hsrxxx.entity.SysProduct;
import com.hsrxxx.entity.SysType;
import com.hsrxxx.entity.vo.ProductVO;
import com.hsrxxx.feign.ProductFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: ordersystem
 * @description:
 * @author: hsrxxx
 * @create: 2021-01-20 12:15
 **/
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductFeign productFeign;

    @GetMapping("/index")
    public String index(){
        return productFeign.index();
    }

    @GetMapping("/findAll/{index}/{limit}")
    public ProductVO findAll(@PathVariable("index") int index, @PathVariable("limit") int limit){
        return productFeign.findAll(index, limit);
    }

    @GetMapping("/findById/{id}")
    public SysProduct findById(@PathVariable("id") long id){
        return productFeign.findById(id);
    }

    @GetMapping("/count")
    public int count(){
        return productFeign.count();
    }

    @DeleteMapping("/deleteById/{id}")
    public void deleteById(@PathVariable("id") long id){
        productFeign.deleteById(id);
    }

    @DeleteMapping("/deleteBatchIds")
    public void deleteBatchIds(@RequestParam(value = "ids") List<Long> ids){
        productFeign.deleteBatchIds(ids);
    }

    @PutMapping("/updateById")
    public void updateById(@RequestBody SysProduct product){
        productFeign.updateById(product);
    }

    @PostMapping("/save")
    public void save(@RequestBody SysProduct product){
        productFeign.save(product);
    }

    @GetMapping("/findAllTypes")
    public List<SysType> findAllTypes(){
        return productFeign.findAllTypes();
    }
}
