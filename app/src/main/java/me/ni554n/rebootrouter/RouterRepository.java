/*
 * Copyright 2016 Nissan Ahmed
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.ni554n.rebootrouter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

/**
 * Repository is the Model of the MVP.
 * It acts as a data repository for the Interactor.
 */
public class RouterRepository implements RouterContract.Repository {

    private static final String PREFERENCE = "me.ni554n.rebootrouter.preferences";
    private static final String GATEWAY = "gateway";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String AUTH = "auth";
    private static final String DEFAULT_GATEWAY = "0.0.0.0";
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";
    private static final String DEFAULT_AUTH = "YWRtaW46YWRtaW4=";
    private Context context;
    private SharedPreferences sharedPreferences;

    public RouterRepository(Context context) {
        this.context = context;
        this.sharedPreferences = context.
                getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
    }

    @Override
    public String getAuth() {
        return sharedPreferences.getString(AUTH, DEFAULT_AUTH);
    }

    @Override
    public RouterFields getFields() {
        String gateway = sharedPreferences.getString(GATEWAY, DEFAULT_GATEWAY);
        String username = sharedPreferences.getString(USERNAME, DEFAULT_USERNAME);
        String password = sharedPreferences.getString(PASSWORD, DEFAULT_PASSWORD);

        if (gateway.equals(DEFAULT_GATEWAY)) {
            gateway = Utils.refreshGateway(context);
        }

        return new RouterFields(gateway, username, password);
    }

    @Override
    public void storeFields(RouterFields fields) {
        String gateway = fields.getGateway();
        String username = fields.getUsername();
        String password = fields.getPassword();

        String auth = username + ":" + password;
        auth = Base64.encodeToString(auth.getBytes(), Base64.DEFAULT);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GATEWAY, gateway);
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.putString(AUTH, auth);
        editor.apply();
    }
}
