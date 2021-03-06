package com.hsrxxx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hsrxxx.web.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 菜单权限表
 * </p>
 *
 * @author 松仁
 * @since 2021-03-08
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class SysMenu extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
    * 菜单ID
    */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
    * 菜单名称
    */
    private String name;

    /**
    * 父菜单ID
    */
    private Long parentId;

    /**
    * 显示顺序
    */
    private String orderNum;

    /**
    * 路由地址
    */
    private String path;

    /**
    * 组件路径
    */
    private String component;

    /**
    * 是否为外链（0是 1否）
    */
    private String isFrame;

    /**
    * 是否缓存（0缓存 1不缓存）
    */
    private String isCache;

    /**
    * 菜单类型（M目录 C菜单 F按钮）
    */
    private String menuType;

    /**
    * 菜单状态（0显示 1隐藏）
    */
    private String visible;

    /**
    * 菜单状态（0正常 1停用）
    */
    private String status;

    /**
    * 权限标识
    */
    private String perms;

    /**
    * 菜单图标
    */
    private String icon;

    /**
     * 子菜单
     * 非数据库字段需添加 @TableField 注解， 否则 mybatis—puls 会加入该字段进行查询
     * 除非自定义sql语句查询
     */
    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<SysMenu>();

}
