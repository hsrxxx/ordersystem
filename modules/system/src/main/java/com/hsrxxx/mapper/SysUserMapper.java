package com.hsrxxx.mapper;

import com.hsrxxx.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 松仁
 * @since 2021-03-08
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 获取用户名是否唯一
     * @param username
     * @return
     */
    @Select("select count(1) from sys_user where username = #{userName} limit 1")
    int checkUserNameUnique(String username);



}
