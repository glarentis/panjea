/*
 * Copyright 2002-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.eurotn.richclient.application.splash;

import javax.swing.JProgressBar;

import org.springframework.richclient.progress.ProgressMonitor;

/**
 * <code>ProgressMonitor</code> implementation that delegates to a <code>JProgressBar</code>.
 * 
 * @author Peter De Bruycker
 */
public class ProgressBarProgressMonitor implements ProgressMonitor {

	private JProgressBar progressBar;
	private boolean canceled;

	public ProgressBarProgressMonitor() {
		this.progressBar = new JProgressBar();
	}

	@Override
	public void done() {
		// not used
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	@Override
	public boolean isCanceled() {
		return canceled;
	}

	@Override
	public void setCanceled(boolean b) {
		this.canceled = b;
	}

	@Override
	public void subTaskStarted(String name) {
		progressBar.setString(name);
		// System.out.println("CARICO IL BEAN " + name);
	}

	@Override
	public void taskStarted(String name, int totalWork) {
		progressBar.setIndeterminate(false);
		progressBar.setMinimum(0);
		progressBar.setMaximum(totalWork);
		progressBar.setString(name);
	}

	@Override
	public void worked(int work) {
		progressBar.setValue(progressBar.getValue() + work);
	}
}
