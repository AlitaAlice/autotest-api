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
    ApiDataBean exec(String command, String input,String verify,String choose);
}
