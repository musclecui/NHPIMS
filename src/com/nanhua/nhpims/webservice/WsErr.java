/*
 *  web service 错误
 */

package com.nanhua.nhpims.webservice;

public class WsErr {
	// 错误代码、错误信息
	public String errCode = "";
	public String errMsg = "";
	
	// 常用错误定义
	// 无错误
	public static final String ERR_NO = "0";
	// 连接异常
	public static final String ERR_CODE_CNT = "1";
	public static final String ERR_MSG_CNT = "连接异常：请检查网络配置、WiFi、服务器";
}
