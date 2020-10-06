package com.example.paqu;

import android.provider.Settings;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xml.sax.Parser;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class CdutHttpHelper{

    private static CdutHttpHelper cdut;
    private long init_time;
    private static ReentrantLock lock = new ReentrantLock();//线程锁
    private String state = "-1";//-1表示未登录
    private String login_state="-1";
    private Map<String, String> cookies = null;//cookies

    public String getState(){
        return this.login_state;
    }

    private CdutHttpHelper(){}

    //判断是否登录成功
    public boolean isGetConnectionSuccessful(){
        return cookies != null;
    }

    //登录获取cookies
    public void getConnection(String username, String password){
        //10分钟内无需再次登录
        if(isGetConnectionSuccessful()){
//            System.out.println("已经登录成功过");
            long nowtime = System.currentTimeMillis();
            if(nowtime - this.init_time <1000*60*10){
               System.out.println("无需再次登录");
                return;
            }
        }
        System.out.println("执行登录操作");
        System.out.println("开始执行爬虫函数");
        String url_login = "http://202.115.133.173:805/Common/Handler/UserLogin.ashx";
//        long endTime = System.currentTimeMillis();
        String sign = String.valueOf(System.currentTimeMillis());
        System.out.println("sign=" + sign);
        String signedpassword = getMD5((username + sign + getMD5(password.getBytes())).getBytes());
        //登录验证

        Connection connect = Jsoup.connect(url_login)
                .timeout(1000*5)//设置延时
                .method(Connection.Method.POST)
                .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko Core/1.63.6721.400 QQBrowser/10.2.2243.400");
        connect.data("Action", "Login");
        connect.data("pwd", signedpassword);
        connect.data("sign", sign);
        connect.data("userName", username);

        Connection.Response response = null;
        try {
//            Connection.Response response = connect.execute();
            response = connect.execute();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String login_state = response.body();
        this.state = login_state;
        this.login_state=login_state;
        System.out.println("login_state:"+login_state);  //连接状态
        if(login_state.equals("0")){
            Map<String, String> getCookies = response.cookies();
            this.cookies = getCookies;  //获取cookie
            this.init_time = System.currentTimeMillis();//设置获取连接的初始时间
        }

    }

    //爬取课程信息
    public String getCourseTable(String termid,String stuID){
        if(cookies ==  null|| !state.equals("0")) return null;
        String url_grade = String.format("http://202.115.133.173:805/Classroom/ProductionSchedule/StuProductionSchedule.aspx" +
                "?termid=%s&stuID=%s",termid,stuID);
        Connection connect = Jsoup.connect(url_grade)
                .timeout(1000*10)
                .method(Connection.Method.GET)
                .cookies(cookies) //携带cookies
                .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) " +
                        "like Gecko Core/1.63.6721.400 QQBrowser/10.2.2243.400");
        String body = null;
        try {
            Connection.Response response = connect.execute();
            body = response.body();
//            System.out.println(body);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return body;
    }

    //爬取成绩信息
    public String getSchoolReport(){
        if(cookies ==  null|| !state.equals("0")) return null;
        String url_grade = "http://202.115.133.173:805/SearchInfo/Score/ScoreList.aspx";
        Connection connect = Jsoup.connect(url_grade)
                .timeout(1000*10)
                .method(Connection.Method.GET)
                .cookies(cookies) //携带cookies
                .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) " +
                        "like Gecko Core/1.63.6721.400 QQBrowser/10.2.2243.400");
        String body = null;
        try {
            Connection.Response response = connect.execute();
            body = response.body();
//            System.out.println(body);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return body;
    }

    //爬取空教室信息
    public String getEmptyClassroom(String EmptyDate){
        System.out.println(EmptyDate);
        if(cookies ==  null|| !state.equals("0")) return null;
        String url_grade = "http://202.115.133.173:805/SearchInfo/Building/EmptyClassRoom.aspx";
        Connection connect = Jsoup.connect(url_grade)
                .timeout(1000*10)//最多请求10s
                .method(Connection.Method.POST)
                .cookies(cookies) //携带cookies
                .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) " +
                        "like Gecko Core/1.63.6721.400 QQBrowser/10.2.2243.400");

//        connect.data("__VIEWSTATEGENERATOR", "7EB77B99");
//        connect.data("__EVENTVALIDATION", "%2FwEdABNSo4EWU9%2FfIJPvu8dGQ58zWKVrV%2FLgio6ll2hdQg88Z1pgy3TOwf55SgKe3aJtBQY4yAAHayGEuMf65kg5Klk224f2Al1AH5b%2Fxiz9s5fvLPJwA3zBPnLkFkgzqryBi6trDleyZpCqGaamlthPjHmPdrkL0TfgYyELCva3D0Ve6KHTYUmdKOl4qV8qwZijiXFsXtRv8UlWIoHf5PM8c8aQVN8s9VX8WrZNmNtHmNQJgJYdUhK3iAiW0IV7Tay%2BPXw8vhcrrpaNPjpoUMQJYGox0oYAIAS%2Fiw%2BrJm6WVjSFW14Sxp17ITOZiKiRy8sV0zYmM2Kupxk7Yklf2l3IPPpRxLZG7YIsS0bit5HkRaMF3BtU%2F3ZrhT%2F8zMWFKEDqp5u%2FyqKEJMuSIyrmp3tBm%2BXHjkQBZi%2BSYrDNJV9j17hKwgdizoDWlKyYJDSHUvx%2BIGU%3D");

        connect.data("ctl00$Content$4btnSubmit", "%E6%9F%A5%E8%AF%A2");//hex编码,意为查询
        connect.data("ctl00$Content$EmptyDate", EmptyDate);//要查的日期
        connect.data("ctl00$Content$dllTeachingBuilding", "");//教学楼


        String body = null;
        try {
            Connection.Response response = connect.execute();
            body = response.body();
//            System.out.println(body);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
//        System.out.println("第二次获取");
//        Connection connect2 = Jsoup.connect(url_grade)
//                .timeout(1000*10)//最多请求10s
//                .method(Connection.Method.POST)
//                .cookies(cookies) //携带cookies
//                .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) " +
//                        "like Gecko Core/1.63.6721.400 QQBrowser/10.2.2243.400");
//
//        Document doc = Jsoup.parse(body);
//        Element str1 = doc.selectFirst("input[id=__VIEWSTATE]");
//        Element str2 = doc.selectFirst("input[id=__VIEWSTATEGENERATOR]");
//        Element str3 = doc.selectFirst("input[id=__EVENTVALIDATION]");
////        String s1 = URLEncoder.encode(str1.attr("value"));
////        String s2 = URLEncoder.encode(str2.attr("value"));
////        String s3 = URLEncoder.encode(str3.attr("value"));
////
////        System.out.println(s1);
////        System.out.println(s2);
////        System.out.println(s3);
////        System.out.println("注入新参数");
//
//        connect2.data("__VIEWSTATE", str1.attr("value"));
//        connect2.data("__VIEWSTATEGENERATOR", str2.attr("value"));
//        connect2.data("__EVENTVALIDATION", "/wEdABOviD6flNYHmSIiUvbxOyGOWKVrV/Lgio6ll2hdQg88Z1pgy3TOwf55SgKe3aJtBQY4yAAHayGEuMf65kg5Klk224f2Al1AH5b/xiz9s5fvLPJwA3zBPnLkFkgzqryBi6trDleyZpCqGaamlthPjHmPdrkL0TfgYyELCva3D0Ve6KHTYUmdKOl4qV8qwZijiXFsXtRv8UlWIoHf5PM8c8aQVN8s9VX8WrZNmNtHmNQJgJYdUhK3iAiW0IV7Tay+PXw8vhcrrpaNPjpoUMQJYGox0oYAIAS/iw+rJm6WVjSFW14Sxp17ITOZiKiRy8sV0zYmM2Kupxk7Yklf2l3IPPpRxLZG7YIsS0bit5HkRaMF3BtU/3ZrhT/8zMWFKEDqp5u/yqKEJMuSIyrmp3tBm+XHid4lO4BZBPDveS/XuF2OGm+OGtZm5E6zwupPWnpZDZs=");
//
//        connect2.data("ctl00%24Content%24btnSubmit", "%E6%9F%A5%E8%AF%A2");//hex编码,意为查询
//        connect2.data("ctl00$24Content$24EmptyDate", EmptyDate);//要查的日期
//        connect2.data("ctl00$24Content$24dllTeachingBuilding", "");//教学楼
//
//        try {
//            Connection.Response response = connect2.execute();
//            body = response.body();
////            System.out.println(body);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//        System.out.println(body);
        return body;
    }

    //工具函数,md5加密
    private static String getMD5(byte[] source) {
        String s = null;
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        // 用来将字节转换成16进制表示的字符
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(source);
            byte[] tmp = md.digest();// MD5 的计算结果是一个 128 位的长整数，
            // 用字节表示就是 16 个字节
            char[] str = new char[16 * 2];// 每个字节用 16 进制表示的话，使用两个字符， 所以表示成 16
            // 进制需要 32 个字符
            int k = 0;// 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) {// 从第一个字节开始，对 MD5 的每一个字节// 转换成 16
                // 进制字符的转换
                byte byte0 = tmp[i];// 取第 i 个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                // 取字节中高 4 位的数字转换,// >>> // 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf];// 取字节中低 4 位的数字转换
            }
            s = new String(str);// 换后的结果转换为字符串
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return s;
    }

    public static CdutHttpHelper getCdutHttpHelper(){//单例
        if(cdut == null){
//            lock.lock();
            cdut = new CdutHttpHelper();
//            System.out.println("新爬虫对象");
            //            lock.unlock();
        }
        return cdut;
    }
}

