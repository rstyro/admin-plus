package com.lrs.common.exception;

import com.lrs.common.constant.ApiResultEnum;

/**
 * 更新重试异常
 */
public class TryAgainException extends ApiException {

    public TryAgainException(ApiResultEnum apiResultEnum) {
        super(apiResultEnum);
    }

}
