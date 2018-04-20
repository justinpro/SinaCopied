package com.example.justin.sinacopied.provider;

import android.content.res.AssetManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class AssetProvider {
    private AssetProvideCallback assetProvideCallback;
    private AssetManager assetManager;

    //获取asset的文件名(其实是为了配合list()这个方法)
    private String[] filenameArrays;
    //用来使用的List集合
    private List<String> filenameList;

    public AssetProvider(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public List<String> getAssetFileName(String filename) {
        try {
            filenameArrays = assetManager.list(filename);
            int len = filenameArrays.length;
            filenameList = new LinkedList<>();
            //Collections.addAll(filenameList, filenameArrays);
            for (int i = 0; i < len; i++) {
                String filter = filenameArrays[i];

                //以点为分隔符的时候，需要注意这里!!!!!!!
                String result = filter.split("\\.")[0];
                filenameList.add(result);
            }
            //因为asset里面只有3个文件，所以fileList的大小也就只有3
            return filenameList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getLoadingMoreFileName(List<String> getList, String date) {
        try {
            filenameArrays = assetManager.list(date);
            for (int i = 0; i < filenameArrays.length; i++) {
                getList.add(filenameArrays[i].split("\\.")[0]);
            }
            return getList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getRandomNews(List<String> randomData) {
        Random random = new Random();
        return randomData.get(random.nextInt(randomData.size()));
    }

    public void assetCallbackWork(AssetProvideCallback assetProvideCallback) {
        this.assetProvideCallback = assetProvideCallback;
    }

    public interface AssetProvideCallback {
        void getRandomTitle(String title);
    }
}
