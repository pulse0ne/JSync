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
Default: true  | If this is set to true, the Syncer will delete any files/directories that it encounters in the target directory that don't exist in the source directory.

### smartCopy
Default: true | If this is set to true, the Syncer will compare file size and last modified time, and will only perform a copy if they differ. When this is set to false, a copy is always performed.

### chunkSize
Default: 1024 * 1024 * 16 (16MB) | This specifies the copy buffer size. It is HIGHLY recommended that this value be a power of 2 (1024, 2048, 4096...etc).

### inclusionFilters
Default: empty List | Specifies the filters used for including files in the sync

### exclusionFilters
Default: empty List | Specifies the filters uesd for excluding files from the sync

### matchAllInclusionFilters
Default: true | When true, the file must match all provided inclusion filters to be included. When false, the file will be included when any one of the filters matches.

### matchAllExclusionFilters
Default: true | When true, the file will be excluded only if all exclusion filters are matched. When false, the file will be excluded if any one of the filters matches.

---

## Filters
Filters can be used to include or exclude certain files. Many filters can be added.

### FileNameSyncFilter
Filters on a provided GLOB string. Example:
```java
SyncFilter filter = new FileNameSyncFilter("**/photo0?.jpg");
SyncOptions opts = new SyncOptions.SyncOptionsBuilder().addInclusionFilter(filter).build();
```
When the Syncer is run, it will only include jpgs that start with 'photo0'.

### FileExtensionSyncFilter
Filters on the provided file extension. Example:
```java
SyncFilter extFilter = new FileExtensionSyncFilter(".exe");
SyncOptions opts = new SyncOptions.SyncOptionsBuilder().addExclusionFilter(extFilter).build();
```
When the Syncer is run, it will exclude all .exe files.

### FileSizeSyncFilter
Filters on the size of the file
```java
SyncFilter sizeFilter = new FileSizeSyncFilter(0, 1024 * 1024 * 4);
SyncOptions opts = new SyncOptions.SyncOptionsBuilder().addInclusionFilter(sizeFilter).build();
```
When the Syncer is run, it will include files that are sized between 0B and 4MB (inclusive).

### LastModifiedSyncFilter
Filters on the last modified time
```java
SyncFilter modFilter = new LastModifiedSyncFilter(638715715000, 1471647337024);
SyncOptions opts = new SyncOptions.SyncOptionsBuilder().addInclusionFilter(modFilter).build();
```
When run, will include files modified between the date and time of my birth, and the time of this writing

## Future plans
- What to do about symlinks
- More file comparison options
- Option to update last modified timestamp
