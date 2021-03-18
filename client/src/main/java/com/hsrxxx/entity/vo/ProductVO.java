package com.hsrxxx.entity.vo;

import com.hsrxxx.entity.SysProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: ordersystem
 * @description:
 * @author: hsrxxx
 * @create: 2021-01-22 14:19
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVO {
    private int code;
    private String msg;
    private int count;
    private List<SysProduct> data;
}
