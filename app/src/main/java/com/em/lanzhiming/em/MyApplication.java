package com.em.lanzhiming.em;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.em.lanzhiming.em.utils.ImageLoadProxy;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * 作者：lanzhm on 2016/7/19 09:27
 * 邮箱：18770915807@163.com
 */
public class MyApplication extends Application {
    @Override
    public void onCreate()
    {
        super.onCreate();
        //友盟推送
        initPush(getApplicationContext());

        //网络请求库
        initOkHttpUtils(getApplicationContext());

        //图片加载库
        ImageLoadProxy.initImageLoader(getApplicationContext());

//        Log.d("DEVICE_ID", DeviceUtil.getDeviceId(getApplicationContext()));
    }

    public static void initOkHttpUtils(Context context) {
        //        ClearableCookieJar cookieJar1 = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
        CookieJarImpl cookieJar1 = new CookieJarImpl(new PersistentCookieStore(context));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor("TAG"))
//                .cookieJar(cookieJar1)
                .sslSocketFactory(HttpsUtils.getSslSocketFactory(null, null, null))
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

//    public static void initImageLoader(Context context) {
//        // This configuration tuning is custom. You can tune every option, you may tune some of them,
//        // or you can create default configuration by
//        //  ImageLoaderConfiguration.createDefault(this);
//        // method.
//        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "imageloader/Cache");
//        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
//        config.threadPriority(Thread.NORM_PRIORITY - 2);
//        config.memoryCacheExtraOptions(480, 800);
//        config.denyCacheImageMultipleSizesInMemory();
//        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
//        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
//        config.tasksProcessingOrder(QueueProcessingType.LIFO);
//        config.memoryCache(new UsingFreqLimitedMemoryCache(2* 1024 * 1024)); // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
//        config.memoryCacheSize(2 * 1024 * 1024);
//        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
//        config.diskCache(new UnlimitedDiskCache(cacheDir));
//        config.diskCacheFileCount(100); //缓存的文件数量
//        config.defaultDisplayImageOptions(DisplayImageOptions.createSimple());
//        config.imageDownloader(new BaseImageDownloader(context,5 * 1000, 30 * 1000));
////        config.writeDebugLogs(); // Remove for release app
//
//        // Initialize ImageLoader with configuration.
//        ImageLoader.getInstance().init(config.build());
//    }
    public static void initPush(Context context) {
        PushAgent mPushAgent = PushAgent.getInstance(context);
        mPushAgent.enable();

        PushAgent.getInstance(context).onAppStart();

        String device_token = UmengRegistrar.getRegistrationId(context);
        Log.d("UMENG-LOG",device_token);
//        AvaDmHcp3dMfSx-_x5-yvE8Ty1LgGmYWyWQD7b-BUsZN




    }


}
