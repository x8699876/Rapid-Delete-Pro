
What is RdPro
-------------------
A Powerful Recursive Directory Purge Utility 
It is fast because it uses multiple thread to do the job.
It does not have problems in removing files directory and files with long names and which buried very deep in the directory structure. 


Disclaimer:
        Deleted files do not go to recycle bean and can't be recovered.
        The author is not responsible for any loss of files or damage incurred by running this utility.

Install:
--------------
upzip and put the files in a directory on the path. 


How to run
----------
open the console and go to the directory which you want to purge. There is a windows executable, batch files and Unix/Linux shell script include to run the application

>rdpro

run rdpro.sh in linux environment.



rdpro -h for help
------------------
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


Force delete the target directories
-------------------------------------

S:\src\6.3-trunk>rdpro target -f
Start to delete all the directories named "target" under "S:\src\6.3-trunk".
There is no way to undelete, please confirm? (y/n or h for help)y
working|
Done in 10 seconds.
Dir Removed:3944, Files removed:28690


Purge an old huge directory
------------------------------
S:\src\b1210-trunk>rdpro

Start to delete everything under "S:\src\b1210-trunk" (y/n or h for help)?y
 *Warning* There is no way to undelete. Confirm again (y/n or h for help)?y
working.
Confirm to delete file:S:\src\b1210-trunk\.externals(y/n/all)?dfd
        response "dfd" not recognized. input again:all
-
Done in 29 seconds.
Dir Removed:14285, Files removed:84409


If somehow it leaves some empty directory after running it, make sure they are locked and run it again.
