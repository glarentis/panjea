/*
 * Copyright 2002-2008 the original author or authors.
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
package it.eurotn.richclient.application.splash;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.SplashScreen;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;

import org.springframework.core.io.Resource;
import org.springframework.richclient.application.splash.MonitoringSplashScreen;
import org.springframework.richclient.progress.ProgressMonitor;
import org.springframework.util.Assert;

/**
 * <code>SplashScreen</code> implementation that delegates to a <code>java.awt.SplashScreen</code> implementation.
 * 
 * @author Peter De Bruycker
 */
public class MustangSplashScreen implements MonitoringSplashScreen, ProgressMonitor {
	private java.awt.SplashScreen splashScreen;
	private float colorSplash = 0f;
	private Image colorSplashImage;
	private Image sepiaSplashImage;

	@Override
	public void dispose() {
		splashScreen.close();
	}

	@Override
	public void done() {

	}

	@Override
	public ProgressMonitor getProgressMonitor() {
		return this;
	}

	/**
	 * Returns the <code>java.awt.SplashScreen</code> implementation that has been set at startup. The splashscreen can
	 * then be used to perform custom painting, etc...
	 * 
	 * @return the splash screen
	 */
	protected java.awt.SplashScreen getSplashScreen() {
		return splashScreen;
	}

	@Override
	public boolean isCanceled() {

		return false;
	}

	@Override
	public void setCanceled(boolean flag) {

	}

	public void setImageResourcePath(Resource resource) {
		try {
			URL url = resource.getURL();
			URL urlSepia = new URL("/it/eurotn/panjea/resources/images/logoSepia.png");
			colorSplashImage = Toolkit.getDefaultToolkit().createImage(url);
			sepiaSplashImage = Toolkit.getDefaultToolkit().createImage(urlSepia);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void splash() {
		splashScreen = java.awt.SplashScreen.getSplashScreen();
		Assert.state(splashScreen != null, "No splash screen defined on startup");
	}

	@Override
	public void subTaskStarted(String s) {

	}

	@Override
	public void taskStarted(String s, int totWorking) {
		System.out.println("Tesk started " + s + " : " + totWorking);

	}

	@Override
	public void worked(int i) {

		this.colorSplash += 0.01f;

		System.out.println("Work " + colorSplash);

		SplashScreen splash = getSplashScreen();
		Graphics2D g2 = splash.createGraphics();

		// g2.setComposite(AlphaComposite.Clear);
		g2.fillRect(0, 0, splash.getSize().width, splash.getSize().height);

		g2.setPaintMode();
		// g2.drawImage(sepiaSplashImage, 0, 0, null);

		g2.setComposite(AlphaComposite.SrcOver.derive(colorSplash));
		g2.drawImage(colorSplashImage, 0, 0, null);

		g2.dispose();
		splash.update();
	}
}
