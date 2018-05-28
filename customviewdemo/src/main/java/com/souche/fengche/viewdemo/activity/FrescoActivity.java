package com.souche.fengche.viewdemo.activity;

import android.app.Application;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.DraweeConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpNetworkFetcher;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.souche.fengche.viewdemo.R;
import com.souche.fengche.viewdemo.fresco.FrescoNetWorkFetcher;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class FrescoActivity extends AppCompatActivity {

    @BindView(R.id.sdv_img)
    SimpleDraweeView mSdvImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFresco(getApplication());
        setContentView(R.layout.activity_fresco);
        ButterKnife.bind(this);
        mSdvImg.setImageURI(Uri.parse("https://cdn.gratisography.com/photos/415H.jpg"));
    }

    private void initFresco(Application application) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(@NonNull Chain chain) throws IOException {
                    final Request req = chain.request();
                    Log.d("Fresco",req.url().toString());
                    return chain.proceed(req);
                }
            })
            .build();

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(application)
            .setNetworkFetcher(new FrescoNetWorkFetcher(okHttpClient))
            .build();
        Fresco.initialize(application,config, DraweeConfig.newBuilder().build());
    }

}
