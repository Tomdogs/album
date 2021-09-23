package com.example.album.media.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @version : v1.0.0
 * @author: Tomdog
 * @since: 2021/9/23
 */
public class ConstructorConstructor {
    public ConstructorConstructor() {
    }

    public <T> ObjectConstructor<T> get(Class<? super T> rawType) {
        ObjectConstructor<T> defaultConstructor = this.newDefaultConstructor(rawType);
        return defaultConstructor;
    }

    private <T> ObjectConstructor<T> newDefaultConstructor(Class<? super T> rawType) {
        try {
            final Constructor<? super T> constructor = rawType.getDeclaredConstructor();
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }

            return new ObjectConstructor<T>() {
                public T construct() {
                    try {
                        Object[] args = null;
                        return (T) constructor.newInstance((Object[])args);
                    } catch (InstantiationException var2) {
                        throw new RuntimeException("Failed to invoke " + constructor + " with no args", var2);
                    } catch (InvocationTargetException var3) {
                        throw new RuntimeException("Failed to invoke " + constructor + " with no args", var3.getTargetException());
                    } catch (IllegalAccessException var4) {
                        throw new AssertionError(var4);
                    }
                }
            };
        } catch (NoSuchMethodException var3) {
            return null;
        }
    }
}
