# JSync
Simplest Usage:
```java
File a = new File("/home/user/src/");
File b = new File("/home/user/dst/");
Syncer syncer = new Syncer(a, b, SyncOptions.DEFAULT_OPTIONS);
syncer.synchronize(null);
```
---

Basic Usage:
```java
File c = new File("/home/evilbert/src/");
File d = new File("/home/evilbert/dst/");
SyncOptions o = new SyncOptions.SyncOptionsBuilder().deleteUnmatchedTargets(false).chunkSize(1024).build();
Syncer syncer = new Syncer(c, d, o);
SyncResults results = syncer.synchronize((rem, tot, msg) -> {
    System.out.println("total: " + total + "/ remaining: " + rem + "/ " + msg);
});

System.out.println(System.lineSeparator() + results.toString());
```
Outputs:
```
total: 0/ remaining: 0/ Loading...
total: 2/ remaining: 2/ Starting synchronize
total: 2/ remaining: 2/ Copying 1.txt
total: 2/ remaining: 1/ Copying 2.txt
total: 2/ remaining: 0/ Done!

Scanned    : 2
Copied     : 2
Deleted    : 0
Scan Time  : 1234
Total Time : 12345
```