package com.sen.api.command.impl;

import com.sen.api.beans.ApiDataBean;
import com.sen.api.command.CommandConstant;

/**
 * Title:
 * Description:
 * Company: http://www.biyouxinli.com/
 *
 * @author zhangxl@biyouxinli.com
 * @date Created in 10:21 2020/7/21
 */
public class CloseCommandExecutor extends AbstractCommandExecutor{
    @Override
    public String getCommand() {
        return CommandConstant.CLOSE;
    }

    @Override
    public ApiDataBean exec(String command, String param,String choose) {
        ApiDataBean apiDataBean = new ApiDataBean();
        apiDataBean.setRun(true);
        apiDataBean.setDesc("连小信接口");
        apiDataBean.setUrl("close");
        apiDataBean.setMethod("post");
        String apiParam  = "{\n" +
                "\"channel\": \"02\",\n" +
                "\"token\": \"4DBA46139BBD395B8B846C4FEA320927C5AF505A36CC0EE96683CE53AB0D5F6C03\"\n" +
                "}";
        apiDataBean.setParam(apiParam);

        apiDataBean.setContains(false);
        apiDataBean.setStatus(0);
        /*
           save and verify
        */
        apiDataBean.setVerify("");
        if (choose == null) {
            apiDataBean.setSave("");
        } else {
            apiDataBean.setSave("currentChat=$.appdata.chatId;dialogId=$.appdata.dialogs.dialogId[0];result=$.appdata.dialogs.replys[0].itemId[" + choose.trim() + "]");
        }
        apiDataBean.setPreParam(null);
        apiDataBean.setSleep(0);
        return apiDataBean;
    }
}
