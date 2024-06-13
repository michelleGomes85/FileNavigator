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

/**
 * A classe FileChecker é responsável por verificar e obter informações sobre
 * arquivos e diretórios. Permite listar arquivos e subdiretórios, obter nomes
 * de arquivos com ou sem extensões, e propriedades de subdiretórios.
 */
public class FileChecker {

	private File fileDirectory;
	private File[] filesSubdirectories;

	/**
	 * Construtor da classe FileChecker. Inicializa a instância com o caminho
	 * fornecido e verifica se é um diretório válido.
	 *
	 * @param path o caminho do diretório a ser verificado
	 * @throws FileCheckerException se o caminho não existir ou não for um diretório
	 */
	public FileChecker(String path) {

		fileDirectory = new File(path);

		if (!fileDirectory.exists())
			throw new FileCheckerException(MSG_INVALID_PATH);
		if (!fileDirectory.isDirectory())
			throw new FileCheckerException(MSG_NOT_DIRECTORY);

		filesSubdirectories = getFilesSubdirectories();
	}

	/**
	 * Obtém uma lista de nomes de arquivos e subdiretórios no diretório, filtrados
	 * por extensão.
	 *
	 * @param withExtension se true, os nomes dos arquivos incluirão suas extensões
	 * @param extension     a extensão dos arquivos a serem listados
	 * @return uma lista de nomes de arquivos e subdiretórios
	 */
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

	/**
	 * Obtém uma lista de nomes de arquivos no diretório, filtrados por extensão.
	 *
	 * @param withExtension se true, os nomes dos arquivos incluirão suas extensões
	 * @param extension     a extensão dos arquivos a serem listados
	 * @return uma lista de nomes de arquivos
	 */
	public String[] getFileNames(boolean withExtension, Extension extension) {

		String[] files = Arrays.stream(Objects.requireNonNull(getFiles())).filter(file -> {
			return extension == Extension.ALL
					|| file.getName().toLowerCase().endsWith(extension.getName().toLowerCase());
		})

				.map(file -> withExtension ? file.getName() : removeExtension(file.getName())).toArray(String[]::new);

		filesSubdirectories = getFiles();

		return files;
	}

	/**
	 * Obtém uma lista de nomes de subdiretórios no diretório.
	 *
	 * @return uma lista de nomes de subdiretórios
	 */
	public String[] getSubdirectoryNames() {

		String[] subdirectory = Arrays.stream(Objects.requireNonNull(getSubdirectories())).map(File::getName)
				.toArray(String[]::new);

		filesSubdirectories = getSubdirectories();

		return subdirectory;
	}

	/**
	 * Obtém os arquivos e subdiretórios no diretório.
	 *
	 * @return uma matriz de arquivos e subdiretórios
	 */
	private File[] getFilesSubdirectories() {
		return fileDirectory.listFiles();
	}

	/**
	 * Obtém os arquivos no diretório.
	 *
	 * @return uma matriz de arquivos
	 */
	private File[] getFiles() {

		File[] files = fileDirectory.listFiles(File::isFile);

		if (files != null)
			Arrays.sort(files);

		return files;
	}

	/**
	 * Obtém os subdiretórios no diretório.
	 *
	 * @return uma matriz de subdiretórios
	 */
	private File[] getSubdirectories() {

		File[] subdirectories = fileDirectory.listFiles(File::isDirectory);

		if (subdirectories != null)
			Arrays.sort(subdirectories);

		return subdirectories;
	}

	/**
	 * Remove a extensão do nome do arquivo.
	 *
	 * @param fileName o nome do arquivo
	 * @return o nome do arquivo sem a extensão
	 */
	private String removeExtension(String fileName) {
		int lastDotIndex = fileName.lastIndexOf('.');
		if (lastDotIndex == -1 || lastDotIndex == 0)
			return fileName;

		return fileName.substring(0, lastDotIndex);
	}

	/**
	 * Obtém as propriedades do subdiretório pelo índice.
	 *
	 * @param index o índice do subdiretório na lista
	 * @return as propriedades do subdiretório em formato de string
	 */
	public String getSubdirectoryProperties(int index) {

		String absolutePath = getAbsolutePath(filesSubdirectories[index]);
		String name = getNameDirectory(filesSubdirectories[index]);
		String lastModification = getLastModification(filesSubdirectories[index]);
		String size = getSizeFile(filesSubdirectories[index]);

		return String.format(FORMAT_PROPERTIES, absolutePath, name, lastModification, size);
	}

	/**
	 * Obtém o tamanho do arquivo ou diretório.
	 *
	 * @param file o arquivo ou diretório
	 * @return o tamanho em bytes
	 */
	private String getSizeFile(File file) {
		return Long.toString((file.isFile()) ? file.length() : getDirectorySize(file));
	}

	/**
	 * Obtém o caminho absoluto do arquivo ou diretório.
	 *
	 * @param file o arquivo ou diretório
	 * @return o caminho absoluto
	 */
	private String getAbsolutePath(File file) {
		return file.getAbsolutePath();
	}

	/**
	 * Obtém o nome do arquivo ou diretório.
	 *
	 * @param file o arquivo ou diretório
	 * @return o nome
	 */
	private String getNameDirectory(File file) {
		return file.getName();
	}

	/**
	 * Obtém a data da última modificação do arquivo ou diretório.
	 *
	 * @param file o arquivo ou diretório
	 * @return a data da última modificação
	 */
	private String getLastModification(File file) {
		return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(file.lastModified()));
	}

	/**
	 * Obtém o tamanho do diretório, incluindo todos os seus arquivos e
	 * subdiretórios.
	 *
	 * @param directory o diretório
	 * @return o tamanho em bytes
	 */
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