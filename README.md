## What is RdPro (Recursive Directory Removal Pro)

* A Recursive Directory Removal/Purge Utility.  It transverses huge directory structures to find and purge matched directories and files that are buried deep. 
* Super-fast. faster than the linux "rm" command.  Multiple threads are used to perform tasks in parallel. Algorithm makes a difference! 
* Can remvove all the target matched directories that are nested deep under the root and spread out under a huge tree directory structure. I found rm is lacking on this feature and it is the main driver for me to start writing this tool. For exmaple, it allows me to remove all the maven created target directories (which contains classes) under all my projects so that allows me to back up the source code only wihtout the generated classes. 
* No problem removing files and directories with long names which windows sometime can't handle.  No more “The file name is too long" issue. 
* The deleted files do not go to the recycle bin. This is a feature and is the reason why it is fast. However note the removed files can't be recovered from the recycle bin. If you are absolutely sure the files and directorues are to be purged, this is the right tool for the job. 

## OS 
*  Cross platform. It is a java application and can run on all the platforms where java is supported. Requires JRE/JDK to be installed on the machine for it to run:
*  Windows 
*  Linux/Unix
*  Mac

## Tech Stack
The project is a good showcase of the 
* GUI builder with maven in IntelliJ IDEA to build a Swing GUI.
* Multi threading using java concurrent ThreadPoolExecutor to performs tasks in parallel.
* Recursive walk algorithem.

## Download

- [Downlod the latest release] (https://github.com/mhisoft/rdpro/releases)
- The latest snapshot version is available under the dist/ folder.

There is a windows executable, batch files and Unix/Linux shell script included to run the application. Unzip and put the files in a directory on the environment path. 

## How to run

If not already, down load and install the latest JRE/JDK 1.7+ from Oracle.

Open the console and go to the directory which you want to purge and run the rdpro.exe.  Run rdpro.sh in unix/linux environment. see below for examples.

## The GUI version 
run the rdproui.exe or rdproui.sh/rdproui.bat for your operating system. 

## Mac
Explode zip and copy the app such as "RdProv1.3.8.app" to ~/Applications


![screen shot](doc/Screen%20Shot%202015-11-29%20at%2012.52.25%20PM.png "rdproui.exe screenshot")


## Hook to the Windows Explorer Context menu
- Edit the repro_reg.reg file change the path to point to where your rdpro is exploded.
double click to import into windows registry
- Right click on the direcotry you want ot purge, you will see the "Recursive Delete Directory" context menu
- click it to popup the rdpro GUI.

![screen shot](doc/11-22-2014%201-14-12%20PM(2).png "Windows exploer context menu")

## Hook into the Context menu in Finders on Mac

* Open Automator, Files menu --> "new"
* On the "Choose a type for your document" prompt, select "Service", for macOS mojave, choose "Quick Action"
* On the left in the search bar, type in "Run Shell Scripts" and select it.
* For "Services receives selected", choose "Files or Folders", in "Finder". Edit the path to where you installed the jar if needed.
* Save as "rdpro" for example or give your own name. This is the name going to be displayed in Finder preview. 

In the Finder, right click on a folder which you want to purge, choose Services, The Recursive Delete Pro menu should be available in the context menu. This is the name you choose to save in the Automator.  The rdpro should popup with the folder pre populated. 

For the MacMojave version, the actio is available at the lower right corner of the finder preview panel. you need to enable the "Preview" in the finder by going to the "View" menuy, enable "Preview".  

![screen shot](doc/rdpro-automator-setup.png "Create service using Automator")

## linux/mac command line 
explode the release to [home]/bin/rdpro for example, then update jar location in the the rdpro.sh to where the jar is
such as :
       `java -jar ~/bin/rdpro/rdpro-ui.jar  "$1" "$2" "$3" "$4" "$5"`

then you can go to the directory where you want to start to delete and issue rdpro.sh 

$ rdpro.sh -h

```
RdPro(v1.3.8,Oct 2018)- a super fast directory and file delete utility by Tony Xue, MHISoft
(https://github.com/mhisoft/rdpro)
Important note: Purged files does not go to recycle bin so can't be recovered! Use Wisely.
unlink tool path:C:/bin/rdpro/tools/linkd.exe %s /D 
Usages (see https://github.com/mhisoft/rdpro/wiki):
	 rdpro [option] path-to-search -d [target-dir] 
	 path-to-search:  The root path to search, default to the current dir.
	 -d or -dir specify the target dir. only dir names matched this name will be deleted. if target file pattern is also specified, only matched files under these matched dirs will be deleted.
	 -tf file match patterns. Use comma to delimit multiple file match patterns. ex: *.repositories,*.log
	 -f  force delete. Use it only when you are absolutely sure. Default:false 
	 -i  interactive, Default:true
	 -unlink  Unlink the hard linked directory first. Files in the linked directory won't be removed. Default:false.
	 -v  verbose mode. Default:false.
Examples:
	Remove everything under a dir (purge a directory and everything under it): 
		>rdpro c:\mytempfiles
	Remove all directories that matches a specified name recursively: 
		>rdpro s:\projects -d target 
	Remove files matches a pattern recursively on Mac or Linux:
		$rdpro.sh /Users/home/projects -d target -tf *.zip

Process finished with exit code -1
      
```       

##  Windows, The command line version rdpro.exe
add the path to the rdpro to the path enviroment variable. then  use "rdpro" in  the command from anywhere. 



## ex: Force delete the "target" directories 

```
	S:\src\6.3-trunk>rdpro target -f
	Start to delete all the directories named "target" under "S:\src\6.3-trunk".
	There is no way to undelete, please confirm? (y/n or h for help)y
	working|
	Done in 10 seconds.
	Dir Removed:3944, Files removed:28690
```

## Purge an old huge directory in whole

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
