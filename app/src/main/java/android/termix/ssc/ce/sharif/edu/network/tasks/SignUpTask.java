package android.termix.ssc.ce.sharif.edu.network.tasks;

import android.termix.ssc.ce.sharif.edu.model.Account;
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
public abstract class SignUpTask extends NetworkTask {
    private final String email;
    private final String password;

    public SignUpTask(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    protected HttpUrl getURL() {
        return HttpUrl.parse(getServerUrl().concat("/api/user/sign_up")).newBuilder().build();
    }

    @Override
    public void run() {
        RequestBody requestBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();
        getOkHttpClient().newCall(getPostRequest(requestBody)).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                onError(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    if (response.isSuccessful()) {
                        onResult(new Account(email, Account.getRole(response.body().string()), true));
                    } else {
                        final HashMap<Integer, String> errorMessages = new HashMap<>();
                        errorMessages.put(400, "درخواست شما معتبر نیست.");
                        errorMessages.put(409, "قبلا ثبت نام کرده اید.");
                        errorMessages.put(500, "مشکلات داخلی سرور، لطفا مجددا تلاش کنید.");
                        onException(new NetworkException(errorMessages.get(response.code()), response.code()));
                    }
                } catch (IOException e) {
                    onError(e);
                }
            }
        });
    }
}
