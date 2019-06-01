package ru.stdian.app.panels;

import ru.stdian.app.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;


public class MainPanel extends JPanel {

	Window window;
	String[] notes;
	JList noteList;

	public void getNotes() {
		File folder = new File("notes");
		notes = folder.list((folder1, name) -> name.endsWith(".txt"));
		if (notes != null) {
			for (int i = 0; i < notes.length; i++) {
				notes[i] = notes[i].replace(".txt", "");
			}
		}
	}

	private void addList() {
		noteList = new JList(notes);
		noteList.setBounds(10, 50, 375, 610);
		noteList.setBorder(BorderFactory.createTitledBorder("Notes"));
		noteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		noteList.setFont(new Font("Arial", Font.PLAIN, 14));

		noteList.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if (notes.length > 0) {
					String s = (String) noteList.getSelectedValue();
					window.getContentPane().removeAll();
					window.getContentPane().add(window.notePanel);
					window.repaint();
					window.pack();
					window.notePanel.getNote(s);
				}
			}
		});

		add(noteList);
	}

	private void addButtons() {
		JButton addButton = new JButton("Add");
		addButton.setBounds(10, 10, 75, 25);
		addButton.addActionListener(e -> {
			window.getContentPane().removeAll();
			window.getContentPane().add(window.notePanel);
			window.repaint();
			window.pack();
			window.notePanel.newNote();
		});
		add(addButton);
	}

	public MainPanel(Window window) {
		this.window = window;
		initPanel();
	}

	private void initPanel() {
		setLayout(null);
		getNotes();
		addButtons();
		addList();
	}
}
