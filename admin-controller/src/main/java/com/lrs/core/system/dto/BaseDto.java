package com.lrs.core.system.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 基本通用参数
 */
@Accessors(chain = true)
@Data
public class BaseDto {
    private String keyword;

    private List<Long> ids;
}
