package app;

import app.gui.UI;

/**
 * A classe FileNavigator é o ponto de entrada principal para a aplicação. Ela
 * inicializa a interface do usuário quando a aplicação é iniciada.
 */
public class FileNavigator {

	/**
	 * O método main é o ponto de entrada da aplicação. Ele cria uma instância de
	 * FileNavigator, que por sua vez, inicializa a interface do usuário.
	 *
	 * @param args argumentos da linha de comando (não utilizados)
	 */
	public static void main(String[] args) {
		new FileNavigator();
	}

	/**
	 * O construtor da classe FileNavigator. Ele inicializa a interface do usuário
	 * criando uma instância da classe UI.
	 */
	public FileNavigator() {
		new UI();
	}

}// class FileNavigator
