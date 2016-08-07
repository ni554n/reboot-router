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

public class RouterFields {

    private String gateway;
    private String username;
    private String password;

    public RouterFields(String gateway, String username, String password) {
        this.gateway = gateway;
        this.username = username;
        this.password = password;
    }

    public String getGateway() {
        return gateway;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return gateway + "/" + username + ":" + password;
    }
}
