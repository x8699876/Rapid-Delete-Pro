<h3>What is RdPro</h3>

* A Powerful Recursive Directory Purge Utility 
* It is fast because it uses multiple thread to do the job.
* It does not have problems in removing files directory and files with long names and which buried very deep in the directory structure. 

<h3>Download and Install<h3>

There is a windows executable, batch files and Unix/Linux shell script included to run the application. Unzip and put the files in a directory on the environment path. 

<h3>How to run</h3>

  open the console and go to the directory which you want to purge. 
  <pre>
  >rdpro
  run rdpro.sh in linux environment.
  </pre>


<h3>rdpro -h for help</h3>

<pre>
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

</pre>


<h3>Force delete the target directories</h3>

<pre>
S:\src\6.3-trunk>rdpro target -f
Start to delete all the directories named "target" under "S:\src\6.3-trunk".
There is no way to undelete, please confirm? (y/n or h for help)y
working|
Done in 10 seconds.
Dir Removed:3944, Files removed:28690
</pre>

<h3>Purge an old huge directory</h3>

<pre>
S:\src\b1210-trunk>rdpro

Start to delete everything under "S:\src\b1210-trunk" (y/n or h for help)?y
 *Warning* There is no way to undelete. Confirm again (y/n or h for help)?y
working.
Confirm to delete file:S:\src\b1210-trunk\.externals(y/n/all)?dfd
        response "dfd" not recognized. input again:all
-
Done in 29 seconds.
Dir Removed:14285, Files removed:84409
</pre>


If somehow it leaves some empty directory after running it, make sure they are locked and run it again. 

<h3>Dsclaimer</h3>
* Deleted files do not go to recycle bin and can't be recovered.
* The author is not responsible for any loss of files or damage incurred by running this utility.

<h3>License</h3>
Apache License 2.0, January 2004 http://www.apache.org/licenses/
