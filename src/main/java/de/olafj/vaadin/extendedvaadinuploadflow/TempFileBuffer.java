package de.olafj.vaadin.extendedvaadinuploadflow;

import com.vaadin.flow.component.upload.Receiver;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;

public class TempFileBuffer implements Receiver {

    private static final Logger LOG = LoggerFactory.getLogger(TempFileBuffer.class);

    private FileInfo fileInfo;

    @Override
    public OutputStream receiveUpload(String fileName, String mimeType) {

        final String tempFileName = ("upload_tmpfile_" + System.currentTimeMillis()).replace(".", "_") + "_" + fileName;
        var ext = FilenameUtils.getExtension(fileName);
        try {
            var file = File.createTempFile(tempFileName, "." + ext);
            this.fileInfo = new FileInfo(file, fileName, mimeType);
            return Files.newOutputStream(file.toPath());
        } catch (IOException e) {
            this.fileInfo = null;
            throw new RuntimeException(e);
        }
    }

    public boolean hasFile() {
        return this.fileInfo != null;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public String getFileName() {
        return fileInfo != null ? fileInfo.getOriginalName() : "";
    }

    public InputStream getInputStream() {
        if (fileInfo != null) {
            final File path = fileInfo.getFile();
            try {
                return new FileInputStream(path);
            } catch (IOException e) {
                LOG.warn("Failed to create InputStream for: '" + getFileName()
                                + "'", e);
            }
        }
        return new ByteArrayInputStream(new byte[0]);
    }

    public static class FileInfo {
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
}
