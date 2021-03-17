/*
 * Created by Obaida Al Mostarihi  3/17/21 10:55 AM
 * Copyright (c) 2021 . All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "LICENSE");
 * Last modified:  3/17/21 10:55 AM
 * Contact: Email : obaidaalmostarihi@gmail.com
 * Package: BackBlaze_Helper.AndroidBackBlazeHelper / BackBlaze Helper
 */

package com.assembliers.androidbackblazehelper.interfaces;

import com.assembliers.androidbackblazehelper.models.UploadAuthModel;

public interface UploadAuthListener {

    void onResponseRetrieved(UploadAuthModel uploadAuthModel);
}
