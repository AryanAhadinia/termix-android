package android.termix.ssc.ce.sharif.edu.network.tasks;

import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.NetworkTask;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;

public abstract class VerifyAccountTask extends NetworkTask<Object> {
    private final String target;

    public VerifyAccountTask(String target) {
        this.target = target;
    }

    @Override
    protected HttpUrl getURL() {
        return HttpUrl.parse(target).newBuilder().build();
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
                    final HashMap<Integer, String> errorMessages = new HashMap<>();
                    errorMessages.put(400, "درخواست شما معتبر نیست.");
                    errorMessages.put(403, "اعتبار لینک تایید تمام شده است.");
                    errorMessages.put(500, "مشکلات داخلی سرور، لطفا مجددا تلاش کنید.");
                    onException(new NetworkException(errorMessages.getOrDefault(response.code(),
                            "دوباره تلاش کنید."),
                            response.code()));
                }
            }
        });
    }
}
