package kaba4cow.taskman.utils.io;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class DataReader {

	private final DataInputStream input;

	public DataReader(String filename) throws IOException {
		File file = new File(filename);
		if (!file.exists())
			throw new FileNotFoundException();
		input = new DataInputStream(new FileInputStream(file));
	}

	public boolean readBoolean() throws IOException {
		return input.readBoolean();
	}

	public int readInt() throws IOException {
		return input.readInt();
	}

	public long readLong() throws IOException {
		return input.readLong();
	}

	public int readByte() throws IOException {
		return input.readUnsignedByte();
	}

	public String readString() throws IOException {
		int length = input.readInt();
		StringBuilder string = new StringBuilder(length);
		for (int i = 0; i < length; i++)
			string.append(input.readChar());
		return string.toString();
	}

	public LocalDate readDate() throws IOException {
		return LocalDate.ofEpochDay(readLong());
	}

	public LocalTime readTime() throws IOException {
		return LocalTime.of(readByte(), readByte());
	}

	public Color readColor() throws IOException {
		int red = readByte();
		int green = readByte();
		int blue = readByte();
		return new Color(red, green, blue);
	}

	public void close() throws IOException {
		input.close();
	}

}
