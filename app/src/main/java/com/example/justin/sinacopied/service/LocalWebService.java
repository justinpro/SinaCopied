package com.example.justin.sinacopied.service;

import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class LocalWebService implements Runnable {
    private String TAG = "LocalWebService";

    private boolean isRunning;
    private ServerSocket serverSocket;

    private int port;
    private AssetManager assetManager;

    public LocalWebService(int port, AssetManager assetManager) {
        this.port = port;
        this.assetManager = assetManager;
    }

    public void start() {
        isRunning = true;
        new Thread(this).start();
    }

    public void stop() {
        try {
            isRunning = false;
            if (serverSocket != null) {
                serverSocket.close();
                serverSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            while (isRunning) {
                //从mPort接口获取请求
                Socket socket = serverSocket.accept();
                handle(socket);
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handle(Socket socket) throws IOException {
        BufferedReader reader = null;
        PrintStream output = null;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String route = null;
            String line;
            while (!TextUtils.isEmpty(line = reader.readLine())) {
                if (line.startsWith("GET /")) {
                    int start = line.indexOf('/') + 1;
                    int end = line.indexOf(' ', start);
                    route = line.substring(start, end);

                    /**
                     * Test
                     * route -> 20171201/doc-android0003.html
                     * route -> favicon.ico
                     */
                    Log.i(TAG, "route -> " + route);
                    break;
                }
            }
            output = new PrintStream(socket.getOutputStream());
            if (route == null) {
                writeServerError(output);
                return;
            }

            //已经获得asset的数据了
            //20171201/doc-android0003.html
            byte[] assetData = loadContent(route);
            /**
             * 没有这个200 Ok的连接成功提示也是不会成功显示的
             */
            output.println("HTTP/1.0 200 OK");

            /**
             * 下面的代码可有可无
             */
            output.println("Content-Type: " + detectMineType(route));
            output.println("Content-Length: " + assetData.length);

            /**
             * 注意了
             * println()
             * 没有这个换行符号是绝对不会显示的
             */
            output.println();
            output.write(assetData);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                output.close();
            }
            if (reader != null) {
                reader.close();
            }
        }
    }

    private byte[] loadContent(String filename) throws IOException {
        InputStream inputStream = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            //打开asset文件
            //20171201/doc-android0003.html
            inputStream = assetManager.open(filename);
            byte[] buffer = new byte[1024];
            int size;
            //开始读取asset数据
            while ((size = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, size);
            }
            byteArrayOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private String detectMineType(String filename) {
        if (TextUtils.isEmpty(filename)) {
            return null;
        } else if (filename.endsWith(".html")) {
            return "text/html";
        } else if (filename.endsWith(".js")) {
            return "application/javascript";
        } else if (filename.endsWith(".css")) {
            return "text/css";
        } else {
            return "application/octet-steam";
        }
    }

    private void writeServerError(PrintStream output) {
        output.println("HTTP/1.0 500 Internal Server");
        output.flush();
    }
}
