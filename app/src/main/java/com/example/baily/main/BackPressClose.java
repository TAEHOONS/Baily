package com.example.baily.main;

import android.app.Activity;
import android.widget.Toast;

public class BackPressClose {
    private long backKeyPressedTime=0;
    private Toast toast;
    private Activity activity;

    public BackPressClose(Activity context){
        this.activity=context;
    }

    public void onBackPressed(){

        if(System.currentTimeMillis()>backKeyPressedTime+2000){
            backKeyPressedTime=System.currentTimeMillis();
            showGuide();
            return;
        }
        if(System.currentTimeMillis()<=backKeyPressedTime+2000){
            backKeyPressedTime=System.currentTimeMillis();
            activity.finish();
            toast.cancel();
        }
    }
    public void showGuide(){
        toast=Toast.makeText(activity,
                "\'뒤로\' 버튼을 한번더 \n누르시면 종료 됩니다",Toast.LENGTH_SHORT);
        toast.show();
    }
}
