package net.wang.can.protol.utils;

import com.google.gson.Gson;
import net.wang.can.profiles.Profile;
import net.wang.can.protol.head.Head;
import net.wang.can.protol.exceptions.HeadTooSmallException;
import util.wang.can.LogUtils;

import java.io.IOException;
import java.io.InputStream;

public class ReadUtils {

    public static Object readGsonObject(String className, InputStream inputStream) throws Exception {
        String readString = readString(inputStream).trim();
        System.out.println(readString);
        Class clazz= Class.forName(className);
        Gson gson = new Gson();
        return gson.fromJson(readString,clazz);
    }

    public static Head readHead(InputStream inputStream) throws HeadTooSmallException {
        byte[] bytes = new byte[Profile.HEAD_SIZE];
        try {
            inputStream.read(bytes,0,Profile.HEAD_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
            throw new HeadTooSmallException();
        }
        String gsonStr = new String(bytes).trim();
        System.out.println(gsonStr);
        Head head = new Gson().fromJson(gsonStr, Head.class);
        return head;
    }

    public static String readString(InputStream inputStream) throws Exception{
        byte[] bytes = new byte[1024];
        StringBuilder builder = new StringBuilder();
        while (inputStream.read(bytes)>0) {
            builder.append(new String(bytes).trim());
        }
        LogUtils.i(" string 读取 :"+builder.toString());
        return builder.toString();
    }
}
