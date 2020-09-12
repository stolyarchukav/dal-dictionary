package org.forzaverita.daldic.util.initial;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class EditorFrame extends JFrame {

	private static final long serialVersionUID = -7690905826247434960L;
	
	private int index;
	private List<Integer> ids;
	private Word word;

	private JTextField wordField;
	private JTextArea descArea;
	private JTextField firstLetterField;
	private JTextField wordRefField;
	private JTextField filter;

	private JSpinner spinner;

	public EditorFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationByPlatform(true);
		setSize(800, 500);
		
		setLayout(new BorderLayout());
		
		JPanel topPanel = new JPanel();
		BoxLayout topLayout = new BoxLayout(topPanel, BoxLayout.X_AXIS);
		topPanel.setLayout(topLayout);
		add(topPanel, BorderLayout.NORTH);
		
		filter = new JTextField("см.");
		topPanel.add(filter);
		
		JButton filterApply = new JButton("apply filter");
		filterApply.addActionListener(e -> loadIdsByFilter());
		topPanel.add(filterApply);
		
		spinner = new JSpinner();
		spinner.setPreferredSize(new Dimension(100, topPanel.getHeight()));
		topPanel.add(spinner);
		
		JButton spinnerApply = new JButton("load word");
		spinnerApply.addActionListener(e -> loadWord((Integer) spinner.getValue()));
		topPanel.add(spinnerApply);
		
		JButton firstWord = new JButton("first word");
		firstWord.addActionListener(e -> {
			index = 0;
			updateSpinner();
			loadWord();
		});
		topPanel.add(firstWord);
		
		JButton lastWord = new JButton("last word");
		lastWord.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				index = ids.size() - 1;
				updateSpinner();
				loadWord();
			}
		});
		topPanel.add(lastWord);
		
		JPanel updatePanel = new JPanel();
		BoxLayout updateLayout = new BoxLayout(updatePanel, BoxLayout.X_AXIS);
		updatePanel.setLayout(updateLayout);
		add(updatePanel, BorderLayout.SOUTH);
		
		JButton delete = new JButton("delete");
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteWord();
			}
		});
		updatePanel.add(delete);
		
		JButton save = new JButton("save");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurrentWord();
			}
		});
		updatePanel.add(save);
		
		JButton createRef = new JButton("create ref");
		createRef.addActionListener(e -> createRef());
		updatePanel.add(createRef);
		
		JButton refToSelected = new JButton("ref to selected");
		refToSelected.addActionListener(e -> refToSelected());
		updatePanel.add(refToSelected);
		
		JButton next = new JButton("next");
		next.addActionListener(e -> {
			if (index != ids.size() - 1) {
				index++;
			}
			updateSpinner();
			loadWord();
		});
		add(next, BorderLayout.EAST);
		
		JButton prev = new JButton("prev");
		prev.addActionListener(e -> {
			if (index != 0) {
				index--;
			}
			updateSpinner();
			loadWord();
		});
		add(prev, BorderLayout.WEST);
		
		JPanel wordPanel = new JPanel();
		BoxLayout wordPanelLayout = new BoxLayout(wordPanel, BoxLayout.Y_AXIS);
		wordPanel.setLayout(wordPanelLayout);
		add(wordPanel);
		
		wordField = new JTextField("");
		wordPanel.add(wordField);
		descArea = new JTextArea("");
		descArea.setLineWrap(true);
		wordPanel.add(descArea);
		firstLetterField = new JTextField("");
		wordPanel.add(firstLetterField);
		wordRefField = new JTextField("");
		wordPanel.add(wordRefField);
		
		loadIdsByFilter();
		updateSpinner();
		loadWord();
	}
	
	protected void deleteWord() {
		if (! Database.getInstance().deleteWord(ids.get(index))) {
			alert("Word not deleted. See error log");
		}
	}

	protected void refToSelected() {
		String wordSelected = descArea.getSelectedText();
		if (wordSelected != null && ! wordSelected.isEmpty()) {
			if (! Database.getInstance().updateRefTo(word, wordSelected)) {
				alert("Word not referred. See error log");
			}
		}
	}

	protected void createRef() {
		String wordSelected = descArea.getSelectedText();
		if (wordSelected != null && ! wordSelected.isEmpty()) {
			if (! Database.getInstance().createReferred(wordSelected, word)) {
				alert("Word not created. See error log");
			}
		}
	}

	private void loadWord(int id) {
		Word word = Database.getInstance().getWord(id);
		if (word != null) {
			setCurrentWord(word);
		}
		else {
			alert("Word was not found");
		}
	}
	
	private void loadWord() {
		loadWord(ids.get(index));
	}

	private void alert(String message) {
		JOptionPane.showMessageDialog(this, message);
	}
	
	private void setCurrentWord(Word word) {
		this.word = word;
		wordField.setText(word.getWord());
		descArea.setText(word.getDescription());
		firstLetterField.setText(word.getFirstLetter());
		wordRefField.setText(String.valueOf(word.getWordReference()));
	}
	
	private void saveCurrentWord() {
		word.setWord(wordField.getText());
		word.setDescription(descArea.getText());
		word.setFirstLetter(firstLetterField.getText());
		word.setWordReference(wordRefField.getText().isEmpty() ? null : Integer.parseInt(wordRefField.getText()));
		if (! Database.getInstance().saveWord(word)) {
			alert("Word not saved. See error log");
		}
	}
	
	private void loadIdsByFilter() {
		String text = filter.getText();
		if (text.isEmpty()) {
			ids = Database.getInstance().getIds();
		}
		else if (text.equals("<>TITLE")) {
			ids = Database.getInstance().getSuspectedTitleIds();
		}
		else {
			ids = Database.getInstance().getIds(filter.getText());
		}
		if (ids.isEmpty()) {
			alert("Words not found. See error log");
		}
		resetSpinner();
		loadWord();
	}
	
	private void updateSpinner() {
		spinner.setValue(ids.get(index));
	}
	
	private void resetSpinner() {
		index = 0;
		updateSpinner();
	}

	public static void main(String[] args) {
		new EditorFrame().setVisible(true);
	}
	
}
