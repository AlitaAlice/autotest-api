package com.sen.api.utils;

import org.testng.Reporter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReportUtil {
	private static String reportName = "自动化测试报告";

	private static String splitTimeAndMsg = " ";
	public static void log(String msg) {
		long timeMillis = Calendar.getInstance().getTimeInMillis();
		Date date = new Date();
		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
//		Reporter.log(timeMillis + splitTimeAndMsg + msg, true);
		Reporter.log(dateFormat.format(date) + splitTimeAndMsg + msg, true);
	}

	public static String getReportName() {
		return reportName;
	}

	public static String getSpiltTimeAndMsg() {
		return splitTimeAndMsg;
	}

	public static void setReportName(String reportName) {
		if(StringUtil.isNotEmpty(reportName)){
			ReportUtil.reportName = reportName;
		}
	}
}

