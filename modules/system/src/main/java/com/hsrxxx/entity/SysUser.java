package com.hsrxxx.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hsrxxx.web.entity.BaseEntity;
import com.hsrxxx.enums.SexEnum;
import com.hsrxxx.enums.StatusEnum;
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

      @TableId(value = "id", type = IdType.AUTO)
      private Long id;

    private String username;

    private String password;

    private String nickname;

    private SexEnum sex;

    private String email;

    private String telephone;

    private String address;

    private String loginIp;

    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginTime;

    private StatusEnum status;

    @TableLogic
    private String delFlag;

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
