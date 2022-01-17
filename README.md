# extendend-vaadin-upload-flow
custom version of vaadin's upload component extended with meaningful 'file-reject' and 'file-remove' events and custom upload-receiver-implementations TempFileBuffer (MultiTempFileBuffer).


## better events

```java

var upl = new FileUpload();
        
upl.addRejectListener(event -> {
    System.out.println(event.getFileName()); 
});

upl.addRemoveListener(event -> {
    System.out.println(event.getFileName());
});

```

## bindable (experimental)
```java
var bu = new BindableSingleFileUpload();
binder.forField(bu).asRequired().bind(BindableBeanExample::getUploadFile, BindableBeanExample::setUploadFile);
```