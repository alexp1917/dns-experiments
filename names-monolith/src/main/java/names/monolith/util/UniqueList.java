package names.monolith.util;

import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.function.Function;


public class UniqueList<T> extends ArrayList<T> {

    private final Class<T> tClass;
    private final Function<T, Comparable<T>> idGetter;

    @SneakyThrows
    public static <T> Function<T, Comparable<T>> makeIdGetter(Class<T> tClass) {
        Method getId = tClass.getMethod("getId");
        if (!Comparable.class.isAssignableFrom(getId.getReturnType()))
            throw new IllegalArgumentException("getId is not comparable on " +
                    tClass.getSimpleName());
        return new Function<T, Comparable<T>>() {
            @SneakyThrows
            @Override
            public Comparable<T> apply(T t) {
                return (Comparable<T>) getId.invoke(t);
            }
        };
    }

    public UniqueList(Class<T> tClass) {
        this.tClass = tClass;
        this.idGetter = makeIdGetter(tClass);
    }

    public UniqueList(int capacity, Class<T> tClass) {
        super(capacity);
        this.tClass = tClass;
        this.idGetter = makeIdGetter(tClass);
    }

    @Override
    public boolean add(T t) {
        return super.add(t);
    }
}
