# JSync
JSync is a library for simple and fast directory synchronization. 

JSync has a JavaFX front-end that can be found [here](https://github.com/pulse0ne/JSyncer).

## Examples
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
---

## Building
Run `ant`

---

## SyncOptions
### deleteUnmatchedTargets
Default: true 


If this is set to true, the Syncer will delete any files/directories that it encounters in the target directory that don't exist in the source directory.

### smartCopy
Default: true


If this is set to true, the Syncer will compare file size and last modified time, and will only perform a copy if they differ. When this is set to false, a copy is always performed.

### chunkSize
Default: 1024 * 1024 * 16 (16MB)


This specifies the copy buffer size. It is HIGHLY recommended that this value be a power of 2 (1024, 2048, 4096...etc).

---

## Future plans
- Include/exclude filters
- What to do about symlinks
- More file comparison options
- Option to update last modified timestamp
