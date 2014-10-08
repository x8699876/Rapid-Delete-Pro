## What is RdPro

* A Powerful Recursive Directory Purge Utility 
* It is very fast because it uses multiple threads to do the job. 
* It can easily remove files and directories with long names which windows sometime can't handle. Also it has no problem in dealing with directory structure nested very very deep. 

## Download and Install

Downlod here: pending

There is a windows executable, batch files and Unix/Linux shell script included to run the application. Unzip and put the files in a directory on the environment path. 

## How to run

Open the console and go to the directory which you want to purge and run the rdpro.  Run rdpro.sh in unix/linux environment. see below for examples.


## rdpro -h for help

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
