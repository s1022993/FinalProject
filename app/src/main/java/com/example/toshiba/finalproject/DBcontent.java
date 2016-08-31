package com.example.toshiba.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Toshiba on 2016/7/20.
 */
public class DBcontent {
    public static final String TABLE_NAME_WALK = "walk";
    public static final String WALK_KEY_ID = "_id";// 編號表格欄位名稱，固定不變
    public static final String WALK_DATETIME_COLUMN = "datetime";// 其它表格欄位名稱
    public static final String WALK_STEP_COLUMN = "step";
    public static final String WALK_CALORIES_COLUMN = "calories";


    public static final String TABLE_NAME_BMI = "bmi";
    public static final String BMI_KEY_ID = "_id";
    public static final String BMI_DATETIME_COLUMN = "datetime";// 其它表格欄位名稱
    public static final String BMI_HEIGHT_COLUMN = "height";
    public static final String BMI_WEIGHT_COLUMN = "weight";
    public static final String BMI_BMIVALUE_COLUMN = "bmivalue";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE_WALK =
            "CREATE TABLE " + TABLE_NAME_WALK + " (" +
                    WALK_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    WALK_DATETIME_COLUMN + " INTEGER NOT NULL, " +
                    WALK_STEP_COLUMN + " INTEGER NOT NULL, " +
                    WALK_CALORIES_COLUMN + " REAL NOT NULL)";

    public static final String CREATE_TABLE_BMI =
            "CREATE TABLE " + TABLE_NAME_BMI + " (" +
                    BMI_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    BMI_DATETIME_COLUMN + " INTEGER NOT NULL, " +
                    BMI_HEIGHT_COLUMN + " REAL NOT NULL, " +
                    BMI_WEIGHT_COLUMN + " REAL NOT NULL, " +
                    BMI_BMIVALUE_COLUMN + " REAL NOT NULL)" ;
    // 資料庫物件
    private SQLiteDatabase db;
    // 建構子
    public DBcontent(Context context) {
        db=MyDBHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    public Item_calories insert_walk(Item_calories item_calories) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();
        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料

        float high = 0,weight=0;
        Cursor cur,cur2;
        //cur =db.rawQuery("SELECT * FROM " + TABLE_NAME_WALK, null);
        cur2 =db.rawQuery("SELECT * FROM " + TABLE_NAME_BMI, null);
        Log.e("all", String.valueOf(getCount_bmi()));
        if(cur2.moveToLast()){
            high=cur2.getFloat(2);
            weight=cur2.getFloat(3);

            Log.e("high", String.valueOf(high));
            Log.e("weight", String.valueOf(weight));
        }
        //high=150;
        //weight=45;
        float calories= (float) ((item_calories.step*(high-100)/100)/4500*weight*3.1);
        calories=(float)(Math.round(calories*100))/100;

        cv.put(WALK_DATETIME_COLUMN, item_calories.datetime);
        cv.put(WALK_STEP_COLUMN, item_calories.step);
        cv.put(WALK_CALORIES_COLUMN, calories);

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME_WALK, null, cv);

        // 設定編號
        item_calories.setId(id);
        item_calories.setCalories(calories);
        // 回傳結果
        return item_calories;
    }

    public Item_bmi insert_bmi(Item_bmi item_bmi) {
        float bmivalue=0;
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();
        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(BMI_DATETIME_COLUMN, item_bmi.datetime);
        cv.put(BMI_HEIGHT_COLUMN, item_bmi.height);
        cv.put(BMI_WEIGHT_COLUMN, item_bmi.weight);
        bmivalue=item_bmi.weight/((item_bmi.height/100)*(item_bmi.height/100));
        bmivalue=(float)(Math.round(bmivalue*100))/100;
        cv.put(BMI_BMIVALUE_COLUMN, bmivalue);
        //Log.e("2", String.valueOf(item_bmi.height));
        //Log.e("3",String.valueOf(item_bmi.weight));
        //Log.e("ano",String.valueOf((item_bmi.height/100)));
        //Log.e("4",String.valueOf(bmivalue));

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME_BMI, null, cv);

        // 設定編號
        item_bmi.setId(id);
        item_bmi.setBmi(bmivalue);
        // 回傳結果
        return item_bmi;
    }

    public boolean update_walk(long id,int datetime, int step, int calories) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的修改資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(BMI_HEIGHT_COLUMN, datetime);
        cv.put(BMI_WEIGHT_COLUMN, step);
        cv.put(BMI_BMIVALUE_COLUMN, calories);

        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = WALK_KEY_ID + "=" + id;

        // 執行修改資料並回傳修改的資料數量是否成功
        return db.update(TABLE_NAME_WALK, cv, where, null) > 0;
    }

    public Item_bmi update_bmi(long id,Item_bmi item_bmi) {
        float bmivalue=0;
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的修改資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(BMI_DATETIME_COLUMN, item_bmi.datetime);
        cv.put(BMI_HEIGHT_COLUMN, item_bmi.height);
        cv.put(BMI_WEIGHT_COLUMN, item_bmi.weight);
        bmivalue=item_bmi.weight/((item_bmi.height/100)*(item_bmi.height/100));
        bmivalue=(float)(Math.round(bmivalue*100))/100;
        cv.put(BMI_BMIVALUE_COLUMN, bmivalue);

        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = BMI_KEY_ID + "=" + id;

        // 執行修改資料並回傳修改的資料數量是否成功

        db.update(TABLE_NAME_BMI, cv, where, null) ;
        item_bmi.setId(id);
        item_bmi.setBmi(bmivalue);
        return  item_bmi;
    }

    public boolean delete_walk(long id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = WALK_KEY_ID + "=" + id;
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME_WALK, where, null) > 0;
    }

    public boolean delete_bmi(long id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = BMI_KEY_ID + "=" + id;
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME_BMI, where, null) > 0;
    }

    public long[] make_bmi_table(long[] arr) {
        Cursor cur;
        int i=0;
        cur =db.rawQuery("SELECT * FROM " + TABLE_NAME_BMI, null);
        if(cur.getCount()!=0) {
            if(cur.moveToFirst()) {
                do {
                    arr[i]=cur.getLong(0);
                    i++;
                } while(cur.moveToNext());
            }
        }
        return arr;
    }
    public float[] make_table_hour(float[] cal){
        int year,month,day,hour;
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH)+1;
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        String str=String.valueOf(year);
        if(month<10){
            str+="0"+month;
        }
        else{
            str+=month;
        }
        if(day<10){
            str+="0"+day;
        }
        else{
            str+=day;
        }
        Cursor cur;
        cur =db.rawQuery("SELECT * FROM " + TABLE_NAME_WALK, null);
        if(cur.getCount()!=0) {
            if(cur.moveToFirst()) {
                do {
                    if(cur.getInt(1)/100==Integer.parseInt(str)){
                        int tim=cur.getInt(1)%100;
                        if(tim==1||tim==2){
                            cal[0]+=cur.getFloat(3);
                        }
                        else if(tim==3||tim==4){
                            cal[0]+=cur.getFloat(3);
                        }
                        else if(tim==5||tim==6){
                            cal[0]+=cur.getFloat(3);
                        }
                        else if(tim==7||tim==8){
                            cal[0]+=cur.getFloat(3);
                        }
                        else if(tim==9||tim==10){
                            cal[0]+=cur.getFloat(3);
                        }
                        else if(tim==11||tim==12){
                            cal[0]+=cur.getFloat(3);
                        }
                    }

                } while(cur.moveToNext());
            }
        }
        return cal;
    }
    public float[] make_table_week(float[] cal){
        int year,month,day,hour;
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH)+1;
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        String str=String.valueOf(year);
        if(month<10){
            str+="0"+month;
        }
        else{
            str+=month;
        }
        Cursor cur;
        cur =db.rawQuery("SELECT * FROM " + TABLE_NAME_WALK, null);
        if(cur.getCount()!=0) {
            if(cur.moveToFirst()) {
                do {
                    if(cur.getInt(1)/10000==Integer.parseInt(str)){
                        int tim=(cur.getInt(1)/100)%100;
                        if(tim==day){
                            cal[6]+=cur.getFloat(3);
                        }
                        else if(tim==day-1){
                            cal[5]+=cur.getFloat(3);
                        }
                        else if(tim==day-2){
                            cal[4]+=cur.getFloat(3);
                        }
                        else if(tim==day-3){
                            cal[3]+=cur.getFloat(3);
                        }
                        else if(tim==day-4){
                            cal[2]+=cur.getFloat(3);
                        }
                        else if(tim==day-5){
                            cal[1]+=cur.getFloat(3);
                        }
                        else if(tim==day-6){
                            cal[1]+=cur.getFloat(3);
                        }
                    }

                } while(cur.moveToNext());
            }
        }
        return cal;
    }
    public float[] make_table_month(float[] cal){
        int year,month,day,hour;
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH)+1;
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        String str=String.valueOf(year);
        Cursor cur;
        cur =db.rawQuery("SELECT * FROM " + TABLE_NAME_WALK, null);
        if(cur.getCount()!=0) {
            if(cur.moveToFirst()) {
                do {
                    if(cur.getInt(1)/1000000==Integer.parseInt(str)){
                        int tim=(cur.getInt(1)/10000)%100;
                        cal[tim-1]+=cur.getFloat(3);
                    }
                } while(cur.moveToNext());
            }
        }
        return cal;
    }
    // 取得資料數量
    public int getCount_walk() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME_WALK, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        return result;
    }
    // 取得資料數量
    public int getCount_bmi() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME_BMI, null);
        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        return result;
    }

    // 取得指定編號的資料物件
    public Item_bmi get_bmi(long id,Item_bmi item_bmi) {
        // 使用編號為查詢條件
        int a=1;
        // 執行查詢
        Cursor cur;

        cur =db.rawQuery("SELECT * FROM " + TABLE_NAME_BMI, null);
        if(cur.getCount()!=0) {
            if(cur.moveToFirst()) {
                do {
                    if(cur.getLong(0)==id){
                        // 關閉Cursor物件
                        //cur.close();
                        // 回傳結果
                        break;
                    }
                    else a++;
                } while(cur.moveToNext());
            }
        }
        item_bmi.datetime=cur.getInt(1);
        item_bmi.height=cur.getFloat(2);
        item_bmi.weight=cur.getFloat(3);
        item_bmi.bmi= cur.getFloat(4);
        return item_bmi;
    }
    public void sample() {

        Item_bmi test2=new Item_bmi(20160805,170,50);
        insert_bmi(test2);
        Item_bmi test=new Item_bmi(20160801,150,45);
        insert_bmi(test);
        //insert_bmi(20160721, 160, 50);
    }
    public void sample_calories() {
        Item_calories test=new Item_calories(2016072422,50);
        insert_walk(test);
        Item_calories test2=new Item_calories(2016082423,100);
        insert_walk(test2);
        Item_calories test3=new Item_calories(2016082308,20);
        insert_walk(test3);
        Item_calories test4=new Item_calories(2016092310,200);
        insert_walk(test4);
    }

}
