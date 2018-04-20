package com.example.justin.sinacopied.io;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;

public class SaveFile {
    private Context context;
    private FileOutputStream fileOutputStream;

    public SaveFile(Context context) {
        this.context = context;
    }

    public boolean saveToExternal(File path, byte[] data) {
        boolean flag = false;
        try {
            fileOutputStream = new FileOutputStream(path);
            fileOutputStream.write(data, 0, data.length);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return flag;
    }
}
