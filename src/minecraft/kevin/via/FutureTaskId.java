package kevin.via;

import com.viaversion.viaversion.api.platform.PlatformTask;
import java.util.concurrent.Future;
import org.jetbrains.annotations.NotNull;

// Kotlin 2.1.0 wcnmd
public final class FutureTaskId implements PlatformTask<Future<?>> {
    @NotNull
    private final Future<?> object;

    public FutureTaskId(@NotNull Future<?> object) {
        super();
        this.object = object;
    }

    @NotNull
    @Override
    public Future<?> getObject() {
        return object;
    }

    public void cancel() {
        this.object.cancel(false);
    }
}
