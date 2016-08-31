package com.example.toshiba.finalproject;


import android.app.Fragment;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class Step extends AppCompatActivity {

    private TabLayout mTabs;
    private ViewPager mViewPager;

    DBcontent dbcontent;

    String[] label_month= {"Jan", "Fev", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    String[] label_week= {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    String[] label_hour= { "2",  "4",  "6",  "8",  "10",  "12"
            ,  "14",  "16",  "18:",  "20",  "22",  "24"};
    float[] values_month = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
    float[] values_week = {0f, 0f, 0f, 0f, 0f, 0f, 0f};
    float[] values_hour = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_step);

        dbcontent = new DBcontent(getApplicationContext());

        mTabs = (TabLayout) findViewById(R.id.tabs);
        mTabs.addTab(mTabs.newTab().setText("月"));
        mTabs.addTab(mTabs.newTab().setText("周"));
        mTabs.addTab(mTabs.newTab().setText("日"));


        //dbcontent.sample();
        dbcontent.sample_calories();

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));

        mTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //http://www.jianshu.com/p/adf7a994613a
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == mTabs.getTabAt(0)) {
                    mViewPager.setCurrentItem(0);
                } else if (tab == mTabs.getTabAt(1)) {
                    mViewPager.setCurrentItem(1);
                } else if (tab == mTabs.getTabAt(2)) {
                    mViewPager.setCurrentItem(2);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
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
                    intent.setClass(Step.this, Step.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.nav_bmi) {
                    Intent intent = new Intent();
                    intent.setClass(Step.this, BMI.class);
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
    class SamplePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return 3;
        }
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Item " + (position + 1);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //View view = getLayoutInflater().inflate(R.layout.pager_item, container, false);//http://www.cnblogs.com/zyf2013/p/3450603.html
            //container.addView(view);
            View layout =  getLayoutInflater().inflate(R.layout.pager_item, container, false);

            container.addView(layout);
            if (position == 0){

                dbcontent.make_table_month(values_month);
                (new LineCardOne((CardView) layout.findViewById(R.id.card1), layout.getContext(),label_month,values_month,"walk")).init();
            }
            else if (position == 1){
                //dbcontent.sample_calories();
                dbcontent.make_table_week(values_week);
                (new LineCardOne((CardView) layout.findViewById(R.id.card1), layout.getContext(), label_week, values_week,"walk")).init();
            }
            else if (position == 2){
                //dbcontent.sample_calories();
                dbcontent.make_table_hour(values_hour);
                (new LineCardOne((CardView) layout.findViewById(R.id.card1), layout.getContext(),label_hour,values_hour,"walk")).init();
            }
            return layout;
            // return view;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


    }
}
