package de.olafj.vaadin.extendedvaadinuploadflow;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.shared.Registration;

public class FileUpload extends Upload  {

    private final de.olafj.vaadin.extendedvaadinuploadflow.FileRejectEvent.Extension fileRejectedExtension;
    private final FileRemovedEvent.Extension fileRemovedExtension;

    public FileUpload(Receiver receiver) {
        this();
        setReceiver(receiver);
    }

    public FileUpload() {
        super();
        this.fileRejectedExtension =  de.olafj.vaadin.extendedvaadinuploadflow.FileRejectEvent.extend(this);
        this.fileRemovedExtension = FileRemovedEvent.extend(this);
    }

    public Registration addRejectListener(ComponentEventListener<de.olafj.vaadin.extendedvaadinuploadflow.FileRejectEvent> listener) {
        return this.fileRejectedExtension.addListener(listener);
    }

    public Registration addRemoveListener(ComponentEventListener<FileRemovedEvent> listener) {
        return this.fileRemovedExtension.addListener(listener);
    }

    public void clear() {
        this.getElement().executeJs("this.files=[]");
    }


}
