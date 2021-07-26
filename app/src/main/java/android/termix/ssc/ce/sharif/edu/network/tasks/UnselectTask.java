package android.termix.ssc.ce.sharif.edu.network.tasks;

import android.termix.ssc.ce.sharif.edu.network.NetworkTask;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author AryanAhadinia
 * @since 1
 */
public class UnselectTask {
    private final int courseId;
    private final int groupId;

    public UnselectTask(int courseId, int groupId) {
        this.courseId = courseId;
        this.groupId = groupId;
    }

    protected HttpUrl getURL() {
        return HttpUrl.parse(NetworkTask.getServerUrl().concat("/api/schedule/unselect")).newBuilder().build();
    }

    public boolean execute() {
        RequestBody requestBody = new FormBody.Builder()
                .add("courseId", String.valueOf(courseId))
                .add("groupId", String.valueOf(groupId))
                .build();
        Request request = new Request.Builder().url(getURL()).delete(requestBody).build();
        try (Response response = NetworkTask.getOkHttpClient().newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            return false;
        }
    }
}
