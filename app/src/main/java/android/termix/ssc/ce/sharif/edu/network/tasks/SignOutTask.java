package android.termix.ssc.ce.sharif.edu.network.tasks;

import android.termix.ssc.ce.sharif.edu.network.NetworkTask;

import okhttp3.HttpUrl;

/**
 * @author AryanAhadinia
 * @since 1
 */
public abstract class SignOutTask extends NetworkTask {

    @Override
    protected HttpUrl getURL() {
        return HttpUrl.parse(getServerUrl().concat("/api/user/sign_out")).newBuilder().build();
    }

    @Override
    public void run() {

    }
}
