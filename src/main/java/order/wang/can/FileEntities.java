package order.wang.can;

import java.util.List;

public class FileEntities {
    List<FileEntity> fileEntities;

    public FileEntities() {
    }

    public FileEntities(List<FileEntity> fileEntities) {
        this.fileEntities = fileEntities;
    }

    public List<FileEntity> getFileEntities() {
        return fileEntities;
    }

    public void setFileEntities(List<FileEntity> fileEntities) {
        this.fileEntities = fileEntities;
    }
}
