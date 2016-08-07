package me.ni554n.rebootrouter;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Interactor contains the actual business logic.
 * It can interact with Presenter or Model directly but not any View.
 * For any UI interaction, it should pass the request to the Presenter.
 */
public class RouterInteractor implements RouterContract.Interactor {

    public static final String SESSION_KEY_PATH = "/wancfg.cmd?action=view";
    public static final String REBOOT_PATH = "/rebootinfo.cgi?sessionKey=";
    private static final String PROTOCOL = "http://";
    private static final String SESSION_KEY_VARIABLE_NAME = "var sessionKey";
    private RouterContract.Repository repository;
    private FeedbackListener listener;
    private String auth;
    private String gateway;
    private String sessionKey;

    public RouterInteractor(RouterContract.Repository repository, FeedbackListener listener) {
        this.repository = repository;
        this.listener = listener;
    }

    @Override
    public boolean validateData(RouterFields routerFields) {
        if (isValid(routerFields)) {
            repository.storeFields(routerFields);
            return true;
        }

        return false;
    }

    @Override
    public boolean isValid(RouterFields fields) {
        return !(fields.getGateway().isEmpty() ||
                fields.getUsername().isEmpty() ||
                fields.getPassword().isEmpty());
    }

    /**
     * This methods requests the router with valid authentication information.
     * If login is successful, router generates a sessionKey for the session.
     * After achieving the sessionKey, it is possible to send a reboot request to the router.
     *
     * Tested on TP-LINK TD-W8950N / TD-W8950ND.
     * Should work on similar firmware.
     */
    @Override
    public void reboot(RouterFields fields) {
        gateway = fields.getGateway();
        auth = repository.getAuth();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    String response = request(new URL(PROTOCOL + gateway + SESSION_KEY_PATH));

                    int start = response.indexOf(SESSION_KEY_VARIABLE_NAME);
                    int end = response.indexOf(";", start);
                    sessionKey = response.substring(start + 18, end - 1);

                    if (!Utils.isDigitsOnly(sessionKey)) {
                        listener.onFeedback(R.string.auth_failed);
                    } else {
                        listener.onFeedback(R.string.rebooting);

                        try {
                            request(new URL(PROTOCOL + gateway + REBOOT_PATH + sessionKey));
                        } catch (IOException e) {
                            listener.onFeedback(R.string.reboot_failed);
                        }
                    }
                } catch (IOException e) {
                    listener.onFeedback(R.string.connection_failed);
                }

                return null;
            }
        }.execute();
    }

    String request(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("referer", "http://" + gateway);
            urlConnection.setRequestProperty("cookie", "Authorization=Basic " + auth);
            urlConnection.connect();

            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            return Utils.inputStreamToString(inputStream);
        } finally {
            urlConnection.disconnect();
        }
    }
}
