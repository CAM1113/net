package server.wang.can;

import com.google.gson.Gson;
import net.wang.can.entitys.OrderEntity;
import net.wang.can.profiles.Profile;
import order.wang.can.FileEntities;
import order.wang.can.FileEntity;
import order.wang.can.OrderString;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OrderSolver {

    public static Object solveOrder(OrderEntity entity) {
        switch (entity.getOrder()) {
            case OrderString.ls:
                return ls(entity);
            default:
                return "";
        }
    }


    public static FileEntities ls(OrderEntity orderEntity) {
        String filePath = orderEntity.getCurrentPath();
        List<FileEntity> list = new ArrayList<>();
        File f = new File(Profile.ROOT_PATH + "\\" + filePath);
        String[] names = f.list();
        for (String string : names) {
            File file = new File(Profile.ROOT_PATH + "\\" + filePath + "\\" + string);
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(filePath + "\\" + string);
            if (file.isDirectory()) {
                fileEntity.setFileType(FileEntity.TYPE_FOLDER);
            } else {
                fileEntity.setFileType(FileEntity.TYPE_FILE);
            }
            list.add(fileEntity);
        }
        FileEntities fileEntities = new FileEntities();
        fileEntities.setFileEntities(list);
        return fileEntities;
    }


}
