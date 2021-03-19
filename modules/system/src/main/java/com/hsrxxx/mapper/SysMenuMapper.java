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
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Select("select distinct m.* from sys_menu m left join sys_role_menu rm on m.id = rm.menu_id left join sys_user_role ur on rm.role_id = ur.role_id left join sys_role ro on ur.role_id = ro.id where ur.user_id = #{userId}")
    List<SysMenu> selectMenuListByUserId(Long userId);

    /**
     * 查询全部菜单
     *
     * @return 菜单列表
     */
    @Select("select distinct m.* from sys_menu m where m.menu_type in ('M', 'C') and m.status = 0 order by m.parent_id, m.order_num")
    List<SysMenu> selectMenuTreeAll();

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Select("select distinct m.* from sys_menu m left join sys_role_menu rm on m.id = rm.menu_id left join sys_user_role ur on rm.role_id = ur.role_id left join sys_role ro on ur.role_id = ro.id left join sys_user u on ur.user_id = u.id where u.id = #{userId} and m.menu_type in ('M', 'C') and m.status = 0  AND ro.status = 0 order by m.parent_id, m.order_num")
    List<SysMenu> selectMenuTreeByUserId(Long userId);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    @Select("select m.id from sys_menu m left join sys_role_menu rm on m.id = rm.menu_id where rm.role_id = #{roleId} order by m.parent_id, m.order_num")
    List<Integer> selectMenuListByRoleId(Long roleId);
}
