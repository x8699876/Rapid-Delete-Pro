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

import org.mhisoft.rdpro.ui.RdProUI;

/**
 * Description:
 *
 * @author Tony Xue
 * @since Nov, 2014
 */
public class FileUtils {

	public static void removeDir(File dir, RdProUI ui, FileRemoveStatistics frs) {
		try {
			if (!dir.delete()) {
				ui.println("\t[warn]Can't remove:" + dir.getAbsolutePath() + ". May be locked. ");
			} else {
				ui.println("\tRemoved dir:" + dir.getAbsolutePath());
				frs.dirRemoved++;
			}
		} catch (Exception e) {
			ui.println("\t[error]:" +e.getMessage());
		}
	}

}
