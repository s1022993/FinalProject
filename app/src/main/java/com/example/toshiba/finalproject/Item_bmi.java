package com.example.toshiba.finalproject;

/**
 * Created by Toshiba on 2016/8/15.
 */
public class Item_bmi {
    public long id;
    public int datetime;
    public float height;
    public float weight;
    public float bmi;

    public Item_bmi(Long id,int datetime,float height,float weight,float bmi){

        this.id=id;
        this.datetime=datetime;
        this.height=height;
        this.weight=weight;
        this.bmi=bmi;
    }
    public Item_bmi(int datetime,float height,float weight){

        this.datetime=datetime;
        this.height=height;
        this.weight=weight;
    }
    public Item_bmi(){

        datetime=0;
        height=0;
        weight=0;
        bmi=0;
    }


    public int getDatetime(){
        return datetime;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setBmi(float bmi) {
        this.bmi = bmi;
    }
}
