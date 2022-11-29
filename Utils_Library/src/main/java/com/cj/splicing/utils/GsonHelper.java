package com.cj.splicing.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonHelper {
    public static Gson gson = new GsonBuilder().serializeNulls ().
            registerTypeAdapterFactory (new NullStringToEmptyAdapterFactory()).create ();
}
