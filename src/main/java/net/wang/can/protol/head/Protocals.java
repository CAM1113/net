package net.wang.can.protol.head;

public interface Protocals {
    //状态
    String STATUS_SUCCESS = "STATUS_SUCCESS";
    String STATUS_FAIL = "STATUS_FAIL";

    String MESSAGE_EXCEPTION_HAPPEN = "MESSAGE_EXCEPTION_HAPPEN";

    //内容类型
    String CONTENT_NONE = "CONTENT_NONE";//内容为空
    String CONTENT_FILE = "CONTENT_FILE";//内容为文件
    String CONTENT_STRING = "CONTENT_STRING";//内容为简单的String
    String CONTENT_GSON_OBJ = "CONTENT_GSON_OBJ";//内容为普通GSON字符串

}
