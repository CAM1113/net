package net.wang.can.protol.exceptions;

//读取Head时，inputStream的大小不够
public class HeadTooSmallException extends Exception{

    public HeadTooSmallException() {
    }

    public HeadTooSmallException(String message) {
        super(message);
    }
}
