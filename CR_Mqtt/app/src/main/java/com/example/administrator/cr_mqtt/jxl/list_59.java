package com.example.administrator.cr_mqtt.jxl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.cr_mqtt.Bean;
import com.example.administrator.cr_mqtt.Constant.Constant;
import com.example.administrator.cr_mqtt.MyAdapter;
import com.example.administrator.cr_mqtt.R;
import com.example.administrator.cr_mqtt.cr.cr_59_103;

import java.util.ArrayList;
import java.util.List;
//可以动态添加设备, 使用ViewGroup类自带的addView方法
public class list_59 extends ActionBarActivity {
    //主页ListView用
    private ListView listView;
    private List<Bean> mDatas;
    private MyAdapter mAdapter;
    Bean bean1 = new Bean("#59-103", "" ,"自习教室", "MQTT");
    Bean bean2 = new Bean("#59-104", "" ,"自习教室", "MQTT");
    Bean bean3 = new Bean("#59-105", "" ,"自习教室", "MQTT");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_59);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//启用返回条
        listView = (ListView) findViewById(R.id.listView_59);
        //每次都会执行,开线程, 一直显示最新数据?
        //线程开错了, 工作线程和UI线程----实时数据已完成, 线程会存在中断问题, 已解决
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initData();
                            listView.invalidate();
                            Toast.makeText(getApplicationContext(),"outside_initDate",Toast.LENGTH_SHORT).show();
                        }
                    });
                    try {
                        Thread.sleep(2000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "sleeping_try", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (InterruptedException e) {
                        Thread.yield();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "thread error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            }
        }).start();

        //initData();
    }

    //方法：初始化View
   // private void initView() {
   //     listView = (ListView) findViewById(R.id.listView_59);
   // }

    //方法；初始化Data
    private void initData() {
        mDatas = new ArrayList<Bean>();
        //数据实时接收mqtt订阅消息
        //将数据装到集合中去
        String msg1="温度: 23℃ 湿度: 83 灯光: off";
        String msg2="温度: 23℃ 湿度: 67 灯光: on";
        String msg3="温度: 23℃ 湿度: 49 灯光: off";
        if(Constant.msg_103!=null)
            msg1=Constant.msg_103;
        if(Constant.msg_104!=null)
            msg2=Constant.msg_104;
        if(Constant.msg_105!=null)
            msg3=Constant.msg_105;
        bean1.setDesc(msg1);
        bean2.setDesc(msg2);
        bean3.setDesc(msg3);

        //Bean bean1 = new Bean("#59-103", msg1 ,"自习教室", "MQTT");
        mDatas.add(bean1);
        //Bean bean2 = new Bean("#59-104", msg2, "多媒体教室", "MQTT");
        mDatas.add(bean2);
        // bean3 = new Bean("#59-105", msg3, "普通教室", "MQTT");
        mDatas.add(bean3);


        //为数据绑定适配器
        mAdapter = new MyAdapter(this,mDatas);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listView.setAdapter(mAdapter);
            }
        });

        //mAdapter.notifyDataSetChanged();

        //ListView item的点击事件, i是item序号, 从0开始
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Intent intent = new Intent(list_59.this, cr_59_103.class);
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            Log.d("HomeActivity", e.toString());
                            Log.d("HomeActivity", "执行失败");
                        }
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }
                Toast.makeText(list_59.this, "Click item" + i, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //重写用于Actionbar的返回操作
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
