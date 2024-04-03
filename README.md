# DiskInfo

<p align="center">
  <img src="app/src/main/ic_launcher-playstore.png" width="128px" height="128px"/>
</p>

### DiskInfo: Quick partition stats on-the-go.🔍

DiskInfo is a simple app to list partitions of your device. (Initial Build)
It also provides you a basic info of all the partitions like :
- Partition name (mount path)
- FileSystem Type (ext4 / f2fs / FAT32 )
  Note : The app shows virtual filesystem (ex : fuse,sdcardfs,etc) instead of 
  actual filesystems (ex : fat32, ntfs, etc) for some partitions and there is no 
  way to show actual fs underneath it without root access.
- Access Type (readOnly/read-Write)
- Block Size 
- Partion storage info (free,used,total space).

The App comes with 6 color themes and also supports material you dynamic theming (Only available in Android 12). 
App icon also supports dynamic theming (changes color with the wallpaper only on Android 12 ).
