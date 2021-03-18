package com.hsrxxx.mapper;

import com.hsrxxx.entity.SysRole;
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
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    @Select("select r.*  from sys_role r left join sys_user_role ur on ur.role_id = r.id left join sys_user u on u.id = ur.user_id WHERE r.del_flag = '0' and ur.user_id = #{userId}")
    List<SysRole> selectRolePermissionByUserId(Long userId);
}
