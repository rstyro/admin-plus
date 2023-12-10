package com.lrs.core.system.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginDto {
    /**
     * 账号与密码
     */
    private String username;
    private String password;
    /**
     * 验证码
     */
    private String code;

    private boolean rememberMe;

}
