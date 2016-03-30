package io.techery.sample;

import android.app.Application;

import io.techery.presenta.mortar.DaggerService;
import io.techery.sample.di.AppModule;
import mortar.MortarScope;
import timber.log.Timber;

public class App extends Application {

    private MortarScope rootScope;

    @Override public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        initMortar();
    }

    private void initMortar() {
        AppComponent component = DaggerService.createComponent(AppComponent.class, new AppModule(this));
        rootScope = MortarScope.buildRootScope()
                .withService(DaggerService.SERVICE_NAME, component)
                .build("Root");
        component.inject(this);
    }

    @Override
    public Object getSystemService(String name) {
        return (rootScope != null && rootScope.hasService(name)) ? rootScope.getService(name) : super.getSystemService(name);
    }
}
