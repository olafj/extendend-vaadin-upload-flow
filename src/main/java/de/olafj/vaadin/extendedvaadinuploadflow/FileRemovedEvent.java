package de.olafj.vaadin.extendedvaadinuploadflow;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.shared.Registration;

public class FileRemovedEvent extends ComponentEvent<FileUpload> {

    private final String fileName;
    private final String mimeType;

    public FileRemovedEvent(FileUpload source, String fileName, String mimeType) {
        super(source, true);
        this.fileName = fileName;
        this.mimeType = mimeType;
    }

    public String getFileName() {
        return fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public static Extension extend(FileUpload upload) {

        upload.getElement().addEventListener("file-remove", domEvent -> {
            ComponentUtil.fireEvent(upload,
                    new FileRemovedEvent(upload,
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

        public Registration addListener(ComponentEventListener<FileRemovedEvent> listener) {
            return ComponentUtil.addListener(upload, FileRemovedEvent.class, listener);
        }

    }
}
