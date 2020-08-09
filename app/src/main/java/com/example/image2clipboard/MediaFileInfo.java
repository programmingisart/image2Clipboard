package com.example.image2clipboard;

import android.graphics.Bitmap;
import android.net.Uri;

public class  MediaFileInfo {

    private String filePath, fileType, fileName;
    private Uri fileUri;
    private Bitmap fileBitmap;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Uri getFileUri() {
        return fileUri;
    }

    public void setFileUri(Uri fileUri) {
        this.fileUri = fileUri;
    }

    public Bitmap getFileBitmap() {
        return fileBitmap;
    }

    public void setFileBitmap(Bitmap fileBitmap) {
        this.fileBitmap = fileBitmap;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


    public String toString() {
        return this.filePath;
    }

}