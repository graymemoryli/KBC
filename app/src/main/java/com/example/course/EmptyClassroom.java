package com.example.course;

public class EmptyClassroom {
    private String ClassRoomName;
    private String Section_1_2;
    private String Section_3_4;
    private String Section_5_6;
    private String Section_7_8;
    private String Section_night;
    private String seat_num;
    public EmptyClassroom(String classroom_name, String seat_num, String section_1_2, String section_3_4, String section_5_6, String section_7_8, String section_night){
        this.ClassRoomName=classroom_name;
        this.seat_num=seat_num;
        this.Section_1_2=section_1_2;
        this.Section_3_4=section_3_4;
        this.Section_5_6=section_5_6;
        this.Section_7_8=section_7_8;
        this.Section_night=section_night;
    }

    public void setSection_1_2(String section_1_2){
        this.Section_1_2=section_1_2;
    }

    public void setSection_3_4(String section_3_4){
        this.Section_3_4=section_3_4;
    }

    public void setSection_5_6(String section_5_6){
        this.Section_5_6=section_5_6;
    }

    public void setSection_7_8(String section_7_8){
        this.Section_7_8=section_7_8;
    }

    public void setSection_night(String section_night){
        this.Section_night=section_night;
    }

    public String getSection_1_2(){
        return this.Section_1_2;
    }

    public String getSection_3_4(){
        return this.Section_3_4;
    }

    public String getSection_5_6(){
        return this.Section_5_6;
    }

    public String getSection_7_8(){
        return this.Section_7_8;
    }

    public String getSection_night(){
        return this.Section_night;
    }

    public String getSeat_num(){
        return this.seat_num;
    }

    public String getClassRoomName(){
        return this.ClassRoomName;
    }

}
