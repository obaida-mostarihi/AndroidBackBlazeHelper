/*
 * Created by Obaida Al Mostarihi  3/17/21 10:10 AM
 * Copyright (c) 2021 . All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "LICENSE");
 * Last modified:  3/17/21 9:50 AM
 * Contact: Email : obaidaalmostarihi@gmail.com
 * Package: BackBlaze_Helper.AndroidBackBlazeHelper / BackBlaze Helper
 */

package com.assembliers.androidbackblazehelper.client;

import org.json.JSONObject;

public interface ClientListener {

       void onConnectionStarted();

       void onRetrievingData(ClientModel data, JSONObject response);

       void onFailure(Exception e);


}

