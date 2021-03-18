package com.hsrxxx.entity.vo;

import com.hsrxxx.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @program: ordersystem
 * @description: 用户角色整合VO
 * @author: hsrxxx
 * @create: 2021-03-10 09:33
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleVO {

    private Long id;

    private String username;

    private String password;

    private StatusEnum status;

    private String roleKey;

}
