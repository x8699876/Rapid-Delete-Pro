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

/**
 * Description:
 *
 * @author Tony Xue
 * @since Sep, 2016
 */
public class OSDetectUtils {

		public enum OSType {
			WINDOWS, LINUX, MAC, SOLARIS
		};// Operating systems.

		private static OSType osType = null;

		public static OSType getOS() {
			if (osType == null) {
				String operSys = System.getProperty("os.name").toLowerCase();
				if (operSys.contains("win")) {
					osType = OSType.WINDOWS;
				} else if (operSys.contains("nix") || operSys.contains("nux")
						|| operSys.contains("aix")) {
					osType = OSType.LINUX;
				} else if (operSys.contains("mac")) {
					osType = OSType.MAC;
				} else if (operSys.contains("sunos")) {
					osType = OSType.SOLARIS;
				}
			}
			return osType;
		}
}
