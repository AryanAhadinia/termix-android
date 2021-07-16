package android.termix.ssc.ce.sharif.edu.network.tasks;

import android.termix.ssc.ce.sharif.edu.model.Course;
import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.NetworkTask;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                onError(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    if (response.isSuccessful()) {
                        String result = response.body().string();
                        JSONObject resultJSON = new JSONObject(result);
                        HashMap<Integer, ArrayList<Course>> courses = new HashMap<>();
                        resultJSON.keys().forEachRemaining(s -> {
                            try {
                                courses.put(Integer.parseInt(s),
                                        Course.parseCourseArray(resultJSON.getJSONArray(s)));
                            } catch (JSONException e) {
                                onError(e);
                            }
                        });
                        onResult(courses);
                    } else {
                        onUnexpected(new NetworkException(response.body().string(), response.code()));
                    }
                } catch (IOException | JSONException e) {
                    onError(e);
                }
            }
        });
    }
}
