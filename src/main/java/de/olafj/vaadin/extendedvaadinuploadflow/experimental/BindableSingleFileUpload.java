package de.olafj.vaadin.extendedvaadinuploadflow.experimental;

import com.vaadin.flow.component.customfield.CustomField;
import de.olafj.vaadin.extendedvaadinuploadflow.FileUpload;
import de.olafj.vaadin.extendedvaadinuploadflow.TempFileBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Consumer;

class BindableSingleFileUpload extends CustomField<File> {

    private static Logger LOG = LoggerFactory.getLogger(BindableSingleFileUpload.class);

    private final FileUpload fileUpload;
    private final TempFileBuffer tempFileBuffer;

    public BindableSingleFileUpload() {
        fileUpload = new FileUpload();
        this.add(fileUpload);
        tempFileBuffer = new TempFileBuffer();
        fileUpload.setReceiver(tempFileBuffer);

        fileUpload.addSucceededListener(event -> {
            this.setValue(tempFileBuffer.getFileInfo().getFile());
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
    protected File generateModelValue() {
        return tempFileBuffer.getFileInfo().getFile();
    }

    @Override
    protected void setPresentationValue(File newPresentationValue) {
        if(newPresentationValue == null) return;
        try {

            if(!tempFileBuffer.hasFile()) {
                tempFileBuffer.receiveUpload(newPresentationValue.getName(), Files.probeContentType(newPresentationValue.toPath()))
                        .write(Files.readAllBytes(newPresentationValue.toPath()));

                this.fileUpload.getElement().executeJs("this.files = [new File([], '" +  tempFileBuffer.getFileInfo().getOriginalName() + "', {" +
                        "  type: '" + tempFileBuffer.getFileInfo().getMimeType() + "', " +
                        "  complete: true" +
                        "})]");
            }

        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}