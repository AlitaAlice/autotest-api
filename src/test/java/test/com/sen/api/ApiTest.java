package test.com.sen.api;

import com.alibaba.fastjson.JSON;
import com.sen.api.beans.ApiDataBean;
import com.sen.api.command.Command;
import com.sen.api.command.CommandFactory;
import com.sen.api.command.domain.DialogCase;
import com.sen.api.configs.ApiConfig;
import com.sen.api.excel.ExcelReader;
import com.sen.api.excepions.ErrorRespStatusException;
import com.sen.api.listeners.AutoTestListener;
import com.sen.api.listeners.RetryListener;
import com.sen.api.utils.*;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.dom4j.DocumentException;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;
import org.testng.annotations.Optional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;

@Listeners({AutoTestListener.class, RetryListener.class})
public class ApiTest extends TestBase {

    /**
     * api请求跟路径
     */
    private static String rootUrl;

    /**
     * 跟路径是否以‘/’结尾
     */
    private static boolean rooUrlEndWithSlash = false;

    /**
     * 所有公共header，会在发送请求的时候添加到http header上
     */
    private static Header[] publicHeaders;

    /**
     * 是否使用form-data传参 会在post与put方法封装请求参数用到
     */
    private static boolean requestByFormData = false;

    /**
     * 配置
     */
    private static ApiConfig apiConfig;
    public static int CaseNumber;

    public static int ProNumber;
    /**
     * 所有api测试用例数据
     */
    protected List<ApiDataBean> dataList = new ArrayList<ApiDataBean>();

    protected List<DialogCase> dialogCaseList = new ArrayList<>();

    protected List<ApiDataBean> dataBean = new ArrayList<>();
    protected List<ApiDataBean> dataBean2 = new ArrayList<>();
    protected List<ApiDataBean> dataBean3 = new ArrayList<>();

    private static HttpClient client;

    static volatile boolean cleanedUp = false;
    static final Object lock = new Object();

    /**
     * 初始化测试数据
     *
     * @throws Exception
     */
    @Parameters("envName")
    @BeforeSuite
    public void init(@Optional("api-config.xml") String envName) throws Exception {
        String configFilePath = Paths.get(System.getProperty("user.dir"), envName).toString();
        ReportUtil.log("api config path:" + configFilePath);
        apiConfig = new ApiConfig(configFilePath);
        // 获取基础数据
        rootUrl = apiConfig.getRootUrl();
        rooUrlEndWithSlash = rootUrl.endsWith("/");

        // 读取 param，并将值保存到公共数据map
        Map<String, String> params = apiConfig.getParams();
        setSaveDates(params);

        List<Header> headers = new ArrayList<Header>();
        apiConfig.getHeaders().forEach((key, value) -> {
            Header header = new BasicHeader(key, value);
            if (!requestByFormData && key.equalsIgnoreCase("content-type") && value.toLowerCase().contains("form-data")) {
                requestByFormData = true;
            }
            headers.add(header);
        });
        publicHeaders = headers.toArray(new Header[headers.size()]);
        client = new SSLClient();
        client.getParams().setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT, 60000); // 请求超时
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000); // 读取超时
    }

//	@Parameters({ "excelPath", "sheetName" })
//	@BeforeTest
//	public void readData(@Optional("case/api-data.xls") String excelPath, @Optional("Sheet1") String sheetName) throws DocumentException, IOException, InvalidFormatException {
//
//		// 获得所有的dataList
//		dataList = readExcelData(ApiDataBean.class, excelPath.split(";"),
//				sheetName.split(";"));
//
//	}


    @AfterClass
    public static void tearDown() {
        synchronized (lock) {
            if (cleanedUp) return;
            // do clean up
            cleanedUp = true;
        }
    }

    /**
     * @return void
     * @Author zhangxl
     * @Description //TODO  转换数据 DialogCase 2 dataList  先搞定input 生成一个 Iterator
     * @Date 14:26 2020/7/20
     * @Param []
     **/
    @BeforeTest
    public List<ApiDataBean> transferData() throws IOException, InvalidFormatException {

        String path = StringUtils.isEmpty(System.getProperty("excelPath")) ? "E://最终版4.xlsx" : System.getProperty("excelPath");
        dialogCaseList = new ExcelReader(path).read();
        //System.out.println(dialogCaseList.toString());
        //ReportUtil.log(dialogCaseList.toString());


        for (int i = 0; i < dialogCaseList.size(); i++) {
            DialogCase dialogCase = dialogCaseList.get(i);
            //dialogCase.getCaseName();
            for (int j = 0; j < dialogCase.getProcesses().size(); j++) {
                String[] exs = dialogCase.getProcesses().get(j).split(":");
                Command command = CommandFactory.getInstance().get(exs[0]);
                if (null == command) {
                    throw new RuntimeException("未找到【" + exs[0] + "】操作");
                }
                if (exs[0].equals("chat")) {
                    // cmd choose
                    if (exs.length == 2) {
                        ApiDataBean apiDataBean = command.exec(exs[0], null, null, exs[1]);
                        dataBean.add(apiDataBean);
                    }

                    //cmd verify choose
                    if (exs.length == 3) {
                        ApiDataBean apiDataBean = command.exec(exs[0], null, exs[1], exs[2]);
                        dataBean.add(apiDataBean);
                    }
                } else if (exs[0].equals("select")) {

                    //cmd choose
                    if (exs.length == 2) {
                        ApiDataBean apiDataBean = command.exec(exs[0], null, null, exs[1]);
                        dataBean.add(apiDataBean);
                    }
                    // cmd verify choose
                    if (exs.length == 3) {
                        ApiDataBean apiDataBean = command.exec(exs[0], null, exs[1], exs[2]);
                        dataBean.add(apiDataBean);
                    }

                }
//				else if (exs[0].equals("input")||exs[0].equals("slide")) {
//
//					ApiDataBean apiDataBean_select = command.exec("select", null, null, null);
//					dataBean.add(apiDataBean_select);
//					//cmd choose
//					if (exs.length == 2) {
//						ApiDataBean apiDataBean = command.exec(exs[0], null, null, exs[1]);
//						dataBean.add(apiDataBean);
//					}
//					// cmd verify choose
//					if (exs.length == 3) {
//						ApiDataBean apiDataBean = command.exec(exs[0], null, exs[1], exs[2]);
//						dataBean.add(apiDataBean);
//					}
//				}
                else {
                    if (exs.length == 3) {
//                        ApiDataBean apiDataBean_select = command.exec("select", null, null, null);
//                        dataBean.add(apiDataBean_select);
                        //cmd  :input : verify
                        ApiDataBean apiDataBean = command.exec(exs[0], exs[1], exs[2], null);
                        dataBean.add(apiDataBean);
                    } else if (exs.length == 2) {
                        //cmd input
                        ApiDataBean apiDataBean = command.exec(exs[0], exs[1], null, null);
                        dataBean.add(apiDataBean);
                    } else if (exs.length == 1) {
                        //cmd
                        ApiDataBean apiDataBean = command.exec(exs[0], null, null, null);
                        dataBean.add(apiDataBean);
                    } else if (exs.length == 4) {
                        //cmd input  verify  choose
                        ApiDataBean apiDataBean = command.exec(exs[0], exs[1], exs[2], exs[3]);
                        dataBean.add(apiDataBean);
                    }
                }
            }
        }

        dataBean2.addAll(dataBean);
//        int j = 0;
//        for (int i = 0; i < dataBean.size() ; i++) {
//            if (dataBean.get(i).getCmdText().equals("input")||dataBean.get(i).getCmdText().equals("slide")) {
//                if (StringUtils.isNotBlank(dataBean.get(i).getChoosetext())) {
//                    Command command2 = CommandFactory.getInstance().get("select");
//                    ApiDataBean apiDataBean_select = command2.exec("select", null, null, "-1");
//                    dataBean2.add(j+i,apiDataBean_select);
//                    j++;
//                }
//                continue;
//            }
//        }
//        dataBean2.remove(2);
//        Command command2 = CommandFactory.getInstance().get("select");
//        ApiDataBean apiDataBean_select = command2.exec("select", null, null, "-1");
//        dataBean2.add(dataBean2.size(),apiDataBean_select);

//        dataBean3.addAll(dataBean2)
        int j=0;
        for (int i = 0; i <dataBean.size() ; i++) {
            if (dataBean.get(i).getCmdText().equals("select") && (dataBean.get(i + 1).getCmdText().equals("input")||dataBean.get(i + 1).getCmdText().equals("slide"))) {
                Command command2 = CommandFactory.getInstance().get("select");
                ApiDataBean apiDataBean_select = command2.exec("select", null, null, "-1");
                dataBean2.add(j+i,apiDataBean_select);
                j++;
            }
            else if ((dataBean.get(i).getCmdText().equals("input")||dataBean.get(i).getCmdText().equals("slide"))&& (StringUtils.isNotBlank(dataBean.get(i).getChoosetext())) &&(dataBean.get(i+1).getCmdText().equals("input")||dataBean.get(i+1).getCmdText().equals("slide")))
           {
                Command command2 = CommandFactory.getInstance().get("select");
                ApiDataBean apiDataBean_select = command2.exec("select", null, null, "-1");
                dataBean2.add(i,apiDataBean_select);
           }
        }
//        System.out.println(dataBean2);
        return null;
    }


//	/**
//	 * 过滤数据，run标记为Y的执行。  获得apiData
//	 *apiDatas
//	 * @return  返回
//	 * @throws DocumentException
//	 */
//	@DataProvider(name = "apiDatas")
//	public Iterator<Object[]> getApiData(ITestContext context)
//			throws DocumentException {
//		List<Object[]> dataProvider = new ArrayList<Object[]>();
//		for (ApiDataBean data : dataList) {
//			if (data.isRun()) {
//				dataProvider.add(new Object[] { data });
//			}
//		}
//		return dataProvider.iterator();  //返回该集合的迭代器
//	}

    /**
     * 过滤数据，run标记为Y的执行。  获得apiData
     * apiDatas
     *
     * @return 返回
     * @throws DocumentException
     */
    @DataProvider(name = "apiDatas2")
    public Iterator<Object[]> getApiData2(ITestContext context)
            throws DocumentException {
        List<Object[]> dataProvider = new ArrayList<Object[]>();
        for (ApiDataBean data : dataBean) {
            if (data.isRun()) {
                dataProvider.add(new Object[]{data});
            }
        }
        return dataProvider.iterator();  //返回该集合的迭代器
    }

    // 遍历迭代器  判断是否有内容，有内容就取走  获得close的apidatabean
    @Test(dataProvider = "apiDatas2")
    public void apiTest(ApiDataBean apiDataBean) throws Exception {
        String responseData = getResponseData(apiDataBean);
        // 对返回结果进行提取保存。
        saveResult(responseData, apiDataBean.getSave());
        // 验证预期信息。
        //getItemId存储
        saveItemId(responseData, apiDataBean.getSave(), apiDataBean.getChoosetext());

        Thread.sleep(500);
        /**
         * 当ItemID为空时，再进行一次save操作，并进行校验
         */
        if (StringUtils.isBlank(ItemIdValue)) {
            String responseData2 = getResponseData(apiDataBean);
            // 对返回结果进行提取保存。
            saveResult(responseData2, apiDataBean.getSave());
            // 验证预期信息。
            //getItemId存储
            saveItemId(responseData2, apiDataBean.getSave(), apiDataBean.getChoosetext());
            verifyResult(responseData2, apiDataBean.getVerify(),
                    apiDataBean.isContains());
        }
        else{
            /**
             *  直接进行校验
             */
            verifyResult(responseData, apiDataBean.getVerify(),
                    apiDataBean.isContains());
        }
    }

    /**
     * 获得ResponseData
     * @param apiDataBean
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    private String getResponseData(ApiDataBean apiDataBean) throws InterruptedException, IOException {
        ReportUtil.log("--- 自动化测试开始 ---");
        String correnturl = parseUrl(apiDataBean.getUrl());
        if (correnturl.equals("http://cbt.shangjin618.com/lianxin-botserver/dialog/close")) {
            CaseNumber++;
        }

        if (correnturl.equals("http://cbt.shangjin618.com/lianxin-botserver/dialog/get")) {
            ReportUtil.log("当前Case为: Case" + CaseNumber);
            //ReportUtil.log("当前用例为: 用例" + (ProNumber + 3));
            ProNumber++;
        }
        ReportUtil.log(String.format("cmd:%s,input:%s,verify:%s,choose:%s", apiDataBean.getCmdText(), apiDataBean.getInputText(), apiDataBean.getVerifyText(), apiDataBean.getChoosetext()));
        if (apiDataBean.getSleep() > 0) {
            // sleep休眠时间大于0的情况下进行暂停休眠
            ReportUtil.log(String.format("sleep %s seconds",
                    apiDataBean.getSleep()));
            Thread.sleep(apiDataBean.getSleep() * 1000);
        }
//		ReportUtil.log("用例选择为: "+apiDataBean.getChoosetext());
        //获得param
        String apiParam = buildRequestParam(apiDataBean);


        // 封装请求方法 获得method
        HttpUriRequest method = parseHttpRequest(apiDataBean.getUrl(),
                apiDataBean.getMethod(), apiParam);
        //定义响应Data
        String responseData;
        try {
            // 执行 获得response
            HttpResponse response = client.execute(method);
            int responseStatus = response.getStatusLine().getStatusCode();
            ReportUtil.log("返回状态码：" + responseStatus);
            if (apiDataBean.getStatus() != 0) {
                Assert.assertEquals(responseStatus, apiDataBean.getStatus(),
                        "返回状态码与预期不符合!");
            }
            //获得respEntity
            HttpEntity respEntity = response.getEntity();
            Header respContentType = response.getFirstHeader("Content-Type");


            if (respContentType != null && respContentType.getValue() != null
                    && (respContentType.getValue().contains("download") || respContentType.getValue().contains("octet-stream"))) {
                String conDisposition = response.getFirstHeader(
                        "Content-disposition").getValue();
                String fileType = conDisposition.substring(
                        conDisposition.lastIndexOf("."),
                        conDisposition.length());
                String filePath = "download/" + RandomUtil.getRandom(8, false)
                        + fileType;
                InputStream is = response.getEntity().getContent();
                Assert.assertTrue(FileUtil.writeFile(is, filePath), "下载文件失败。");
                // 将下载文件的路径放到{"filePath":"xxxxx"}进行返回
                responseData = "{\"filePath\":\"" + filePath + "\"}";
            } else {
//				responseData = DecodeUtil.decodeUnicode(EntityUtils
//						.toString(respEntity));

                //responseData即是response请求param
                responseData = EntityUtils.toString(respEntity, "UTF-8");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            method.abort();
        }
        // 输出返回数据log
        ReportUtil.log("小信响应为:" + responseData);
        return responseData;
    }

    private String buildRequestParam(ApiDataBean apiDataBean) {
        // 分析处理预参数 （函数生成的参数）
        String preParam = buildParam(apiDataBean.getPreParam());
        savePreParam(preParam);// 保存预存参数 用于后面接口参数中使用和接口返回验证中
        // 处理参数
        String apiParam = buildParam(apiDataBean.getParam());
        return apiParam;
    }

    /**
     * 封装请求方法
     *
     * @param url    请求路径
     * @param method 请求方法
     * @param param  请求参数
     * @return 请求方法
     * @throws UnsupportedEncodingException
     */
    private HttpUriRequest parseHttpRequest(String url, String method, String param) throws UnsupportedEncodingException {
        // 处理url
        url = parseUrl(url);
//		ReportUtil.log("请求方式:" + method);
        ReportUtil.log("请求链接:" + url);
        //ReportUtil.log("请求数据为:" + param.replace("\r\n", "").replace("\n", ""));
        ReportUtil.log("请求数据为:" + param.replace("\r\n", ""));
        //upload表示上传，也是使用post进行请求
        if ("post".equalsIgnoreCase(method) || "upload".equalsIgnoreCase(method)) {
            // 封装post方法
            HttpPost postMethod = new HttpPost(url);
            postMethod.setHeaders(publicHeaders);
            //如果请求头的content-type的值包含form-data 或者 请求方法为upload(上传)时采用MultipartEntity形式
            HttpEntity entity = parseEntity(param, requestByFormData || "upload".equalsIgnoreCase(method));
            postMethod.setEntity(entity);
            return postMethod;
        } else if ("put".equalsIgnoreCase(method)) {
            // 封装put方法
            HttpPut putMethod = new HttpPut(url);
            putMethod.setHeaders(publicHeaders);
            HttpEntity entity = parseEntity(param, requestByFormData);
            putMethod.setEntity(entity);
            return putMethod;
        } else if ("delete".equalsIgnoreCase(method)) {
            // 封装delete方法
            HttpDelete deleteMethod = new HttpDelete(url);
            deleteMethod.setHeaders(publicHeaders);
            return deleteMethod;
        } else {
            // 封装get方法
            HttpGet getMethod = new HttpGet(url);
            getMethod.setHeaders(publicHeaders);
            return getMethod;
        }
    }

    /**
     * 格式化url,替换路径参数等。
     *
     * @param shortUrl
     * @return
     */
    private String parseUrl(String shortUrl) {
        // 替换url中的参数
        shortUrl = getCommonParam(shortUrl);
        if (shortUrl.startsWith("http")) {
            return shortUrl;
        }
        if (rooUrlEndWithSlash == shortUrl.startsWith("/")) {
            if (rooUrlEndWithSlash) {
                shortUrl = shortUrl.replaceFirst("/", "");
            } else {
                shortUrl = "/" + shortUrl;
            }
        }
        return rootUrl + shortUrl;
    }

    /**
     * 格式化参数，如果是from-data格式则将参数封装到MultipartEntity否则封装到StringEntity
     *
     * @param param    参数
     * @param formData 是否使用form-data格式
     * @return
     * @throws UnsupportedEncodingException
     */
    private HttpEntity parseEntity(String param, boolean formData) throws UnsupportedEncodingException {
        if (formData) {
            Map<String, String> paramMap = JSON.parseObject(param,
                    HashMap.class);
            MultipartEntity multiEntity = new MultipartEntity();
            for (String key : paramMap.keySet()) {
                String value = paramMap.get(key);
                Matcher m = funPattern.matcher(value);
                if (m.matches() && m.group(1).equals("bodyfile")) {
                    value = m.group(2);
                    multiEntity.addPart(key, new FileBody(new File(value)));
                } else {
                    multiEntity.addPart(key, new StringBody(paramMap.get(key)));
                }
            }
            return multiEntity;
        } else {
            return new StringEntity(param, "UTF-8");
        }
    }

}
