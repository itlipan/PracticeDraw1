package com.souche.fengche.viewdemo.fresco;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;

import com.facebook.common.memory.PooledByteStreams;
import com.facebook.common.util.ByteConstants;
import com.facebook.imageformat.DefaultImageFormats;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imagepipeline.memory.GenericByteArrayPool;
import com.facebook.imagepipeline.memory.PoolConfig;
import com.souche.fengche.viewdemo.AppInstance;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * 处理大图
 */
public class BigImageHandler {

    private static PoolConfig config = PoolConfig.newBuilder().build();
    private static PooledByteStreams pooledByteStreams =
        new PooledByteStreams(
            new GenericByteArrayPool(
                config.getMemoryTrimmableRegistry(),
                config.getSmallByteArrayPoolParams(),
                config.getSmallByteArrayPoolStatsTracker()));
    private static ActivityManager acm = (ActivityManager) AppInstance.INSTANCE.getSystemService(ACTIVITY_SERVICE);

    public static PooledByteStreams getPooledByteStreams(){
        return pooledByteStreams;
    }


    @Nullable
    public static InputStream handleImg(Pair<ImageFormat, Pair<Integer, Integer>> imageInfoPair, ByteArrayOutputStream baos) {
        ImageFormat imageFormat = imageInfoPair.first;
        Pair<Integer,Integer> sizeInfo = imageInfoPair.second;
        if (imageFormat == null || sizeInfo == null) return null;

        Bitmap.CompressFormat bmFormat = null;
        int imgSize = 0;
        if (imageFormat == DefaultImageFormats.PNG) {
            bmFormat = Bitmap.CompressFormat.PNG;
            imgSize = sizeInfo.first * sizeInfo.second * 4;
        }else if (imageFormat == DefaultImageFormats.JPEG) {
            bmFormat = Bitmap.CompressFormat.JPEG;
            imgSize = sizeInfo.first * sizeInfo.second * 3;
        }else {
            bmFormat = Bitmap.CompressFormat.WEBP;
            imgSize = sizeInfo.first * sizeInfo.second * 3;
        }

        if (imgSize == 0) return null;

        if (checkOOM(imgSize)) {
            final ByteArrayOutputStream baosBmOut = new ByteArrayOutputStream();
            try {
                final InputStream origin = new ByteArrayInputStream(baos.toByteArray());
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inSampleSize = 4;
                //
                Bitmap bm = BitmapFactory.decodeStream(origin,null,options);
                bm.compress(bmFormat,80,baosBmOut);
                bm.recycle();
                origin.close();
            } catch (IOException e) {
                return null;
            }
            return new ByteArrayInputStream(baosBmOut.toByteArray());
        }
        return null;
    }

    /**
     * 判断可用内存 OOM 问题
     * @param imgSize
     * @return true 超过应用内存限制,可能导致 OOM
     */
    private static boolean checkOOM(int imgSize) {
        final int maxMemory = Math.min(acm.getMemoryClass() * ByteConstants.MB, Integer.MAX_VALUE);
        return (imgSize / 1024 / 1024) > (maxMemory/2);
    }
}
