package com.hsrxxx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hsrxxx.common.core.annotation.Excel;
import com.hsrxxx.common.core.annotation.Excel.ColumnType;
import com.hsrxxx.web.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 松仁
 * @since 2021-03-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysRole extends BaseEntity {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.AUTO)
      @Excel(name = "角色序号", cellType = ColumnType.NUMERIC)
      private Long id;

    @Excel(name = "角色名称")
    private String roleName;

    @Excel(name = "角色权限")
    private String roleKey;

    /** 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示） */
    private boolean menuCheckStrictly;

    @Excel(name = "角色状态", readConverterExp = "0=正常,1=停用")
    private String status;

    @TableLogic
    private String delFlag;

    /** 菜单组 */
    @TableField(exist = false)
    private Long[] menuIds;

    public SysRole()
    {

    }

    public SysRole(Long roleId)
    {
        this.id = roleId;
    }

    public boolean isAdmin()
    {
        return isAdmin(this.id);
    }

    public static boolean isAdmin(Long roleId)
    {
        return roleId != null && 1L == roleId;
    }

}
