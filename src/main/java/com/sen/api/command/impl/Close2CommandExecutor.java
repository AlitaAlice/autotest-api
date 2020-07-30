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
public class Close2CommandExecutor extends AbstractCommandExecutor {
    @Override
    public String getCommand() {
        return CommandConstant.CLOSE2;
    }

    @Override
    public ApiDataBean exec(String command, String input,String verify,String choose) {
        ApiDataBean apiDataBean = new ApiDataBean();
        apiDataBean.setRun(true);
        apiDataBean.setDesc("连小信接口");
        apiDataBean.setUrl("close");
        apiDataBean.setMethod("post");
        String apiParam = "{\n" +
                "    \"token\": \"${token}\",\n" +
                "    \"channel\": \"${channel}\",\n" +
                "\"currentChat\": \"${currentChat}\"\n" +
                "}";
        apiDataBean.setParam(apiParam);

        apiDataBean.setContains(false);
        apiDataBean.setStatus(0);
        /*
           save and verify
        */
        apiDataBean.setVerify("");
        apiDataBean.setSave("");
        apiDataBean.setCmdText(command);
        apiDataBean.setVerifyText(verify);
        apiDataBean.setInputText(input);
//        if (verify==null|verify=="-1") {
//            apiDataBean.setVerify("");
//        }else
//        {
//            apiDataBean.setVerify("$.appdata.dialogs.says.content[0]="+verify.trim());
//        }
//        if (choose == null|choose=="-1") {
//            apiDataBean.setSave("");
//        } else {
//            apiDataBean.setSave("currentChat=$.appdata.chatId;dialogId=$.appdata.dialogs.dialogId[0];result=$.appdata.dialogs.replys[0].itemId[" + choose.trim() + "]");
//        }
        apiDataBean.setPreParam(null);
        apiDataBean.setSleep(2);
        return apiDataBean;
    }
}
