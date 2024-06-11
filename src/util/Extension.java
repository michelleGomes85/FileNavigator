package util;

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
	
	public static String[] names() {
		
		String[] names = new String[Extension.values().length];
		
		int index = 0;
		for (Extension extension : Extension.values())
			names[index++] = extension.getName();
		
		return names;
	}
}//enum Extension
