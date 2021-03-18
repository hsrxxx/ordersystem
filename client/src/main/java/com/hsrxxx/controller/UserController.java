package com.hsrxxx.controller;

import com.hsrxxx.entity.SysUser;
import com.hsrxxx.feign.UserFeign;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserFeign userFeign;

    @GetMapping("/findAll/{index}/{limit}")
    public List<SysUser> findAll(@PathVariable("index") int index, @PathVariable("limit") int limit){
        return userFeign.findAll(index, limit);
    }

    @GetMapping("/findById/{id}")
    public SysUser findById(@PathVariable("id") long id){
        return userFeign.findById(id);
    }

    @GetMapping("/count")
    public int count(){
        return userFeign.count();
    }

    @DeleteMapping("/deleteById/{id}")
    public void deleteById(@PathVariable("id") long id){
        userFeign.deleteById(id);
    }

    @DeleteMapping("/deleteBatchIds")
    public void deleteBatchIds(@RequestParam(value = "ids") List<Long> ids){
        userFeign.deleteBatchIds(ids);
    }

    @PutMapping("/updateById")
    public void updateById(@RequestBody SysUser user){
        userFeign.updateById(user);
    }

    @PostMapping("/save")
    public void save(@RequestBody SysUser user){
        userFeign.save(user);
    }
}
