package android.termix.ssc.ce.sharif.edu.network.tasks;

import android.termix.ssc.ce.sharif.edu.network.NetworkTask;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;

/**
 * @author AryanAhadinia
 * @since 1
 */
public abstract class TestTokenTask extends NetworkTask {
    @Override
    protected HttpUrl getURL() {
        return HttpUrl.parse(getServerUrl().concat("/test/token")).newBuilder().build();
    }

    @Override
    public void run() {
        getOkHttpClient().newCall(getRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                onError(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    onResult(null);
                } else {
                    onException(null);
                }
            }
        });
    }
}
