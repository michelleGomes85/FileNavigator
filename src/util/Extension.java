package util;

/**
 * Enumeração que representa diferentes tipos de extensões de arquivo.
 */
public enum Extension {

	ALL("ALL"),
	DOCX("DOCX"),
	EXE("EXE"),
	JAVA("JAVA"),
	JPG("JPG"),
	PDF("PDF"),
	PNG("PNG"),
	PPTX("PPTX"),
	RTF("RTF"),
	TXT("TXT"),
	XLSX("XLSX");
	
	private String name;

	private Extension(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Obtém uma lista com os nomes de todas as extensões de arquivo.
	 *
	 * @return uma matriz de strings com os nomes de todas as extensões de arquivo
	 */
	public static String[] names() {
		
		String[] names = new String[Extension.values().length];
		
		int index = 0;
		for (Extension extension : Extension.values())
			names[index++] = extension.getName();
		
		return names;
	}
}//enum Extension
