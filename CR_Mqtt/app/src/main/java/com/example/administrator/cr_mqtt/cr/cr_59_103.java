package com.example.administrator.cr_mqtt.cr;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.example.administrator.cr_mqtt.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

/*
实时动态思路: https://blog.csdn.net/ww897532167/article/details/74139843
 */
//chart : https://blog.csdn.net/zhangphil/article/details/47656521
public class cr_59_103 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cr_59_103);
        Log.d("HomeActivity", "进入103");//成功进入
       // LineChart mLineChart = (LineChart) findViewById(R.id.chart);
        drawTheChartByMPAndroid("温度变化图");
        drawShidu("湿度变化图");
        drawDenguang("灯光变化图");

    }

    /*
    说明: 改为带形参的,
    参数: 标题, 待添加...
     */
    private void drawTheChartByMPAndroid(String title) {
        LineChart mLineChart = (LineChart) (LineChart) findViewById(R.id.chart_wendu);
        LineData lineData = getLineData(36, 1000,title);
        showChart(mLineChart, lineData, Color.rgb(0, 0, 1));
    }
    private void drawShidu(String title) {
        LineChart mLineChart = (LineChart) (LineChart) findViewById(R.id.chart_shidu);
        LineData lineData = getLineData(36, 1000,title);
        showChart(mLineChart, lineData, Color.rgb(0, 1, 1));
    }
    private void drawDenguang(String title) {
        LineChart mLineChart = (LineChart) (LineChart) findViewById(R.id.chart_dengguang);
        LineData lineData = getLineData(36, 1000,title);
        showChart(mLineChart, lineData, Color.rgb(1, 0, 1));
    }
    /*
    方法: 显示折线图
    参数: 组件, 数据, 背景色
     */
    private void showChart(LineChart lineChart, LineData lineData, int color) {
        lineChart.setDrawBorders(false); //在折线图上添加边框
        lineChart.setDescription(""); //数据描述
        lineChart.setNoDataTextDescription("You need to provide data for the chart.");

        lineChart.setDrawGridBackground(false); //表格颜色
        lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); //表格的颜色，设置一个透明度

        lineChart.setTouchEnabled(true); //可点击

        lineChart.setDragEnabled(true);  //可拖拽
        lineChart.setScaleEnabled(true);  //可缩放

        lineChart.setPinchZoom(false);

        lineChart.setBackgroundColor(color); //设置背景颜色

        lineChart.setData(lineData);  //填充数据

        Legend mLegend = lineChart.getLegend(); //设置标示，就是那个一组y的value的

        mLegend.setForm(Legend.LegendForm.CIRCLE); //样式
        mLegend.setFormSize(6f); //字体
        mLegend.setTextColor(Color.WHITE); //颜色

        lineChart.setVisibleXRange(1, 7);   //x轴可显示的坐标范围
        XAxis xAxis = lineChart.getXAxis();  //x轴的标示
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x轴位置
        xAxis.setTextColor(Color.WHITE);    //字体的颜色
        xAxis.setTextSize(10f); //字体大小
        xAxis.setGridColor(Color.WHITE);//网格线颜色
        xAxis.setDrawGridLines(false); //不显示网格线
        //xAxis.setTypeface(mTf);

        YAxis axisLeft = lineChart.getAxisLeft(); //y轴左边标示
        YAxis axisRight = lineChart.getAxisRight(); //y轴右边标示
        axisLeft.setTextColor(Color.WHITE); //字体颜色
        axisLeft.setTextSize(10f); //字体大小
        axisLeft.setAxisMaxValue(1000f); //最大值
        axisLeft.setLabelCount(6, true); //显示格数
        axisLeft.setGridColor(Color.WHITE); //网格线颜色
        //axisLeft.setTypeface(mTf);

        axisRight.setDrawAxisLine(false);
        axisRight.setDrawGridLines(false);
        axisRight.setDrawLabels(false);

        lineChart.animateX(2500);  //立即执行动画
    }

    /*
    方法: 获取数据
    参数: 参数个数, 参数范围
    说明: 形参换为MQTT数据
     */
    private LineData getLineData(int count, float range, String title) {
        ArrayList<String> xValues = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            // x轴显示的数据，这里默认使用数字下标显示
            xValues.add("" + (i+1));
        }

        // y轴的数据
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            float value = (int) (Math.random() * range);
            yValues.add(new Entry(value, i));
        }

        // create a dataset and give it a type
        // y轴的数据集合
        LineDataSet lineDataSet = new LineDataSet(yValues, title);
        // mLineDataSet.setFillAlpha(110);
        // mLineDataSet.setFillColor(Color.RED);

        //用y轴的集合来设置参数
        lineDataSet.setLineWidth(1.75f); // 线宽
        lineDataSet.setCircleSize(3f);// 显示的圆形大小
        lineDataSet.setColor(Color.WHITE);// 显示颜色
        lineDataSet.setCircleColor(Color.WHITE);// 圆形的颜色
        lineDataSet.setHighLightColor(Color.WHITE); // 高亮的线的颜色
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setValueTextColor(Color.WHITE); //数值显示的颜色
        lineDataSet.setValueTextSize(8f);     //数值显示的大小
        //lineDataSet.setValueTypeface(mTf);

        ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
        lineDataSets.add(lineDataSet); // 添加数据集合

        //创建lineData
        LineData lineData = new LineData(xValues, lineDataSets);
        return lineData;
    }

}
