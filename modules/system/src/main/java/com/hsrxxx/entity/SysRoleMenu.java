package com.hsrxxx.entity;

import com.hsrxxx.web.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色和菜单关联表
 * </p>
 *
 * @author 松仁
 * @since 2021-03-08
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class SysRoleMenu {

    private static final long serialVersionUID=1L;

      /**
     * 角色ID
     */
        private Long roleId;

      /**
     * 菜单ID
     */
      private Long menuId;


}
