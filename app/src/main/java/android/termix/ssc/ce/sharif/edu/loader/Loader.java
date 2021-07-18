package android.termix.ssc.ce.sharif.edu.loader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author AryanAhadinia
 * @since 1
 */
public abstract class Loader<T> implements Runnable {
    protected List<Source> sources;

    private final ArrayList<Source> outOfControlSources;
    private boolean waitingForPrior;

    public Loader(Source[] sources) {
        this.sources = Arrays.asList(sources);
        this.outOfControlSources = new ArrayList<>();
        this.waitingForPrior = true;
    }

    public synchronized void postResult(Source source, T loaded, Source... revokeControlsFrom) {
        if (!outOfControlSources.contains(source)) {
            if (waitingForPrior) {
                onPriorLoad(loaded);
                waitingForPrior = false;
            } else {
                onPosteriorLoad(loaded);
            }
            outOfControlSources.addAll(Arrays.asList(revokeControlsFrom));
        }
    }

    public abstract void onPriorLoad(T loaded);

    public abstract void onPosteriorLoad(T loaded);

    public abstract void onFail(Exception e);
}
