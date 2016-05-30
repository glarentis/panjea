/*
 * Copyright 2005 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.jidesoft.spring.richclient.components;

import org.springframework.richclient.application.statusbar.support.StatusBarProgressMonitor;
import org.springframework.richclient.progress.ProgressMonitor;

import com.jidesoft.status.ProgressStatusBarItem;

/**
 * An adapter that converts the JIDE ProgressStatusBarItem to implement the Spring RCP ProgressMonitor interface.
 * 
 * @author Jonny Wray
 * 
 */
public class ProgressStatusBarAdapter implements ProgressMonitor {

	private int numTasks;
	private final ProgressStatusBarItem barItem;
	private boolean canceled = false;

	{
		numTasks = 0;
	}

	/**
	 * Constructs an adapter to convert a JIDE ProgressStatusBarItem into a Spring RCP ProgressMonitor.
	 * 
	 * @param barItem
	 *            The ProgressStatusBarItem to convert
	 */
	public ProgressStatusBarAdapter(ProgressStatusBarItem barItem) {
		this(barItem, true);
	}

	public ProgressStatusBarAdapter(ProgressStatusBarItem barItem, boolean displayCancelButton) {
		this.barItem = barItem;
		if (displayCancelButton) {
			barItem.setCancelCallback(new ProgressStatusBarItem.CancelCallback() {

				@Override
				public void cancelPerformed() {
					setCanceled(true);
				}
			});
		} else {
			barItem.setCancelCallback(null);
		}
	}

	/**
	 * Notifies that the work is done; that is, either the main task is completed or the user cancelled it.
	 * 
	 * done() can be called more than once; an implementation should be prepared to handle this case.
	 */
	@Override
	public void done() {
		numTasks--;
		if (numTasks <= 0) {
			numTasks = 0;
			barItem.setProgress(100);
		} else {
			barItem.setProgressStatus("operazioni attive:" + numTasks);
		}
	}

	/**
	 * Returns true if the user does some UI action to cancel this operation. (like hitting the Cancel button on the
	 * progress dialog). The long running operation typically polls isCanceled().
	 */
	@Override
	public boolean isCanceled() {
		return canceled;
	}

	/**
	 * Attempts to cancel the monitored task.
	 */
	@Override
	public void setCanceled(boolean b) {
		this.canceled = b;
	}

	public void setCancelEnabled(boolean cancelEnabled) {
		barItem.getCancelButton().setEnabled(cancelEnabled);
	}

	/**
	 * Null method, no subTask support currently
	 */
	@Override
	public void subTaskStarted(String name) {

	}

	@Override
	public void taskStarted(String name, int totalWork) {
		if (totalWork == StatusBarProgressMonitor.UNKNOWN) {
			barItem.setIndeterminate(true);
		} else {
			barItem.setProgress(0);
		}
		numTasks++;
		barItem.setProgressStatus("operazioni attive:" + numTasks);
	}

	/**
	 * Notifies that a percentage of the work has been completed. This is called by clients when the work is performed
	 * and is used to update the progress monitor.
	 * 
	 * @param work
	 *            the percentage complete (0..100)
	 */
	@Override
	public void worked(int work) {
		barItem.setProgress(work);
	}

}
