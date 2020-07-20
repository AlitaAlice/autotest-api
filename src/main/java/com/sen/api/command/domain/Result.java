package com.sen.api.command.domain;

import lombok.Data;

/**
 * @author xiangfeng@biyouxinli.com.cn
 * @className: CommandResult
 * @description: 命令结果
 * @date 2020/2/5
 */
@Data
public class Result {

    public static String SUCCESS = "0";

    public static String FAIL = "1";

    private String resultCode = SUCCESS;

    private String resultMsg;

    /**
     * 当前对话长度
     */
    private Integer msgSize;

    /**
     * @methodName: success
     * @description: 返回成功的方法
     * @author: xiangfeng@biyouxinli.com.cn
     * @date: 2020/2/5
     **/
    public static Result success() {
        return new Result();
    }

    /**
     * @methodName: fail
     * @description: 返回失败的方法
     * @author: xiangfeng@biyouxinli.com.cn
     * @date: 2020/2/5
     **/
    public static Result fail(String message) {
        Result result = new Result();
        result.setResultCode(FAIL);
        result.setResultMsg(message);
        return result;
    }

    /**
     * @methodName: isSuccess
     * @description: 是否成功
     * @author: xiangfeng@biyouxinli.com.cn
     * @date: 2020/2/5
     **/
    public Boolean isSuccess() {
        if (SUCCESS.equals(this.resultCode)) {
            return true;
        } else {
            return false;
        }
    }


}
