
# Android BackBlaze Helper

[![](https://jitpack.io/v/yorondahacker/AndroidBackBlazeHelper.svg)](https://jitpack.io/#yorondahacker/AndroidBackBlazeHelper)


Android BackBlaze Helper is an android library that makes backblaze functions easy to work with.



## Features
- uploading images to backblaze bucket

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

    implementation 'com.github.yorondahacker:AndroidBackBlazeHelper:1.0.0'

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

### Upload Images

To upload images create an object of BlazeFileUploader
and pass the BlazeClinet object and your bucketId.
```java
BlazeFileUploader blazeFileUploader = 
new BlazeFileUploader(  client , bucketId); //TODO replace bucketId with your Bucket Id
```

Now to start uploading the images use this method and add your image uri and the image name.

```java
blazeFileUploader.startUploading(imageUri, imageName);
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
                     public void onUploadConnection(HttpURLConnection uploadConnection
                     , DataOutputStream uploadWriter) {
                         Log.v(TAG , "uploading connecting to BackBlaze");
                         //You can add some headers or get the uploading progress
                     }

                     @Override
                     public void onUploadFinished(String response) {
                         Log.v(TAG , "uploading finised " +response);
                     }

                     @Override
                     public void onUploadFailed(Exception e) {
                     //Handle Errors
                     }
                 });
```
#### I'll be updating this lib and add more futures
                 
