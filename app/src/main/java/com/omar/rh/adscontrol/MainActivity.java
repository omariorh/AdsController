package com.omar.rh.adscontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ironsource.mediationsdk.IronSource;
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
                .statut(6)
                .limitAdmobBannerClicks(false).bannerMaxNum(2).bannerTimer(90)
                .limitAdmobInterClicks(false).interMaxNum(2).interTimer(90)
                .limitAdmobNativeClicks(false).nativeMaxNum(2).nativeTimer(90)
                .admob_app_id("ca-app-pub-3940256099942544")
                .admob_inter("ca-app-pub-3940256099942544/1033173712")
                .admob_banner("ca-app-pub-3940256099942544/6300978111")
                .admob_native("ca-app-pub-3940256099942544/2247696110")
                .fb_inter("IMG_16_9_APP_INSTALL#934233773653608_934659690277683")
                .Fb_banner("IMG_16_9_APP_INSTALL#760988004308874_760990247641983")
                .fb_native("IMG_16_9_APP_INSTALL#934233773653608_934659293611056")
                .iron_appkey("dd6036d1")
                .iron_inter("DefaultInterstitial")
                .iron_banner("DefaultBanner")
                .mopub_inter("24534e1901884e398f1253216226017e")
                .mopub_banner("b195f8dd8ded45fe847ad89ed1d016da")
                .mopub_reward("")
                .interval(1);

        ads.init();
        ads.showBanners();
        ads.showNative();
    }

    public void clickme(View view) {
        ads.callBack(new adscontroller.adsCallback() {
            @Override
            public void adscall() {
                ads.showBanners();
                Toast.makeText(context, "click called successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onResume() {
        super.onResume();
        IronSource.onResume(this);
    }
    protected void onPause() {
        super.onPause();
        IronSource.onPause(this);
    }
}
