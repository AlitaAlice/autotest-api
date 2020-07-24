package com.sen.api;

import org.testng.TestNG;
import org.testng.collections.Lists;

import java.util.List;

/**
 * Title:
 * Description:
 * Company: http://www.biyouxinli.com/
 *
 * @author zhangxl@biyouxinli.com
 * @date Created in 11:36 2020/7/23
 */
class TestngRun {
    public static void main(String[] args){

        TestNG testng = new TestNG();

        List suites = Lists.newArrayList();

        suites.add("E:\\job\\api_autotest\\testng.xml");//path to xml..
        testng.setTestSuites(suites);

        testng.run();

    }

}