package model;

/**
 * Exceção personalizada para a classe FileChecker. Lançada quando ocorrem erros
 * relacionados à verificação de arquivos e diretórios.
 */
public class FileCheckerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FileCheckerException(String message) {
		super(message);
	}

}// class FileException
