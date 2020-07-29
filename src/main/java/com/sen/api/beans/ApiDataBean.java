package com.sen.api.beans;

import lombok.Data;

@Data
public class ApiDataBean extends BaseBean {
	private boolean run;
	private String desc; // 接口描述
	private String url;
	private String method;
	private String param;
	private boolean contains;
	private int status;
	private String verify;
	private String save;
	private String preParam;
	private String cmdText;
	private String inputText;
	private String verifyText;
	private String choosetext;
	private int sleep;
	public boolean isRun() {
		return run;
	}

}
