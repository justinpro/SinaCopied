package com.example.justin.sinacopied.util;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.justin.sinacopied.R;
import com.example.justin.sinacopied.adapter.HomeNewsRecycler;
import com.example.justin.sinacopied.io.IndexFileName;
import com.example.justin.sinacopied.io.SaveFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadUtil extends Service {
    private static NotificationCompat.Builder notify;
    private static NotificationManager manager;
    private static RemoteViews remoteViews;

    private static DownloadUtil downloadUtil;
    private OkHttpClient okHttpClient;

    private static boolean stopMe = false;
    private String TAG = "shit";

    //Toast 用的消息队列
    //Toast 要放到主线程中才能使用
    static Handler toastHandler = new Handler(Looper.getMainLooper());

    //通知用的消息队列
    private static Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                //停止服务的标志
                stopMe = true;

                remoteViews.setTextViewText(R.id.notification_title, "finish");

                //进度条不可见
                remoteViews.setViewVisibility(R.id.download_progress, View.GONE);
                //remoteViews.setProgressBar(R.id.download_progress, 0, 0, false);
                manager.notify(0, notify.build());
            }
            remoteViews.setProgressBar(R.id.download_progress, 100, msg.arg1, false);
            manager.notify(0, notify.build());
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        okHttpClient = new OkHttpClient();
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notify = new NotificationCompat.Builder(getApplicationContext());

        remoteViews = new RemoteViews(getApplicationContext().getPackageName()
                , R.layout.custom_notification);
        notify.setContent(remoteViews);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        download(HomeNewsRecycler.Img_Path, "1-Test");
        return super.onStartCommand(intent, flags, startId);
    }

    public static DownloadUtil get() {
        if (downloadUtil != null) {
            downloadUtil = new DownloadUtil();
        }
        return downloadUtil;
    }

    public DownloadUtil() {
    }

    //下载
    public void download(final String url, final String dir) {
        Request request = new Request.Builder().url(url).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //下载失败
                //listener.onDownloadFaild();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = null;
                ByteArrayOutputStream byteArrayOutputStream;

                long sum = 0;
                int len = 0;
                byte[] buffRead = new byte[512];
                byte[] buffWrite = null;

                SaveFile saveFile = new SaveFile(getApplicationContext());
                //建立下载目录文件夹
                String savePath = isExitsDir(dir);
                //生成下载路径
                File file = new File(savePath, IndexFileName.getFileName(url));

                if (file.exists()) {

                    toastHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext()
                                    , "文件已存在", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.i(TAG, "文件存在关闭服务");
                    stopSelf();
                    Log.i(TAG, "已经执行关闭程序");
                } else {
                    //必须初始化的3属性
                    notify.setContentTitle("Download");
                    notify.setContentText("waiting");
                    notify.setSmallIcon(R.drawable.icon);
                    manager.notify(0, notify.build());

                    try {
                        inputStream = response.body().byteStream();
                        long total = response.body().contentLength();

                        byteArrayOutputStream = new ByteArrayOutputStream();

                        Message message;

                        while ((len = inputStream.read(buffRead)) != -1) {
                            /** 写入byte数组输出流
                             *  用len是为了防止重复写入流中
                             * */
                            byteArrayOutputStream.write(buffRead, 0, len);
                            //全部赋给 没有大小 的byte数组
                            buffWrite = byteArrayOutputStream.toByteArray();

                            //进度
                            sum += len;
                            int progress = (int) (sum * 1.0f / total * 100);

                            //Log.i(TAG, "sum -> " + sum);
                            Log.i(TAG, "progress -> " + progress + "\n" + sum * 1.0f + "\n" + total * 100);
                            message = Message.obtain();
                            message.arg1 = progress;
                            handler.sendMessage(message);
                        }
                        if (saveFile.saveToExternal(file, buffWrite)) {
                            //what = 1
                            handler.sendEmptyMessage(1);
                            toastHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext()
                                            , "下载完成", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            //一定要放在后面
                            Log.i(TAG, "执行stopself");
                            stopSelf();
                            Log.i(TAG, "stopself执行完毕");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @NonNull
    private String isExitsDir(String dir) throws IOException {
        File downloadFile = new File(
                Environment.getExternalStorageDirectory().getAbsolutePath(), dir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        return downloadFile.getAbsolutePath();
    }

    public interface OnDownloadListener {
        //下载成功
        void onDownloadSuccess();

        //下载进度
        void onDownloading(int progress);

        //下载失败
        void onDownloadFaild();

        //停止服务
        void stop(boolean flag);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void stopMySelf() {
        if (stopMe) {
            Log.i(TAG, "自我终结服务");
            stopSelf();
        }
        Log.i(TAG, "退出 if");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}
