package android.termix.ssc.ce.sharif.edu.loader;

public interface LoaderCallback<T> {

    void onPriorLoad(T t);

    void onPosteriorLoad(T t);

    void onFail(Exception e);
}
