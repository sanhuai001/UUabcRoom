package com.uuabc.classroomlib.custom;

import androidx.annotation.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by user on 2018/3/26.
 */

public class ParameterizedGenericsType implements ParameterizedType {
    private final Class raw;
    private final Type[] args;

    public ParameterizedGenericsType(Class raw, Type[] args) {
        this.raw = raw;
        this.args = args != null ? args : new Type[0];
    }

    @NonNull
    @Override
    public Type[] getActualTypeArguments() {
        return args;
    }

    @NonNull
    @Override
    public Type getRawType() {
        return raw;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
