package Anwendung;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

public class Main {
	static XMLInputFactory factory = XMLInputFactory.newInstance();
	static XMLStreamReader parser;
	static DefaultTableModel model = new DefaultTableModel();
	static JTable table = new JTable(model);

	public static void main(String[] args) {

		try {

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("Muster-Ausschreibungs-LV-ErdMauerBetonarbeiten-xml32.xml"));

			NodeList listOfKategories = doc.getElementsByTagName("BoQBody");

			Node firstKategoryNode = listOfKategories.item(0);
			if (firstKategoryNode.getNodeType() == Node.ELEMENT_NODE) {
				Element firstKategoryElement = (Element) firstKategoryNode;

				parseBody(firstKategoryElement);
			}

		} catch (SAXParseException err) {
			System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
			System.out.println(" " + err.getMessage());

		} catch (SAXException e) {
			Exception x = e.getException();
			((x == null) ? e : x).printStackTrace();

		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	static void parseBody(Element e) {

		Node n = e.getFirstChild();
		while (n.getNextSibling() != null) {
			n = n.getNextSibling();
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element child = (Element) n;
				if (child.getNodeName() == "Itemlist") {
					printItem(child);
					return;
				}
				NodeList categorylistList = child.getElementsByTagName("BoQCtgy");
				Element categ = (Element) categorylistList.item(0);
				printCateg(categ);

				NodeList bodyList = child.getElementsByTagName("BoQBody");
				Element bodyElement = (Element) bodyList.item(0);
				parseBody(bodyElement);
				
			}
		}
	}

	// bekommt Element <Itemlist> mit 1-n Items
	static void printItem(Element e) {
		// hier noch Schleife bauen
		String[] row = new String[4];
		row[0] = "irgendwas";
		row[1] = "irgendwas anderes";
		row[2] = "irgendwas anderes";
		row[3] = "irgendwas anderes";
		model.addRow(row);
		System.out.println("da werden die Items gedruckt");
		
		//zum Test 2.row 
		row[0] = "irgendwas";
		row[1] = "irgendwas anderes";
		row[2] = "irgendwas anderes";
		row[3] = "irgendwas anderes";

		model.addRow(row);
		System.out.println("da werden die Items gedruckt");
		
		
	}

	// bekommt Element <BoQCtgy ID, RNoPart>
	static void printCateg(Element e) {
		System.out.println("da werden die Categories gedruckt");
	}
}
