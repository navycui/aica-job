package bnet.library.client.dto;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileForm {
    private final List<FileParam> params;

    public static FileForm fileForm() {
        return new FileForm();
    }

    FileForm() {
        this.params = new ArrayList<FileParam>();
    }

    public FileForm add(String name, File file, String filename) {
        this.params.add(new FileParam(name, file, filename));
        return this;
    }

    public List<FileParam> build() {
        return new ArrayList<FileParam>(this.params);
    }
}
