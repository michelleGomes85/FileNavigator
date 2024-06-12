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

	public UI() {

		initComponent();
		configureEventListeners();

		draw();
		events();

		setVisible(true);
	}

	private void events() {
		
		// Evento quando o usuário aperta Enter para pesquisar o diretório
		searchDirectoryTextField.addActionListener((e) -> searchDirectory());

		// Evento caso o usuário desmarque ou marque a opção de exibir extensão
		displayExtensionCheckBox.addItemListener(e -> updateData(current));
		
		// Evento caso o usuário escolha uma extensão do JComboBox
		extensionComboBox.addItemListener(e -> updateData(current));
		
		// Evento se o usuário clicar em algum item do JList
		filesSubdirectoriesList.addListSelectionListener(e -> getProperties(filesSubdirectoriesList.getSelectedIndex()));
		
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

	private void updateData(OptionRadioButton option) {
		
		resetFields();
		
		if (fileChecker == null)
			return;
		
		switch (option) {
		
		case FILES:
			filesSubdirectoriesList.setListData(fileChecker.getFileNames(displayExtensionCheckBox.isSelected(), Extension.valueOf(extensionComboBox.getSelectedItem().toString())));
			break;
			
		case SUBDIRECTORIES: 
			filesSubdirectoriesList.setListData(fileChecker.getSubdirectoryNames());
			break;
			
		case FILES_SUBDIRECTORIES: 
			filesSubdirectoriesList.setListData(fileChecker.getFileNamesSubdirectories(displayExtensionCheckBox.isSelected(), Extension.valueOf(extensionComboBox.getSelectedItem().toString())));
			break;
		}
		
		if (filesSubdirectoriesList.getModel().getSize() > 0)
			filesSubdirectoriesList.setSelectedIndex(0);
	}
	
	private void getProperties(int selectedIndex) {
		
		if (selectedIndex >= 0)
			propertiesTextArea.setText(fileChecker.getSubdirectoryProperties(selectedIndex));
	}

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

	private void initComponent() {

		setTitle(TITLE_PROGRAM);
		setSize(SIZE_UI[0], SIZE_UI[1]);

		getContentPane().setLayout(null);
		this.setLocationRelativeTo(null);
		setResizable(false);
	}

	private void draw() {

		drawSourceDirectory();

		drawFilesAndSubdirectoriesPanel();

		drawPropertiesPanel();
	}

	private void drawSourceDirectory() {

		JLabel diretorioLabel = createLabel(LABEL_DIRECTORY, KeyEvent.VK_D, SwingConstants.CENTER,
				SwingConstants.CENTER, 12, 15, 67, 16);
		getContentPane().add(diretorioLabel);

		searchDirectoryTextField = createTextField(83, 12, 750, 22, LABEL_DIRECTORY_TIP,
				new Font("Arial", Font.PLAIN, 15), 38);
		
		getContentPane().add(searchDirectoryTextField);
	}

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

		JLabel typeLabel = createLabel(LABEL_TYPE, KeyEvent.VK_T, SwingConstants.LEFT, SwingConstants.CENTER, 15, 224, 50, 14);
		fileSubdirectoriesPanel.add(typeLabel);

		extensionComboBox = createComboBox(50, 220, 78, 22, LABEL_TYPE_TIP, 4, Extension.names(), 0);
		fileSubdirectoriesPanel.add(extensionComboBox);

		displayExtensionCheckBox = createCheckBox(LABEL_EXTENTION, KeyEvent.VK_E, LABEL_EXTENTION_TIP, true, 10, 268, 291, 23);
		fileSubdirectoriesPanel.add(displayExtensionCheckBox);
	}

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

	private void addButtonDisplay(JPanel panelConteudoDiretorio) {
		
		ButtonGroup buttonGroup = new ButtonGroup();

		fileButton = createRadioButton(BUTTON_FILE, KeyEvent.VK_A, BUTTON_FILE_TIP, 6, 22, 75, 23);
		buttonGroup.add(fileButton);
		panelConteudoDiretorio.add(fileButton);

		subdirectoriesButton = createRadioButton(BUTTON_SUBDIRECTORY, KeyEvent.VK_S, BUTTON_SUBDIRECTORY_TIP, 6, 49, 103, 23);
		buttonGroup.add(subdirectoriesButton);
		panelConteudoDiretorio.add(subdirectoriesButton);

		fileSubdirectoriesButton = createRadioButton(BUTTON_FILE_SUBDIRECTORY, KeyEvent.VK_U, BUTTON_FILE_SUBDIRECTORY_TIP, 6, 75, 165, 23);
		buttonGroup.add(fileSubdirectoriesButton);
		panelConteudoDiretorio.add(fileSubdirectoriesButton);
		
		fileSubdirectoriesButton.setSelected(true);
	}

	private void configureEventListeners() {

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

	}

	private JLabel createLabel(String text, int mnemonic, int horizontalAlignment, int verticalAlignment, int x, int y,
			int width, int height) {

		JLabel label = new JLabel(text);

		label.setDisplayedMnemonic(mnemonic);
		label.setHorizontalAlignment(horizontalAlignment);
		label.setVerticalAlignment(verticalAlignment);
		label.setBounds(x, y, width, height);

		return label;
	}

	private JTextField createTextField(int x, int y, int width, int height, String tooltip, Font font, int columns) {

		JTextField textField = new JTextField();

		textField.setToolTipText(tooltip);
		textField.setFont(font);
		textField.setColumns(columns);
		textField.setBounds(x, y, width, height);
		return textField;
	}

	private JPanel createPanel(int x, int y, int width, int height) {

		JPanel panel = new JPanel();

		panel.setLayout(null);
		panel.setBounds(x, y, width, height);
		return panel;
	}

	private JScrollPane createScrollPane(int x, int y, int width, int height) {

		JScrollPane scrollPane = new JScrollPane();

		scrollPane.setBounds(x, y, width, height);
		return scrollPane;
	}

	private JTextArea createTextArea() {

		JTextArea textArea = new JTextArea();

		textArea.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		textArea.setBorder(new LineBorder(new Color(0, 0, 0)));
		textArea.setEditable(false);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);

		return textArea;
	}

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

	private JList<String> createJList() {
		return new JList<>();
	}
	
	private void resetFields() {
		
		searchDirectoryTextField.setText(EMPTY);
		
		filesSubdirectoriesList.setListData(new String[0]);
		
		propertiesTextArea.setText(EMPTY);
		
		searchDirectoryTextField.requestFocus();
	}

}// class UI