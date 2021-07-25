package android.termix.ssc.ce.sharif.edu.network.tasks;

import android.termix.ssc.ce.sharif.edu.model.Course;
import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.NetworkTask;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;

/**
 * @author AryanAhadinia
 * @since 1
 */
public abstract class GetMySelectionsTask extends NetworkTask<ArrayList<Course.CourseIdentifier>> {
    @Override
    protected HttpUrl getURL() {
        return HttpUrl.parse(getServerUrl().concat("/api/schedule/my_selections")).newBuilder().build();
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
                        JSONArray resultJsonArray = new JSONArray(response.body().string());
                        response.body().close();
                        ArrayList<Course.CourseIdentifier> courseIdentifiers = new ArrayList<>();
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            courseIdentifiers.add(new Course.CourseIdentifier(
                                    resultJsonArray.getJSONObject(i).getInt("courseId"),
                                    resultJsonArray.getJSONObject(i).getInt("groupId")));
                        }
                        onResult(courseIdentifiers);
                    } else {
                        final HashMap<Integer, String> errorMessages = new HashMap<>();
                        errorMessages.put(403, "دسترسی لازم را ندارید.");
                        errorMessages.put(500, "مشکلات داخلی سرور، لطفا مجددا تلاش کنید.");
                        onException(new NetworkException(errorMessages.getOrDefault(response.code(),
                                "دوباره تلاش کنید."),
                                response.code()));
                    }
                } catch (IOException | JSONException e) {
                    onError(e);
                }
            }
        });
    }
}
