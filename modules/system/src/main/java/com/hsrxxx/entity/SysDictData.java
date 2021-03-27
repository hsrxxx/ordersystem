package com.hsrxxx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.hsrxxx.common.core.annotation.Excel;
import com.hsrxxx.common.core.annotation.Excel.ColumnType;
import com.hsrxxx.web.entity.BaseEntity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 字典数据表
 * </p>
 *
 * @author 松仁
 * @since 2021-03-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysDictData extends BaseEntity {

    private static final long serialVersionUID=1L;

      /**
     * 字典编码
     */
        @TableId(value = "dict_code", type = IdType.AUTO)
        @Excel(name = "字典编码", cellType = ColumnType.NUMERIC)
      private Long dictCode;

      /**
     * 字典排序
     */
      @Excel(name = "字典排序", cellType = ColumnType.NUMERIC)
      private Integer dictSort;

      /**
     * 字典标签
     */
      @Excel(name = "字典标签")
      private String dictLabel;

      /**
     * 字典键值
     */
      @Excel(name = "字典键值")
      private String dictValue;

      /**
     * 字典类型
     */
      @Excel(name = "字典类型")
      private String dictType;

      /**
     * 样式属性（其他样式扩展）
     */
      private String cssClass;

      /**
     * 表格回显样式
     */
      private String listClass;

      /**
     * 是否默认（Y是 N否）
     */
      @Excel(name = "是否默认", readConverterExp = "Y=是,N=否")
      private String isDefault;

      /**
     * 状态（0正常 1停用）
     */
      @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
      private String status;

}
