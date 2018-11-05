package com.ssh.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class JsonUtils {

    private static Gson gson = new GsonBuilder().serializeNulls().create();

    public static String serialize(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T deserialize(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

}
