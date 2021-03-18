package com.hsrxxx.mapper;

import com.hsrxxx.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author 松仁
 * @since 2021-03-08
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Select("select distinct m.perms from sys_menu m left join sys_role_menu rm on m.id = rm.menu_id left join sys_user_role ur on rm.role_id = ur.role_id left join sys_role r on r.id = ur.role_id where m.status = '0' and ur.user_id = #{userId}")
    List<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据用户查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    @Select("select distinct m.*from sys_menu m left join sys_role_menu rm on m.id = rm.menu_id left join sys_user_role ur on rm.role_id = ur.role_id left join sys_role ro on ur.role_id = ro.id where ur.user_id = #{userId}")
    List<SysMenu> selectMenuListByUserId(Long userId);
}
