package com.lrs.common.constant;

/**
 * 这是一个常量的类
 * @author rstyro
 *
 */
public interface Const {

	/**
	 * 分页参数
	 */
	String PAGE_NO="pageNum";
	String PAGE_SIZE="pageSize";
	/**
	 * 请求追踪ID
	 */
	public static final String TRACKER_ID = "trackerId";

	String SESSION_CODE="SESSION_CODE";
	String SESSION_USER="SESSION_USER";
	String SESSION_ALL_MENU="SESSION_ALL_MENU";
	String GLOBAL_SESSION="GLOBAL_SESSION";
	String SESSION_QX="QX";

	String YES="Y";
	String NO="N";


	/**
	 * 防重提交 redis key
	 */
	String REPEAT_SUBMIT_KEY = "repeat_submit:";

}
