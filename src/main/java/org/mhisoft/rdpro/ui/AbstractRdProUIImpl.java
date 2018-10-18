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

package org.mhisoft.rdpro.ui;

import java.io.IOException;

import org.mhisoft.rdpro.FileUtils;
import org.mhisoft.rdpro.RdProRunTimeProperties;

/**
 * Description:
 *
 * @author Tony Xue
 * @since Nov, 2014
 */
public abstract class AbstractRdProUIImpl implements RdProUI {

	public void printBuildAndDisclaimer() {
		println("RdPro(" + version +","+ build +")- a super fast directory and file delete utility by Tony Xue, MHISoft");
		println("(https://github.com/mhisoft/rdpro)");
		println("Important note: Purged files does not go to recycle bin so can't be recovered! Use Wisely.");
		try {
			println("unlink tool path:" + FileUtils.getRemoveHardLinkCommandTemplate())  ;
		} catch (IOException e) {
			println("unlink tool is not setup. please refer to the project WIKI on the github, error: " + e.getMessage());
		}
	}

	public void dumpArguments(String[] args, RdProRunTimeProperties props) {
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			println("arg["+i+"]:" + arg);
		}

		println("parsed properties:") ;
		println(props.toString());

	}

}
