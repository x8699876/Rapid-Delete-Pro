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

	@Test
	public void testUnlink1() {
		try {

			String dir = "S:\\tomcat-servers\\plateau-talent-management-b1611\\webapps\\learning";
			System.out.println(dir);
			System.out.println("isSymlink_WIN=" + FileUtils.isSymlink(dir));
			FileUtils.UnLinkResp out = FileUtils.unlinkDir(dir);
			System.out.println("output of command:" + out);
			boolean unlinked = !Files.exists(Paths.get(dir));
			System.out.println("check dir exists afterward, unlinked=" + unlinked);


		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testUnlink2() {

		try {


			String realDirNoLink = "S:\\tomcat-servers\\plateau-talent-management-b1611\\webapps\\temp";
			System.out.println(realDirNoLink);
			System.out.println("isSymlink_WIN=" + FileUtils.isSymlink(realDirNoLink));
			FileUtils.UnLinkResp out = FileUtils.unlinkDir(realDirNoLink);
			System.out.println("output of command:" + out);


			//FileUtils.unlinkDir("S:\\tomcat-servers\\plateau-talent-management-b1611\\webapps\tools");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testUnlink3() {

		try {

			String realDirNoLink = "S:\\tomcat-servers\\plateau-talent-management-b1611\\webapps\\tools";
			System.out.println(realDirNoLink);
			System.out.println("isSymlink_WIN=" + FileUtils.isSymlink(realDirNoLink));
			FileUtils.UnLinkResp out = FileUtils.unlinkDir(realDirNoLink);
			System.out.println("output of command:" + out);


			System.out.println("do it again");
			 out = FileUtils.unlinkDir(realDirNoLink);
			System.out.println("output of command:" + out);


			//FileUtils.unlinkDir("S:\\tomcat-servers\\plateau-talent-management-b1611\\webapps\tools");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static FileUtils.UnLinkResp makeTestLink(String linkDir) {

		String testDir =System.getProperty("user.home")+"/bin/hlink/test-folder";
		new File(testDir).mkdir();
		new File(testDir+"/notalink").mkdir();


		System.out.println("Make test link:" + linkDir);
		//make the link
		FileUtils.UnLinkResp ret = new FileUtils.UnLinkResp();
		String command = System.getProperty("user.home")+"/bin/hlink/hlink %s %s";
		String source = System.getProperty("user.home")+"/projects/mhisoft/rdpro/target";
		command = String.format(command, source, linkDir);
		ret.commandOutput = FileUtils.executeCommand(command);
		Assert.assertTrue(new File(linkDir).exists());
		return ret;

	}

	// isSymlink  always areturn true for Mac, for both real dir and links.
	// so can't count on it

	@Test
	public void macUnlinkTest() {
		if (OSDetectUtils.getOS() != OSDetectUtils.OSType.MAC)
			return;
		
		try {

			String realDirNoLink = System.getProperty("user.home")+ "/bin/hlink/test-folder/notalink";
			System.out.println(realDirNoLink);
			System.out.println("isSymlink=" + FileUtils.isSymlink(realDirNoLink));
			FileUtils.UnLinkResp out = FileUtils.unlinkDir(realDirNoLink);
			System.out.println("output of command:" + out);
			System.out.println("");

			String linkDir = System.getProperty("user.home")+"/bin/hlink/test-folder/rdpro-target-link";
			FileUtilsTest.makeTestLink(linkDir) ;


			System.out.println(linkDir);
			System.out.println("isSymlink=" + FileUtils.isSymlink(linkDir));
			out = FileUtils.unlinkDir(linkDir);
			System.out.println("output of command:" + out);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}



}
