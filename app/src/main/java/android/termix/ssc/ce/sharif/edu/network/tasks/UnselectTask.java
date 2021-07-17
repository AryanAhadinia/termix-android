package android.termix.ssc.ce.sharif.edu.network.tasks;

import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.NetworkTask;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author AryanAhadinia
 * @since 1
 */
public abstract class UnselectTask extends NetworkTask {
    private final int courseId;
    private final int groupId;

    public UnselectTask(int courseId, int groupId) {
        this.courseId = courseId;
        this.groupId = groupId;
    }

    @Override
    protected HttpUrl getURL() {
        return HttpUrl.parse(getServerUrl().concat("/api/schedule/unselect")).newBuilder().build();
    }

    @Override
    public void run() {
        RequestBody requestBody = new FormBody.Builder()
                .add("courseId", String.valueOf(courseId))
                .add("groupId", String.valueOf(groupId))
                .build();
        getOkHttpClient().newCall(getDeleteRequest(requestBody)).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                onError(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    onResult(null);
                } else {
                    final HashMap<Integer, String> errorMessages = new HashMap<>();
                    errorMessages.put(400, "درخواست شما معتبر نیست.");
                    errorMessages.put(403, "حساب کاربری تایید شده نیست.");
                    errorMessages.put(500, "مشکلات داخلی سرور، لطفا مجددا تلاش کنید.");
                    onException(new NetworkException(errorMessages.getOrDefault(response.code(),
                            "دوباره تلاش کنید."),
                            response.code()));
                }
            }
        });
    }
}
