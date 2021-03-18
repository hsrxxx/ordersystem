package com.hsrxxx.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户和角色关联表
 * </p>
 *
 * @author 松仁
 * @since 2021-03-08
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class SysUserRole implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 用户ID
     */
        private Long userId;

      /**
     * 角色ID
     */
      private Long roleId;


}
