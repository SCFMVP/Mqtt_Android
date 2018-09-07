package com.example.administrator.cr_mqtt.Constant;

public class Constant {
    public static String Host;
    public static String UserName;
    public static String Password;

    //103,104,105三个教室,/test/103三个topic, msg是数据包
    //数据包格式: 温度: 23℃ 湿度: 83 灯光: off
    public static String topic_103;
    public static String msg_103;
    public static String topic_104;
    public static String msg_104;
    public static String topic_105;
    public static String msg_105;
}