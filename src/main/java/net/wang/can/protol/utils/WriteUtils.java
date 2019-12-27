package net.wang.can.protol.utils;

import com.google.gson.Gson;
import net.wang.can.profiles.Profile;
import net.wang.can.protol.head.Head;
import net.wang.can.protol.head.Protocals;
import net.wang.can.protol.exceptions.HeadTooLargeException;
import util.wang.can.LogUtils;

import java.io.*;

public class WriteUtils {

    public static boolean writeFile(File file, OutputStream outputStream) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(file);
        streamCopy(fileInputStream, outputStream);
        return true;
    }

    public static boolean writeGsonObject(Object o, OutputStream outputStream) throws Exception {
        if (o == null) {
            LogUtils.w("o == null");
            throw new Exception("o == null");
        }
        String s = new Gson().toJson(o);
        writeString(s, outputStream);
        return true;
    }

    public static void writeHead(String status, String contentType, String message, String others, OutputStream outputStream) throws Exception {
        Head head = new Head();
        head.setStatus(status.trim().replace("\n", " "));
        head.setContentType(contentType.trim().replace("\n", " "));
        head.setMessage(message.trim().replace("\n", " "));//去除换行
        head.setOther(others.trim().replace("\n", " "));//去除换行
        writeHead(head, outputStream);
    }

    public static void writeHead(Head head, OutputStream outputStream) throws Exception {
        String string = new Gson().toJson(head);
        if (string.length() > Profile.HEAD_SIZE) {
            throw new HeadTooLargeException("Head's size can't larger than " + Profile.HEAD_SIZE);
        }
        byte[] targetBytes = new byte[Profile.HEAD_SIZE];

        string.getBytes(0, string.length(), targetBytes, 0);
        outputStream.write(targetBytes, 0, Profile.HEAD_SIZE);
    }


    //从输入流读取内容并拷贝到到输出流
    private static void streamCopy(InputStream inputStream, OutputStream outputStream) throws Exception {
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(bytes)) > 0) {
            outputStream.write(bytes, 0, len);
        }
    }

    private static void writeString(String s, OutputStream outputStream) throws Exception {
        byte[] bytes = s.getBytes();
        outputStream.write(bytes);
    }
}
