package android.termix.ssc.ce.sharif.edu.loader;

/**
 * @author AryanAhadinia
 * @since 1
 */
public abstract class Loader<T> implements Runnable {
    private boolean inControl;
    private boolean priorLoaded;

    public abstract void onPriorLoad(T loaded);

    public abstract void onPosteriorLoad(T loaded);

    public abstract void onFail(Exception e);

    public void postResult(T loaded) {
        if (inControl) {
            if (priorLoaded) {
                onPosteriorLoad(loaded);
            } else {
                onPriorLoad(loaded);
            }
        }
    }
}
