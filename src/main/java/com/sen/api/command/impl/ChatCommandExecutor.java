package com.sen.api.command.impl;

import com.sen.api.beans.ApiDataBean;
import com.sen.api.command.CommandConstant;

/**
 * Title:
 * Description:
 * Company: http://www.biyouxinli.com/
 *
 * @author zhangxl@biyouxinli.com
 * @date Created in 14:31 2020/7/21
 */
public class ChatCommandExecutor extends AbstractCommandExecutor {
    @Override
    public String getCommand() {
        return CommandConstant.CHAT;
    }

    @Override
    public ApiDataBean exec(String command, String input,String choose,String verify) {
        ApiDataBean apiDataBean = new ApiDataBean();
        apiDataBean.setRun(true);
        apiDataBean.setDesc("连小信接口");
        apiDataBean.setUrl("get");
        apiDataBean.setMethod("post");
        String apiParam = "{\n" +
                "   \"token\": \"4DBA46139BBD395B8B846C4FEA320927C5AF505A36CC0EE96683CE53AB0D5F6C03\",\n" +
                "    \"channel\": \"02\",\n" +
                "    \"currentChat\": \"${currentChat}\"\n" +
                "}";
        apiDataBean.setParam(apiParam);
        apiDataBean.setContains(false);
        apiDataBean.setStatus(0);
        /*
           save and verify
        */
        if (verify == null | verify == "-1") {
            apiDataBean.setVerify("");
        } else {
            apiDataBean.setVerify("$.appdata.dialogs.says.content[0]=" + verify.trim());
        }
        if (choose == null|choose=="-1") {
            apiDataBean.setSave("currentChat=$.appdata.chatId;dialogId=$.appdata.dialogs.dialogId[0];");
        } else {
            apiDataBean.setSave("currentChat=$.appdata.chatId;dialogId=$.appdata.dialogs.dialogId[0];result=$.appdata.dialogs.replys");
            apiDataBean.setChoosetext(choose);
        }
        apiDataBean.setPreParam(null);
        apiDataBean.setSleep(0);
        return apiDataBean;
    }

}
