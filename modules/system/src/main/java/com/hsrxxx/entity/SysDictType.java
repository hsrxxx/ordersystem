package com.hsrxxx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 字典类型表
 * </p>
 *
 * @author 松仁
 * @since 2021-03-21
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class SysDictType implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 字典主键
     */
        @TableId(value = "dict_id", type = IdType.AUTO)
      private Long dictId;

      /**
     * 字典名称
     */
      private String dictName;

      /**
     * 字典类型
     */
      private String dictType;

      /**
     * 状态（0正常 1停用）
     */
      private String status;

      /**
     * 创建者
     */
      private String createBy;

      /**
     * 创建时间
     */
        @TableField(fill = FieldFill.INSERT)
        @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
      private LocalDateTime createTime;

      /**
     * 更新者
     */
      private String updateBy;

      /**
     * 更新时间
     */
        @TableField(fill = FieldFill.INSERT_UPDATE)
        @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
      private LocalDateTime updateTime;

      /**
     * 备注
     */
      private String remark;


}
