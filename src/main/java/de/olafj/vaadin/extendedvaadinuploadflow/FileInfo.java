package de.olafj.vaadin.extendedvaadinuploadflow;

import java.io.File;

public class FileInfo {
    private final File file;
    private final String originalName;
    private final String mimeType;

    public FileInfo(File file, String originalName, String mimeType) {
        this.file = file;
        this.originalName = originalName;
        this.mimeType = mimeType;
    }

    public File getFile() {
        return file;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getOriginalName() {
        return originalName;
    }
}