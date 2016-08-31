package com.example.toshiba.finalproject;

import android.animation.PropertyValuesHolder;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.db.chart.Tools;
import com.db.chart.view.LineChartView;
import com.db.chart.view.Tooltip;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

public class BMI extends AppCompatActivity {

    private TabLayout mTabs;
    private ViewPager mViewPager;

    String[] mLabels1= {"Jan", "Fev", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    String[] mLabels2= {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    String[] mLabels3= {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"
            , "13", "14", "15", "16", "17", "18:", "19", "20", "21", "22", "23", "24"};
    float[] mValues1 = {3.5f, 4.7f, 4.3f, 8f, 6.5f, 9.9f, 7f, 8.3f, 7.0f, 7f, 8.3f, 7.0f};
    float[] mValues2 = {4.5f, 2.5f, 2.5f, 9f, 4.5f, 9.5f, 5f};
    float[] mValues3 = {4.5f, 2.5f, 2.5f, 9f, 4.5f, 9.5f, 5f, 8.3f, 1.8f, 1.8f, 1.8f, 1.8f
            , 4.5f, 2.5f, 2.5f, 9f, 4.5f, 9.5f, 5f, 8.3f, 1.8f, 1.8f, 1.8f, 1.8f};
    float[] test={0,0,0,0,0,0,0,0,0,0,0,0};

    //int mYear,mMonth,mDay;
    String timestring;
    //Button btn;

    private DBcontent dbcontent;
    private ListView listView_bmi;
    Vector list_bmi = new Vector();
    private ArrayAdapter<String> listAdapter_bmi;
    int bmi_all;
    int modify_dele_bmi_position=0;
    int dele_position;
    Item_bmi item_bmi;

    LineCardOne lineCardOne;

    //Vector chartdata=new Vector();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        int flag=1;
        dbcontent = new DBcontent(getApplicationContext());
        item_bmi=new Item_bmi();
        bmi_all=dbcontent.getCount_bmi();
        final long table[]=new long[bmi_all];
        dbcontent.make_bmi_table(table);
        if(bmi_all!=0){
            for(int i=0;i<bmi_all;i++){
                //Log.e(i+":",String.valueOf(table[i]));
            }
        }

        if(bmi_all==0){
            bmi_all=1;
            flag=0;
        }
        float[] test2 =new float [bmi_all];
        String[] test3=new String[bmi_all];

        //listview
        listView_bmi = (ListView)findViewById(R.id.listView_bmi);

        //Toast.makeText(getApplicationContext(), String.valueOf(bmi_all), Toast.LENGTH_SHORT).show();
        if(flag==0){
            test2[0]=0;
            test3[0]="";
            flag=1;
        }
        else{
            for(int i=0;i<bmi_all;i++){
                dbcontent.get_bmi(table[i], item_bmi);
                //Toast.makeText(getApplicationContext(), String.valueOf(time), Toast.LENGTH_SHORT).show();
                list_bmi.add(String.valueOf(item_bmi.datetime)+" "+String.valueOf(item_bmi.height)+" "+String.valueOf(item_bmi.weight)+" "+String.valueOf(item_bmi.bmi));
                test2[i]=item_bmi.bmi;
                test3[i]=String.valueOf(item_bmi.datetime);
            }
        }
        listAdapter_bmi = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list_bmi);
        listView_bmi.setAdapter(listAdapter_bmi);
        listView_bmi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                modify_dele_bmi_position = (int)table[position];
                dele_position=position;
                //Toast.makeText(getApplicationContext(), String.valueOf(modify_dele_bmi_position), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                modify_dele_bmi();
            }
        });

        lineCardOne=(new LineCardOne((CardView)findViewById(R.id.card1), this, test3, test2,"bmi"));//.init();
        lineCardOne.init();

        //fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                add_bmi();
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation view item clicks here.
                Fragment fragment = null;
                int id = item.getItemId();

                if (id == R.id.nav_walk) {
                    Intent intent = new Intent();
                    intent.setClass(BMI.this, Step.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.nav_bmi) {
                    Intent intent = new Intent();
                    intent.setClass(BMI.this, BMI.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.nav_setting) {

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }



    private void modify_dele_bmi() {
        final View bmiview = LayoutInflater.from(BMI.this).inflate(R.layout.add_bmi, null);
        dbcontent.get_bmi(modify_dele_bmi_position , item_bmi);
        //Toast.makeText(getApplicationContext(), String.valueOf(item_bmi.id), Toast.LENGTH_SHORT).show();
        final EditText edit_hight=(EditText)bmiview.findViewById(R.id.editText);
        edit_hight.setText(String.valueOf(item_bmi.height));
        final EditText edit_weight=(EditText)bmiview.findViewById(R.id.editText2);
        edit_weight.setText(String.valueOf(item_bmi.weight));
        final TextView txv=(TextView)bmiview.findViewById(R.id.textView_time);
        txv.setText(String.valueOf(item_bmi.datetime));
        timestring=String.valueOf(item_bmi.datetime);
        Button btn= (Button) bmiview.findViewById(R.id.button_time);
        btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // 設定初始日期
                final Calendar c = Calendar.getInstance();
                // 跳出日期選擇器
                DatePickerDialog dpd = new DatePickerDialog(bmiview.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                timestring = String.valueOf(year) + String.valueOf(monthOfYear + 1) + String.valueOf(dayOfMonth);
                                txv.setText(String.valueOf(year) + "_" + String.valueOf(monthOfYear + 1) + "_" + String.valueOf(dayOfMonth));
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });
        //包含多個選項及複選框的對話框
        AlertDialog dialog = new AlertDialog.Builder(BMI.this)
                .setIcon(android.R.drawable.btn_star_big_on)
                .setView(bmiview)
                .setPositiveButton("修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        item_bmi.datetime = Integer.parseInt(timestring);
                        item_bmi.height = Float.parseFloat(edit_hight.getText().toString());
                        item_bmi.weight = Float.parseFloat(edit_weight.getText().toString());
                        dbcontent.update_bmi(modify_dele_bmi_position , item_bmi);
                        //int a=modify_dele_bmi_position -1;
                        //Toast.makeText(getApplicationContext(), String.valueOf(a), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getApplicationContext(), String.valueOf(item_bmi.id), Toast.LENGTH_SHORT).show();
                        renew(dele_position , "modify");


                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("刪除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dbcontent.delete_bmi(modify_dele_bmi_position );
                        renew(dele_position,"dele");
                        //dbcontent.reset_bmi(modify_dele_bmi_position+1);
                        //dbcontent.reset_bmi_two(modify_dele_bmi_position + 1, dbcontent.getCount_bmi() + 1);


                    }
                })
                .create();
        dialog.show();
    }

    private void add_bmi() {
        final Calendar c = Calendar.getInstance();
        final View item = LayoutInflater.from(BMI.this).inflate(R.layout.add_bmi, null);
        final TextView txv=(TextView)item.findViewById(R.id.textView_time);
        txv.setText( String.valueOf(c.get(Calendar.YEAR))+"_"+ String.valueOf(c.get(Calendar.MONTH)+1)+"_"+String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
        timestring=String.valueOf(c.get(Calendar.YEAR))+"_"+ String.valueOf(c.get(Calendar.MONTH)+1)+"_"+String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        final EditText edit_hight=(EditText)item.findViewById(R.id.editText);
        final EditText edit_weight=(EditText)item.findViewById(R.id.editText2);
        final Button btn= (Button) item.findViewById(R.id.button_time);
        btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // 設定初始日期
                //final Calendar c = Calendar.getInstance();
                // 跳出日期選擇器
                DatePickerDialog dpd = new DatePickerDialog(item.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                timestring = String.valueOf(year) + String.valueOf(monthOfYear + 1) + String.valueOf(dayOfMonth);
                                txv.setText(String.valueOf(year) + "_" + String.valueOf(monthOfYear + 1) + "_" + String.valueOf(dayOfMonth));
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });
        //包含多個選項及複選框的對話框
        AlertDialog dialog = new AlertDialog.Builder(BMI.this)
                .setIcon(android.R.drawable.btn_star_big_on)
                .setTitle("新增")
                .setView(item)
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Item_bmi item = new Item_bmi(Integer.parseInt(timestring), Float.parseFloat(edit_hight.getText().toString()), Float.parseFloat(edit_weight.getText().toString()));
                        dbcontent.insert_bmi(item);
                        renew(item.id,"add");

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create();
        dialog.show();
    }

    private void renew(long position,String diff) {
        bmi_all=dbcontent.getCount_bmi();
        final long[] table=new long[bmi_all];
        //Toast.makeText(getApplicationContext(), String.valueOf(bmi_all), Toast.LENGTH_SHORT).show();
        //list_bmi.clear();
        dbcontent.make_bmi_table(table);

        if(bmi_all!=0){
            for(int i=0;i<bmi_all;i++){
                //Log.e(i+":",String.valueOf(table[i]));
            }
        }
        int flag=1;
        if(bmi_all==0){
            bmi_all=1;
            flag=0;
        }
        float[] test2 =new float [bmi_all];
        String[] test3=new String[bmi_all];

        if(flag==0){
            test2[0]=0;
            test3[0]="";
            flag=1;
        }
        else{
            for(int i=0;i<bmi_all;i++){
                dbcontent.get_bmi(table[i], item_bmi);
                //Log.e("test2", String.valueOf(item_bmi.bmi));
                //Log.e("test3", String.valueOf(item_bmi.datetime));
                //list_bmi.add(String.valueOf(item_bmi.datetime) + " " + String.valueOf(item_bmi.height) + " " + String.valueOf(item_bmi.weight) + " " + String.valueOf(item_bmi.bmi));
                test2[i]=item_bmi.bmi;
                test3[i]=String.valueOf(item_bmi.datetime);
            }
        }


        //listview
        if(diff=="add"){
            dbcontent.get_bmi(position, item_bmi);
            //Toast.makeText(getApplicationContext(), String.valueOf(time), Toast.LENGTH_SHORT).show();
            list_bmi.add(String.valueOf(item_bmi.datetime)+" "+String.valueOf(item_bmi.height)+" "+String.valueOf(item_bmi.weight)+" "+String.valueOf(item_bmi.bmi));
        }
        else if(diff=="modify"){
            dbcontent.get_bmi(table[(int)position] , item_bmi);
            list_bmi.setElementAt(String.valueOf(item_bmi.datetime) + " " + String.valueOf(item_bmi.height)+" "+String.valueOf(item_bmi.weight)+" "+String.valueOf(item_bmi.bmi),(int)position);

        }
        else if(diff=="dele"){
            list_bmi.removeElementAt((int)position);
        }
        listAdapter_bmi = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list_bmi);
        listView_bmi.setAdapter(listAdapter_bmi);
        listView_bmi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                modify_dele_bmi_position = (int) table[position];
                //Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                modify_dele_bmi();
            }
        });

        lineCardOne=(new LineCardOne((CardView)findViewById(R.id.card1), getApplicationContext(), test3, test2,"bmi"));//.init();
        lineCardOne.play();
    }

    /*private void renew_add(long lg) {
        bmi_all=dbcontent.getCount_bmi();
        float[] test2 =new float [bmi_all];
        String[] test3=new String[bmi_all];

        for(int i=1;i<=bmi_all;i++){
            dbcontent.get_bmi(i, item_bmi);
            test2[i-1]=item_bmi.bmi;
            test3[i-1]=String.valueOf(item_bmi.datetime);
        }
        //listview
        dbcontent.get_bmi(lg, item_bmi);
        //Toast.makeText(getApplicationContext(), String.valueOf(time), Toast.LENGTH_SHORT).show();
        list_bmi.add(String.valueOf(item_bmi.datetime)+" "+String.valueOf(item_bmi.height)+" "+String.valueOf(item_bmi.weight)+" "+String.valueOf(item_bmi.bmi));
        listAdapter_bmi = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list_bmi);
        listView_bmi.setAdapter(listAdapter_bmi);

        lineCardOne=(new LineCardOne((CardView)findViewById(R.id.card1), getApplicationContext(), test3, test2));//.init();
        lineCardOne.play();
    }
    private void renew_modify(long position) {
        bmi_all=dbcontent.getCount_bmi();
        float[] test2 =new float [bmi_all];
        String[] test3=new String[bmi_all];

        for(int i=1;i<=bmi_all;i++){
            dbcontent.get_bmi(i, item_bmi);
            test2[i-1]=item_bmi.bmi;
            test3[i-1]= String.valueOf(item_bmi.datetime);
        }
        //listview
        dbcontent.get_bmi(position + 1, item_bmi);
        list_bmi.setElementAt(String.valueOf(item_bmi.datetime) + " " + String.valueOf(item_bmi.height) + " " + String.valueOf(item_bmi.weight) + " " + String.valueOf(item_bmi.bmi), (int) position);
        //list_bmi.set(position, String.valueOf(item_bmi.datetime) + " " + String.valueOf(item_bmi.height) + " " + String.valueOf(item_bmi.weight) + " " + String.valueOf(item_bmi.bmi));
        listAdapter_bmi = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list_bmi);
        listView_bmi.setAdapter(listAdapter_bmi);

        lineCardOne=(new LineCardOne((CardView)findViewById(R.id.card1), getApplicationContext(), test3, test2));//.init();
        lineCardOne.play();
    }*/


}