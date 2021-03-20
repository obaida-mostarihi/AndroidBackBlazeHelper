/*
 * Copyright (c) 2021 . All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * Last modified:  3/19/21 6:12 PM
 * Contact: Email : obaidaalmostarihi@gmail.com
 * Package: BackBlaze_Helper.AndroidBackBlazeHelper / BackBlaze Helper
 *
 */

package com.assembliers.androidbackblazehelper.filedownloader;

public class MultiDownload {

    public String fileName;
    public String fileExtension = "";


    public MultiDownload(String fileName, String fileExtension) {
        this.fileName = fileName;
        this.fileExtension = fileExtension;
    }


    public MultiDownload(String fileName) {
        this.fileName = fileName;
    }

    public MultiDownload() {
    }



    public void init(String fileName, String fileExtension){
        this.fileName = fileName;
        this.fileExtension = fileExtension;
    }
    public void init(String fileName){
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
