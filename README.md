
# Android BackBlaze Helper

[![](https://jitpack.io/v/yorondahacker/AndroidBackBlazeHelper.svg)](https://jitpack.io/#yorondahacker/AndroidBackBlazeHelper)


Android BackBlaze Helper is an android library that makes backblaze functions easy to work with.



## Features
- Uploading files to backblaze bucket
- Choosing file type
- Show upload progress
- Upload multiple files

  i'm working on more futures...







## Installing
- **Step 1.** Add the JitPack repository to your **project gradle file**
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

- **Step 2.** Add the implementation dependency to your **app gradle file**
```groovy
dependencies {
    ...

    implementation 'com.github.yorondahacker:AndroidBackBlazeHelper:1.0.1'

    ...
}
```

- **Step 3.** Gradle sync and you're ready to go!

---

## Usage

### BlazeClient

Create a blaze client object and add your applicationKeyId , applicationKey.
```java
BlazeClient client = new BlazeClient(this,applicationKeyId //TODO replace applicationKeyId with your key
,applicationKey); //TODO replace applicationKey with your key
```

### Upload Files

To upload files create an object of BlazeFileUploader
and pass the BlazeClinet object and your bucketId.
```java
BlazeFileUploader blazeFileUploader = 
new BlazeFileUploader(  client , bucketId); //TODO replace bucketId with your Bucket Id
```

Now to start uploading the files use this method and add your file uri and the file name.

```java
blazeFileUploader.startUploading(fileUri, fileName);
```
To change it's type use this line

```java
blazeFileUploader.startUploading(fileUri, fileName , contentType); // replace the content type with mime type or choose one from FileTypes.class e.g. FileTypes.IMAGE_TYPE
```

To upload multiple files please use the model MultiFile.class here is how..

```java
 ArrayList<MultiFile> arrayList = new ArrayList<>();
 MultiFile multiFile = new MultiFile();

 for(int i = 0; i < count; i++){
 
                        Uri fileUri = data.getClipData().getItemAt(i).getUri();

                        multiFile.init(fileUri, new File(fileUri.getPath()).getAbsolutePath() , FileTypes.IMAGE_TYPE);

                        arrayList.add(multiFile);

                    }
                    
                    //use this method...
         blazeFileUploader.startUploadingMultipleFiles(arrayList);

```


### Uploading Listeners
To listen to changes while uploading use this method.

```java
         
     blazeFileUploader.setOnUploadingListener(new UploadListener() {
     
                    @Override
                    public void onUploadStarted() {
                    Log.v(TAG , "uploading started");

                    }                         

                    @Override
                    public void onUploadProgress(int percentage, long progress, long total) {
                           Log.v(TAG , "uploading on progress %"+percentage);

                    }

                    @Override
                    public void onUploadFinished(String response , boolean allFilesUploaded) {
                         Log.v(TAG , "uploading finised " +response);

                    }

                    @Override
                    public void onUploadFailed(Exception e) {
                     //Handle Errors

                    }
                });
```


#### I'll be updating this lib and add more futures
                 
