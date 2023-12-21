package com.lrs.common.constant;

/**
 * 这是一个常量的类
 * @author rstyro
 *
 */
public interface Const {

	/**
	 * 头部参数
	 */
	interface HeaderKey{
		/**
		 * 分页参数
		 */
		String PAGE_NO="pageNum";
		String PAGE_SIZE="pageSize";
		/**
		 * 请求追踪ID
		 */
		public static final String TRACKER_ID = "trackerId";
	}

	/**
	 * Session key
	 */
	interface SessionKey{
		/**
		 * 验证码
		 */
		String SESSION_CODE="SESSION_CODE";
		/**
		 * 用户
		 */
		String SESSION_USER="SESSION_USER";

		String SESSION_ALL_MENU="SESSION_ALL_MENU";
		String GLOBAL_SESSION="GLOBAL_SESSION";

	}

	/**
	 * Redis 缓存key
	 */
	interface RedisKey {
		/**
		 * 防重提交 redis key
		 */
		String REPEAT_SUBMIT_KEY = "repeat_submit:";
		/**
		 * 用户登录报错key
		 */
		String USER_ACCOUNT_ERR_KEY = "user:account:err:";

	}


}
