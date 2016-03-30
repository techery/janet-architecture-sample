package io.techery.sample.service;

import android.content.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import io.techery.janet.ActionHolder;
import io.techery.janet.ActionServiceWrapper;
import io.techery.janet.CommandActionBase;
import io.techery.janet.CommandActionService;
import io.techery.janet.JanetException;
import io.techery.presenta.mortar.DaggerService;
import timber.log.Timber;

public class DaggerCommandServiceWrapper extends ActionServiceWrapper {

    private final CommandInjector injector;

    public DaggerCommandServiceWrapper(CommandActionService service, Context context) {
        super(service);
        CommandsComponent component = DaggerService.getDaggerComponent(context);
        this.injector = new CommandInjector(component);
    }

    @Override protected <A> boolean onInterceptSend(ActionHolder<A> holder) {
        try {
            injector.inject((CommandActionBase) holder.action());
        } catch (Throwable throwable) {
            Timber.e(throwable, "Can't inject command %s", holder.action());
        }
        return false;
    }

    @Override protected <A> void onInterceptCancel(ActionHolder<A> holder) {}

    @Override protected <A> void onInterceptStart(ActionHolder<A> holder) {}

    @Override protected <A> void onInterceptProgress(ActionHolder<A> holder, int progress) {}

    @Override protected <A> void onInterceptSuccess(ActionHolder<A> holder) {}

    @Override protected <A> void onInterceptFail(ActionHolder<A> holder, JanetException e) {}

    private static class CommandInjector {

        private static final Map<Class<? extends CommandActionBase>, Method> cache = new HashMap<>();
        private Object commandsComponent;

        private CommandInjector(Object commandsComponent) {
            this.commandsComponent = commandsComponent;
        }

        public void inject(CommandActionBase command) {
            Class<? extends CommandActionBase> commandClass = command.getClass();
            try {
                Method injectableMethod = findInjectableMethod(commandClass);
                injectableMethod.invoke(commandsComponent, command);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException | NullPointerException e) {
                String detailMessage = "No graph method found to inject " + commandClass.getSimpleName() + ". Check your component";
                NullPointerException exception = new NullPointerException(detailMessage);
                exception.setStackTrace(e.getStackTrace());
                throw exception;
            }
        }

        private Method findInjectableMethod(Class<? extends CommandActionBase> commandClass) throws NoSuchMethodException {
            Method cachedMethod = cache.get(commandClass);
            if (cachedMethod != null) {
                return commandsComponent.getClass().getDeclaredMethod(cachedMethod.getName(), commandClass);
            }
            // Find proper injectable method of component to inject presenter instance
            for (Method m : commandsComponent.getClass().getDeclaredMethods()) {
                for (Class pClass : m.getParameterTypes()) {
                    if (pClass.equals(commandClass)) {
                        cache.put(commandClass, m);
                        return m;
                    }
                }
            }
            return null;
        }
    }
}
