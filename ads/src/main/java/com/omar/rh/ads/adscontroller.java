package com.omar.rh.ads;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import static com.facebook.ads.AdSettings.IntegrationErrorMode.INTEGRATION_ERROR_CRASH_DEBUG_MODE;
import static com.omar.rh.ads.units.Admob_app_id;
import static com.omar.rh.ads.units.Admob_banner;
import static com.omar.rh.ads.units.Admob_inter;
import static com.omar.rh.ads.units.Admob_native;
import static com.omar.rh.ads.units.Fb_banner;
import static com.omar.rh.ads.units.Fb_inter;
import static com.omar.rh.ads.units.Fb_native;
import static com.omar.rh.ads.units.bannerMaxNum;
import static com.omar.rh.ads.units.interMaxNum;
import static com.omar.rh.ads.units.interTimer;
import static com.omar.rh.ads.units.interval;
import static com.omar.rh.ads.units.limitAdmobInterClicks;
import static com.omar.rh.ads.units.limitAdmobNativeClicks;
import static com.omar.rh.ads.units.nativeMaxNum;
import static com.omar.rh.ads.units.nativeTimer;
import static com.omar.rh.ads.units.statut;
import static com.omar.rh.ads.units.limitAdmobBannerClicks;
import static com.omar.rh.ads.units.bannerTimer;
import static com.omar.rh.ads.units.userInterval;

public class adscontroller extends AppCompatActivity{

    public String TAG = this.getClass().getSimpleName();
    private TinyDB tinyDB;
    Context context;
    Activity activity;
    private config config;
    Boolean isNative = false;
    Boolean isBanner = false;

    //Admob
    public InterstitialAd interstitialAd;

    //Facebook
    public com.facebook.ads.InterstitialAd interstitialAdfb;

    // Natives
    FrameLayout nativeAdLayoutAdm;
    private com.facebook.ads.NativeAd nativeAd;
    private NativeAdLayout nativeAdLayout;
    private LinearLayout adViewNativeFb;

    //banners
    public com.google.android.gms.ads.AdView mAdView2;
    public AdRequest addRequest2;
    public LinearLayout bnlinear, adContainer, separator, mybanners;
    int hasAdView = 0;
    private com.facebook.ads.AdView adView;
    Boolean isFaceBanner = false;

    public LinearLayout nativecont;
    private adsCallback adsCallback;

    public adscontroller(Context context) {
        this.context = context;
    }



    public void init() {
        activity = (Activity) context;
        tinyDB = new TinyDB(context);

        // >> facebook
        AudienceNetworkAds.initialize(context);
        AdSettings.setIntegrationErrorMode(INTEGRATION_ERROR_CRASH_DEBUG_MODE);
        interstitialAdfb = new com.facebook.ads.InterstitialAd(context, Fb_inter);

        //Admob
        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(Admob_inter);

        setNative();
        setBanners();
        loadInterAds();

    }

    private void interCallBack() {
        try {
            adsCallback.adscall();
        } catch (Exception e) {
        }
    }

    public void setNative() {

        try {
            nativecont = this.activity.findViewById(R.id.natives);
            View view = View.inflate(context, R.layout.nativecountainer, null);
            nativecont.addView(view);
            nativeAdLayout = this.activity.findViewById(R.id.native_ad_container);
            nativeAdLayoutAdm = this.activity.findViewById(R.id.id_native_ad);
            isNative = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "You need to add a Linear Layout for native in your xml");
            isNative = false;
        }
    }


    public void showNative() {
        callNativeFunc(statut);
    }

    public void showNative(int statut) {
        callNativeFunc(statut);
    }


    private void callNativeFunc(int statut){
        if (isNative) {
            switch (statut) {
                case 1:
                    Admob_native_loader();
                    break;
                case 2:
                    loadNativeFbAd();
                    break;
                default:
                    nativeAdLayoutAdm.setVisibility(View.GONE);
                    nativeAdLayout.setVisibility(View.GONE);

            }
        }
    }

    public void setBanners() {
        try {
            //banners
            mybanners = this.activity.findViewById(R.id.banners);
            View view = View.inflate(context, R.layout.bannercountainer, null);
            mybanners.addView(view);
            adContainer = this.activity.findViewById(R.id.banner_container);
            separator = this.activity.findViewById(R.id.separator);
            bnlinear = this.activity.findViewById(R.id.bnrlinear);

            // ->Facebook
            adView = new com.facebook.ads.AdView(context, Fb_banner, AdSize.BANNER_HEIGHT_50);

            // ->Admob
            mAdView2 = new com.google.android.gms.ads.AdView(context);
            mAdView2.setAdSize(com.google.android.gms.ads.AdSize.SMART_BANNER);
            mAdView2.setAdUnitId(Admob_banner);
            isBanner = true;
        } catch (Exception e) {
            e.printStackTrace();
            isBanner = false;
        }

    }

    public void showBanners() {
        showBannersFunction(statut);
    }

    public void showBanners(int statut) {
        showBannersFunction(statut);
    }

    private void showBannersFunction(int statut){
        if (isBanner) {
            switch (Integer.valueOf(statut)) {
                case 1:
                    isFaceBanner = false;
                    admBnr();
                    adContainer.setVisibility(View.GONE);
                    separator.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    callFaceBanner();
                    break;
                default:
                    adContainer.setVisibility(View.GONE);
                    bnlinear.setVisibility(View.GONE);
                    separator.setVisibility(View.GONE);
            }
        }
    }

    private void callFaceBanner() {
        isFaceBanner = true;
        bnlinear.setVisibility(View.GONE);
        adContainer.setVisibility(View.VISIBLE);
        separator.setVisibility(View.VISIBLE);

        // Request an ad
        com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.d(TAG,"listener: onError with facebook "+isFaceBanner);

                if (hasAdView != 1) {
                    adContainer.setVisibility(View.GONE);
                    separator.setVisibility(View.GONE);
                    adContainer.removeAllViews();
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Ad loaded callback
                if (isFaceBanner){
                    bnlinear.setVisibility(View.GONE);
                    adContainer.setVisibility(View.VISIBLE);
                    separator.setVisibility(View.VISIBLE);
                    adContainer.removeAllViews();
                    adContainer.addView(adView);
                }
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG,"listener: onAdClicked");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG,"listener: onLoggingImpression with facebook "+isFaceBanner);
                hasAdView = 1;
            }
        };
        adView.loadAd();
        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
    }

    public interface adsCallback {
        void adscall();
    }

    public adscontroller callBack(int changeUpcomingStatut,adscontroller.adsCallback listener) {
        adsCallback = listener;
        intentStar(statut);
        statut = changeUpcomingStatut;
        return this;
    }

    public adscontroller callBack(adscontroller.adsCallback listener) {
        adsCallback = listener;
        intentStar(statut);
        return this;
    }

    public void loadInterAds() {
        switch (Integer.valueOf(statut)) {
            case 1:
                if (limitAdmobInterClicks) {
                    int interNumClick = tinyDB.getInt("interNumClick", 0);
                    if (interNumClick < interMaxNum) {
                        AdRequest adRequest = new AdRequest.Builder()
                                .build();
                        interstitialAd.loadAd(adRequest);
                    } else {
                        long interSavedTime = tinyDB.getLong("interTimerMili", 0L);
                        if (System.currentTimeMillis() >= interSavedTime + (long)(interTimer * 1000)) {
                            AdRequest adRequest = new AdRequest.Builder()
                                    .build();
                            interstitialAd.loadAd(adRequest);
                        }else{
                            try {
                            interstitialAdfb.loadAd();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    AdRequest adRequest = new AdRequest.Builder()
                            .build();
                    interstitialAd.loadAd(adRequest);
                }
                break;
            case 2:
                try {
                    interstitialAdfb.loadAd();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void intentStar(int statut) {
//        Toast.makeText(context, Admob_app_id, Toast.LENGTH_SHORT).show();

        immediateAds(statut);
    }

    public void showInter(){
        immediateAds(statut);
    }

    public void showInter(int statut){
        immediateAds(statut);
    }

    private void immediateAds(int statut) {
        if (interval > userInterval){
            interval = 1;
        }
        if (userInterval == interval || userInterval == 0) {
            switch (statut) {
                case 1:
                    if (limitAdmobInterClicks) {
                        int interNumClick = tinyDB.getInt("interNumClick", 0);
                        if (interNumClick < interMaxNum) {
                            callAdmobInter();
                        } else {
                            long interSavedTime = tinyDB.getLong("interTimerMili", 0L);
                            if (System.currentTimeMillis() >= interSavedTime + (long) (interTimer * 1000)) {
                                tinyDB.putInt("interNumClick", 0);
                                callAdmobInter();
                            } else {
                                callFaceInter();
                            }
                        }
                    } else {
                        callAdmobInter();
                    }

                    break;
                case 2:
                    callFaceInter();
                    break;
                default:
                    interCallBack();
                    loadInterAds();
                    interval = 1;
            }
        }else{
            interCallBack();
            loadInterAds();
        }
    }

    private void callAdmobInter(){
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            interstitialAd.show();
            interstitialAd.setAdListener(new AdListener() {
                public void onAdClosed() {
                    interCallBack();
                    loadInterAds();
                }

                @Override
                public void onAdLeftApplication() {
                    if (limitAdmobInterClicks) {
                        tinyDB.putInt("interNumClick", tinyDB.getInt("interNumClick", 0) + 1);
                        if (tinyDB.getInt("interNumClick", 0) >= interMaxNum) {
                            tinyDB.putLong("interTimerMili", System.currentTimeMillis());
                        }
                    }
                }
            });
        } else {
            interCallBack();
            loadInterAds();
        }
        interval++;
    }

    private void callFaceInter(){
        if (interstitialAdfb.isAdLoaded()) {
            interstitialAdfb.show();
            interstitialAdfb.setAdListener(new InterstitialAdListener() {
                @Override
                public void onError(Ad ad, AdError adError) {
                }

                @Override
                public void onAdLoaded(Ad ad) {
                }

                @Override
                public void onAdClicked(Ad ad) {
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                }

                @Override
                public void onInterstitialDisplayed(Ad ad) {
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    interCallBack();
                    loadInterAds();
                }
            });
        } else {
            interCallBack();
            loadInterAds();
        }
        interval++;
    }
    public void admBnr() {
        if (limitAdmobBannerClicks) {
            int bannerNumClick = tinyDB.getInt("bannerNumClick", 0);
            if (bannerNumClick < bannerMaxNum) {
                calladmobBanner();
            } else {
                long bannerSavedTime = tinyDB.getLong("bannerTimerMili", 0L);
                if (System.currentTimeMillis() >= bannerSavedTime + (long)(bannerTimer * 1000)) {
                    tinyDB.putInt("bannerNumClick", 0);
                    calladmobBanner();
                }else{
                    callFaceBanner();
                }
            }
        } else {
            calladmobBanner();
        }
    }

    private void calladmobBanner(){
        bnlinear.setVisibility(View.VISIBLE);
        addRequest2 = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        if (mAdView2.getAdSize() != null || mAdView2.getAdUnitId() != null)
            mAdView2.loadAd(addRequest2);
        // else Log state of adsize/adunit
        ((LinearLayout) this.activity.findViewById(R.id.adlinear)).removeAllViews();
        ((LinearLayout) this.activity.findViewById(R.id.adlinear)).addView(mAdView2);

        mAdView2.setAdListener(new AdListener() {
            public void onAdLeftApplication() {
                if (limitAdmobBannerClicks) {
                    tinyDB.putInt("bannerNumClick", tinyDB.getInt("bannerNumClick", 0) + 1);
                    if (tinyDB.getInt("bannerNumClick", 0) >= bannerMaxNum) {
                        tinyDB.putLong("bannerTimerMili", System.currentTimeMillis());
                        callFaceBanner();
                    }
                }
            }
        });
    }


    public void loadNativeFbAd() {
        nativeAdLayoutAdm.setVisibility(View.GONE);
        nativeAdLayout.setVisibility(View.VISIBLE);
        nativeAd = new com.facebook.ads.NativeAd(context, Fb_native);

        nativeAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
                Log.e("ads", "Native ad finished downloading all assets.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
                Log.e("ads", "Native ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Native ad is loaded and ready to be displayed
                Log.d("ads", "Native ad is loaded and ready to be displayed!");
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                // Inflate Native Ad into Container

                nativeAdLayout.removeAllViews();
                inflateAd(nativeAd);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                Log.d("ads", "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
                Log.d("ads", "Native ad impression logged!");
            }
        });
        // Request an ad
        nativeAd.loadAd();
    }

    private void inflateAd(NativeAd nativeAd) {

        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
        nativeAdLayout = this.activity.findViewById(R.id.native_ad_container);
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        adViewNativeFb = (LinearLayout) inflater.inflate(R.layout.nativefacebook, nativeAdLayout, false);
        nativeAdLayout.addView(adViewNativeFb);

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = this.activity.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(context, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        MediaView nativeAdIcon = adViewNativeFb.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adViewNativeFb.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adViewNativeFb.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adViewNativeFb.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adViewNativeFb.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adViewNativeFb.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adViewNativeFb.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adViewNativeFb,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews);
    }



    public void Admob_native_loader() {
        if (limitAdmobNativeClicks) {
            int nativeNumClick = tinyDB.getInt("nativeNumClick", 0);
            if (nativeNumClick < nativeMaxNum) {
                calladmvtv();
            } else {
                long nativeSavedTime = tinyDB.getLong("nativeTimerMili", 0L);
                if (System.currentTimeMillis() >= nativeSavedTime + (long)(nativeTimer * 1000)) {
                    tinyDB.putInt("nativeNumClick", 0);
                    calladmvtv();
                }else{
                    this.showNative(2);
                }
            }
        } else {
            calladmvtv();
        }

    }
    private void calladmvtv(){
        nativeAdLayout.setVisibility(View.GONE);
        nativeAdLayoutAdm.setVisibility(View.VISIBLE);
        AdLoader adLoader = new AdLoader.Builder(context, Admob_native)
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        //the native ad will be available inside this method  (unifiedNativeAd)
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        UnifiedNativeAdView unifiedNativeAdView = (UnifiedNativeAdView) inflater.inflate(R.layout.nativeadmob, null);
                        mapUnifiedNativeAdToLayout(unifiedNativeAd, unifiedNativeAdView);

                        nativeAdLayoutAdm.removeAllViews();
                        nativeAdLayoutAdm.addView(unifiedNativeAdView);
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        nativeAdLayoutAdm.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAdLeftApplication() {
                        super.onAdLeftApplication();
                        if (limitAdmobNativeClicks) {
                            tinyDB.putInt("nativeNumClick", tinyDB.getInt("nativeNumClick", 0) + 1);
                            if (tinyDB.getInt("nativeNumClick", 0) >= nativeMaxNum) {
                                tinyDB.putLong("nativeTimerMili", System.currentTimeMillis());
                                showNative(2);
                            }
                        }
                    }
                })
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void mapUnifiedNativeAdToLayout(UnifiedNativeAd adFromGoogle, UnifiedNativeAdView myAdView) {
        com.google.android.gms.ads.formats.MediaView mediaView = myAdView.findViewById(R.id.ad_media);
        myAdView.setMediaView(mediaView);

        myAdView.setHeadlineView(myAdView.findViewById(R.id.ad_headline));
        myAdView.setBodyView(myAdView.findViewById(R.id.ad_body));
        myAdView.setCallToActionView(myAdView.findViewById(R.id.ad_call_to_action));
        myAdView.setIconView(myAdView.findViewById(R.id.ad_icon));
        myAdView.setPriceView(myAdView.findViewById(R.id.ad_price));
        myAdView.setStoreView(myAdView.findViewById(R.id.ad_store));
        myAdView.setAdvertiserView(myAdView.findViewById(R.id.ad_advertiser));

        ((TextView) myAdView.getHeadlineView()).setText(adFromGoogle.getHeadline());

        if (adFromGoogle.getBody() == null) {
            myAdView.getBodyView().setVisibility(View.GONE);
        } else {
            ((TextView) myAdView.getBodyView()).setText(adFromGoogle.getBody());
        }

        if (adFromGoogle.getCallToAction() == null) {
            myAdView.getCallToActionView().setVisibility(View.GONE);
        } else {
            ((Button) myAdView.getCallToActionView()).setText(adFromGoogle.getCallToAction());
        }

        if (adFromGoogle.getIcon() == null) {
            myAdView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) myAdView.getIconView()).setImageDrawable(adFromGoogle.getIcon().getDrawable());
        }

        if (adFromGoogle.getPrice() == null) {
            myAdView.getPriceView().setVisibility(View.GONE);
        } else {
            ((TextView) myAdView.getPriceView()).setText(adFromGoogle.getPrice());
        }


        if (adFromGoogle.getStore() == null) {
            myAdView.getStoreView().setVisibility(View.GONE);
        } else {
            ((TextView) myAdView.getStoreView()).setText(adFromGoogle.getStore());
        }

        if (adFromGoogle.getAdvertiser() == null) {
            myAdView.getAdvertiserView().setVisibility(View.GONE);
        } else {
            ((TextView) myAdView.getAdvertiserView()).setText(adFromGoogle.getAdvertiser());
        }

        myAdView.setNativeAd(adFromGoogle);
    }



    public static class config {
        public config() {
        }

        public config admob_app_id(String admob_app_id) {
            Admob_app_id = admob_app_id;
            return this;
        }

        public config limitAdmobBannerClicks(Boolean limitadmobBannerClicks) {
            limitAdmobBannerClicks = limitadmobBannerClicks;
            return this;
        }

        public config bannerMaxNum(int banner_MaxNum) {
            bannerMaxNum = banner_MaxNum;
            return this;
        }

        public config bannerTimer(int bannertimer) {
            bannerTimer = bannertimer;
            return this;
        }

        public config limitAdmobInterClicks(Boolean limitadmobinterClicks) {
            limitAdmobInterClicks = limitadmobinterClicks;
            return this;
        }

        public config interMaxNum(int inter_MaxNum) {
            interMaxNum = inter_MaxNum;
            return this;
        }

        public config interTimer(int intertimer) {
            interTimer = intertimer;
            return this;
        }

        public config limitAdmobNativeClicks(Boolean limitAdmobnativeClicks) {
            limitAdmobNativeClicks = limitAdmobnativeClicks;
            return this;
        }

        public config nativeMaxNum(int native_MaxNum) {
            nativeMaxNum = native_MaxNum;
            return this;
        }

        public config nativeTimer(int nativetimer) {
            nativeTimer = nativetimer;
            return this;
        }

        public config admob_inter(String admob_inter) {
            Admob_inter = admob_inter;
            return this;
        }

        public config admob_banner(String admob_banner) {
            Admob_banner = admob_banner;
            return this;
        }

        public config admob_native(String admob_native) {
            Admob_native = admob_native;
            return this;
        }

        public config fb_inter(String fb_inter) {
            Fb_inter = fb_inter;
            return this;
        }

        public config Fb_banner(String fb_banner) {
            Fb_banner = fb_banner;
            return this;
        }

        public config fb_native(String fb_native) {
            Fb_native = fb_native;
            return this;
        }
//
        public config statut(int statut) {
            units.statut = statut;
            return this;
        }

        public config interval(int userInterval) {
            units.userInterval = userInterval;
            return this;
        }

//
        public config privacy_url(String privacy_url) {
            units.privacy_url = privacy_url;
            return this;
        }

    }

}
