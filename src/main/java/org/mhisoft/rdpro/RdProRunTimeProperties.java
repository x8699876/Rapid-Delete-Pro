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

import java.util.Arrays;

/**
 * Run time properties
 */
public class RdProRunTimeProperties {
	String rootDir = null;
	String targetDir = null;

	boolean verbose = false;
	boolean forceDelete = false;
	boolean interactive = true;
	Integer numberOfWorkers = 5;
	String[] targetFilePatterns;
	boolean unLinkDirFirst=false;

	boolean success = true;
	boolean answerYforAll = false;
	boolean debug = false;
	boolean dryRun;

	public String getRootDir() {
		return rootDir;
	}

	public void setRootDir(String rootDir) {
		this.rootDir = rootDir;
	}

	public String getTargetDir() {
		return targetDir;
	}

	public void setTargetDir(String targetDir) {
		this.targetDir = targetDir;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public boolean isForceDelete() {
		return forceDelete;
	}

	public void setForceDelete(boolean forceDelete) {
		this.forceDelete = forceDelete;
	}

	public boolean isInteractive() {
		return interactive;
	}

	public void setInteractive(boolean interactive) {
		this.interactive = interactive;
	}

	public Integer getNumberOfWorkers() {
		return numberOfWorkers;
	}

	public void setNumberOfWorkers(Integer numberOfWorkers) {
		this.numberOfWorkers = numberOfWorkers;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isAnswerYforAll() {
		return answerYforAll;
	}

	public void setAnswerYforAll(boolean answerYforAll) {
		this.answerYforAll = answerYforAll;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public String[] getTargetFilePatterns() {
		return targetFilePatterns;
	}

	public String getTargetFilePatternString() {
		return targetFilePatterns==null?"all files":Arrays.toString(targetFilePatterns);
	}

	public void setTargetFilePatterns(String[] targetFilePatterns) {
		this.targetFilePatterns = targetFilePatterns;
	}

	public void setTargetFilePatterns(final String targetFilePatterns) {
		this.targetFilePatterns = FileUtils.split(targetFilePatterns==null?null:targetFilePatterns.trim(), ',', ';');
	}

	public boolean isUnLinkDirFirst() {
		return unLinkDirFirst;
	}

	public void setUnLinkDirFirst(boolean unLinkDirFirst) {
		this.unLinkDirFirst = unLinkDirFirst;
	}

	public boolean isDryRun() {
		return dryRun;
	}

	public void setDryRun(boolean dryRun) {
		this.dryRun = dryRun;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("RdProRunTimeProperties{");
		sb.append("rootDir='").append(rootDir).append('\'');
		sb.append(", targetDir='").append(targetDir).append('\'');
		sb.append(", targetFilePatterns=").append(Arrays.toString(targetFilePatterns));
		sb.append(", verbose=").append(verbose);
		sb.append(", dryRun=").append(dryRun);
		sb.append(", forceDelete=").append(forceDelete);
		sb.append(", unLinkDirFirst=").append(unLinkDirFirst);
		sb.append(", interactive=").append(interactive);
		sb.append(", numberOfWorkers=").append(numberOfWorkers);
		sb.append(", success=").append(success);
		sb.append(", answerYforAll=").append(answerYforAll);
		sb.append(", debug=").append(debug);
		sb.append('}');
		return sb.toString();
	}
}
