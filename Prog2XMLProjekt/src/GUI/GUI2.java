package GUI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;

import javax.swing.*;

public class GUI2 implements ActionListener {
	public JFrame frame;

	static String[][] arrayFuerGui;

	String ueberschrift[] = { "OZ", "Menge", "Einh.", "Kurztext" };

	public File file;

	public GUI2() {
		frame = new JFrame();

		// Frame data
		frame.setTitle("GAEB X-83 Data Opener");
		frame.setBounds(0, 0, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);

		JLabel name = new JLabel("<html><span style='font-size:18px'>GAEB X-83 Data Opener</span></html>");

		// Menu data
		JMenuBar mb = new JMenuBar();
		JMenu info = new JMenu("Programm");
		JMenuItem about = new JMenuItem("About");
		JMenuItem menuExit = new JMenuItem("Quit programm");
		info.add(about);
		info.add(menuExit);
		mb.add(info);

		// Action Listeners
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				JOptionPane.showMessageDialog(null, "Developerteam:\nKamila Sultanova, s0564709;"
						+ "\nSandra Ljubic, s0555006;" + "\nDavid O. Mitchell, s0570705.");
			}
		});

		menuExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				System.exit(0);
			}
		});

		JButton fileChooser = new JButton("<html><span style='font-size:10px'>Open file</span></html>",
				new ImageIcon("/Users/elena/eclipse-workspace/final/src/file.png"));
		fileChooser.addActionListener(this);
		fileChooser.setActionCommand("fileChooser");

		JButton exit = new JButton("<html><span style='font-size:10px'>Exit</span></html>",
				new ImageIcon("/Users/elena/eclipse-workspace/final/src/exit.png"));
		exit.addActionListener(this);
		exit.setActionCommand("exitProgramm");

		mb.setBounds(0, 0, 800, 25);
		name.setBounds(250, 30, 400, 50);

		fileChooser.setBounds(20, 510, 125, 50);
		exit.setBounds(655, 510, 125, 50);

		frame.add(mb);
		frame.add(name);
		frame.add(fileChooser);
		frame.add(exit);
		frame.setVisible(true);

	}

	public void OpenFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("/Users/elena/eclipse-workspace/final"));
		int result = fileChooser.showOpenDialog(new JFrame());
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			System.out.println(selectedFile.getAbsolutePath());
			this.file = new File(selectedFile.getAbsolutePath());
			arrayFuerGui = ReadXML.xml(this.file);
			for (String[] i : arrayFuerGui) {
				System.out.println(Arrays.toString(i));
			}
			AddTable();

		}
	}

	// TODO:
	public void AddTable() {

		JTable table = new JTable(arrayFuerGui, ueberschrift);
		table.setBounds(50, 120, 700, 350);
		JScrollPane sp = new JScrollPane(table);

		// sp.revalidate();
		// table.getTableHeader().setPreferredSize(new Dimension(table.getSize().width,
		// 40));

		frame.add(table);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("fileChooser")) {
			OpenFile();
		}

		if (e.getActionCommand().contentEquals("exitProgramm")) {
			System.exit(0);
		}

	}

}
