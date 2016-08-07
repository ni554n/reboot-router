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
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

/**
 * Activity represents View for the MVP pattern.
 * It can't have any business logic.
 * View should request to the Presenter to do the work except UI tasks.
 */
public class RouterActivity extends AppCompatActivity
        implements RouterContract.View, View.OnClickListener, TextWatcher {

    private RouterContract.Presenter routerPresenter;
    private CoordinatorLayout coordinatorLayout;
    private TextInputEditText gatewayEditText;
    private TextInputEditText usernameEditText;
    private TextInputEditText passwordEditText;
    private RouterFields routerFields;
    private boolean fieldsHasChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_router);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.layout_coordinator);

        gatewayEditText = (TextInputEditText) findViewById(R.id.input_gateway);
        gatewayEditText.addTextChangedListener(this);

        usernameEditText = (TextInputEditText) findViewById(R.id.input_username);
        usernameEditText.addTextChangedListener(this);

        passwordEditText = (TextInputEditText) findViewById(R.id.input_password);
        passwordEditText.addTextChangedListener(this);

        routerPresenter = new RouterPresenter(this);
        routerPresenter.setupFields();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {

            if (fieldsHasChanged) {
                String gateway = gatewayEditText.getText().toString();
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                routerFields = new RouterFields(gateway, username, password);

                routerPresenter.rebootRouter(routerFields, true);
                fieldsHasChanged = false;
            } else {
                routerPresenter.rebootRouter(routerFields, false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        routerPresenter.destroyInstances();
        super.onDestroy();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setFields(RouterFields routerFields) {
        this.routerFields = routerFields;

        gatewayEditText.setText(routerFields.getGateway());
        usernameEditText.setText(routerFields.getUsername());
        passwordEditText.setText(routerFields.getPassword());
    }

    @Override
    public void showMessage(int resId) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout,
                this.getString(resId),
                Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ResourcesCompat.getColor(getResources(),
                R.color.colorPrimaryDark, null));
        snackbar.show();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        fieldsHasChanged = true;
    }
}
