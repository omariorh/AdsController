package com.omar.rh.adscontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.omar.rh.ads.adscontroller;

public class MainActivity extends AppCompatActivity {
    Context context = this;
    adscontroller ads = new adscontroller(this);
    int ff =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        new adscontroller.config()
                .statut(1)
                .limitAdmobBannerClicks(false).bannerMaxNum(2).bannerTimer(90)
                .limitAdmobInterClicks(false).interMaxNum(2).interTimer(90)
                .limitAdmobNativeClicks(false).nativeMaxNum(2).nativeTimer(90)
                .admob_app_id("ca-app-pub-3940256099942544")
                .admob_inter("ca-app-pub-3940256099942544/1033173712")
                .admob_banner("ca-app-pub-3940256099942544/6300978111")
                .admob_native("ca-app-pub-3940256099942544/2247696110")
                .fb_inter("")
                .Fb_banner("")
                .fb_native("");

        ads.init();
        ads.showBanners();
        ads.showNative();
    }

    public void clickme(View view) {
        ads.callBack(2,new adscontroller.adsCallback() {
            @Override
            public void adscall() {
                ads.showNative();
                ads.showBanners();
            }
        });
//                Intent intent = new Intent(MainActivity.this,MainActivity.class);
//                startActivity(intent);
    }
}
