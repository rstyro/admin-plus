package com.lrs.common.enums;

public enum LockType {
    PARAM,     // 根据参数锁定
    SYSTEM_USER,      // 根据系统用户锁定
    APP_USER,      // 根据app用户锁定
    IP,        // 根据IP锁定
    SESSION    // 根据会话锁定
}
