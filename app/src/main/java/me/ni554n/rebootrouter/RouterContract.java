package me.ni554n.rebootrouter;

import android.content.Context;

/**
 * This interfaces represent the contracts between other classes.
 */
public interface RouterContract {

    interface View {

        void showMessage(int resId);

        void setFields(RouterFields routerFields);

        Context getContext();
    }

    interface Presenter {

        void setupFields();

        void rebootRouter(RouterFields routerFields, boolean hasChanged);

        void destroyInstances();
    }

    interface Interactor {

        boolean validateData(RouterFields routerFields);

        boolean isValid(RouterFields routerFields);

        void reboot(RouterFields fields);

        @FunctionalInterface
        interface FeedbackListener {
            void onFeedback(int resId);
        }
    }

    interface Repository {

        RouterFields getFields();

        String getAuth();

        void storeFields(RouterFields fields);
    }
}
