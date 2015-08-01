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

/**
* Description:
*
* @author Tony Xue
* @since Aug, 2015
*/
public enum Confirmation {

	YES("y"), NO("n"), YES_TO_ALL("all"), HELP("h") , QUIT("q") ;

	String displayName;

	private Confirmation(String s) {
		displayName =s;
	}

	@Override
	public String toString() {
		return displayName;
	}

	public static Confirmation fromString(String text) {
		if (text != null) {
			for (Confirmation b : Confirmation.values()) {
				if (text.equalsIgnoreCase(b.displayName)) {
					return b;
				}
			}
		}
		return null;
	}


}
