# JSync
Simplest Usage:
```java
File a = new File("/home/user/src/");
File b = new File("/home/user/dst/");
Syncer syncer = new Syncer(a, b, SyncOptions.DEFAULT_OPTIONS);
syncer.synchronize(null);
```
