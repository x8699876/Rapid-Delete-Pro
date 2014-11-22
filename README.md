## What is RdPro

* A Powerful Recursive Directory Removal/Purge Utility.  It transverse huge directory structures to find and pruge directories buried deep. All files under the the targeted directories will be deleted. 
* Super-fast.  Multiple threads are used to perform tasks in parallel. Algorithm makes a difference!
* Easily remove a directory of the same name which is nested and spread out under a huge directory structure. 
* No problem removing files and directories with long names which windows sometime can't handle.  No more “the file name is too long" problem. 
* The deleted files do not go to the recycle bin and can't be recovered. Make sure you read the disclaimer and understand what to expect. 

## Tech Stack
The project is a good showcase of the 
* GUI builder with maven in IntelliJ IDEA to build a Swing GUI.
* Multi threading using java concurrent ThreadPoolExecutor
* Recursive walk algorithem

## Download

- [Downlod the latest release] (https://github.com/tonyfresh024/rdpro/releases/download/v1.0/RdPro-1.0.zip)

There is a windows executable, batch files and Unix/Linux shell script included to run the application. Unzip and put the files in a directory on the environment path. 

## How to run

Open the console and go to the directory which you want to purge and run the rdpro.exe.  Run rdpro.sh in unix/linux environment. see below for examples.

## The GUI version 
run the rdproui.exe or rdproui.sh/rdproui.bat for your operating system. 

![screen shot](doc/11-21-2014%2010-10-21%20PM.png "rdproui.exe screenshot")


## Hook to the Windows Explorer Context menu
- Edit the repro_reg.reg file change the path to point to where your rdpro is exploded.
double click to import into windows registry
- Right click on the direcotry you want ot purge, you will see the "Recursive Delete Directory" context menu
- click it to popup the rdpro GUI.

![screen shot](doc/11-22-2014 1-14-12 PM.png "Windows exploer context menu")



##  The command line version rdpro.exe
rdpro -h for help

```
RdPro  - A Powerful Recursive Directory Purge Utility (v0.9 build 203 MHISoft Oct 2014, Shareware, Tony Xue)
Disclaimer:
        Deleted files do not go to recycle bean and can't be recovered.
        The author is not responsible for any loss of files or damage incurred by running this utility.
Usages:
         rdpro [option] path-to-search [target-dir]
          path-to-search  root path to search, default to the current dir.
         -d/-dir specify the target dir
         -f force delete
         -i interactive, default true
         -v verbose mode
         
Examples:
        Remove everything under a dir (purge a directory and everything under it): rdpro c:\mytempfiles
        Remove all directories that match a specified name recursively:
                rdpro -d target s:\projects
                rdpro s:\projects target

```


## Force delete the target directories

```
	S:\src\6.3-trunk>rdpro target -f
	Start to delete all the directories named "target" under "S:\src\6.3-trunk".
	There is no way to undelete, please confirm? (y/n or h for help)y
	working|
	Done in 10 seconds.
	Dir Removed:3944, Files removed:28690
```

## Purge an old huge directory

```
	S:\src\b1210-trunk>rdpro

	Start to delete everything under "S:\src\b1210-trunk" (y/n or h for help)?y
 	*Warning* There is no way to undelete. Confirm again (y/n or h for help)?y
	working.
	Confirm to delete file:S:\src\b1210-trunk\.externals(y/n/all)?dfd
        	response "dfd" not recognized. input again:all
	-
	Done in 29 seconds.
	Dir Removed:14285, Files removed:84409
```


If somehow it leaves some empty directory after running it, make sure they are locked and run it again. 

## Disclaimer
Deleted files do not go to recycle bin and can't be recovered.The author is not responsible for any loss of files or damage incurred by running this utility.

## License
Apache License 2.0, January 2004 http://www.apache.org/licenses/
