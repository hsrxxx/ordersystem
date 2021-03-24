package com.hsrxxx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class SysType implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Long id;

    private String name;

    private String createBy;

      @TableField(fill = FieldFill.INSERT)
      @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
      private LocalDateTime createTime;

    private String updateBy;

      @TableField(fill = FieldFill.INSERT_UPDATE)
      @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
      private LocalDateTime updateTime;

    private String remark;

    @TableLogic
    private String delFlag;


}
