package android.termix.ssc.ce.sharif.edu.network.tasks;

import android.termix.ssc.ce.sharif.edu.model.Account;
import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.NetworkTask;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;

/**
 * @author AryanAhadinia
 * @since 1
 */
public abstract class GetMyAccountTask extends NetworkTask {
    @Override
    protected HttpUrl getURL() {
        return HttpUrl.parse(getServerUrl().concat("/api/user/my_account")).newBuilder().build();
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
                        JSONObject resultJsonObject = new JSONObject(response.body().string());
                        Account account = new Account(
                                resultJsonObject.getString("email"),
                                Account.getRole(resultJsonObject.getString("role")),
                                resultJsonObject.getBoolean("verified"));
                        onResult(account);
                    } else {
                        final HashMap<Integer, String> errorMessages = new HashMap<>();
                        errorMessages.put(401, "چنین حسابی وجود ندارد.");
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
