package util;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.Component;

public class InputOutput {
	
	/**
	 * Exibe uma caixa de diálogo com uma mensagem de erro.
	 * 
	 * @param posicao - relativo ao <i>Component</i> onde vai ser exibida a caixa de dialogo 
	 * @param mensagem - mensagem de erro a ser exibida na caixa de diálogo;
	 * @param titulo - texto da barra de título da caixa de diálogo.
	 */
	public static void msgError(Component posicao, String mensagem, String titulo) {
		showMessageDialog(posicao, mensagem, titulo, ERROR_MESSAGE);
	}
	
	/**
	 * Exibe uma mensagem informativa em uma caixa de diálogo.
	 * 
	 * @param posicao - relativo ao <i>Component</i> onde vai ser exibida a caixa de dialogo 
	 * @param mensagem - mensagem informativa a ser exibida na caixa de diálogo;
	 * @param titulo - texto da barra de título da caixa de diálogo.
	 * 
	 */
	public static void msgInfo(Component posicao, String mensagem, String titulo) {
		showMessageDialog(posicao, mensagem, titulo, INFORMATION_MESSAGE);
	}
	
	/**
	 * Exibe uma pergunta em uma caixa de diálogo para o usuário responder Sim ou Não.
	 * 
	 * @param pergunta pergunta a ser exibido na caixa de diálogo;
	 * @param titulo texto a ser exibido na barra de título da caixa de diálogo.
	 * 
	 * @return o valor <code>YES_OPTION</code> se a resposta for sim, <code>NO_OPTION</code> se for não ou <code>CLOSED_OPTION</code> se a caixa de diálogo 
	 * for fechada sem que o usuário responda a pergunta.
	 * 
	 * @see javax.swing.JOptionPane#YES_OPTION
	 * @see javax.swing.JOptionPane#NO_OPTION
	 * @see javax.swing.JOptionPane#CLOSED_OPTION
	 */
	public static int msgConfirm(String pergunta, String titulo) {
		return showConfirmDialog(null, pergunta, titulo, YES_NO_OPTION, QUESTION_MESSAGE);
	}
	
	/**
	 * Converte uma <i>String</i> para um valor <i>Integer</i> caso seja possivel
	 * @param valor
	 * @return Integer se for possivel retorna o valor convertido, 
	 * se não retorna <code>null</code>
	 */
	public static Integer convertIntegerValue(String valor) {
		try {
			return Integer.parseInt(valor);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Converte uma <i>String</i> para um valor <i>Double</i> caso seja possivel
	 * @param valor
	 * @return Double se for possivel retorna o valor convertido, 
	 * se não retorna <code>null</code>
	 */
	public static Double convertDoubleValue(String valor) {
		try {
			return Double.parseDouble(valor);
		} catch (Exception e) {
			return null;
		}
	}
}//class EntradaESaida