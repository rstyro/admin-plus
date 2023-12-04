package com.lrs.core.system.dto;

import lombok.Data;

import java.util.List;

@Data
public class SysRoleDto extends  BaseDto{
    private Long userId;
    private List<Integer> roleIdList;

}
