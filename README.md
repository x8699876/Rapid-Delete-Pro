It is fast because it uses mutiple thread to do the job.
It does not have problems in removing files directory and files with long names and which buried very deep in the direcotry structure. 


Disclaimer:
        Deleted files does not go to recycle bean and can't be recovered.
        The author is not responsible for any lost of files or damage incurred by running this utility.

Install:
--------------
upzip and put the files in a direcotry on the path. 


How to run
----------
open the console and go to the directory which you want to purge.
>rdpro

run rdpro.sh in linux enviroment.



rdpro -h for help
------------------


force delete the target direcoties
-------------------------------------

S:\src\6.3-trunk>rdpro target -f
Start to delete all the directories named "target" under "S:\src\6.3-trunk".
There is no way to undelete, please confirm? (y/n or h for help)y
working|
Done in 10 seconds.
Dir Removed:3944, Files removed:28690


purge a old big directory
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
