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
public abstract class ChangePasswordTask extends NetworkTask<Object> {
    private final String newPassword;

    public ChangePasswordTask(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    protected HttpUrl getURL() {
        return HttpUrl.parse(getServerUrl().concat("/api/user/change_password")).newBuilder().build();
    }

    @Override
    public void run() {
        RequestBody requestBody = new FormBody.Builder()
                .add("password", String.valueOf(newPassword))
                .build();
        getOkHttpClient().newCall(getPostRequest(requestBody)).enqueue(new Callback() {
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
                    errorMessages.put(401, "چنین حسابی وجود ندارد.");
                    errorMessages.put(403, "دسترسی لازم را ندارید.");
                    errorMessages.put(500, "مشکلات داخلی سرور، لطفا مجددا تلاش کنید.");
                    onException(new NetworkException(errorMessages.getOrDefault(response.code(),
                            "دوباره تلاش کنید."),
                            response.code()));
                }
            }
        });
    }
}
