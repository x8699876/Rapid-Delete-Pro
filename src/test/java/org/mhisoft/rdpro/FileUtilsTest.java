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


	static String testDir =System.getProperty("user.home")+"/test-folder";
	static String tooslDir ="S:\\projects\\mhisoft\\rdpro\\dist\\tools";

	public static FileUtils.UnLinkResp setupTestLinks(String linkToDir) {


		new File(testDir).mkdir();
		new File(testDir+"/notalink").mkdir();
		new File(testDir+"/folder2").mkdir();


		System.out.println("Make test link:" + linkToDir);
		//make the link
		FileUtils.UnLinkResp ret = new FileUtils.UnLinkResp();
		String command, source;


		if (OSDetectUtils.getOS() == OSDetectUtils.OSType.MAC) {
			command= System.getProperty("user.home") + "/bin/hlink/hlink %s %s";
			source = System.getProperty("user.home") + "/projects/mhisoft/rdpro/target";
			command = String.format(command, source, linkToDir);
			ret.commandOutput = FileUtils.executeCommand(command);

		}
		else {
			command= tooslDir+"\\linkd %s %s";
			source =  "S:\\projects\\mhisoft\\rdpro\\target";
			command = String.format(command,  linkToDir, source);
			ret.commandOutput = FileUtils.executeCommand(command);


			/*

			https://stackoverflow.com/questions/23217460/how-to-create-soft-symbolic-link-using-java-nio-files
			Run secpol.msc
Go to Security Settings|Local Policies|User Rights Assignment|Create symbolic links
Add your user name.
Restart your session.
Win10 with UAC turned off - I had to set Local Policies > Security Options > User Account Control: Run all
administrators in Admin Approval Mode = Disabled - otherwise - same FileSystemException: A required privilege is not held by the client


			*/




		}
		Assert.assertTrue(new File(linkToDir).exists());



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
	public void winUnlinkTest() {
		if (OSDetectUtils.getOS() != OSDetectUtils.OSType.WINDOWS)
			return;

		String linkDir = testDir +"/rdpro-target-link";
		try {


			FileUtilsTest.setupTestLinks(linkDir) ;


			String realDirNoLink = testDir+"/notalink";
			System.out.println(realDirNoLink);
			System.out.println("isSymlink=" + FileUtils.isSymlink(realDirNoLink));
			System.out.println("isSymbolicLink=" + FileUtils.isSymbolicLink(realDirNoLink));
			FileUtils.UnLinkResp out = FileUtils.unlinkDir(realDirNoLink);
			Assert.assertFalse(out.unlinked);
			System.out.println("output of command:" + out);
			System.out.println("=====================");



			System.out.println(linkDir);
			System.out.println("isSymlink=" + FileUtils.isSymlink(linkDir));
			System.out.println("isSymbolicLink=" + FileUtils.isSymbolicLink(linkDir));
			out = FileUtils.unlinkDir(linkDir);
			System.out.println("output of command:" + out);
			Assert.assertTrue(out.unlinked);

			System.out.println("=====================");
			/*
			f you have a symbolic link that is a directory (made with mklink /d) then
			using del will delete all of the files in the target directory (the directory that the link points to),
			 rather than just the link.

SOLUTION: rmdir on the other hand will only delete the directory link, not what the link points to.
*/

			String symbolicLink =testDir + "/symbolic-link";
			System.out.println(symbolicLink);
			System.out.println("isSymlink=" + FileUtils.isSymlink(symbolicLink));
			System.out.println("isSymbolicLink=" + FileUtils.isSymbolicLink(symbolicLink));

			out = FileUtils.unlinkDir(symbolicLink);
			System.out.println("output of command:" + out);
			Assert.assertTrue(out.unlinked);



		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {

			FileUtilsTest.setupTestLinks(linkDir) ;
		}

	}






	//	@Test
	public void testIsFileMatchTargetFilePattern() {
		String dir = "D:\\repository\\com\\successfactors\\learning\\apps\\learning\\standard-modules\\mobile\\mobile-web\\b1605.0.1";
		String[] regexPatterns = new String[]{"_*.repositories", "*.pom", "*-b1605.0.1*", "*-b1605.0.1", "mobile*", "*"};
		File fDir = new File(dir);
		File[] files = fDir.listFiles();

		for (String regexPattern : regexPatterns) {
			System.out.println("match pattern [" + regexPattern + "]:");
			for (File file : files) {
				System.out.println("\t" + file.getName() + " matches:" + FileUtils.isFileMatchTargetFilePattern(file, regexPattern));
			}
		}
	}




	// isSymlink  always areturn true for Mac, for both real dir and links.
	// so can't count on it

	@Test
	public void macUnlinkTest() {
		if (OSDetectUtils.getOS() != OSDetectUtils.OSType.MAC)
			return;
		
		try {

			String linkDir = testDir+"/rdpro-target-link";
			FileUtilsTest.setupTestLinks(linkDir) ;


			String realDirNoLink = testDir+"/notalink";
			System.out.println(realDirNoLink);
			System.out.println("isSymlink=" + FileUtils.isSymlink(realDirNoLink));
			FileUtils.UnLinkResp out = FileUtils.unlinkDir(realDirNoLink);
			Assert.assertFalse(out.unlinked);
			System.out.println("output of command:" + out);
			System.out.println("=====================");



			System.out.println(linkDir);
			System.out.println("isSymlink=" + FileUtils.isSymlink(linkDir));
			out = FileUtils.unlinkDir(linkDir);
			System.out.println("output of command:" + out);
			Assert.assertTrue(out.unlinked);

			System.out.println("=====================");
			//hunlink works for the sybolic links too. awesome. 
			// sybolic link ln -s folder2 symlink-folder2
			String symbolicLink = testDir+"/symlink-folder2";

			System.out.println(symbolicLink);
			System.out.println("isSymlink=" + FileUtils.isSymlink(symbolicLink));
			out = FileUtils.unlinkDir(symbolicLink);
			System.out.println("output of command:" + out);
			Assert.assertTrue(out.unlinked);


			FileUtilsTest.setupTestLinks(linkDir) ;



		} catch (IOException e) {
			e.printStackTrace();
		}

	}



}
