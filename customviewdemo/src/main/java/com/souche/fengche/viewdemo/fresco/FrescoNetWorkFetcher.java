package com.souche.fengche.viewdemo.fresco;

import android.net.Uri;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Pair;

import com.facebook.common.logging.FLog;
import com.facebook.common.memory.PooledByteStreams;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpNetworkFetcher;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.GenericByteArrayPool;
import com.facebook.imagepipeline.memory.PoolConfig;
import com.facebook.imagepipeline.producers.BaseNetworkFetcher;
import com.facebook.imagepipeline.producers.BaseProducerContextCallbacks;
import com.facebook.imagepipeline.producers.Consumer;
import com.facebook.imagepipeline.producers.ProducerContext;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class FrescoNetWorkFetcher extends
    BaseNetworkFetcher<OkHttpNetworkFetcher.OkHttpNetworkFetchState> {

    private static final String TAG = "OkHttpNetworkFetchProducer";
    private static final String QUEUE_TIME = "queue_time";
    private static final String FETCH_TIME = "fetch_time";
    private static final String TOTAL_TIME = "total_time";
    private static final String IMAGE_SIZE = "image_size";

    private final Call.Factory mCallFactory;

    private Executor mCancellationExecutor;

    /**
     * @param okHttpClient client to use
     */
    public FrescoNetWorkFetcher(OkHttpClient okHttpClient) {
        this(okHttpClient, okHttpClient.dispatcher().executorService());
    }

    /**
     * @param callFactory custom {@link Call.Factory} for fetching image from the network
     * @param cancellationExecutor executor on which fetching cancellation is performed if
     * cancellation is requested from the UI Thread
     */
    public FrescoNetWorkFetcher(Call.Factory callFactory, Executor cancellationExecutor) {
        mCallFactory = callFactory;
        mCancellationExecutor = cancellationExecutor;
    }

    @Override
    public OkHttpNetworkFetcher.OkHttpNetworkFetchState createFetchState(
        Consumer<EncodedImage> consumer,
        ProducerContext context) {
        return new OkHttpNetworkFetcher.OkHttpNetworkFetchState(consumer, context);
    }



    @Override
    public void fetch(final OkHttpNetworkFetcher.OkHttpNetworkFetchState fetchState, final Callback callback) {
        fetchState.submitTime = SystemClock.elapsedRealtime();
        final Uri uri = fetchState.getUri();
        final Request request = new Request.Builder()
            .cacheControl(new CacheControl.Builder().noStore().build())
            .url(uri.toString())
            .get()
            .build();
        fetchWithRequest(fetchState, callback, request);
    }

    @Override
    public void onFetchCompletion(OkHttpNetworkFetcher.OkHttpNetworkFetchState fetchState, int byteSize) {
        fetchState.fetchCompleteTime = SystemClock.elapsedRealtime();
    }

    @Override
    public Map<String, String> getExtraMap(OkHttpNetworkFetcher.OkHttpNetworkFetchState fetchState, int byteSize) {
        Map<String, String> extraMap = new HashMap<>(4);
        extraMap.put(QUEUE_TIME, Long.toString(fetchState.responseTime - fetchState.submitTime));
        extraMap.put(FETCH_TIME, Long.toString(fetchState.fetchCompleteTime - fetchState.responseTime));
        extraMap.put(TOTAL_TIME, Long.toString(fetchState.fetchCompleteTime - fetchState.submitTime));
        extraMap.put(IMAGE_SIZE, Integer.toString(byteSize));
        return extraMap;
    }

    protected void fetchWithRequest(
        final OkHttpNetworkFetcher.OkHttpNetworkFetchState fetchState,
        final Callback callback,
        final Request request) {
        final Call call = mCallFactory.newCall(request);

        fetchState.getContext().addCallbacks(
            new BaseProducerContextCallbacks() {
                @Override
                public void onCancellationRequested() {
                    if (Looper.myLooper() != Looper.getMainLooper()) {
                        call.cancel();
                    } else {
                        mCancellationExecutor.execute(new Runnable() {
                            @Override public void run() {
                                call.cancel();
                            }
                        });
                    }
                }
            });

    call.enqueue(
        new okhttp3.Callback() {
          @Override
          public void onResponse(Call call, Response response) {
            fetchState.responseTime = SystemClock.elapsedRealtime();
            final ResponseBody body = response.body();
            try {
              if (!response.isSuccessful()) {
                handleException(
                    call, new IOException("Unexpected HTTP code " + response), callback);
                return;
              }

              long contentLength = body.contentLength();
              if (contentLength < 0) {
                contentLength = 0;
              }
              if (contentLength == 0) {
                callback.onResponse(body.byteStream(), (int) contentLength);
              } else if (contentLength
                  <= 1024 * 1024 * 2) { // 2M 临界, 只是图片预估处理,其实本质图片在内存的大小与外存中大小无直接关系,外存中不同文件有压缩率的处理
                callback.onResponse(body.byteStream(), (int) contentLength);
              } else {
                  // copy is
                  ByteArrayOutputStream baos = null;
                  InputStream newis = null;

                  try {
                      baos= new ByteArrayOutputStream();
                      BigImageHandler.getPooledByteStreams().copy(body.byteStream(), baos);

                      // handle inputstream copy
                      Pair<ImageFormat, Pair<Integer, Integer>> imageInfoPair =
                          BigImageParser.parseMetaData(new ByteArrayInputStream(baos.toByteArray()));
                      newis = BigImageHandler.handleImg(imageInfoPair, baos);
                      if (newis == null) {
                        callback.onResponse(new ByteArrayInputStream(baos.toByteArray()), (int) contentLength);
                      } else {
                        callback.onResponse(newis, newis.available());
                      }
                  } finally {
                      if (baos != null) baos.close();

                      if (newis != null) newis.close();
                  }
              }
            } catch (Exception e) {
              handleException(call, e, callback);
            } finally {
              try {
                body.close();
              } catch (Exception e) {
                FLog.w(TAG, "Exception when closing response body", e);
              }
            }
          }

          @Override
          public void onFailure(Call call, IOException e) {
            handleException(call, e, callback);
          }
        });
    }

    /**
     * Handles exceptions.
     *
     * <p> OkHttp notifies callers of cancellations via an IOException. If IOException is caught
     * after request cancellation, then the exception is interpreted as successful cancellation
     * and onCancellation is called. Otherwise onFailure is called.
     */
    private void handleException(final Call call, final Exception e, final Callback callback) {
        if (call.isCanceled()) {
            callback.onCancellation();
        } else {
            callback.onFailure(e);
        }
    }
}
