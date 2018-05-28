package com.souche.fengche.viewdemo.fresco;

import android.util.Pair;

import com.facebook.imageformat.DefaultImageFormats;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imageformat.ImageFormatChecker;
import com.facebook.imageutils.BitmapUtil;
import com.facebook.imageutils.WebpUtil;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;

public class BigImageParser {

    /**
     * 利用输入流图片数据获取图片处理相关信息
     * @param inputStream
     * @return DefaultImageFormats && Pair<宽,高>
     */
    public static Pair<ImageFormat,Pair<Integer,Integer>> parseMetaData(InputStream inputStream) {
        final ImageFormat imageFormat = ImageFormatChecker.getImageFormat_WrapIOException(
            inputStream);
        // BitmapUtil.decodeDimensions has a bug where it will return 100x100 for some WebPs even though
        // those are not its actual dimensions
        final Pair<Integer, Integer> dimensions;
        if (DefaultImageFormats.isWebpFormat(imageFormat)) {
            dimensions = readWebPImageSize(inputStream);
        } else {
            dimensions = readImageSize(inputStream);
        }
        return new Pair<>(imageFormat,dimensions);
    }

    /**
     * We get the size from a WebP image
     */
    @Nullable
    private static Pair<Integer, Integer> readWebPImageSize(InputStream inputStream) {
        final Pair<Integer, Integer> dimensions = WebpUtil.getSize(inputStream);
        return dimensions;
    }

    /**
     * We get the size from a generic image
     */
    @Nullable
    private static Pair<Integer, Integer> readImageSize(InputStream is) {
        InputStream inputStream = null;
        Pair<Integer, Integer> dimensions = null;
        try {
            inputStream = is;
            dimensions = BitmapUtil.decodeDimensions(inputStream);
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // Head in the sand
                }
            }
        }
        return dimensions;
    }
}
