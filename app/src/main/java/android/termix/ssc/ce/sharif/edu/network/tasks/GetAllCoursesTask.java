package android.termix.ssc.ce.sharif.edu.network.tasks;

import android.termix.ssc.ce.sharif.edu.network.NetworkTask;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;

/**
 * @author AryanAhadinia
 * @since 1
 */
public abstract class GetAllCoursesTask extends NetworkTask {

    @Override
    protected HttpUrl getURL() {
        return HttpUrl.parse(getServerUrl().concat("/api/schedule/all_courses")).newBuilder().build();
    }

    @Override
    public void run() {
        getOkHttpClient().newCall(getRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {

            }
        });
    }
}
