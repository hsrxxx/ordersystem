package com.hsrxxx.feign;

import com.hsrxxx.entity.SysProduct;
import com.hsrxxx.entity.SysType;
import com.hsrxxx.entity.vo.ProductVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: ordersystem
 * @description:
 * @author: hsrxxx
 * @create: 2021-01-20 12:09
 **/
@FeignClient("system")
public interface ProductFeign {

    @GetMapping("/product/index")
    String index();

    @GetMapping("/product/findAll/{index}/{limit}")
    ProductVO findAll(@PathVariable("index") int index, @PathVariable("limit") int limit);

    @GetMapping("/product/findById/{id}")
    SysProduct findById(@PathVariable("id") long id);

    @GetMapping("/product/count")
    int count();

    @DeleteMapping("/product/deleteById/{id}")
    void deleteById(@PathVariable("id") long id);

    @DeleteMapping("/product/deleteBatchIds")
    void deleteBatchIds(@RequestParam(value = "ids") List<Long> ids);

    @PutMapping("/product/updateById")
    void updateById(@RequestBody SysProduct product);

    @PostMapping("/product/save")
    void save(@RequestBody SysProduct product);

    @GetMapping("/type/findAll")
    List<SysType> findAllTypes();
}
