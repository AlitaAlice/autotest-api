package com.sen.api.utils;



import com.alibaba.fastjson.JSONObject;
import com.sen.api.command.domain.Reply;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Title:
 * Description:  response->itemId    拿到itemId
 * Company: http://www.biyouxinli.com/
 *
 * @author zhangxl@biyouxinli.com
 * @date Created in 10:00 2020/7/22
 */
public class MatchUtil {


    /**
     * @Author zhangxl
     * @Description //TODO
     * @Date 11:03 2020/7/22
     * @Param [str  ResultStr, resultContent 用户选择的Str]
     * @return java.lang.String
     **/
    public static String  getItemId(String ResultStr, String chooseStr) {
//        String jsontext = trimString(ResultStr, "[","]");
        List<Reply> replyList = new ArrayList<>();

//        replyList = JSONObject.parseArray(jsontext, Reply.class);
        replyList = JSONObject.parseArray(ResultStr, Reply.class);
        String ItemId = null;
        for (int i = 0; i <replyList.size() ; i++) {
            if(replyList.get(i).getContent().contains(chooseStr))
            {
                ItemId = replyList.get(i).getItemId();
                break;
            }
        }
        return ItemId;
    }

    public void a()
    {}


    /**
     * <h5>功能:去除字符串开头结尾的指定字符串</h5>
     *
     * @param str 要处理的字符串
     * @param delStr 指定要去除的字符串
     * @return
     */
    public static String trimString(String str, String delStr,String endStr){
        int delStrLength = delStr.length();
        int endStrLength = endStr.length();
        if (str.startsWith(delStr)) {
            str = str.substring(delStrLength);
        }
        if (str.endsWith(endStr)) {
            str = str.substring(0,str.length() - endStrLength);
        }
        return str;
    }
}
