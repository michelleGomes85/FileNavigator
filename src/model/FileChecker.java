package model;

import static util.Constants.FORMAT_PROPERTIES;
import static util.Constants.MSG_INVALID_PATH;
import static util.Constants.MSG_NOT_DIRECTORY;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import util.Extension;

public class FileChecker {

	private File fileDirectory;
	private File[] filesSubdirectories;

	public FileChecker(String path) {

		fileDirectory = new File(path);
		
		if (!fileDirectory.exists())
			throw new FileCheckerException(MSG_INVALID_PATH);
		if (!fileDirectory.isDirectory())
			throw new FileCheckerException(MSG_NOT_DIRECTORY);
		
		filesSubdirectories = getFilesSubdirectories();
	}

	public String[] getFileNamesSubdirectories(boolean withExtension, Extension extension) {

		String[] files = getFileNames(withExtension, extension);

		String[] subdirectories = getSubdirectoryNames();

		String[] combined = new String[files.length + subdirectories.length];

		System.arraycopy(files, 0, combined, 0, files.length);
		System.arraycopy(subdirectories, 0, combined, files.length, subdirectories.length);

		Arrays.sort(combined);
		
		filesSubdirectories = getFilesSubdirectories();

		return combined;
	}

	public String[] getFileNames(boolean withExtension, Extension extension) {

		String[] files = Arrays.stream(Objects.requireNonNull(getFiles())).filter(file -> {
			return extension == Extension.ALL
					|| file.getName().toLowerCase().endsWith(extension.getName().toLowerCase());
		})

				.map(file -> withExtension ? file.getName() : removeExtension(file.getName())).toArray(String[]::new);
		
		filesSubdirectories = getFiles();
		
		return files;
	}

	public String[] getSubdirectoryNames() {

		String[] subdirectory = Arrays.stream(Objects.requireNonNull(getSubdirectories())).map(File::getName).toArray(String[]::new);
		
		filesSubdirectories = getSubdirectories();
		
		return subdirectory;
	}

	private File[] getFilesSubdirectories() {
		return fileDirectory.listFiles();
	}

	private File[] getFiles() {

		File[] files = fileDirectory.listFiles(File::isFile);

		if (files != null)
			Arrays.sort(files);

		return files;
	}

	private File[] getSubdirectories() {

		File[] subdirectories = fileDirectory.listFiles(File::isDirectory);

		if (subdirectories != null)
			Arrays.sort(subdirectories);

		return subdirectories;
	}

	private String removeExtension(String fileName) {
		int lastDotIndex = fileName.lastIndexOf('.');
		if (lastDotIndex == -1 || lastDotIndex == 0)
			return fileName;

		return fileName.substring(0, lastDotIndex);
	}

	public String getSubdirectoryProperties(int index) {

		String absolutePath = getAbsolutePath(filesSubdirectories[index]);
		String name = getNameDirectory(filesSubdirectories[index]);
		String lastModification = getLastModification(filesSubdirectories[index]);
		String size = getSizeFile(filesSubdirectories[index]);

		return String.format(FORMAT_PROPERTIES, absolutePath, name, lastModification, size);
	}

	private String getSizeFile(File file) {
		return Long.toString((file.isFile()) ? file.length() : getDirectorySize(file));
	}

	private String getAbsolutePath(File file) {
		return file.getAbsolutePath();
	}

	private String getNameDirectory(File file) {
		return file.getName();
	}

	private String getLastModification(File file) {
		return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(file.lastModified()));
	}

	private static long getDirectorySize(File directory) {

		long size = 0;

		if (directory.isDirectory()) {

			File[] files = directory.listFiles();

			if (files != null) {
				for (File file : files)
					size += (file.isFile()) ? file.length() : getDirectorySize(file);
			}
		}

		return size;
	}

}// class FileChecker