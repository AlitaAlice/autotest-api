package com.sen.api.command.impl;

import com.sen.api.beans.ApiDataBean;
import com.sen.api.command.Command;
import com.sen.api.command.CommandConstant;
import com.sen.api.command.domain.Result;

/**
 * Title:
 * Description:
 * Company: http://www.biyouxinli.com/
 *
 * @author zhangxl@biyouxinli.com
 * @date Created in 14:47 2020/7/20
 */
public class InputCommandExecutor extends AbstractCommandExecutor {


    @Override
    public ApiDataBean exec(String command, String param) {
        ApiDataBean apiDataBean = new ApiDataBean();
        apiDataBean.setRun(true);
        apiDataBean.setDesc("连小信接口");
        apiDataBean.setUrl("get");
        apiDataBean.setMethod("post");
        String apiParam = null;
        apiParam = "{\n" +
                "   \"token\": \"4DBA46139BBD395B8B846C4FEA320927C5AF505A36CC0EE96683CE53AB0D5F6C03\",\n" +
                "    \"channel\": \"02\",\n" +
                "    \"currentChat\": \"${currentChat}\",\n" +
                "    \"result\": [\n" +
                "        {\n" +
                "            \"result\":" +param +
                "            \"showText\":" +param +
                "            \"replyType\": \"02\",\n" +
                "            \"time\": 10250,\n" +
                "            \"dialogId\": \"${dialogId}\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"currentTime\": \"2020-07-18 15:31:16\",\n" +
                "    \"timestamp\": 1595057476775\n" +
                "}";
        apiDataBean.setContains(false);
        apiDataBean.setStatus(0);
        /*
           save and verify
        */
        apiDataBean.setVerify("");
        apiDataBean.setSave("");
        apiDataBean.setPreParam(null);
        apiDataBean.setSleep(0);
        return apiDataBean;
    }

    @Override
    public String getCommand() {
        return  CommandConstant.INPUT;
    }
}
