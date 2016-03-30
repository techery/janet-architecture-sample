package io.techery.sample.di;

import dagger.Module;

@Module(
        includes = {
                AppModule.class,
                StorageModule.class,
                ServiceModule.class,
                UtilModule.class,
                ActivityHolderModule.class
        })
public class RootModule {
}
