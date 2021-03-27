package com.hsrxxx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 参数配置表
 * </p>
 *
 * @author 松仁
 * @since 2021-03-27
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class SysConfig implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 参数主键
     */
        @TableId(value = "config_id", type = IdType.AUTO)
      private Integer configId;

      /**
     * 参数名称
     */
      private String configName;

      /**
     * 参数键名
     */
      private String configKey;

      /**
     * 参数键值
     */
      private String configValue;

      /**
     * 系统内置（Y是 N否）
     */
      private String configType;

      /**
     * 创建者
     */
      private String createBy;

      /**
     * 创建时间
     */
        @TableField(fill = FieldFill.INSERT)
      private LocalDateTime createTime;

      /**
     * 更新者
     */
      private String updateBy;

      /**
     * 更新时间
     */
        @TableField(fill = FieldFill.INSERT_UPDATE)
      private LocalDateTime updateTime;

      /**
     * 备注
     */
      private String remark;


}
