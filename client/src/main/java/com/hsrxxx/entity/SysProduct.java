package com.hsrxxx.entity;

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
public class SysProduct implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer id;

    private String name;

    private Double price;

    private String flavor;

    private Integer tid;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    private String remark;

    private String delFlag;

    // 菜品分类
    // 表示该属性不为数据库表字段，但又是必须使用的。
    private String type;

}
