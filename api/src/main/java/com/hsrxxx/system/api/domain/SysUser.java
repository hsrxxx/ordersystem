package com.hsrxxx.system.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.hsrxxx.system.api.enums.SexEnum;
import com.hsrxxx.system.api.enums.StatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class SysUser implements Serializable {

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

    private LocalDateTime loginTime;

    private StatusEnum status;

    @TableLogic
    private String delFlag;

}
