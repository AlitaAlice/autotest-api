package com.sen.api.command.domain;

import lombok.Data;

import java.util.List;

/**
 * @author xiangfeng@biyouxinli.com.cn
 * @className: DialogCase
 * @description: 对话测试的一个用户
 * @date 2020/2/5
 */
@Data
public class DialogCase {

    private String caseName;

    private List<String> processes;

}
