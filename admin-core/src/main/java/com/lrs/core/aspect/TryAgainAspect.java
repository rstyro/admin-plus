package com.lrs.core.aspect;

import com.lrs.common.constant.ApiResultEnum;
import com.lrs.common.exception.ApiException;
import com.lrs.common.exception.TryAgainException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

/**
 * 更新失败，尝试重试切片
 * @author  rstyro
 */
@Aspect
@Configuration
public class TryAgainAspect {

	/**
	 * 默认重试几次
	 */
	private static final int    DEFAULT_MAX_RETRIES = 3;

	private int                 maxRetries          = DEFAULT_MAX_RETRIES;
	private int                 order               = 1;

	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}

	public int getOrder() {
		return this.order;
	}

	@Pointcut("@annotation(IsTryAgain)")
	public void retryOnOptFailure() {
		// pointcut mark
	}

	@Around("retryOnOptFailure()")
	@Transactional(rollbackFor = Exception.class)
	public Object doConcurrentOperation(ProceedingJoinPoint pjp) throws Throwable {
		int numAttempts = 0;
		do {
			numAttempts++;
			try {
				//再次执行业务代码
				return pjp.proceed();
			} catch (TryAgainException ex) {
				if (numAttempts > maxRetries) {
					//log failure information, and throw exception
//					如果大于 默认的重试机制 次数，我们这回就真正的抛出去了
					throw new ApiException(ApiResultEnum.ERROR);
				}else{
					//如果 没达到最大的重试次数，将再次执行
					System.out.println("=====正在重试====="+numAttempts+"次");
				}
			}
		} while (numAttempts <= this.maxRetries);

		return null;
	}
}
