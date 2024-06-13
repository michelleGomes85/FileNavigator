package util;

/**
 * Interface que contém constantes utilizadas no programa Verificador de
 * Arquivos.
 */
public interface Constants {

	String TITLE_PROGRAM = "Verificador de arquivos";

	String MSG_INVALID_PATH = "O caminho inserido não existe";
	String MSG_NOT_DIRECTORY = "O caminho inserido não pertence a um diretório";

	String DATE_FORMAT = "dd/MM/yyyy - HH:mm:ss";
	
	String EMPTY = "";

	int[] SIZE_UI = { 860, 423 };
	
	String LABEL_DIRECTORY = "Diretório: ";
	String LABEL_DIRECTORY_TIP = "Digite o caminho do diretório";
	String LABEL_FILE_SUBDIRECTORY = "Arquivos e Subdiretórios";
	String LABEL_FILE_SUBDIRECTORY_TIP = "Clique em um dos arquivos ou subdiretórios para obter suas propriedades";
	String LABEL_TYPE = "Tipo: ";
	String LABEL_TYPE_TIP = "Selecione a extensão dos arquivos a serem exibidos";
	String LABEL_EXTENTION = "Exibir extensão ";
	String LABEL_EXTENTION_TIP = "Marque ou desmarque para exibir a extensão dos arquivos";
	String LABEL_PROPERTIES = "Propriedades";
	
	String BUTTON_FILE = "Arquivos";
	String BUTTON_FILE_TIP = "Exibir somente Arquivos";
	
	String BUTTON_SUBDIRECTORY = "Subdiretórios";
	String BUTTON_SUBDIRECTORY_TIP = "Subdiretórios";
	
	String BUTTON_FILE_SUBDIRECTORY = "Arquivos e Subdiretórios";
	String BUTTON_FILE_SUBDIRECTORY_TIP = "Exibir Arquivos e Subdiretórios";
	
	String TITLE_BORDER = "Conteúdo do diretório";
	
	String FORMAT_PROPERTIES = "\n - Caminho Absoluto: %s\n - Nome: %s\n - Última Modificação: %s\n - Tamanho em Bytes: %s Bytes";
	
	
	
}// interface Constants
