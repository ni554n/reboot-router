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

import android.support.annotation.NonNull;

/**
 * Presenter acts as a middle-man.
 * It gets work requests from the view and pass them to the Interactor.
 * It can't contain any information of Views or any reference from Android Framework.
 */
public class RouterPresenter implements RouterContract.Presenter,
        RouterContract.Interactor.FeedbackListener {

    private RouterContract.View routerView;
    private RouterContract.Interactor routerInteractor;
    private RouterContract.Repository routerRepository;

    public RouterPresenter(@NonNull RouterContract.View routerView) {
        this.routerView = routerView;
        routerRepository = new RouterRepository(routerView.getContext());
        routerInteractor = new RouterInteractor(routerRepository, this);
    }

    @Override
    public void setupFields() {
        if (!Utils.isWifiConnected(routerView.getContext())) {
            onFeedback(R.string.no_wifi);
        }

        routerView.setFields(routerRepository.getFields());
    }

    @Override
    public void rebootRouter(RouterFields routerFields, boolean hasChanged) {
        if (hasChanged) {
            if (routerInteractor.validateData(routerFields)) {
                routerInteractor.reboot(routerFields);
            } else {
                onFeedback(R.string.empty_fields);
            }
        } else {
            routerInteractor.reboot(routerFields);
        }
    }

    @Override
    public void onFeedback(int resId) {
        routerView.showMessage(resId);
    }

    @Override
    public void destroyInstances() {
        routerView = null;
        routerRepository = null;
        routerInteractor = null;
    }
}
