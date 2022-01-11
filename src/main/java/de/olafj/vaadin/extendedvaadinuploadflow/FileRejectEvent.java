package de.olafj.vaadin.extendedvaadinuploadflow;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.upload.FileRejectedEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.shared.Registration;

public class FileRejectEvent extends FileRejectedEvent {

    private final String fileName;
    private final String mimeType;

    public FileRejectEvent(Upload source, String errorMessage, String fileName, String mimeType) {
        super(source, errorMessage);
        this.fileName = fileName;
        this.mimeType = mimeType;
    }

    public String getFileName() {
        return fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public static Extension extend(Upload upload) {

        upload.getElement().addEventListener("file-reject", domEvent -> {
            ComponentUtil.fireEvent(upload,
                    new FileRejectEvent(upload,
                            domEvent.getEventData().getString("event.detail.error"),
                            domEvent.getEventData().getString("event.detail.file.name"),
                            domEvent.getEventData().getString("event.detail.file.type")));

        }).addEventData("event.detail.file.name").addEventData("event.detail.file.type");

        return new Extension(upload);

    }

    public static final class Extension {

        private final Upload upload;

        protected Extension(Upload upload) {
            this.upload = upload;
        }

        public Registration addListener(ComponentEventListener<FileRejectEvent> listener) {
            return ComponentUtil.addListener(upload, FileRejectEvent.class, listener);
        }

    }


}
