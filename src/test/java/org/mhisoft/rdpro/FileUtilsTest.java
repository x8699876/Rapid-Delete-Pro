/*
 *
 *  * Copyright (c) 2014- MHISoft LLC and/or its affiliates. All rights reserved.
 *  * Licensed to MHISoft LLC under one or more contributor
 *  * license agreements. See the NOTICE file distributed with
 *  * this work for additional information regarding copyright
 *  * ownership. MHISoft LLC licenses this file to you under
 *  * the Apache License, Version 2.0 (the "License"); you may
 *  * not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing,
 *  * software distributed under the License is distributed on an
 *  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  * KIND, either express or implied.  See the License for the
 *  * specific language governing permissions and limitations
 *  * under the License.
 *
 */

package org.mhisoft.rdpro;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

/**
 * Description:
 *
 * @author Tony Xue
 * @since Jan, 2016
 */
public class FileUtilsTest {


    static String testDir = System.getProperty("user.home") + "/test-folder";
    static String tooslDir = "S:\\projects\\mhisoft\\rdpro\\dist\\tools";

    /*
         <DIR>          folder2
         <DIR>          notalink
         <JUNCTION>     rdpro-target-link [S:\projects\mhisoft\rdpro\target]
         <SYMLINKD>     symbolic-link [S:\projects\mhisoft\rdpro\target]

        prepare the symbolic link with admin rights
         mklink  /D symbolic-link S:\projects\mhisoft\rdpro\target

     */

    public static FileUtils.UnLinkResp setupTestLinks(String linkToDir) {


        new File(testDir).mkdir();
        new File(testDir + "/notalink").mkdir();
        new File(testDir + "/folder2").mkdir();



        //make the link
        FileUtils.UnLinkResp ret = new FileUtils.UnLinkResp();
        String command, source=null;

        try {
            if (OSDetectUtils.getOS() == OSDetectUtils.OSType.MAC) {
                System.out.println("Make test link using ln -s source [target_link] :" + linkToDir);
               // command = System.getProperty("user.home") + "/bin/rdpro/tools/hlink %s %s";
                command = "ln -s  %s %s";
                source = System.getProperty("user.home") + "/projects/mhisoft/rdpro/target";
                command = String.format(command, source, linkToDir);
                ret.commandOutput = FileUtils.executeCommand(command);
                System.out.println(ret.commandOutput);

            } else {
                System.out.println("Make test link using linkd:" + linkToDir);
                command = tooslDir + "\\linkd %s %s";
                source = "S:\\projects\\mhisoft\\rdpro\\target";
                command = String.format(command, linkToDir, source);
                ret.commandOutput = FileUtils.executeCommand(command);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(new File(linkToDir).exists());

			/*

			https://stackoverflow.com/questions/23217460/how-to-create-soft-symbolic-link-using-java-nio-files
			Run secpol.msc
Go to Security Settings|Local Policies|User Rights Assignment|Create symbolic links
Add your user name.
Restart your session.
Win10 with UAC turned off - I had to set Local Policies > Security Options > User Account Control:
Run all administrators in Admin Approval Mode = Disabled
otherwise - same FileSystemException: A required privilege is not held by the client

https://superuser.com/questions/782298/how-do-i-grant-myself-permission-to-make-symbolic-links-on-windows-8-1



			*/


		try {
			Files.createSymbolicLink( Paths.get(testDir+"/symbolic-link"), Paths.get(source) );
		} catch (FileAlreadyExistsException e) {
			//ok
		}

		catch (IOException e) {
			e.printStackTrace();
		}

		Assert.assertTrue(new File(testDir+"/symbolic-link").exists());

		return ret;

	}

    @Test
    public void linksTest() {

        String junctionDir = testDir + "/rdpro-target-link";
        try {


            FileUtilsTest.setupTestLinks(junctionDir);


            String realDirNoLink = testDir + "/notalink";
            System.out.println(realDirNoLink +"is not a link.");
            System.out.println("isJunction=" + FileUtils.isJunction(realDirNoLink));
            Assert.assertFalse(FileUtils.isJunction(realDirNoLink));

            System.out.println("Files.isSymbolicLink=" + FileUtils.isSymbolicLink(realDirNoLink));



            System.out.println("\n============================================");
            //mac, no junction or hard link is made. only symbolic links

            FileUtils.UnLinkResp out;



            if (OSDetectUtils.getOS() == OSDetectUtils.OSType.WINDOWS) {
                System.out.println(junctionDir +" is  a junction.");
                System.out.println("isJunction=" + FileUtils.isJunction(junctionDir));
                Assert.assertTrue(FileUtils.isJunction(junctionDir));
                Assert.assertFalse(FileUtils.isSymbolicLink(junctionDir));

                out = FileUtils.removeWindowsJunction(junctionDir);
                System.out.println("Try linkd /d on the dir, output of command:" + out);
                Assert.assertTrue(out.unlinked);
            }
            else {
                System.out.println(junctionDir +" is  a symbolic linkf made by \"ln -s\".");
                Assert.assertTrue(FileUtils.isSymbolicLink(junctionDir));
            }

            System.out.println("Files.isSymbolicLink=" + FileUtils.isSymbolicLink(junctionDir));

            System.out.println("\n============================================");

			/*
			f you have a symbolic link that is a directory (made with mklink /d) then
			using del will delete all of the files in the target directory (the directory that the link points to),
			 rather than just the link.

SOLUTION: rmdir on the other hand will only delete the directory link, not what the link points to.
*/

            String symbolicLink = testDir + "/symbolic-link";
            System.out.println(symbolicLink +" is a symbolic link.");
            System.out.println("isJunction=" + FileUtils.isJunction(symbolicLink));
            Assert.assertFalse(FileUtils.isJunction(symbolicLink));

            System.out.println("Files.isSymbolicLink==" + FileUtils.isSymbolicLink(symbolicLink));
            Assert.assertTrue(FileUtils.isSymbolicLink(symbolicLink));

            //linkd /d will remove symbolic link as well
            out = FileUtils.removeSymbolicLink(symbolicLink);
            System.out.println("output of command:" + out);
            Assert.assertTrue(out.unlinked);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            FileUtilsTest.setupTestLinks(junctionDir);
        }

    }


    @Test
    public void testIsFileMatchTargetFilePattern() {
        String dir = "D:\\repository\\com\\successfactors\\learning\\apps\\learning\\standard-modules\\mobile\\mobile-web\\b1605.0.1";
        String[] regexPatterns = new String[]{"_*.repositories", "*.pom", "*-b1605.0.1*", "*-b1605.0.1.*", "*-b1605.0.1", "mobile*", "*"};
        File fDir = new File(dir);
        File[] files = fDir.listFiles();

        if (files != null) {
            for (String regexPattern : regexPatterns) {
                System.out.println("match pattern [" + regexPattern + "]:");

                for (File file : files) {
                    System.out.println("\t" + file.getName() + " matches:" + FileUtils.isFileMatchTargetFilePattern(file, regexPattern));
                }
            }
        }
    }


    @Test
    public void testIsFileMatchTargetFilePattern2_caseInsensitive() {
        String fname = "a.MP4";
        String fname2 = "summer-12132.MP4";
        //System.out.println("\t" + fname + " matches:" + FileUtils.isFileMatchTargetFilePattern(new File(fname), "*.mp4"));
        Assert.assertTrue(FileUtils.isFileMatchTargetFilePattern(new File(fname), "*.mp4"));
        Assert.assertTrue(FileUtils.isFileMatchTargetFilePattern(new File(fname), "*.MP4"));
        Assert.assertFalse(FileUtils.isFileMatchTargetFilePattern(new File(fname), "*.MP3"));
        Assert.assertFalse(FileUtils.isFileMatchTargetFilePattern(new File(fname), "*.MP"));
        Assert.assertTrue(FileUtils.isFileMatchTargetFilePattern(new File(fname2), "summer-*.*"));
    }



}
