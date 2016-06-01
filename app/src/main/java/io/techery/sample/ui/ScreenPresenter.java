package io.techery.sample.ui;

import com.trello.rxlifecycle.RxLifecycle;

import javax.inject.Inject;

import io.techery.janet.ReadActionPipe;
import io.techery.janet.helper.ActionStateSubscriber;
import io.techery.presenta.mortarscreen.presenter.InjectablePresenter;
import io.techery.sample.presenta.ActivityHolder;
import io.techery.sample.ui.view.ScreenView;
import io.techery.sample.utils.ActionPipeCacheWiper;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.internal.util.UtilityFunctions;

public class ScreenPresenter<V extends ScreenView> extends InjectablePresenter<V> {

    @Inject
    protected ActivityHolder activityHolder;

    public ScreenPresenter(PresenterInjector injector) {
        super(injector);
    }

    public <T> Observable<T> bind(Observable<T> source) {
        return source
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycle.<T>bindView(getView()));
    }

    public <A> ActionStateSubscriber<A> bindPipe(ReadActionPipe<A> pipe) {
        ActionStateSubscriber<A> subscriber = new ActionStateSubscriber<>();
        bind(pipe.observe())
                .subscribe(subscriber);
        return subscriber;
    }

    public <A> ActionStateSubscriber<A> bindPipeCached(ReadActionPipe<A> pipe) {
        ActionStateSubscriber<A> subscriber = new ActionStateSubscriber<>();
        bind(pipe.observeWithReplay().compose(new ActionPipeCacheWiper<>(pipe)))
                .subscribe(subscriber);
        return subscriber;
    }

}
