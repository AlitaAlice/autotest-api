package com.sen.api.command;



import com.sen.api.command.impl.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiangfeng@biyouxinli.com.cn
 * @className: CommandFactory
 * @description: 命令工厂
 * @date 2020/2/5
 */
public class CommandFactory  {

    private static final CommandFactory instance = new CommandFactory();

    private Map<String, Command> commandMap = new HashMap<>();

    public CommandFactory() {
        this.register(CommandConstant.SELECT, new SelectCommandExecutor());
//        this.register(CommandConstant.SLEEP, new SleepCommandExecutor());
//        this.register(CommandConstant.ALL_DIALOG_CONTAIN, new AllDialogContainCommandExecutor());
        this.register(CommandConstant.INPUT, new InputCommandExecutor());
//        this.register(CommandConstant.SLIDE, new SlideCommandExecutor());
//        this.register(CommandConstant.SELECT_DEFINE, new SelectDefineCommandExecutor());
    }

    public static CommandFactory getInstance() {
        return instance;
    }

    /**
     * @methodName: register
     * @description: 注册handler类
     * @author: xiangfeng@yintong.com.cn
     * @date: 2018/6/6
     **/
    public void register(String key, Command handler) {
        commandMap.put(key, handler);
    }

    /**
     * @methodName: get
     * @description: 获取handler类
     * @author: xiangfeng@yintong.com.cn
     * @date: 2018/6/6
     **/
    public Command get(String key) {
        return commandMap.get(key);
    }


}
