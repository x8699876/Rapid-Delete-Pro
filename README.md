## What is RdPro (Rapid Delete Pro)
* How to delete large folders on Windows and Mac super fast? This tool does the job.
* It is a "rm" replacement. Only faster! and more functionality. 
* Multiple worker threads to perform delete tasks in parallel for SSD drives. Algorithm makes a difference! 
* Recursively transverses huge directory structures to seasrch and purge matched directories and files. Those to-be-deleted target directories and files could be buried deep under and spreaded across the large directory structure. I found the "rm" linux/unix command lacks this feature. It is the main driver for me to start writing this tool. For exmaple, it allows to remove all the maven created target directories (which contains the generated classes) under all my projects so that allows me to back up the source code only wihtout the generated classes. 
* No problem removing files and directories with long names which windows sometime can't handle.  No more “The file name is too long" issue. 
* The deleted files do not go to the recycle bin. This is a feature and is also a reason why it is fast. However note the removed files can't be recovered from the recycle bin. If you are absolutely sure the files and directorues are to be purged, this is the right tool for the job. 

## Cross platform
*  It is a java application and can run on all the platforms where java is supported. Requires JRE/JDK to be installed on the machine for it to run. If not already availabe on your system, download and install the JDK 1.8+ from Oracle:https://www.oracle.com/technetwork/java/javase/overview/index.html
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

## Instructions
* Requires java JDK. If not already availabe on your system, download and install the JDK 1.8+ from Oracle.
https://www.oracle.com/technetwork/java/javase/overview/index.html

* **Windows**: 
  Exploded the downloaded zip into a c:\bin\rdpro and add the directory to the system path. </br>
  Run the rdpro.bat, rdpro.exe for the command line or  the rdpro-ui.exe for the GUI version of the App. </br>
  Also see below on how to integrate with the windows explorer. </br>

* **Mac OS**: 
  Exploded the downloaded zip to ~/bin/rdpro</br>
  The "RdProv1.3.8.app" is the MacOS App,  copy it to the ~/Applications and you can launch it from the LaunchPad.</br>
  Also see below on how to add as service/action to the context menu in the Finder.</br> 

* **Unix/linux**, Mac command line : run the ./rdpro.sh. See examples below.


![screen shot](doc/rdpro-v1.3.10-screenshot-10-29-2018%204-41-35%20PM.png "rdproui.exe screenshot")



### Hook into the Context menu in Finders on Mac

* Open Automator, Files menu --> "new"
* On the "Choose a type for your document" prompt, select "Service", for macOS mojave, choose "Quick Action"
* On the left in the search bar, type in "Run Shell Scripts" and select it.
* For "Services receives selected", choose "Files or Folders", in "Finder". Edit the path to where you installed the jar if needed.
* for the "Pass input",  select "as arguments"
* Type in the script as shown on the screenshot.
* Save as "rdpro" for example or give your own name. This is the name going to be displayed in Finder preview. 

In the Finder, right click on a folder which you want to purge, choose Services, The Recursive Delete Pro menu should be available in the context menu. This is the name you choose to save in the Automator.  The rdpro should popup with the folder pre populated. 

For the MacMojave version, the rdpro is also avaialble at the lower right corner of the Finder's preview panel. You need to enable the "Preview" in the finder by going to the "View" menu, enable "Preview".  

**Notes:**
If you see "ERROR: java.io.IOException: Cannot run program "/Users/yourname/bin/rdpro/tools/hunlink": error=13, Permission denied", make sure the hunlink has the execute permission. go to the directory and issue the below command. Do the same for the hlink.

  >chmod 777 hunlink


![screen shot](doc/rdpro-automator-setup.png "Create service using Automator")



### Hook to the Windows Explorer Context menu
- Edit the repro_reg.reg file change the path to point to where your rdpro is exploded.
double click to import into windows registry
- In the explorer, right click on the direcotry you want ot purge, you will see the "Recursive Delete Directory" context menu
- click it to popup the rdpro GUI app.

![screen shot](doc/11-22-2014%201-14-12%20PM(2).png "Windows exploer context menu")

### Linux/Unix/Mac command line 
Explode the release to [home]/bin/rdpro for example. Export the directory to the PATH variable so you can run it from anywhere. 
Update jar location in the the rdpro.sh to where the jar is installed if you are not using the default location,
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


### ex: Force delete the "target" directories 

```
	S:\src\6.3-trunk>rdpro target -f
	Start to delete all the directories named "target" under "S:\src\6.3-trunk".
	There is no way to undelete, please confirm? (y/n or h for help)y
	working|
	Done in 10 seconds.
	Dir Removed:3944, Files removed:28690
```

### Purge an old huge directory in whole

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
