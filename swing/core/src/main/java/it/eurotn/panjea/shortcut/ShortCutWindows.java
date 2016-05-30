package it.eurotn.panjea.shortcut;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.commons.io.IOUtils;

public class ShortCutWindows implements ShortCutCreator {

	@Override
	public void create(Path path) {
		if (path == null) {
			return;
		}
		StringBuilder sb = new StringBuilder(1000);
		sb.append("Set wsc = WScript.CreateObject(\"WScript.Shell\")");
		sb.append("\nSet lnk = wsc.CreateShortcut(wsc.SpecialFolders(\"desktop\") & \"\\")
		.append(path.getFileName().toString()).append(".lnk\")");
		sb.append("\nlnk.targetpath = \"");
		sb.append(path.toString());
		sb.append("\\").append(getExeName()).append("\"");
		sb.append("\nlnk.description = \"Avvia panjea\"");
		sb.append("\nlnk.IconLocation = \"").append(path.resolve(("panjea.ico"))).append("\"");
		sb.append("\nlnk.save");
		File scriptFile = path.resolve("panjeavb.vbs").toFile();
		try (FileOutputStream fos = new FileOutputStream(scriptFile)) {
			IOUtils.write(sb.toString(), fos);
			ProcessBuilder pb = new ProcessBuilder("cscript", scriptFile.getAbsolutePath());
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getExeName() {
		if (System.getProperty("sun.arch.data.model").equals("64")) {
			return "panjea.exe";
		} else {
			return "panjea32.exe";
		}
	}
}
