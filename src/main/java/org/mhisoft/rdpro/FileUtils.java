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

import java.util.Properties;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.mhisoft.rdpro.ui.RdProUI;

/**
 * Description:
 *
 * @author Tony Xue
 * @since Nov, 2014
 */
public class FileUtils {

	public static void removeDir(File dir, RdProUI ui, FileRemoveStatistics frs, final boolean verbose, final boolean unLinkFirst) {
		try {


			if (dir.exists()) {
			 //if still exist delete.
				if (!dir.delete()) {
					ui.println("\t[warn]Can't remove:" + dir.getAbsolutePath() + ". May be locked. ");
				} else {
					if (verbose)
						ui.println("\tRemoved dir:" + dir.getAbsolutePath());
					frs.dirRemoved++;
				}
			}
		} catch (Exception e) {
			ui.println("\t[error]:" +e.getMessage());
		}
	}


	private static void copyFileUsingFileChannels(File source, File dest)
			throws IOException {
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			inputChannel = new FileInputStream(source).getChannel();
			outputChannel = new FileOutputStream(dest).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		} finally {
			inputChannel.close();
			outputChannel.close();
		}
	}

	private static final int BUFFER = 8192;

	private static void nioBufferCopy(File source, File target) {
		FileChannel in = null;
		FileChannel out = null;

		try {
			in = new FileInputStream(source).getChannel();
			out = new FileOutputStream(target).getChannel();

			ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER);
			while (in.read(buffer) != -1) {
				buffer.flip();

				while(buffer.hasRemaining()){
					out.write(buffer);
				}

				buffer.clear();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(in);
			close(out);
		}
	}

	private static void close(Closeable closable) {
		if (closable != null) {
			try {
				closable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * Split the string into array
	 * @param str The original string
	 * @param deli The delimiter
	 * @return
	 */
	public static String[] split(final String str, final String deli) {
		String[] arr = null;
		if (str != null && str.trim().length() > 0) {
			final StringTokenizer st = new StringTokenizer(str, deli);
			arr = new String[st.countTokens()];
			for (int i = 0; st.hasMoreTokens(); i++) {
				arr[i] = st.nextToken().trim();
			}
		}

		return arr;
	}



	public static boolean isFileMatchTargetFilePattern(final File f, final String targetPattern) {
		String regex = targetPattern.replace(".", "\\.");
		regex = regex.replace("?", ".?").replace("*", ".*");
		return f.getName().matches(regex);

	}

	/**
	 * Return true as long as one file pattern matches.
	 * it checks nulls on targetPatterns. If nothing matches, return true.
	 * @param f
	 * @param targetPatterns
	 * @return
	 */
	public static boolean isFileMatchTargetFilePatterns(final File f, final String[] targetPatterns) {
		if (targetPatterns==null)
			return true; //nothing to match
		for (String targetPattern : targetPatterns) {
			boolean b = isFileMatchTargetFilePattern(f, targetPattern );
			if (b)
				return true;
		}

		return false;
	}


	static final String default_linkd_path = "C:/bin/rdpro/tools/linkd.exe" ;
    static String default_mac_hunlink_path = System.getProperty("user.home")+ "/bin/rdpro/tools/hunlink" ;

    //cache it for performance. 
    static String commandTemplate=null;

	public  static String getRemoveHardLinkCommandTemplate() throws IOException {
		if (commandTemplate!=null)
			return commandTemplate;

		//read rdpro.properties in the user home's folder?
		String homeDir = System.getProperty("user.home");
		Properties config = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(homeDir+"/rdpro.properties");
			// load a properties file
			config.load(input);

		} catch (IOException ex) {
			//
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					//e.printStackTrace();
				}
			}
		}


//		if (OSDetectUtils.getOS()== OSDetectUtils.OSType.WINDOWS || OSDetectUtils.getOS()== OSDetectUtils.OSType.LINUX ) {
//		}
		if (OSDetectUtils.getOS()== OSDetectUtils.OSType.MAC) {

			String pathToLinkd = (config.getProperty("pathToUnlinkDirExecutable")==null? default_mac_hunlink_path : config.getProperty("pathToUnlinkDirExecutable"));
			if (!new File(pathToLinkd).exists())
				throw new IOException("pathToUnlinkDirExecutable is not valid, make sure the linkd.exe exists in the path specified:" + pathToLinkd);

			commandTemplate = pathToLinkd+ " %s";


		}
		else if (OSDetectUtils.getOS()== OSDetectUtils.OSType.WINDOWS) {
			String pathToLinkd = (config.getProperty("pathToUnlinkDirExecutable")==null? default_linkd_path : config.getProperty("pathToUnlinkDirExecutable"));
			File f = new File(pathToLinkd);
			if ( !f.exists())
				throw new IOException("pathToUnlinkDirExecutable is not valid, make sure the linkd.exe exists in the path specified:" + pathToLinkd);

			commandTemplate = pathToLinkd+ " %s /D ";

		}
		else {
			throw new IOException("OS not supported:" +OSDetectUtils.getOS());
		}
		return commandTemplate;
	}


	/**
	 * Return true if the direcotry does not exist after unlink. ie.e. we can assume it has been unlinked.

	 */

	public static class UnLinkResp {
		public boolean unlinked;
		public String commandOutput;

		@Override
		public String toString() {
			return "UnLinkResp{" +
					"unlinked=" + unlinked +
					", commandOutput='" + commandOutput + '\'' +
					'}';
		}
	}

	public static UnLinkResp unlinkDir(final String dir) throws IOException {
		UnLinkResp ret = new UnLinkResp();
		if (OSDetectUtils.getOS()== OSDetectUtils.OSType.MAC) {
			if (isSymlink(dir)) {
				String command = getRemoveHardLinkCommandTemplate();
				command = String.format(command, dir);
				ret.commandOutput = executeCommand(command);
				ret.unlinked = !Files.exists(Paths.get(dir));
			}
		}
		else if (OSDetectUtils.getOS()== OSDetectUtils.OSType.WINDOWS) {

			if (isSymbolicLink(dir)) {
				//use rmdir to remove the symbolic link
				Files.delete(Paths.get(dir));
				ret.unlinked = !new File(dir).exists();
			}

			else if (isSymlink(dir)) {   //we have to check with linkd first. or it will remove the none symbolic linked dir as well.
				String command = getRemoveHardLinkCommandTemplate();
				command = String.format(command, dir);
				ret.commandOutput = executeCommand(command) ;
				ret.unlinked =  ret.commandOutput.contains("The delete operation succeeded");
			}
		}
		else {
			throw new IOException("unlinkDir() error, OS not supported:" +OSDetectUtils.getOS());
		}

		return ret;


	}


	/**
	 * returns true for symbolic link , also "soft" link
	 * for windows, it is creates using mklink /D  Link Target
	 * uses rmdir to remove soft links on windows.
	 * @param file
	 * @return
	 */
	public static boolean isSymbolicLink (String file) {
		Path path = Paths.get(file);
		boolean isSymbolicLink = Files.isSymbolicLink(path);
		return isSymbolicLink;
	}



	/**
	 * Is the directory a link.
	 * @param file
	 * @return
	 * @throws IOException
	 */
	//not working for my hard link
	//for MAC it is returning true always.
	public static boolean isSymlink(String file) throws IOException {

		if (OSDetectUtils.getOS()== OSDetectUtils.OSType.MAC) {
			String command = getRemoveHardLinkCommandTemplate();
			command = String.format(command, file);
			String out = executeCommand(command);
			boolean isLink= !out.contains("Operation not permitted");
			return isLink;
		}
		if (OSDetectUtils.getOS()== OSDetectUtils.OSType.WINDOWS) {
			String command = getRemoveHardLinkCommandTemplate();
			command = String.format(command, file);
			//remove /D
			command = command.substring(0, command.lastIndexOf("/D"));
			String out = executeCommand(command);
			boolean isLink= !out.contains("is not linked to another directory");
			return isLink;
		}
		else {
			throw new IOException("unlinkDir() error, OS not supported:" +OSDetectUtils.getOS());
		}



	}


	public static String executeCommand(String command) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();

	}



}
