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
