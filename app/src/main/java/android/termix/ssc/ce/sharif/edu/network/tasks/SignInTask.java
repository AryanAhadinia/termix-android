package android.termix.ssc.ce.sharif.edu.network.tasks;

import android.termix.ssc.ce.sharif.edu.network.NetworkTask;

import okhttp3.HttpUrl;

/**
 * @author AryanAhadinia
 * @since 1
 */
public abstract class SignInTask extends NetworkTask {
    private final String username;
    private final String password;

    public SignInTask(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected HttpUrl getURL() {
        return null;
    }

    @Override
    public void run() {

    }
}
