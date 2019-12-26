package net.wang.can.protol.head;

public interface Protocals {
    //状态
    String STATUS_SUCCESS = "STATUS_SUCCESS";
    String STATUS_FAIL = "STATUS_FAIL";


    //内容类型
    String CONTENT_NONE = "CONTENT_NONE";//内容为空
    String CONTENT_FILE = "CONTENT_FILE";//内容为文件，代表要执行上传文件操作，存储路径在other中
    String CONTENT_IMG = "CONTENT_IMG";//内容为图片
    String CONTENT_STRING = "CONTENT_STRING";//内容为简单的String
    String CONTENT_GSON_OBJ = "CONTENT_GSON_OBJ";//内容为普通GSON字符串
    String CONTENT_ORDER_GSON_OBJ = "CONTENT_ORDER_GSON_OBJ";//内容为命令字符窜



    //信息类型
    String MESSAGE_NORMAL = "MESSAGE_NORMAL";//正常
    String MESSAGE_FILE_IS_NOT_EXIST = "MESSAGE_FILE_IS_NOT_EXIST";//文件不存在
    String MESSAGE_OBJECT_IS_NULL = "MESSAGE_OBJECT_IS_NULL";//对象内容为空


}
