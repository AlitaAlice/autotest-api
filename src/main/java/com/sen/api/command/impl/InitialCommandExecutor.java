package com.sen.api.command.impl;

import com.sen.api.beans.ApiDataBean;
import com.sen.api.command.CommandConstant;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Title:
 * Description:
 * Company: http://www.biyouxinli.com/
 *
 * @author zhangxl@biyouxinli.com
 * @date Created in 10:17 2020/7/21
 */
public class InitialCommandExecutor  extends AbstractCommandExecutor{
    @Override
    public String getCommand() {
        return CommandConstant.INITIAL;
    }
    @Override
    public ApiDataBean exec(String command, String input,String verify,String choose) {
        ApiDataBean apiDataBean = new ApiDataBean();
        apiDataBean.setRun(true);
        apiDataBean.setDesc("连小信接口");
        apiDataBean.setUrl("get");
        apiDataBean.setMethod("post");
        String apiParam = null;
        long timeMillis = Calendar.getInstance().getTimeInMillis();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        apiParam = "{\n" +
                "    \"token\": \"${token}\",\n" +
                "    \"channel\": \"${channel}\",\n" +
                "    \"currentChat\": null,\n" +
                "    \"currentTime\": \"" + dateFormat.format(date)+"\",\n" +
                "    \"timestamp\":" +timeMillis+
                " \n" +
                "}";

        apiDataBean.setParam(apiParam);
        apiDataBean.setContains(false);
        apiDataBean.setStatus(0);
        apiDataBean.setCmdText(command);
        apiDataBean.setVerifyText(verify);
        apiDataBean.setInputText(input);
        /*
           save and verify
        */
//        if (verify==null||verify=="-1") {
//            apiDataBean.setVerify("");
//        }else
//        {
//            apiDataBean.setVerify("$.appdata.dialogs.says.content[0]="+verify.trim());
//        }
        if (verify==null||verify.equals("-1")) {
            apiDataBean.setVerify("");
        }else
        {
            apiDataBean.setVerify("$..content="+verify.trim());
        }
        if (choose == null||choose=="-1") {
            apiDataBean.setSave("currentChat=$.appdata.chatId;dialogId=$.appdata.dialogs.dialogId[0];");
        } else {
            apiDataBean.setSave("currentChat=$.appdata.chatId;dialogId=$.appdata.dialogs.dialogId[0];result=$.appdata.dialogs.replys[0].itemId[" + choose.trim() + "]");
        }
        apiDataBean.setPreParam(null);
        apiDataBean.setSleep(0);
        return apiDataBean;
    }

}
