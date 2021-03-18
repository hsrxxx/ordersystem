package com.hsrxxx.feign;

import com.hsrxxx.entity.SysUser;
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
public interface UserFeign {

    @GetMapping("/user/findAll/{index}/{limit}")
    List<SysUser> findAll(@PathVariable("index") int index, @PathVariable("limit") int limit);

    @GetMapping("/user/findById/{id}")
    SysUser findById(@PathVariable("id") long id);

    @GetMapping("/user/count")
    int count();

    @DeleteMapping("/user/deleteById/{id}")
    void deleteById(@PathVariable("id") long id);

    @DeleteMapping("/user/deleteBatchIds")
    void deleteBatchIds(@RequestParam(value = "ids") List<Long> ids);

    @PutMapping("/user/updateById")
    void updateById(@RequestBody SysUser user);

    @PostMapping("/user/save")
    void save(@RequestBody SysUser user);

}
