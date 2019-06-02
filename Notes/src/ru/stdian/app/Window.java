package ru.stdian.app;

import javax.swing.*;
import  ru.stdian.app.panels.*;

import java.awt.*;

public class Window extends JFrame {

	public MainPanel mainPanel;
	public NotePanel notePanel;

	public Window() {
		initWindow();
	}


	private void initWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {}
		mainPanel = new MainPanel(this);
		notePanel = new NotePanel(this);

		setPreferredSize(new Dimension(400, 700));
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		getContentPane().add(mainPanel);

		pack();

		setTitle("Notes");
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Window());
	}

}
