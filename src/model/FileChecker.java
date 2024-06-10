package model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import util.Constants;

public class FileChecker implements Constants {

	private File fileDirectory;

	public FileChecker(String path) {

		fileDirectory = new File(path);

		if (!fileDirectory.exists())
			throw new FileCheckerException(MSG_INVALID_PATH);

		if (!fileDirectory.isDirectory())
			throw new FileCheckerException(MSG_NOT_DIRECTORY);
	}

	private File[] getFilesSubdirectories() {
		return fileDirectory.listFiles();
	}

	private File[] getFiles() {
		return fileDirectory.listFiles(File::isFile);
	}

	private File[] getSubdirectories() {
		return fileDirectory.listFiles(File::isDirectory);
	}

	public String[] getFileNamesSubdirectories() {
		return Arrays.stream(Objects.requireNonNull(getFilesSubdirectories())).map(File::getName)
				.toArray(String[]::new);
	}

	public String[] getSubdirectoryNames() {
		return Arrays.stream(Objects.requireNonNull(getSubdirectories())).map(File::getName).toArray(String[]::new);
	}

	public String[] getFileNames() {
		return Arrays.stream(Objects.requireNonNull(getFiles())).map(File::getName).toArray(String[]::new);
	}

	public String[] getFileNames(boolean withoutExtension) {
		
		String[] fileNames = getFileNames();

		if (!withoutExtension)
			return fileNames;

		return Arrays.stream(fileNames).map(name -> {
			int lastDotIndex = name.lastIndexOf('.');
			return (lastDotIndex == -1) ? name : name.substring(0, lastDotIndex);
		}).toArray(String[]::new);

	}

	public String[] getFileNames(String extension) {
		return Arrays.stream(Objects.requireNonNull(getFiles()))
				.filter(file -> file.getName().toLowerCase().endsWith(extension.toLowerCase())).map(File::getName)
				.toArray(String[]::new);
	}

	public String getAbsolutePath() {
		return fileDirectory.getAbsolutePath();
	}

	public String getNameDirectory() {
		return fileDirectory.getName();
	}

	public String getLastModification() {
		return new SimpleDateFormat(DATE_FORMAT).format(new Date(fileDirectory.lastModified()));
	}

}// class FileChecker
