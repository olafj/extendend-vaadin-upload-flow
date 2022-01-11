package de.olafj.vaadin.extendedvaadinuploadflow;

import com.vaadin.flow.component.upload.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MultiTempFileBuffer implements Receiver {

    private static final Logger LOG = LoggerFactory.getLogger(MultiTempFileBuffer.class);

    private final List<FileInfo> fileInfos = new ArrayList<>();

    @Override
    public OutputStream receiveUpload(String fileName, String mimeType) {

        final String tempFileName = "upload_tmpfile_" + fileName + "_"
                + System.currentTimeMillis();
        try {
            var file = File.createTempFile(tempFileName, null);
            this.fileInfos.add(new FileInfo(file, fileName, mimeType));
            return Files.newOutputStream(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasFile() {
        return this.fileInfos.size() > 0;
    }

    public List<FileInfo> getFileInfos() {
        return fileInfos;
    }


    public InputStream getInputStream(FileInfo fileInfo) {
        if (fileInfo != null) {
            final File path = fileInfo.getFile();
            try {
                return new FileInputStream(path);
            } catch (IOException e) {
                LOG.warn("Failed to create InputStream for: '" + fileInfo.getOriginalName()
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
