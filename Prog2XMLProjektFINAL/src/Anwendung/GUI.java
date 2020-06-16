package Anwendung;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class GUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	String[][] arrayFuerGui = ReadXML.xml();

	String ueberschrift[] = { "OZ", "Menge", "Einh.", "Kurztext" };

	public GUI() {
		super();
		this.setBounds(30, 40, 200, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		this.setSize(800, 400);
		this.setLocation(200, 150);
		JTable table = new JTable(arrayFuerGui, ueberschrift);

		JScrollPane sp = new JScrollPane(table);
		this.add(sp);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
