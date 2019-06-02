package ru.stdian.app.panels;

import ru.stdian.app.Window;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class NotePanel extends JPanel {

	private Window window;
	private JTextArea noteArea;
	private JTextArea noteTitle;

	private JButton removeButton;

	public NotePanel(Window window) {
		this.window = window;
		init();
	}

	public void getNote(String title) {
		File file = new File("notes/" + title + ".txt");
		try {
			Scanner sc = new Scanner(file);
			noteTitle.setText(title);
			window.setTitle(title);
			String txt = "";
			while (sc.hasNextLine()) {
				txt += sc.nextLine() + "\n";
			}
			noteArea.setText(txt);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		removeButton.setEnabled(true);
	}

	private void addNoteArea() {
		noteArea = new JTextArea();
		noteArea.setBounds(10, 90, 375, 570);
		noteArea.setFont(new Font("Arial", Font.PLAIN, 14));
		noteArea.setBorder(new LineBorder(Color.BLACK, 2));

		add(noteArea);
	}

	private void addNoteTitle() {
		noteTitle = new JTextArea();
		noteTitle.setFont(new Font("Arial", Font.BOLD, 16));
		noteTitle.setBounds(10, 50, 375, 25);
		noteTitle.setBorder(new LineBorder(Color.BLACK, 2));

		add(noteTitle);
	}

	private void addButtons() {
		JButton exitButton = new JButton("Exit");
		exitButton.setBounds(10, 10, 75, 25);
		add(exitButton);

		JButton saveButton = new JButton("Save");
		saveButton.setBounds(95, 10, 75, 25);
		add(saveButton);

		removeButton = new JButton("Remove");
		removeButton.setBounds(180, 10, 75, 25);
		add(removeButton);

		exitButton.addActionListener(e -> closeNote());

		saveButton.addActionListener(e -> saveNote());

		removeButton.addActionListener(e -> removeNote());
	}

	private void saveNote() {
		String title = noteTitle.getText();
		String text = noteArea.getText();
		try {
			FileWriter writer = new FileWriter("notes/" + title + ".txt");
			writer.write(text);
			writer.flush();
			writer.close();
			JOptionPane.showMessageDialog(window, "Note saved successfully!", "Info", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(window, e1.toString(), "Info", JOptionPane.ERROR_MESSAGE);
		}
		window.mainPanel.getNotes();
		DefaultListModel model = new DefaultListModel();
		for (int i = 0; i < window.mainPanel.notes.length; i++) {
			model.add(i, window.mainPanel.notes[i]);
		}
		window.mainPanel.noteList.setModel(model);
		closeNote();
	}

	private void removeNote() {
		try {
			System.gc();
			File file = new File("notes/" + window.mainPanel.noteList.getSelectedValue() + ".txt").getAbsoluteFile();
			if (file.delete()) {
				window.mainPanel.getNotes();
				DefaultListModel model = new DefaultListModel();
				for (int i = 0; i < window.mainPanel.notes.length; i++) {
					model.add(i, window.mainPanel.notes[i]);
				}
				window.mainPanel.noteList.setModel(model);
				closeNote();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(window, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void closeNote() {
		window.setTitle("Notes");
		window.getContentPane().removeAll();
		window.getContentPane().add(window.mainPanel);
		window.repaint();
		window.pack();
	}

	public void newNote() {
		noteTitle.setText("Untitled");
		noteArea.setText("");
		window.setTitle("Untitled");
		removeButton.setEnabled(false);
	}

	private void init() {
		setLayout(null);
		addButtons();
		addNoteArea();
		addNoteTitle();
	}

}
