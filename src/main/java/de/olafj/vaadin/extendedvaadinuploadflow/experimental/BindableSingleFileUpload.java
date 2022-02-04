package de.olafj.vaadin.extendedvaadinuploadflow.experimental;

import com.vaadin.flow.component.customfield.CustomField;
import de.olafj.vaadin.extendedvaadinuploadflow.FileInfo;
import de.olafj.vaadin.extendedvaadinuploadflow.FileUpload;
import de.olafj.vaadin.extendedvaadinuploadflow.TempFileBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Consumer;

public class BindableSingleFileUpload extends CustomField<FileInfo> {

    private static final Logger LOG = LoggerFactory.getLogger(BindableSingleFileUpload.class);

    private final FileUpload fileUpload;
    private final TempFileBuffer tempFileBuffer;

    public BindableSingleFileUpload() {
        fileUpload = new FileUpload();
        this.add(fileUpload);
        tempFileBuffer = new TempFileBuffer();
        fileUpload.setReceiver(tempFileBuffer);

        fileUpload.addSucceededListener(event -> {
            this.setValue(tempFileBuffer.getFileInfo());
        });

        fileUpload.addRemoveListener(event -> {
            this.clear();
        });
    }

    public void withFileUpload(Consumer<FileUpload> work) {
        work.accept(this.fileUpload);
    }

    @Override
    public void clear() {
        super.clear();
        fileUpload.getElement().executeJs("this.files=[]");
    }

    @Override
    protected FileInfo generateModelValue() {
        return tempFileBuffer.getFileInfo();
    }

    @Override
    protected void setPresentationValue(FileInfo newPresentationValue) {
        if(newPresentationValue == null) return;
        try {

            if(!tempFileBuffer.hasFile()) {
                tempFileBuffer.receiveUpload(newPresentationValue.getOriginalName(), Files.probeContentType(newPresentationValue.getFile().toPath()))
                        .write(Files.readAllBytes(newPresentationValue.getFile().toPath()));
            }

            this.fileUpload.getElement().executeJs("this.files = [new File([], '" +  tempFileBuffer.getFileInfo().getOriginalName() + "', {" +
                    "  type: '" + tempFileBuffer.getFileInfo().getMimeType() + "', " +
                    "  complete: true" +
                    "})]");


        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}