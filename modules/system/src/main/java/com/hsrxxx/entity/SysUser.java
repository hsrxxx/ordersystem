package com.hsrxxx.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hsrxxx.common.core.annotation.Excel;
import com.hsrxxx.common.core.annotation.Excel.Type;
import com.hsrxxx.common.core.annotation.Excel.ColumnType;
import com.hsrxxx.web.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author 松仁
 * @since 2021-01-28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUser extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 用户ID
     */
    @Excel(name = "用户序号", cellType = ColumnType.NUMERIC, prompt = "用户编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户账号
     */
    @Excel(name = "登录名称")
    private String username;

    /**
     * 用户昵称
     */
    @Excel(name = "用户名称")
    private String nickname;

    /**
     * 用户性别
     */
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    /**
     * 用户邮箱
     */
    @Excel(name = "用户邮箱")
    private String email;

    /**
     * 用户手机
     */
    @Excel(name = "手机号码")
    private String telephone;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户地址
     */
    @Excel(name = "用户地址")
    private String address;

    /**
     * 用户最后登陆ip
     */
    @Excel(name = "最后登录IP", type = Type.EXPORT)
    private String loginIp;

    /**
     * 用户最后登陆时间
     */
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginTime;

    /**
     * 菜单状态（0正常 1停用）
     */
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 是否删除标识（0存在 2删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 用户角色组
     */
    @TableField(exist = false)
    private Long[] roleIds;

    public SysUser(Long id) {
        this.id = id;
    }

    public boolean isAdmin()
    {
        return isAdmin(this.id);
    }

    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }
}
