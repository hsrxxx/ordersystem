package com.hsrxxx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
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
 * 字典类型表
 * </p>
 *
 * @author 松仁
 * @since 2021-03-21
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class SysDictType extends BaseEntity {

    private static final long serialVersionUID=1L;

      /**
     * 字典主键
     */
        @TableId(value = "dict_id", type = IdType.AUTO)
        @Excel(name = "字典主键", cellType = ColumnType.NUMERIC)
      private Long dictId;

      /**
     * 字典名称
     */
      @Excel(name = "字典名称")
      private String dictName;

      /**
     * 字典类型
     */
      @Excel(name = "字典类型")
      private String dictType;

      /**
     * 状态（0正常 1停用）
     */
      @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
      private String status;

}
