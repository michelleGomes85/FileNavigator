package app.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import model.FileChecker;
import model.FileCheckerException;
import util.Constants;
import util.Extension;
import util.InputOutput;
import util.OptionRadioButton;

/**
 * A classe UI é responsável pela interface gráfica da aplicação. Ela estende
 * JFrame e implementa a interface Constants. Nesta classe, os componentes da
 * interface do usuário são inicializados e configurados.
 */
public class UI extends JFrame implements Constants {

	private static final long serialVersionUID = 1L;

	private JTextField searchDirectoryTextField;

	private JList<String> filesSubdirectoriesList;

	private JComboBox<String> extensionComboBox;
	private JCheckBox displayExtensionCheckBox;

	private JRadioButton fileButton;
	private JRadioButton subdirectoriesButton;
	private JRadioButton fileSubdirectoriesButton;

	private JTextArea propertiesTextArea;

	private FileChecker fileChecker;

	private OptionRadioButton current = OptionRadioButton.FILES_SUBDIRECTORIES;

	/**
	 * Construtor da classe UI. Inicializa os componentes, configura os ouvintes de
	 * eventos, desenha a interface e torna a janela visível.
	 */
	public UI() {

		initComponent();
		configureEventListeners();

		draw();
		events();

		setVisible(true);
	}

	/**
	 * Método responsável por configurar os eventos dos componentes da interface.
	 */
	private void events() {

		// Evento quando o usuário aperta Enter para pesquisar o diretório
		searchDirectoryTextField.addActionListener((e) -> searchDirectory());

		// Evento caso o usuário desmarque ou marque a opção de exibir extensão
		displayExtensionCheckBox.addItemListener(e -> updateData(current));

		// Evento caso o usuário escolha uma extensão do JComboBox
		extensionComboBox.addItemListener(e -> updateData(current));

		// Evento se o usuário clicar em algum item do JList
		filesSubdirectoriesList
				.addListSelectionListener(e -> getProperties(filesSubdirectoriesList.getSelectedIndex()));

		// Eventos JRadioButton
		fileButton.addItemListener(e -> {

			if (e.getStateChange() == ItemEvent.SELECTED) {
				current = OptionRadioButton.FILES;
				updateData(current);
			}
		});

		subdirectoriesButton.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				current = OptionRadioButton.SUBDIRECTORIES;
				updateData(current);
			}
		});

		fileSubdirectoriesButton.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				current = OptionRadioButton.FILES_SUBDIRECTORIES;
				updateData(current);
			}
		});
	}

	/**
	 * Atualiza os dados da lista com base na opção selecionada (arquivos,
	 * subdiretórios ou ambos).
	 *
	 * @param option a opção selecionada para exibir na lista.
	 */
	private void updateData(OptionRadioButton option) {

		resetFields();

		if (fileChecker == null)
			return;

		switch (option) {

		case FILES:
			filesSubdirectoriesList.setListData(fileChecker.getFileNames(displayExtensionCheckBox.isSelected(),
					Extension.valueOf(extensionComboBox.getSelectedItem().toString())));
			break;

		case SUBDIRECTORIES:
			filesSubdirectoriesList.setListData(fileChecker.getSubdirectoryNames());
			break;

		case FILES_SUBDIRECTORIES:
			filesSubdirectoriesList
					.setListData(fileChecker.getFileNamesSubdirectories(displayExtensionCheckBox.isSelected(),
							Extension.valueOf(extensionComboBox.getSelectedItem().toString())));
			break;
		}

		if (filesSubdirectoriesList.getModel().getSize() > 0)
			filesSubdirectoriesList.setSelectedIndex(0);
	}

	/**
	 * Obtém e exibe as propriedades do subdiretório selecionado na área de texto de
	 * propriedades.
	 *
	 * @param selectedIndex o índice do subdiretório selecionado na lista.
	 */
	private void getProperties(int selectedIndex) {

		if (selectedIndex >= 0)
			propertiesTextArea.setText(fileChecker.getSubdirectoryProperties(selectedIndex));
	}

	/**
	 * Pesquisa o diretório especificado no campo de texto e atualiza os dados da
	 * interface.
	 */
	private void searchDirectory() {

		String textSearch = searchDirectoryTextField.getText();

		try {
			fileChecker = new FileChecker(textSearch);
			updateData(OptionRadioButton.FILES_SUBDIRECTORIES);
		} catch (FileCheckerException e) {
			InputOutput.msgError(this, e.getMessage(), TITLE_PROGRAM);
			resetFields();
		}
	}

	/**
	 * Inicializa os componentes da interface.
	 */
	private void initComponent() {

		setTitle(TITLE_PROGRAM);
		setSize(SIZE_UI[0], SIZE_UI[1]);

		getContentPane().setLayout(null);
		this.setLocationRelativeTo(null);
		setResizable(false);
	}

	/**
	 * Desenha os componentes da interface.
	 */
	private void draw() {

		drawSourceDirectory();

		drawFilesAndSubdirectoriesPanel();

		drawPropertiesPanel();
	}

	/**
	 * Desenha o campo de entrada para o diretório de origem.
	 */
	private void drawSourceDirectory() {

		JLabel diretorioLabel = createLabel(LABEL_DIRECTORY, KeyEvent.VK_D, SwingConstants.CENTER,
				SwingConstants.CENTER, 12, 15, 67, 16);
		getContentPane().add(diretorioLabel);

		searchDirectoryTextField = createTextField(83, 12, 750, 22, LABEL_DIRECTORY_TIP,
				new Font("Arial", Font.PLAIN, 15), 38);

		getContentPane().add(searchDirectoryTextField);
	}

	/**
	 * Desenha o painel de arquivos e subdiretórios.
	 */
	private void drawFilesAndSubdirectoriesPanel() {

		JPanel fileSubdirectoriesPanel = createPanel(0, 39, 420, 345);
		getContentPane().add(fileSubdirectoriesPanel);

		JLabel fileSubdirectoriesLabel = createLabel(LABEL_FILE_SUBDIRECTORY, KeyEvent.VK_R, SwingConstants.LEFT,
				SwingConstants.CENTER, 10, 10, 291, 14);

		fileSubdirectoriesLabel.setToolTipText(LABEL_DIRECTORY_TIP);

		fileSubdirectoriesLabel.setForeground(Color.BLACK);
		fileSubdirectoriesPanel.add(fileSubdirectoriesLabel);

		JScrollPane fileSubdirectoriesListScrollPane = createScrollPane(10, 28, 400, 182);
		fileSubdirectoriesPanel.add(fileSubdirectoriesListScrollPane);

		filesSubdirectoriesList = createJList();
		filesSubdirectoriesList.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		fileSubdirectoriesLabel.setLabelFor(filesSubdirectoriesList);
		filesSubdirectoriesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		filesSubdirectoriesList.setBorder(new LineBorder(new Color(0, 0, 0)));
		fileSubdirectoriesListScrollPane.setViewportView(filesSubdirectoriesList);

		JLabel typeLabel = createLabel(LABEL_TYPE, KeyEvent.VK_T, SwingConstants.LEFT, SwingConstants.CENTER, 15, 224,
				50, 14);
		fileSubdirectoriesPanel.add(typeLabel);

		extensionComboBox = createComboBox(50, 220, 78, 22, LABEL_TYPE_TIP, 4, Extension.names(), 0);
		fileSubdirectoriesPanel.add(extensionComboBox);

		displayExtensionCheckBox = createCheckBox(LABEL_EXTENTION, KeyEvent.VK_E, LABEL_EXTENTION_TIP, true, 10, 268,
				291, 23);
		fileSubdirectoriesPanel.add(displayExtensionCheckBox);
	}

	/**
	 * Desenha o painel de propriedades.
	 */
	private void drawPropertiesPanel() {

		JPanel panelPropriedades = createPanel(424, 39, 420, 345);
		getContentPane().add(panelPropriedades);

		JLabel propriedadesLabel = createLabel(LABEL_PROPERTIES, KeyEvent.VK_P, SwingConstants.LEFT,
				SwingConstants.CENTER, 0, 11, 277, 14);
		panelPropriedades.add(propriedadesLabel);

		JScrollPane propriedadesTextAreaScrollPane = createScrollPane(0, 28, 410, 182);
		panelPropriedades.add(propriedadesTextAreaScrollPane);

		propertiesTextArea = createTextArea();
		propriedadesTextAreaScrollPane.setViewportView(propertiesTextArea);

		JPanel panelConteudoDiretorio = createPanel(8, 222, 373, 115);
		panelConteudoDiretorio
				.setBorder(new TitledBorder(null, TITLE_BORDER, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelPropriedades.add(panelConteudoDiretorio);

		addButtonDisplay(panelConteudoDiretorio);
	}

	/**
	 * Adiciona os botões de rádio ao painel de conteúdo do diretório.
	 *
	 * @param panelConteudoDiretorio o painel onde os botões de rádio serão adicionados.
	 */
	private void addButtonDisplay(JPanel panelConteudoDiretorio) {

		ButtonGroup buttonGroup = new ButtonGroup();

		fileButton = createRadioButton(BUTTON_FILE, KeyEvent.VK_A, BUTTON_FILE_TIP, 6, 22, 75, 23);
		buttonGroup.add(fileButton);
		panelConteudoDiretorio.add(fileButton);

		subdirectoriesButton = createRadioButton(BUTTON_SUBDIRECTORY, KeyEvent.VK_S, BUTTON_SUBDIRECTORY_TIP, 6, 49,
				103, 23);
		buttonGroup.add(subdirectoriesButton);
		panelConteudoDiretorio.add(subdirectoriesButton);

		fileSubdirectoriesButton = createRadioButton(BUTTON_FILE_SUBDIRECTORY, KeyEvent.VK_U,
				BUTTON_FILE_SUBDIRECTORY_TIP, 6, 75, 165, 23);
		buttonGroup.add(fileSubdirectoriesButton);
		panelConteudoDiretorio.add(fileSubdirectoriesButton);

		fileSubdirectoriesButton.setSelected(true);
	}

	/**
	 * Configura os ouvintes de eventos da janela.
	 */
	private void configureEventListeners() {

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

	}

	/**
	 * Cria um JLabel com as propriedades especificadas.
	 *
	 * @param text o texto do rótulo.
	 * @param mnemonic o atalho do rótulo.
	 * @param horizontalAlignment o alinhamento horizontal do rótulo.
	 * @param verticalAlignment o alinhamento vertical do rótulo.
	 * @param x a coordenada x do rótulo.
	 * @param y a coordenada y do rótulo.
	 * @param width a largura do rótulo.
	 * @param height a altura do rótulo.
	 * @return um JLabel configurado.
	 */
	private JLabel createLabel(String text, int mnemonic, int horizontalAlignment, int verticalAlignment, int x, int y,
			int width, int height) {

		JLabel label = new JLabel(text);

		label.setDisplayedMnemonic(mnemonic);
		label.setHorizontalAlignment(horizontalAlignment);
		label.setVerticalAlignment(verticalAlignment);
		label.setBounds(x, y, width, height);

		return label;
	}

	/**
	 * Cria um JTextField com as propriedades especificadas.
	 *
	 * @param x a coordenada x do campo de texto.
	 * @param y a coordenada y do campo de texto.
	 * @param width a largura do campo de texto.
	 * @param height a altura do campo de texto.
	 * @param tooltip a dica de ferramenta do campo de texto.
	 * @param font a fonte do campo de texto.
	 * @param columns o número de colunas do campo de texto.
	 * @return um JTextField configurado.
	 */
	private JTextField createTextField(int x, int y, int width, int height, String tooltip, Font font, int columns) {

		JTextField textField = new JTextField();

		textField.setToolTipText(tooltip);
		textField.setFont(font);
		textField.setColumns(columns);
		textField.setBounds(x, y, width, height);
		return textField;
	}

	/**
	 * Cria um JPanel com as propriedades especificadas.
	 *
	 * @param x a coordenada x do painel.
	 * @param y a coordenada y do painel.
	 * @param width a largura do painel.
	 * @param height a altura do painel.
	 * @return um JPanel configurado.
	 */
	private JPanel createPanel(int x, int y, int width, int height) {

		JPanel panel = new JPanel();

		panel.setLayout(null);
		panel.setBounds(x, y, width, height);
		return panel;
	}

	/**
	 * Cria um JScrollPane com as propriedades especificadas.
	 *
	 * @param x a coordenada x do painel de rolagem.
	 * @param y a coordenada y do painel de rolagem.
	 * @param width a largura do painel de rolagem.
	 * @param height a altura do painel de rolagem.
	 * @return um JScrollPane configurado.
	 */
	private JScrollPane createScrollPane(int x, int y, int width, int height) {

		JScrollPane scrollPane = new JScrollPane();

		scrollPane.setBounds(x, y, width, height);
		return scrollPane;
	}

	/**
	 * Cria um JTextArea com as propriedades especificadas.
	 *
	 * @return um JTextArea configurado.
	 */
	private JTextArea createTextArea() {

		JTextArea textArea = new JTextArea();

		textArea.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		textArea.setBorder(new LineBorder(new Color(0, 0, 0)));
		textArea.setEditable(false);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);

		return textArea;
	}

	/**
	 * Cria um JRadioButton com as propriedades especificadas.
	 *
	 * @param text o texto do botão de rádio.
	 * @param mnemonic o atalho do botão de rádio.
	 * @param tooltip a dica de ferramenta do botão de rádio.
	 * @param x a coordenada x do botão de rádio.
	 * @param y a coordenada y do botão de rádio.
	 * @param width a largura do botão de rádio.
	 * @param height a altura do botão de rádio.
	 * @return um JRadioButton configurado.
	 */
	private JRadioButton createRadioButton(String text, int mnemonic, String tooltip, int x, int y, int width,
			int height) {

		JRadioButton radioButton = new JRadioButton(text);

		radioButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		radioButton.setMnemonic(mnemonic);
		radioButton.setToolTipText(tooltip);
		radioButton.setVerticalAlignment(SwingConstants.TOP);
		radioButton.setHorizontalAlignment(SwingConstants.CENTER);
		radioButton.setBounds(x, y, width, height);

		return radioButton;
	}

	/**
	 * Cria um JComboBox com as propriedades especificadas.
	 *
	 * @param x a coordenada x do combo box.
	 * @param y a coordenada y do combo box.
	 * @param width a largura do combo box.
	 * @param height a altura do combo box.
	 * @param tooltip a dica de ferramenta do combo box.
	 * @param maxRowCount o número máximo de itens visíveis no combo box.
	 * @param items os itens do combo box.
	 * @param selectedIndex o índice do item selecionado por padrão.
	 * @return um JComboBox configurado.
	 */
	private JComboBox<String> createComboBox(int x, int y, int width, int height, String tooltip, int maxRowCount,
			String[] items, int selectedIndex) {

		JComboBox<String> comboBox = new JComboBox<>(items);

		comboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		comboBox.setToolTipText(tooltip);
		comboBox.setMaximumRowCount(maxRowCount);
		comboBox.setBounds(x, y, width, height);
		comboBox.setSelectedIndex(selectedIndex);

		return comboBox;
	}

	/**
	 * Cria um JCheckBox com as propriedades especificadas.
	 *
	 * @param text o texto do checkbox.
	 * @param mnemonic o atalho do checkbox.
	 * @param tooltip a dica de ferramenta do checkbox.
	 * @param selected o estado inicial do checkbox (selecionado ou não).
	 * @param x a coordenada x do checkbox.
	 * @param y a coordenada y do checkbox.
	 * @param width a largura do checkbox.
	 * @param height a altura do checkbox.
	 * @return um JCheckBox configurado.
	 */
	private JCheckBox createCheckBox(String text, int mnemonic, String tooltip, boolean selected, int x, int y,
			int width, int height) {

		JCheckBox checkBox = new JCheckBox(text);

		checkBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		checkBox.setToolTipText(tooltip);
		checkBox.setMnemonic(mnemonic);
		checkBox.setSelected(selected);
		checkBox.setBounds(x, y, width, height);

		return checkBox;
	}

	/**
	 * Cria um JList configurado.
	 *
	 * @return um JList configurado.
	 */
	private JList<String> createJList() {
		return new JList<>();
	}

	/**
	 * Reseta os campos da interface do usuário.
	 */
	private void resetFields() {

		searchDirectoryTextField.setText(EMPTY);

		filesSubdirectoriesList.setListData(new String[0]);

		propertiesTextArea.setText(EMPTY);

		searchDirectoryTextField.requestFocus();
	}

}// class UI