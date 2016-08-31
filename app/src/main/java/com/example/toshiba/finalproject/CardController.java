package com.example.toshiba.finalproject;

import android.os.Handler;
import android.support.v7.widget.CardView;

/**
 * Created by Toshiba on 2016/8/8.
 */
public class CardController {
    //private final ImageButton mPlayBtn;
    //private final ImageButton mUpdateBtn;


    private final Runnable showAction = new Runnable() {
        @Override
        public void run() {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    show(unlockAction);
                }
            }, 500);
        }
    };

    private final Runnable unlockAction =  new Runnable() {
        @Override
        public void run() {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    unlock();
                }
            }, 500);
        }
    };


    protected boolean firstStage;


    protected CardController(CardView card){
        super();

        /*RelativeLayout toolbar = (RelativeLayout) card.findViewById(R.id.chart_toolbar);
        mPlayBtn = (ImageButton) toolbar.findViewById(R.id.play);
        mUpdateBtn = (ImageButton) toolbar.findViewById(R.id.update);

        mPlayBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss(showAction);
            }
        });

        mUpdateBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                update();
            }
        });*/
    }


    public void init(){
        show(unlockAction);
    }
    public void play(){
        //show(unlockAction);
        dismiss(showAction);
    }

    protected void show(Runnable action){
        lock();
        firstStage = false;
    }

    protected void update(){
        lock();
        firstStage = !firstStage;
    }

    protected void dismiss(Runnable action){
        lock();
    }


    private void lock(){
        //mPlayBtn.setEnabled(false);
        //mUpdateBtn.setEnabled(false);
    }

    private void unlock(){
        //mPlayBtn.setEnabled(true);
        //mUpdateBtn.setEnabled(true);
    }
}
