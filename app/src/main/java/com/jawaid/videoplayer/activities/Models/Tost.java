package com.jawaid.videoplayer.activities.Models;

import android.content.Context;
import android.widget.Toast;

public class Tost {
    public static void show(Context context,String msg){
        Toast.makeText(context,msg+" ",Toast.LENGTH_SHORT).show();
    }
    public static void showLong(Context context,String msg){
        Toast.makeText(context,msg+" ",Toast.LENGTH_LONG).show();
    }
}
