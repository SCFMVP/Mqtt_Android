package com.example.administrator.cr_mqtt;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.cr_mqtt.Constant.Constant;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    String TelephonyIMEI="";

    private MqttClient client;//client
    private MqttConnectOptions options;//配置
    MqttConnectThread mqttConnectThread = new MqttConnectThread();//连接服务器任务

    Button button;//发送消息按钮0/1
    Button btnConnect;
    TextView textView;//接收到的消息
    EditText txtIp,txtUserName,txtPassword;

    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public MainActivity() {
    }
    /*
        启动初始化
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log_txt("+++++++>>>>>>>>>>>>>>>新<<<<<<<<<<<<<<<<<<+++的+++++++++", "+++++++>>>>>>>>>>>>>>>日<<<<<<<<<<<<<<<<<<+++志+++++++++");
        button = (Button) findViewById(R.id.button1);//获取发送消息按钮
                button.setOnTouchListener(buttonTouch);//设置按钮的触摸事件, 绑定button方法

        btnConnect=(Button)findViewById(R.id.btnConnect);
                btnConnect.setOnTouchListener(ConnecMqttHost);

        textView = (TextView) findViewById(R.id.textView1);//显示接收的消息文本框
        txtIp=(EditText)findViewById(R.id.txtIp);
        txtUserName=(EditText)findViewById(R.id.txtUseName);
        txtPassword=(EditText)findViewById(R.id.txtPassword);


        TelephonyManager mTm = (TelephonyManager)this.getSystemService(TELEPHONY_SERVICE);
        TelephonyIMEI ="admin_android";// mTm.getDeviceId();
        //Toast.makeText(getApplicationContext(), TelephonyIMEI, 500).show();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {//更换内容
                    try {
                        //跳转页面
                        Intent intent=new Intent(MainActivity.this,HomeAcrivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //Snackbar.make(view, "作者邮箱: 1638414737@qq.com", Snackbar.LENGTH_SHORT)
                     //       .setAction("Action", null).show();
                }
            });
        }
    }

    /*  连接Mqtt服务器方法 */
    private View.OnTouchListener ConnecMqttHost =new View.OnTouchListener()
    {//已连接状态下再次点击Toast
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) //按下
            {
                if(txtIp.getText().toString().trim().isEmpty()||txtPassword.getText().toString().trim().isEmpty()||txtUserName.getText().toString().trim().isEmpty())
                {
                    showExitDialog("提示","请检查\n主机地址, 用户名和密码\n是否空缺!");
                    return false;
                }
                else
                {   //判断效果不理想

                        if(mqttConnectThread.isAlive())
                            showExitDialog("警告","已连接, 请勿重复连接!");
                        else
                        {
                            MyMqttInit();//初始化配置MQTT客户端
                            Log_txt("初始化", "MyMqttInit初始化完成");
                            Log_txt("连接服务器","开始连接服务器");
                            try {
                                runOnUiThread(new Runnable() {//
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "初始化完成, 开始连接服务器", Toast.LENGTH_SHORT).show();

                                    }
                                });
                                mqttConnectThread.start();//执行连接lf0l4re服务器任务
                                //保存至Constant类, 后面再建立一个客户端
                                Constant.Host = txtIp.getText().toString();
                                Constant.UserName = txtUserName.getText().toString();
                                Constant.Password = txtPassword.getText().toString();
                            }catch(Exception ex){
                                //showExitDialog("Error","请勿重复连接");
                            }
                        }

                }
            }
            return false;
        }


    };
    /*  初始化配置Mqtt  */
    private void MyMqttInit()
    {
        try
        {
            Log_txt("连接主机","正在连接主机");
            //(1)主机地址"tcp://10.0.2.2"(2)客户端ID,一般以客户端唯一标识符(不能够和其它客户端重名)(3)最后一个参数是指数据保存在内存(具体保存什么数据,以后再说,其实现在我也不是很确定)
            client = new MqttClient(txtIp.getText().toString(),TelephonyIMEI,new MemoryPersistence());
            Log_txt("连接主机","主机连接成功");
        } catch (MqttException e) {
            // TODO Auto-generated catch block
            Log.d("Mainfest", ">>>主机连接失败<<<"+e);
            e.printStackTrace();
            showExitDialog("error","主机连接失败\n"+e.toString());
        }
        runOnUiThread(new Runnable() {//
            public void run() {
                Toast.makeText(getApplicationContext(), "MQTT连接配置中...", Toast.LENGTH_SHORT).show();

            }
        });
        Log_txt("Mqtt客户端配置","正在配置MQTT客户端...");
        Log.d("Mainfest", ">>>MQTT连接配置<<<");
        options = new MqttConnectOptions();//MQTT的连接设置
        options.setCleanSession(true);//设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
        options.setUserName(txtUserName.getText().toString());//设置连接的用户名(自己的服务器没有设置用户名)
        options.setPassword(txtPassword.getText().toString().toCharArray());//设置连接的密码(自己的服务器没有设置密码)
        options.setConnectionTimeout(10);// 设置连接超时时间 单位为秒
        options.setKeepAliveInterval(20);// 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        client.setCallback(new MqttCallback() {
            @Override//获取消息会执行这里--arg0是主题,arg1是消息
            public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
                // TODO Auto-generated method stub
                final String topic = arg0;//主题
                final String msgString = arg1.toString();//消息
                if(arg0.equals("/test/103"))
                {
                    Constant.topic_103=df.format(new Date());
                    Constant.msg_103=arg1.toString();
                }
                if(arg0.equals("/test/104"))
                {
                    Constant.topic_104=df.format(new Date());
                    Constant.msg_104=arg1.toString();
                }
                if(arg0.equals("/test/105"))
                {
                    Constant.topic_105=df.format(new Date());
                    Constant.msg_105=arg1.toString();
                }
                runOnUiThread(new Runnable() {//因为操作的是主界面的控件所以用刷新UI的线程,最好用handle哈,我这里怎么简单怎么写
                    public void run() {
                        //Toast.makeText(getApplicationContext(),"主题:"+topic+"消息:"+msgString, 500).show();
                        textView.setText("主题:" + topic + "\n消息:" + msgString);
                        Log_txt("Mqtt主题: "+topic,msgString);
                    }
                });
            }

            @Override//订阅主题后会执行到这里
            public void deliveryComplete(IMqttDeliveryToken arg0) {
                // TODO Auto-generated method stub
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Topic订阅成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }



            @Override//连接丢失后，会执行这里
            public void connectionLost(Throwable arg0) {
                Log_txt("连接丢失","Mqtt服务器连接丢失");
                // TODO Auto-generated method stub
                runOnUiThread(new Runnable() {//
                    public void run() {
                        Toast.makeText(getApplicationContext(), "连接丢失", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /*连接服务器任务*/   //PS: 这又是一个class, 不允许调用showExitDialog方法
    class MqttConnectThread extends Thread
    {
        public void run()
        {
            try
            {
                try {
                    Log_txt("服务器连接","开始连接服务器");
                    runOnUiThread(new Runnable() {//
                        public void run() {
                            Toast.makeText(getApplicationContext(), "开始连接服务器", Toast.LENGTH_SHORT).show();

                        }
                    });
                    client.connect(options);//连接服务器,连接不上会阻塞在这
                    Log_txt("服务器连接", "成功连接服务器");
                    runOnUiThread(new Runnable() {//
                        public void run() {
                            Toast.makeText(getApplicationContext(), "成功连接服务器", Toast.LENGTH_SHORT).show();

                        }
                    });
                } catch (MqttException e) {
                    runOnUiThread(new Runnable() {//
                        public void run() {
                            Toast.makeText(getApplicationContext(), "连接失败,检查网络是否配置正确", Toast.LENGTH_SHORT).show();

                        }
                    });
                    Log.d("MqttConnectThread", ">>>"+e+"<<<");
                    return;
                }

                //格式: /cr/deviceID     deviceID主题发送该设备所有数据的数据包
                //# 是一个匹配主题中任意层次数的通配符。比如说，如果你订阅了test/device/#，你就可以接收到以下这些主题的消息。
                //test/device/后面随便是什么
                client.subscribe("/#",0);//设置接收的主题--所有主题
                client.subscribe("/test/yang",0);//设置接收的主题
                client.subscribe("/test/fengwu",0);//设置接收的主题

                runOnUiThread(new Runnable() {//
                    public void run() {
                        Toast.makeText(getApplicationContext(), "连接成功", Toast.LENGTH_SHORT).show();
                        btnConnect.setText("已 连 接");
                        btnConnect.setBackgroundColor(Color.LTGRAY);
                    }
                });
            }
            catch (MqttSecurityException e)
            {
                Log.d("安全问题连接失败",e.toString());
                showExitDialog("11","安全问题连接失败");
                //安全问题连接失败
            }
            catch (MqttException e)
            {
                Log.d("连接失败原因",e.toString());
                showExitDialog("22", e.toString());
                //连接失败原因
            }
        }
    }

    /*按钮触摸事件测试用*/
    private View.OnTouchListener buttonTouch = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            MqttMessage msgMessage = null;//Mqtt消息变量
            if (event.getAction() == MotionEvent.ACTION_DOWN) //按下
            {
                Log_txt("Btn_DOWN","msg: 1");
                msgMessage = new MqttMessage("1".getBytes());
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) //松开
            {
                Log_txt("Btn_UP","msg: 0");
                msgMessage = new MqttMessage("0".getBytes());
            }

            try
            {
                client.publish("/test/button",msgMessage);//发送主题为"/test/button"的消息
            } catch (MqttPersistenceException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (MqttException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (Exception e) {
                //其余的状态msgMessage = null;所以加了这个catch (Exception e)
            }

            return false;
        }
    };
    /* 调用系统自带弹框 */
    private void showExitDialog(String title,String msg){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("确定", null)
                .show();
    }

//日志系统已完成
    public void Log_txt(String title,String msg)
    {
        //这里文本写入换行-----------why?!
        String str1="\r\n/**********************Log Below**********************/"+"\r\n\r\n";
        String str2=" * 时间: "+df.format(new Date())+"\r\n\r\n";
        String str3=" * 标题: " + title + "\r\n\r\n";
        String str4=" * 信息: " + msg + "\r\n";
        SimpleDateFormat df1=new SimpleDateFormat("yyyy-MM-dd");
        String sdCardDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        //showExitDialog("tip",sdCardDir);
        File saveFile = new File(sdCardDir,df1.format(new Date())+"_Log.txt");
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(saveFile,true); //追加写入
        } catch (FileNotFoundException e) {
            showExitDialog("Error","日志创建失败");
        }
        try {
            outStream.write(str1.getBytes());
            outStream.write(str2.getBytes());
            outStream.write(str3.getBytes());
            outStream.write(str4.getBytes());
            outStream.close();
        } catch (IOException e) {
            showExitDialog("Error","日志写入失败");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            System.exit(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
