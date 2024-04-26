package kaba4cow.taskman.utils;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import kaba4cow.taskman.Application;
import kaba4cow.taskman.utils.io.DataReader;
import kaba4cow.taskman.utils.io.DataWriter;

public final class ApplicationUtils {

	private static final String RESOURCES_LOCATION;

	private static final String DIRECTORY;

	private ApplicationUtils() {
	}

	static {
		RESOURCES_LOCATION = "/kaba4cow/taskman/resources/";
		DIRECTORY = System.getProperty("user.home") + File.separator + ".taskman" + File.separator;
		File directoryFile = new File(DIRECTORY);
		if (!directoryFile.exists())
			directoryFile.mkdir();
	}

	public static RandomAccessFile getLockFile() throws IOException {
		return new RandomAccessFile(new File(DIRECTORY + "lock"), "rw");
	}

	public static DataReader getDataReader(String filename) throws IOException {
		return new DataReader(DIRECTORY + filename);
	}

	public static DataWriter getDataWriter(String filename) throws IOException {
		return new DataWriter(DIRECTORY + filename);
	}

	public static Image getApplicationIconImage() {
		URL iconResource = Application.class.getResource(RESOURCES_LOCATION + "icon.png");
		return Toolkit.getDefaultToolkit().createImage(iconResource);
	}

	public static String[] readLines(String filename) {
		try {
			URL url = Application.class.getResource(RESOURCES_LOCATION + filename);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
			String line;
			List<String> lines = new ArrayList<>();
			while ((line = reader.readLine()) != null)
				lines.add(line);
			reader.close();
			return lines.toArray(new String[lines.size()]);
		} catch (IOException e) {
			return new String[0];
		}
	}

}
