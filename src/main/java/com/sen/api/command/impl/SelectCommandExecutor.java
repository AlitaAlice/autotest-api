package com.sen.api.command.impl;

import com.sen.api.beans.ApiDataBean;
import com.sen.api.command.Command;
import com.sen.api.command.CommandConstant;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Title:
 * Description:
 * Company: http://www.biyouxinli.com/
 *
 * @author zhangxl@biyouxinli.com
 * @date Created in 14:50 2020/7/20
 */
public class SelectCommandExecutor extends AbstractCommandExecutor {
    @Override
    public ApiDataBean exec(String command, String input,String verify,String choose) {
        ApiDataBean apiDataBean = new ApiDataBean();
        apiDataBean.setRun(true);
        apiDataBean.setDesc("连小信接口");
        apiDataBean.setUrl("get");
        apiDataBean.setMethod("post");
        apiDataBean.setCmdText(command);
        apiDataBean.setVerifyText(verify);
        apiDataBean.setInputText(input);
        String apiParam = null;
        long timeMillis = Calendar.getInstance().getTimeInMillis();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        apiParam = "{\n" +
                "    \"token\": \"${token}\",\n" +
                "    \"channel\": \"${channel}\",\n" +
                "    \"currentChat\": \"${currentChat}\",\n" +
                "    \"result\": [\n" +
                "        {\n" +
                "            \"result\": \"${ItemId}\",\n" +
                "            \"replyType\": \"01\",\n" +
                "            \"time\": " +(int) ((Math.random() * 9 + 1) * 10000)+
                ",\n" +
                "            \"dialogId\": \"${dialogId}\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"currentTime\": \"" + dateFormat.format(date)+"\",\n" +
                "    \"timestamp\":" +timeMillis+
                " \n" +
                "}";
//        apiParam = "{\"token\":\"${token}\",\"channel\":\"${channel}\",\"currentChat\":\"${currentChat}\",\"result\":[{\"result\":\"${ItemId}\",\"showText\":\"跳过，直接给解决方案\",\"replyType\":\"01\",\"time\":2143,\"dialogId\":\"${dialogId}\"}],\"currentTime\":\"2020-07-27\n" +
//                "19:10:31\",\"timestamp\":1595848231985}";
        apiDataBean.setParam(apiParam);
        apiDataBean.setContains(false);
        apiDataBean.setStatus(0);
        /*
           save and verify
        */
//        if (verify==null||verify.equals("-1")) {
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
//        if (choose == null|choose=="-1") {
//            apiDataBean.setSave("currentChat=$.appdata.chatId;dialogId=$.appdata.dialogs.dialogId[0];");
//        } else {
//            apiDataBean.setSave("currentChat=$.appdata.chatId;dialogId=$.appdata.dialogs.dialogId[0];result=$.appdata.dialogs.replys[0].itemId[" + choose.trim() + "]");
//        }
        if (choose == null||choose.equals("-1")) {
            apiDataBean.setSave("currentChat=$.appdata.chatId;dialogId=$.appdata.dialogs.dialogId[0];");
        } else {
            apiDataBean.setSave("currentChat=$.appdata.chatId;dialogId=$.appdata.dialogs.dialogId[0];result=$.appdata.dialogs.replys");
            apiDataBean.setChoosetext(choose);
        }
        apiDataBean.setPreParam(null);
        apiDataBean.setSleep(0);
        return apiDataBean;
    }


    @Override
    public String getCommand() {
        return CommandConstant.SELECT;
    }
}
