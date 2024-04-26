package kaba4cow.taskman.utils.io;

import java.awt.Color;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class DataWriter {

	private final DataOutputStream output;

	public DataWriter(String filename) throws IOException {
		File file = new File(filename);
		if (!file.exists())
			file.createNewFile();
		output = new DataOutputStream(new FileOutputStream(file));
	}

	public DataWriter writeBoolean(boolean b) throws IOException {
		output.writeBoolean(b);
		return this;
	}

	public DataWriter writeInt(int i) throws IOException {
		output.writeInt(i);
		return this;
	}

	public DataWriter writeLong(long l) throws IOException {
		output.writeLong(l);
		return this;
	}

	public DataWriter writeByte(int b) throws IOException {
		output.writeByte(b);
		return this;
	}

	public DataWriter writeString(String string) throws IOException {
		output.writeInt(string.length());
		output.writeChars(string);
		return this;
	}

	public DataWriter writeDate(LocalDate date) throws IOException {
		return writeLong(date.toEpochDay());
	}

	public DataWriter writeTime(LocalTime time) throws IOException {
		return writeByte(time.getHour()).writeByte(time.getMinute());
	}

	public DataWriter writeColor(Color color) throws IOException {
		output.writeByte(color.getRed());
		output.writeByte(color.getGreen());
		output.writeByte(color.getBlue());
		return this;
	}

	public void close() throws IOException {
		output.close();
	}

}
