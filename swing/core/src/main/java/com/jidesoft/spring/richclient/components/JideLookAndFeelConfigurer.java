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

import java.awt.Font;

import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;

import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticTheme;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.office2003.Office2003Painter;
import com.jidesoft.utils.SystemInfo;

/**
 * A bean that allows the look and feel for an application to be configured in a platform specific way from Spring RCP
 * configuration files. Currently, Windows, MacOS X and Linux are supported. If nothing is specified within the
 * configuration sensible defaults are used. Windows systems will use the WINDOWS_LNF with the OFFICE2003_STYLE for XP
 * and ECLIPSE_STYLE for all other. MacOS X will use the AQUA_LNF and Linux the METAL_LNF. Other platforms will use the
 * JVM defaults.
 *
 * @author Jonny Wray
 *
 */
public class JideLookAndFeelConfigurer implements InitializingBean {

	private static final Logger log = Logger.getLogger(JideLookAndFeelConfigurer.class);
	private int windowsStyle = LookAndFeelFactory.ECLIPSE_STYLE;
	private int windowsXPStyle = LookAndFeelFactory.OFFICE2003_STYLE;
	private int linuxStyle = LookAndFeelFactory.XERTO_STYLE;

	private String linuxLNF = LookAndFeelFactory.METAL_LNF;
	private String macosxLNF = LookAndFeelFactory.AQUA_LNF;
	private String windowsLNF = LookAndFeelFactory.WINDOWS_LNF;
	private final String xplatformLNF = LookAndFeelFactory.METAL_LNF;
	private String userSkin;
	private String userTheme;

	private SettingsManager settingsManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		userTheme = settingsManager.getUserSettings().getString("look_and_feel_theme");
		userSkin = settingsManager.getUserSettings().getString("look_and_feel_style");
		linuxLNF = settingsManager.getUserSettings().getString("look_and_feel_style_linux");
		windowsLNF = settingsManager.getUserSettings().getString("look_and_feel_style_windows");

		applyCustomUIManagerValue();

		if (userTheme == null || userTheme.isEmpty()) {
			if (SystemInfo.isWindows() && isLookAndFeelInstalled(windowsLNF)) {
				if (log.isDebugEnabled()) {
					log.debug("Detected and using windows look and feel " + windowsLNF);
				}
				UIManager.setLookAndFeel(windowsLNF);
				if (SystemInfo.isWindowsXP()) {
					if (log.isDebugEnabled()) {
						log.debug("Using Windows XP style " + windowsXPStyle);
					}
					LookAndFeelFactory.installJideExtension(windowsXPStyle);
					if (LookAndFeelFactory.OFFICE2003_STYLE == windowsXPStyle) {
						Office2003Painter.setNative(true);
					}
				} else {
					if (log.isDebugEnabled()) {
						log.debug("Using Windows style " + windowsStyle);
					}
					LookAndFeelFactory.installJideExtension(windowsStyle);
				}
			} else if (SystemInfo.isLinux() && isLookAndFeelInstalled(linuxLNF)) {
				if (log.isDebugEnabled()) {
					log.debug("Detected and using Linux look and feel " + linuxLNF);
				}
				UIManager.setLookAndFeel(linuxLNF);
				LookAndFeelFactory.installJideExtension(linuxStyle);
			} else if (SystemInfo.isMacOSX() && isLookAndFeelInstalled(macosxLNF)) {
				if (log.isDebugEnabled()) {
					log.debug("Detected and using MacOS X look and feel " + macosxLNF);
				}
				UIManager.setLookAndFeel(macosxLNF);
				LookAndFeelFactory.installJideExtension();
			} else { // cross-platform default
				if (log.isDebugEnabled()) {
					log.debug("Using cross platform look and feel " + xplatformLNF);
				}
				UIManager.setLookAndFeel(xplatformLNF);
				LookAndFeelFactory.installJideExtension();

			}
			settingsManager.getUserSettings().setString("look_and_feel_style",
					UIManager.getLookAndFeel().getClass().getCanonicalName());
			settingsManager.getUserSettings().save();
		} else {
			applyLAF(userSkin, userTheme);
		}
		applyFontSize();
	}

	private void applyCustomUIManagerValue() {
		LookAndFeelFactory.addUIDefaultsCustomizer(new LookAndFeelFactory.UIDefaultsCustomizer() {
			@Override
			public void customize(UIDefaults defaults) {
				defaults.put("DockableFrameTitlePane.showIcon", true);
			}
		});
		/*
		 * UIManager.put("TextField.disabledBackground", new ColorUIResource(Color.WHITE));
		 * UIManager.put("TextField.inactiveBackground", new ColorUIResource(Color.WHITE));
		 * UIManager.put("FormattedTextField.inactiveBackground", new ColorUIResource(Color.WHITE));
		 */
	}

	private void applyFontSize() {
		Object[] objs = UIManager.getLookAndFeel().getDefaults().keySet().toArray();
		for (Object obj : objs) {
			if (obj.toString().toUpperCase().indexOf(".FONT") != -1) {
				Font font = UIManager.getFont(obj);
				try {
					float fontSize = settingsManager.getUserSettings().getFloat(obj.toString());
					if (fontSize != 0.0) {
						font = font.deriveFont(fontSize);
						UIManager.put(obj, new FontUIResource(font));
					}
				} catch (SettingsException e) {
					// non faccio nulla
					log.debug(e.getStackTrace());
				}
			}
		}
	}

	public void applyLAF(String skinName, String themeName) throws Exception {
		Class<LookAndFeel> cl = (Class<LookAndFeel>) Class.forName(skinName);
		LookAndFeel skin = cl.newInstance();
		if (skin instanceof PlasticLookAndFeel) {
			if (userTheme != null && !themeName.isEmpty()) {
				Class<PlasticTheme> cl2 = (Class<PlasticTheme>) Class.forName(themeName);
				PlasticLookAndFeel.setPlasticTheme(cl2.newInstance());
			}
		}
		UIManager.setLookAndFeel(skin);

	}

	public void decrementFontSize() {
		Object[] objs = UIManager.getLookAndFeel().getDefaults().keySet().toArray();

		for (Object obj : objs) {
			if (obj.toString().toUpperCase().indexOf(".FONT") != -1) {
				Font font = UIManager.getFont(obj);
				try {
					settingsManager.getUserSettings().setFloat(obj.toString(), font.getSize() - 1);
				} catch (SettingsException e) {
					throw new RuntimeException(e);
				}
				font = font.deriveFont((float) (font.getSize() - 1));
				UIManager.put(obj, new FontUIResource(font));
			}
		}
		try {
			settingsManager.getUserSettings().save();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		SwingUtilities.updateComponentTreeUI(Application.instance().getActiveWindow().getControl());
	}

	/**
	 * @return Returns the settingsManager.
	 */
	public SettingsManager getSettingsManager() {
		return settingsManager;
	}

	public void incrementFontSize() {
		Object[] objs = UIManager.getLookAndFeel().getDefaults().keySet().toArray();
		for (Object obj : objs) {
			if (obj.toString().toUpperCase().indexOf(".FONT") != -1) {
				Font font = UIManager.getFont(obj);
				try {
					settingsManager.getUserSettings().setFloat(obj.toString(), font.getSize() + 1);
				} catch (SettingsException e) {
					throw new RuntimeException(e);
				}
				font = font.deriveFont((float) (font.getSize() + 1));
				UIManager.put(obj, new FontUIResource(font));
			}
		}
		try {
			settingsManager.getUserSettings().save();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		SwingUtilities.updateComponentTreeUI(Application.instance().getActiveWindow().getControl());
	}

	/**
	 *
	 * @param lookAndFeelName
	 * @return
	 */
	private boolean isLookAndFeelInstalled(String lookAndFeelName) {

		try {
			Class.forName(lookAndFeelName).newInstance();
			if (log.isDebugEnabled()) {
				log.debug("Found look and feel " + lookAndFeelName);
			}
			return true;
		} catch (InstantiationException e) {
			if (log.isDebugEnabled()) {
				log.debug("Could not instantiate " + lookAndFeelName);
			}
			return false;
		} catch (IllegalAccessException e) {
			if (log.isDebugEnabled()) {
				log.debug("Illegal attempt to access " + lookAndFeelName);
			}
			return false;
		} catch (ClassNotFoundException e) {
			if (log.isDebugEnabled()) {
				log.debug("Could not find " + lookAndFeelName);
			}
			return false;
		}
	}

	/**
	 * Sets the look and feel to use on Linux
	 *
	 * @param lnf
	 *            The look and feel as a class constant from com.jidesoft.plaf.LookAndFeelFactory
	 */
	public void setLinuxLNF(String lnf) {
		this.linuxLNF = lnf;
	}

	public void setLinuxStyle(int style) {
		this.linuxStyle = style;
	}

	/**
	 * Sets the look and feel to use on MacOS X
	 *
	 * @param lnf
	 *            The look and feel as a class constant from com.jidesoft.plaf.LookAndFeelFactory
	 */
	public void setMacOSXLNF(String lnf) {
		this.macosxLNF = lnf;
	}

	/**
	 * @param settingsManager
	 *            The settingsManager to set.
	 */
	public void setSettingsManager(SettingsManager settingsManager) {
		this.settingsManager = settingsManager;
	}

	/**
	 * Sets the look and feel to use on Windows
	 *
	 * @param lnf
	 *            The look and feel as a class constant from com.jidesoft.plaf.LookAndFeelFactory
	 */
	public void setWindowsLNF(String lnf) {
		this.windowsLNF = lnf;
	}

	/**
	 * Sets the style to be used with windows (not XP).
	 *
	 * @param style
	 *            The style as a class constant from com.jidesoft.plaf.LookAndFeelFactory
	 */
	public void setWindowsStyle(int style) {
		this.windowsStyle = style;
	}

	/**
	 * Sets the style to be used on windows XP
	 *
	 * @param style
	 *            The style as a class constant from com.jidesoft.plaf.LookAndFeelFactory
	 */
	public void setWindowsXPStyle(int style) {
		this.windowsXPStyle = style;
	}
}
