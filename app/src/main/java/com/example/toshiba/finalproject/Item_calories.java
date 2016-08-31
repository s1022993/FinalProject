package com.example.toshiba.finalproject;

/**
 * Created by Toshiba on 2016/8/23.
 */
public class Item_calories {
    public long id;
    public int datetime;
    public int step;
    public float calories;
    public Item_calories(int datetime,int step){
        this.datetime=datetime;
        this.step=step;
    }
    public int getDatetime(){
        return datetime;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

}
