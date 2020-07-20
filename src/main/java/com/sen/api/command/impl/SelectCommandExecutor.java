package com.sen.api.command.impl;

import com.sen.api.beans.ApiDataBean;
import com.sen.api.command.Command;
import com.sen.api.command.CommandConstant;

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
    public ApiDataBean exec(String command, String param) {
        return null;
    }

    @Override
    public String getCommand() {
        return CommandConstant.SELECT;
    }
}
