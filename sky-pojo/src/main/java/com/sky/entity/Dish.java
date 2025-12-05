package com.sky.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Dish implements Serializable {

    private Long id;

    // 菜品名称
    private String name;

    // 分类id
    private Long categoryId;

    // 价格
    private BigDecimal price;

    // 菜品编码
    private String code;

    // 图片
    private String image;

    // 描述信息
    private String description;

    // 状态 1 启售 0 停售
    private Integer status;

    private Integer sort;

    // 创建时间
    private LocalDateTime createTime;

    // 修改时间
    private LocalDateTime updateTime;

    // 创建人id
    private Long createUser;

    // 修改人id
    private Long updateUser;
}
