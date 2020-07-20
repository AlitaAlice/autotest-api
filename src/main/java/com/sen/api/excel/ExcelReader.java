package com.sen.api.excel;


import com.little.utils.io.excel.Excel;
import com.sen.api.command.domain.DialogCase;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiangfeng@biyouxinli.com.cn
 * @className: ExcelReader
 * @description:
 * @date 2020/2/5
 */
public class ExcelReader {

    private String path;

    public ExcelReader(String path) {
        this.path = path;
    }

    public List<DialogCase> read() throws IOException, InvalidFormatException {
        List<List<String>> lists = Excel.from(new File(path)).readToList(0, false);
        List nullList = new ArrayList();
        nullList.add(null);
        for (int i = 0; i <lists.size() ; i++) {
            lists.get(i).removeAll(nullList);
        }
        //System.out.println(lists.toString());
        List<DialogCase> cases = lists.stream().filter(l -> !l.isEmpty()).map(l -> {
            String caseName = l.get(0);
            l.remove(0);
            DialogCase dialogCase = new DialogCase();
            dialogCase.setCaseName(caseName);
            dialogCase.setProcesses(l);
            return dialogCase;
        }).collect(Collectors.toList());
        return cases;
    }

}
