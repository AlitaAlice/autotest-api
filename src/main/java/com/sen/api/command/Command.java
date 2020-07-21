package com.sen.api.command;


import com.sen.api.beans.ApiDataBean;
import com.sen.api.command.domain.Result;

/**
 * @author xiangfeng@biyouxinli.com.cn
 * @className: Command
 * @description: 命令接口
 * @date 2020/2/5
 */
public interface Command {
//
//    Result exec(String command, String param);

    //ApiDataBean exec(String command, String param);

    ApiDataBean exec(String command, String param,String choose);
}
