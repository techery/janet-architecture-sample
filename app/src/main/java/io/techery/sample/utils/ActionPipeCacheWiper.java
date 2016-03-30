package io.techery.sample.utils;

import io.techery.janet.ActionState;
import io.techery.janet.ReadActionPipe;
import rx.Observable;

public class ActionPipeCacheWiper<A> implements Observable.Transformer<ActionState<A>, ActionState<A>> {

    private final ReadActionPipe<A> actionPipe;

    public ActionPipeCacheWiper(ReadActionPipe<A> actionPipe) {
        this.actionPipe = actionPipe;
    }

    @Override public Observable<ActionState<A>> call(Observable<ActionState<A>> tObservable) {
        return tObservable.doOnNext(state -> {
            if (state.status != ActionState.Status.START && state.status != ActionState.Status.PROGRESS)
                actionPipe.clearReplays();
        });
    }
}
