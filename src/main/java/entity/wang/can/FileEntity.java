package entity.wang.can;

public class FileEntity {

    public static final String TYPE_FOLDER = "TYPE_FOLDER";
    public static final String TYPE_FILE = "TYPE_FILE";

    String fileName = "";
    String fileType = "";

    public FileEntity() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
