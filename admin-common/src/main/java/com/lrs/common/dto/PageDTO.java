package com.lrs.common.dto;

import lombok.Data;

@Data
public class PageDTO {
    /**
     * 当前页
     */
     public Integer page;
    /**
     * 每页显示的大小
     */
    public Integer pageSize;
}
