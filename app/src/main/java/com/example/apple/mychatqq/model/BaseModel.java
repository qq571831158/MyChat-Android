package com.example.apple.mychatqq.model;

import com.google.gson.Gson;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by apple on 2017/4/4.
 */
public class BaseModel<T> {
    private String code;
    private String message;


    private T contents;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getContents() {
        return contents;
    }

    public void setContents(T contents) {
        this.contents = contents;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public BaseModel(String code,  String message,T contents) {
        this.code = code;
        this.contents = contents;
        this.message = message;
    }
    public BaseModel(){}
    @Override
    public String toString() {
        return getCode()+getMessage()+getContents();
    }


    public static BaseModel fromJson(String json, Class clazz) {
        Gson gson = new Gson();
        Type objectType = type(BaseModel.class, clazz);
        return gson.fromJson(json, objectType);
    }

    public String toJson(Class<T> clazz) {
        Gson gson = new Gson();
        Type objectType = type(BaseModel.class, clazz);
        return gson.toJson(this, objectType);
    }

    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }
}
