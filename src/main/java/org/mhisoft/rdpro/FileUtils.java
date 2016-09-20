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

import java.util.StringTokenizer;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

	public static void removeDir(File dir, RdProUI ui, FileRemoveStatistics frs, final boolean verbose) {
		try {
			if (!dir.delete()) {
				ui.println("\t[warn]Can't remove:" + dir.getAbsolutePath() + ". May be locked. ");
			} else {
				if (verbose)
					ui.println("\tRemoved dir:" + dir.getAbsolutePath());
				frs.dirRemoved++;
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


	static final String tools_path_win ="C:/bin/rdpro/tools";
	static final String linkd_path = tools_path_win+ "/linkd.exe" ;
	//static final String hlink_path = "S:\\projects\\mhisoft\\rdpro\\dist\\tools\lhunlink" ;
	public static void unlinkDir(final String dir) {
		if (OSDetectUtils.getOS()== OSDetectUtils.OSType.WINDOWS || OSDetectUtils.getOS()== OSDetectUtils.OSType.LINUX ) {
			//use linkd executable
			try {
				String command =linkd_path +" "+ dir +" /D ";
				Runtime.getRuntime().exec(command);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (OSDetectUtils.getOS()== OSDetectUtils.OSType.MAC) {
			//use hlink
		}

	}


	public static boolean isSymlink(String file) throws IOException {
		Path f = Paths.get(file);
		boolean isSymbolicLink = Files.isSymbolicLink(f);
		return  isSymbolicLink;
	}

	public static void main(String[] args) {

		try {
			System.out.println( "S:/tomcat-servers/plateau-talent-management-b1611/webapps/learning : " +
					FileUtils.isSymlink("S:/tomcat-servers/plateau-talent-management-b1611/webapps/learning"));
			FileUtils.unlinkDir("S:\\tomcat-servers\\plateau-talent-management-b1511\\webapps\\learning");
			FileUtils.unlinkDir("S:\\tomcat-servers\\plateau-talent-management-b1511\\webapps\\tools");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
