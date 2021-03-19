/*
 * Copyright (c) 2021 . All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * Last modified:  3/19/21 7:52 AM
 * Contact: Email : obaidaalmostarihi@gmail.com
 * Package: BackBlaze_Helper.AndroidBackBlazeHelper / BackBlaze Helper
 *
 */

package com.assembliers.androidbackblazehelper.upload_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerSideEncryption {

    @SerializedName("algorithm")
    @Expose
    private String algorithm;
    @SerializedName("mode")
    @Expose
    private String mode;

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

}