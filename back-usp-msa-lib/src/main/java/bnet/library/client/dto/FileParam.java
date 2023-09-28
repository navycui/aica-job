package bnet.library.client.dto;

import java.io.File;
import java.io.Serializable;

public class FileParam implements Cloneable, Serializable {
    private static final long serialVersionUID = 4641201634983715838L;
    private String name;
    private String filename;
    private File file;

    public String getName() {
        return name;
    }
    public String getFilename() {
        return filename;
    }
    public File getFile() {
        return file;
    }

    public FileParam(String name, File file, String filename) {
        this.name = name;
        this.filename = filename;
        this.file = file;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
