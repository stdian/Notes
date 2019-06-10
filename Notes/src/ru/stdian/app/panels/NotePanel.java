package ru.stdian.app.panels;

import ru.stdian.app.Notifications;
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
	public JTextArea noteArea;
	private JTextArea noteTitle;
	public Thread thread1;
	public Thread thread2;

	private JButton saveButton;
	private JButton exitButton;
	private JButton removeButton;

	public NotePanel(Window window) {
		this.window = window;
		init();
	}


	public void initThread() {
		noteTitle.setBounds(10, -35, 375, 25);
		exitButton.setBounds(10, -30, 75, 25);
		removeButton.setBounds(180, -30, 75, 25);
		saveButton.setBounds(95, -30, 75, 25);
		noteArea.setBounds(10, 90, 375, 570);

		thread1 = new Thread(() -> {
			try {
				for (int i = 710; i >= 90; i-=2) {
					Thread.sleep(1);
					noteArea.setBounds(10, i, 375, 570);
				}
			} catch (Exception ignored) {}
		});
		thread2 = new Thread(() -> {
			try {
				for (int i = -75; i <= 10; i++) {
					Thread.sleep(4);
					exitButton.setBounds(10, i, 75, 25);
					saveButton.setBounds(95, i, 75, 25);
					removeButton.setBounds(180, i, 75, 25);
					noteTitle.setBounds(10, i + 40, 375, 25);
				}
			} catch (Exception ignored) {}
		});
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
			initThread();
			thread1.start();
			thread2.start();
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
		noteTitle.setBounds(10, -35, 375, 25);
		noteTitle.setBorder(new LineBorder(Color.BLACK, 2));

		add(noteTitle);
	}

	private void addButtons() {
		exitButton = new JButton("Exit");
		exitButton.setBounds(10, -30, 75, 25);
		add(exitButton);

		saveButton = new JButton("Save");
		saveButton.setBounds(95, -30, 75, 25);
		add(saveButton);

		removeButton = new JButton("Remove");
		removeButton.setBounds(180, -30, 75, 25);
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
			Notifications.showInfoNotification("Info", "Note saved successfully!");
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(window, e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
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
		initThread();
		thread1.start();
		thread2.start();
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
