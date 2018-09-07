package com.example.administrator.cr_mqtt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.administrator.cr_mqtt.jxl.list_59;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

//底部菜单栏框架参考: https://www.cnblogs.com/leestar54/p/4222565.html
//首页ListView框架参考: https://www.cnblogs.com/smyhvae/p/4477079.html
//个人中心框架参考: https://blog.csdn.net/asfang/article/details/79144127

public class HomeAcrivity extends ActionBarActivity {

    // tab用参数
    private TabHost tabHost;
    private RadioGroup radiogroup;
    private int menuid;
    //底部菜单栏中的组件都在此绑定
    public TextView txtLog;
    public ScrollView scrollView;
    //主页ListView用
    private ListView listView;
    private List<Bean> mDatas;
    private MyAdapter mAdapter;
    //个人中心
    private ImageView mHBack;
    private ImageView mHHead;
    private ImageView mUserLine;
    private TextView mUserName;
    private TextView mUserVal;

    private ItemView mNickName;
    private ItemView mSex;
    private ItemView mSignName;
    private ItemView mPass;
    private ItemView mPhone;

    private ItemView mAbout;
    private ItemView mCode;
    private ItemView mUpdate;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.go_home);
        txtLog=(TextView)findViewById(R.id.txtLog);
        txtLog.setMovementMethod(ScrollingMovementMethod.getInstance());
        scrollView = (ScrollView) findViewById(R.id.svResult);

        //开启日志线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    showLog();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }
            }
        }).start();

        PrintLog("测试", "我是消息1");
        PrintLog("测试", "我是消息2");
        PrintLog("测试", "我是消息3");
        PrintLog("测试", "我是消息4");
        PrintLog("测试", "我是消息5");
        PrintLog("测试", "我是消息6");
        PrintLog("测试", "我是消息7");
        //listView
        initView();
        initData();
        //个人中心
        initMyCenter();
        setData();

        //底部菜单栏
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("main").setIndicator("main")
                .setContent(R.id.fragment_main));
        tabHost.addTab(tabHost.newTabSpec("mycenter").setIndicator("mycenter")
                .setContent(R.id.fragment_mycenter));
        tabHost.addTab(tabHost.newTabSpec("search").setIndicator("search")
                .setContent(R.id.fragment_search));
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                menuid = checkedId;
                int currentTab = tabHost.getCurrentTab();
                switch (checkedId) {
                    case R.id.radio_main:
                        tabHost.setCurrentTabByTag("main");
                        //如果需要动画效果就使用
                        setCurrentTabWithAnim(currentTab, 0, "main");
                        getSupportActionBar().setTitle("首页");
                        break;
                    case R.id.radio_mycenter:
                        //tabHost.setCurrentTabByTag("mycenter");
                        setCurrentTabWithAnim(currentTab, 1, "mycenter");
                        getSupportActionBar().setTitle("个人中心");

                        break;
                    case R.id.radio_search:
                        tabHost.setCurrentTabByTag("search");
                        getSupportActionBar().setTitle("日志");
                }
                // 刷新actionbar的menu
                getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
            }
        });
        }

    //方法：初始化View
    private void initView() {
        listView = (ListView) findViewById(R.id.listView);
    }

    //方法；初始化Data
    private void initData() {
        mDatas = new ArrayList<Bean>();

        //将数据装到集合中去
        Bean bean = new Bean("#59-电信学院", "电信学院有专业: 物联网工程, 电子信息工程, 通信工程, 电气及其自动化, 测量及控制等专业","教室数量: 3", "MQTT");
        mDatas.add(bean);

        bean = new Bean("#64-艺术学院", "艺术学院有专业: 模具专业, 啦啦啦专业, 啦啦啦专业, 啦啦啦专业, 啦啦啦专业", "教室数量: 50", "MQTT");
        mDatas.add(bean);

        bean = new Bean("#65-东方学院", "Android为ListView和GridView打造万能适配器", "教室数量: 45", "MQTT");
        mDatas.add(bean);

        bean = new Bean("#11-数理学院", "Android为ListView和GridView打造万能适配器", "教室数量: 50", "MQTT");
        mDatas.add(bean);

        bean = new Bean("#60-计算机学院", "Android为ListView和GridView打造万能适配器", "教室数量: 50", "MQTT");
        mDatas.add(bean);

        bean = new Bean("#18-人文社科学院", "Android为ListView和GridView打造万能适配器", "教室数量: 50", "MQTT");
        mDatas.add(bean);

        bean = new Bean("#19-外国语学院", "Android为ListView和GridView打造万能适配器", "教室数量: 50", "MQTT");
        mDatas.add(bean);

        //为数据绑定适配器
        mAdapter = new MyAdapter(this,mDatas);

        listView.setAdapter(mAdapter);

        //ListView item的点击事件, i是item序号, 从0开始
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i)
                {
                    case 0://跳转页面
                        Log.d("HomeActivity", "点击");
                        Intent intent=new Intent(HomeAcrivity.this,list_59.class);
                        try{
                        startActivity(intent);
                            Log.d("HomeActivity", "执行成功");
                        }
                        catch(Exception e)
                        {
                            Log.d("HomeActivity",e.toString());
                            Log.d("HomeActivity", "执行失败");
                        }
                        Log.d("HomeActivity", "start");
                        break;
                    case 1:break;
                    case 2:break;
                    case 4:break;
                        default: break;
                }
                Log.d("HomeActivity", "switch 外");
                Toast.makeText(HomeAcrivity.this, "Click item" + i, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData() {
        //设置背景磨砂效果
        Glide.with(this).load(R.drawable.dribble)
                .bitmapTransform(new BlurTransformation(this, 25), new CenterCrop(this))
                .into(mHBack);
        //设置圆形图像
        Glide.with(this).load(R.drawable.dribble)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(mHHead);

        //设置用户名整个item的点击事件
        mNickName.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(HomeAcrivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
        //sex
        mSex.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(HomeAcrivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
        mSignName.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(HomeAcrivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
        mPass.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(HomeAcrivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
        mPhone.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                PrintLog("QQ跳转", "正在打开later的聊天界面");
                String url = "mqqwpa://im/chat?chat_type=wpa&uin=852165869";//uin是发送过去的qq号码
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                PrintLog("QQ跳转", "成功打开later的聊天界面");
                Toast.makeText(HomeAcrivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
        mAbout.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "当前版本: 1.0.0.1", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        mCode.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                showOnlyDialog("", "");
                Toast.makeText(HomeAcrivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
        mUpdate.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "已是最新版本", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    private void initMyCenter() {
        //顶部头像控件
        mHBack = (ImageView) findViewById(R.id.h_back);
        mHHead = (ImageView) findViewById(R.id.h_head);
        mUserLine = (ImageView) findViewById(R.id.user_line);
        mUserName = (TextView) findViewById(R.id.user_name);
        mUserVal = (TextView) findViewById(R.id.user_val);
        //下面item控件
        mNickName = (ItemView) findViewById(R.id.nickName);
        mSex = (ItemView) findViewById(R.id.sex);
        mSignName = (ItemView) findViewById(R.id.signName);
        mPass = (ItemView) findViewById(R.id.pass);
        mPhone = (ItemView) findViewById(R.id.phone);
        mAbout = (ItemView) findViewById(R.id.about);

        mCode = (ItemView) findViewById(R.id.code);
        mUpdate = (ItemView) findViewById(R.id.update);

}
    /*退出按钮*/
    public void ExitButtonCallback(View view)
    {
        System.exit(0);
    }
    /* 调用系统自带弹框 */
    public void showOnlyDialog(String title,String msg){
        new AlertDialog.Builder(this)
                .setTitle("请选择")
                .setIcon(android.R.drawable.ic_menu_camera)
                .setSingleChoiceItems(new String[]{"作者QQ", "作者女朋友QQ", "CSDN博客", "Google+"}, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        String url = "mqqwpa://im/chat?chat_type=wpa&uin=852165869";//uin是发送过去的qq号码
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                                        break;
                                    case 1:String url1 = "mqqwpa://im/chat?chat_type=wpa&uin=1638414737";//uin是发送过去的qq号码
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url1)));
                                        break;
                                    case 2:String url2 = "https://blog.csdn.net/qq_37832932";//uin是发送过去的qq号码
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url2)));
                                        break;
                                    case 3:showExitDialog("Error","无法访问");
                                        break;
                                }
                                 dialog.dismiss();
                            }
                        }
                )
                .show();
    }
    /* 调用系统自带弹框 */
    private void showExitDialog(String title,String msg){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("确定", null)
                .show();
    }
    public void PrintLog(String title, String msg)
    {
        handler.post(new Runnable() {
            @Override
            public void run() {
                //设置ScrollView滚动到顶部
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);

            }
        });
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        txtLog.append("\n/**********************Log Below**********************/"+"\n");
        txtLog.append(" * 时间: "+df.format(new Date())+"\n");
        txtLog.append(" * 标题: "+title+"\n");
        txtLog.append(" * 信息: " + msg + "\n");
       // svResult.fullScroll(ScrollView.FOCUS_DOWN);

        handler.post(new Runnable() {
            @Override
            public void run() {
                //设置ScrollView滚动到顶部
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);

            }
        });
    }
    // 这个方法是关键，用来判断动画滑动的方向
    private void setCurrentTabWithAnim(int now, int next, String tag) {
        if (now > next) {
            tabHost.getCurrentView().startAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
            tabHost.setCurrentTabByTag(tag);
            tabHost.getCurrentView().startAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
        } else {
            tabHost.getCurrentView().startAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
            tabHost.setCurrentTabByTag(tag);
            tabHost.getCurrentView().startAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
        }
    }
    public void showLog()
    {
        handler.post(new Runnable() {
            @Override
            public void run() {
                //设置ScrollView滚动到顶部
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);

            }
        });
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
                        //InputStream is = null;
                        String sdCardDir = Environment.getExternalStorageDirectory().getAbsolutePath();
                        //File file = new File(sdCardDir + "/a.txt");
                        //is = getAssets().open(df.format(new Date())+"_Log.txt");

                            //String text = new String(buffer, "UTF-8");
                            //txtLog.setText(text);
                    FileReader fr= null;
                    String str="";//not null!!!!
                    try {
                        fr = new FileReader(sdCardDir+"/"+df.format(new Date())+"_Log.txt");
                    } catch (FileNotFoundException e) {

                    }
                    //可以换成工程目录下的其他文本文件----这里有一个null问题
                    BufferedReader br=new BufferedReader(fr);
                    try {
                        while(br.readLine()!=null){
                            String s=br.readLine();
                            s+="\r\n";//换行问题在这里, 这里UI换行
                            str+=s;
                            System.out.println(s);
                        }
                        br.close();
                    } catch (IOException e) {
                        Log.d("<<<<>>>>>>",e.toString());
                        showExitDialog("tip",e.toString());
                    }
                    System.out.println("niha"+str);
                    txtLog.setText(str);
                }
            });
        handler.post(new Runnable() {
            @Override
            public void run() {
                //设置ScrollView滚动到顶部
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        switch (menuid) {
            case R.id.radio_main:
                getMenuInflater().inflate(R.menu.main, menu);
                break;
            case R.id.radio_mycenter:
                menu.clear();
                break;
            case R.id.radio_search:
                menu.clear();
                break;
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}