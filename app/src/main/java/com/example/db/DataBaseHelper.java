package com.example.db;

import android.content.Context;
import android.provider.DocumentsContract;

import com.example.course.CourseInfo;
import com.example.course.Course;
import com.example.course.EmptyClassroom;
import com.example.course.Week;
import com.example.db.CourseDataBase;
import com.example.paqu.CdutHttpHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataBaseHelper {
    CdutHttpHelper cdutHttpHelper;
    public DataBaseHelper(){}

    public ArrayList<Course> messageIntoDB(String userAccount,String passWord){
        String[] colorarray = {"#FF9C07","#33C2E0","#627CC9","#E29886","#DD544B","#A6FF7272",
                "#A69C704E","#A6B841AA","#83FF7B","#535CA3"
        };
        int colorlistlenth  = colorarray.length;
        int colorindex = 0;
        Map<String, CourseInfo> courseinfo = new HashMap<String, CourseInfo>();
        ArrayList<Course> courseList = new ArrayList<Course>();

        cdutHttpHelper=CdutHttpHelper.getCdutHttpHelper();
        cdutHttpHelper.getConnection(userAccount,passWord);
        String body=cdutHttpHelper.getCourseTable("201901",userAccount);
        Document doc= Jsoup.parse(body);
        Elements tab2 = doc.select("table[class=tab2]").select("td");//选中课程介绍信息
        for( Element element : tab2 ){
            Pattern pattern=Pattern.compile("[(](.*?)[)](.*?) [(]ID.*?师\\[(.*?)\\]");
            Matcher matcher=pattern.matcher(element.text());
            while(matcher.find()) {
//			         System.out.println(matcher.group(1)+" "+matcher.group(2)+" "+matcher.group(3));
                courseinfo.put(matcher.group(1), new CourseInfo(matcher.group(2),matcher.group(3),colorarray[colorindex]));
                colorindex = (colorindex + 1)%colorlistlenth;
            }
        }

        //具体每节课信息
        Elements tab1 = doc.select("table[class=tab1]").select("tr");//选中具体课程信息
        tab1.remove(0);//去除前两个无用信息
        tab1.remove(0);
        int week = 1;
        for( Element element : tab1 ){
            Elements td = element.select("td");
            td.remove(0);//
            int count = 1;//计数,包括午休
            int segments = 1;//节次,不包括午休
            int day = 1;//星期
            for( Element course : td ) {
                if(count>=13) {//每天初始化
                    count=1;
                    segments=1;
                    day++;
                }
//                System.out.println(day+" "+count+" "+ segments);
                String colspan = course.attr("colspan");
                if(colspan.length()==0) {
                    count++;
                    if(count!=6) {
                        segments++;
                    }
                }else {
                    int cols = Integer.parseInt(colspan);//转换为int类型
                    int classEnd = segments + cols - 1;//结束节次
                    if(classEnd>=12) classEnd=11;
                    String [] arr = course.text().split("\\s+");
                    if(arr.length>1) {
                        String courseAbbreviation = arr[0].substring(0,arr[0].length()-2);//切割简写
                        String classRoom = arr[1];//教室
//                        System.out.println(courseAbbreviation);
//                        System.out.println(classRoom);
                      CourseInfo course_info = courseinfo.get(courseAbbreviation);
                        if (course_info != null){
                            String courseName = course_info.getCourseName();
                            String teacher = course_info.getTeacher();
                            String color = course_info.getColor();
//                            System.out.println(courseName+" "+teacher+" "+classRoom+" "+week+" "+day+" "+segments+" "+classEnd);
                            courseList.add(new Course(courseName,teacher,classRoom,week,day,segments,classEnd));
                        }
                    }
                    else {
//                        System.out.println(course.text()+" "+"noteacher"+" "+"noroom"+" "+week+" "+day+" "+segments+" "+classEnd);
                        courseList.add(new Course(course.text(),"","",week,day,segments,classEnd));
                    }
                    count = count + cols;
                    segments = segments + cols;
                }
            }
            week++;
        }
        return courseList;
    }

    //空教室
    public ArrayList<EmptyClassroom> EmptyClassroomHelper(String html){
        ArrayList<EmptyClassroom> EmptyClassroomList = new ArrayList<EmptyClassroom>();
        if (html == null) return null;
        System.out.println("开始解析");
        Document doc = Jsoup.parse(html);
        // 课程信息
        Elements classroom_list = doc.select("li[class=item]");//选中全部教室
        for( Element element : classroom_list ){
            Elements  property_list  = element.select("div");//选中单个教室
            if (property_list.size() == 7){
                String classroom_name = property_list.get(0).text();//教室名
                String seat_num = property_list.get(1).text();//座位数
                String section_1_2 = property_list.get(2).text();//1-2节
                String section_3_4 = property_list.get(3).text();//3-4
                String section_5_6 = property_list.get(4).text();//5-6
                String section_7_8 = property_list.get(5).text();//7-8
                String section_night = property_list.get(6).text();//晚上
                EmptyClassroomList.add(new EmptyClassroom(classroom_name, seat_num, section_1_2, section_3_4, section_5_6, section_7_8, section_night));
            }
        }
        //替换教室状态为单个字符
        for(EmptyClassroom emptyClassroom:EmptyClassroomList){
            emptyClassroom.setSection_1_2(EmptyClassroomStateReplace(emptyClassroom.getSection_1_2()));
            emptyClassroom.setSection_3_4(EmptyClassroomStateReplace(emptyClassroom.getSection_3_4()));
            emptyClassroom.setSection_5_6(EmptyClassroomStateReplace(emptyClassroom.getSection_5_6()));
            emptyClassroom.setSection_7_8(EmptyClassroomStateReplace(emptyClassroom.getSection_7_8()));
            emptyClassroom.setSection_night(EmptyClassroomStateReplace(emptyClassroom.getSection_night()));
        }
//        for(EmptyClassroom emptyClassroom:EmptyClassroomList){
//            System.out.println(emptyClassroom);
//        }
        return EmptyClassroomList;
    }

    public ArrayList<Week> WeekHelper(String html) {
        ArrayList<Week> weeklist = new ArrayList<>();
        if (html == null) return null;//*********************************
        Document doc = Jsoup.parse(html);
        Elements tab1 = doc.select("table[class=tab1]").select("tr");//选中具体课程信息
        tab1.remove(0);//去除前两个无用信息
        tab1.remove(0);

        for( Element element : tab1 ){
            Elements td = element.select("td");
            String weekinfo = td.get(0).text()+"结束";//获取周次信息
//            System.out.println(weekinfo);
            Pattern p=Pattern.compile("(.*?)周 (.*?)/(.*?)-(.*?)/(.*?)结束");//不加这个匹配不上最后的日期
            Matcher m=p.matcher(weekinfo);
            Week week = new Week();

            if(m.find()){
                week.setWeekno(Integer.parseInt((String) m.group(1).trim()));
                week.setStartmonth(Integer.parseInt((String)m.group(2).trim()));
                week.setStartday(Integer.parseInt((String)m.group(3).trim()));
                week.setEndmonth(Integer.parseInt((String)m.group(4).trim()));
                week.setEndday(Integer.parseInt((String)m.group(5).trim()));
                weeklist.add(week);
            }

        }
//        for (Week week:weeklist){
//            System.out.println(week);
//        }
        return weeklist;
    }

    //字符替换函数
    private String EmptyClassroomStateReplace(String state){
        switch (state) {
            case "正常教学":
                state = "课";
                break;
            case "外部借用":
                state = "借";
                break;
            case "空闲中":
                state = "空";
                break;
        }
        return state;
    }
}
