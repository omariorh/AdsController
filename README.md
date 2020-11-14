<h3>

<h2> GRADLE </h2>

>- Add these libraries to your gradle (Module)

    implementation 'com.github.omariorh:adscontroller:2.0.0'
    implementation 'com.github.bumptech.glide:glide:4.11.0'
    implementation 'com.unity3d.ads:unity-ads:3.4.8'
    implementation 'com.facebook.android:audience-network-sdk:5.+'
    implementation 'com.google.android.gms:play-services-ads:17.1.0'
    
>- Add maven { url 'https://jitpack.io' } to your gradle (Project) inside allprojects -> repositories like this:

     allprojects {
     repositories {
         google()
         jcenter()
         maven { url 'https://jitpack.io' }  
     }
    }

<h2> MANIFEST </h2>

>- Add these persmissions in your manifest  ->

      <uses-permission android:name="android.permission.INTERNET" />
      <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
      
>- Add meta data for Admob in your manifest inside application tag  ->      

     <meta-data
        android:name="com.google.android.gms.ads.APPLICATION_ID"
        android:value="ca-app-pub-4132316188280309~1229123683" />

<h2> XML </h2>

>- To show Banner add this to your XML  ->

        <LinearLayout
        android:id="@+id/banners"
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>


>- To show Native add this to your XML  ->
            
        <LinearLayout
        android:id="@+id/natives"
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/> 

>- To show House Ads Interstitial, your XML needs to start with RelativeLayout and add this to the end of your XML just before the closing tag of RelativeLayout ->

        <LinearLayout
        android:orientation="vertical"
        android:id="@+id/housead_inter_xml"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>

<h2> JAVA CLASS </h2>

>- Add this at the beginning of your activity class 

    adscontroller ads = new adscontroller(this);

>- Add this in your onCreate method and put your data accordingly to configure you ads class, do it one time only

    new adscontroller.config()
       .statut(2)
       .admob_app_id("ca-app-pub-3940256099942544")
       .admob_inter("ca-app-pub-3940256099942544/1033173712")
       .admob_banner("ca-app-pub-3940256099942544/6300978111")
       .admob_native("ca-app-pub-3940256099942544/2247696110")
       .fb_inter("")
       .Fb_banner("")
       .fb_native("")
       .Unity_app_id("")
       .unity_inter("")
       .housead_banner("")
       .housead_inter("")
       .housead_inter_link("https://google.com")
       .Housead_banner_link("https://google.com");

>- Add this at the beginning of your activity class to initialize ads class

       ads.init();

>- Override your onBackPressed() with this code in every activity class you have

        @Override
          public void onBackPressed() {
            ads.onBakcoverride(new adscontroller.onBakcoverride() {
              @Override
                public void callBack() {
                 // TODO: --> you can replace the below code with your own code

                 // Replace ClassName with this class name
                  MainActivity.super.onBackPressed();
              }
           });
          }



>-  you can show ads depending on your config statut above

            ads.showInter();
            ads.showBanners();
            ads.showNative();

>-  OR you can specify your ads statut like that

           ads.showInter(2);
           ads.showBanners(2);
           ads.showNative(2);

>-  if you want a function to perform after showing ads

          ads.callBack(new adscontroller.adsCallback() {
                @Override
                  public void adscall() {
                     //TODO function to show after ads showed or closed
                      ads.showNative(1);
                      ads.showBanners(1);
                      Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                      startActivity(intent);
                   }
               });

>-  You can also change the upcoming statut after click performed by adding statut at the beginning of ads.callBack

          ads.callBack(2,new adscontroller.adsCallback() {
                @Override
                  public void adscall() {
                     //TODO function to show after ads showed or closed
                      Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                      startActivity(intent);
                   }
               });
               
<h2> STATUT ADS </h2>

    1 = ADMOB

    2 = FACEBOOK AUDIENCE

    3 = UNITY ADS

    4 = HOUSE ADS (yours)

</h3>
