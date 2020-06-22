package GUI;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ReadXML {
	static XMLInputFactory factory = XMLInputFactory.newInstance();
	static XMLStreamReader parser;
	static int level = 0;
	static int[] levelCnt = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	static String wrk;

	public static String[][] xml(File file) {
		ArrayList<String[]> tableArray = new ArrayList<String[]>();
		try {

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(file);

			NodeList listOfBodies = doc.getElementsByTagName("BoQBody");

			Node firstBodyNode = listOfBodies.item(0);
			if (firstBodyNode.getNodeType() == Node.ELEMENT_NODE) {
				Element firstBodyElement = (Element) firstBodyNode;

				parseBody(firstBodyElement, tableArray);
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

		// von ArrayList to Array umwandeln
		String[][] tab = new String[tableArray.size()][4];
		for (int i = 0; i < tableArray.size(); i++) {
			tab[i] = tableArray.get(i);
		}

//		System.out.println("_________________________________________________________");
//		for (int i = 0; i < tab.length; i++) {
//			for (int j = 0; j < tab[i].length; j++) {
//				String val = tableArray.get(i)[j];
//				System.out.println(val);
//			}
//			System.out.println("_________________________________________________________");
//		}

		return tab;

	}

	static void parseBody(Element e, ArrayList<String[]> tableArray) {
		Node n = e.getFirstChild();
		while (n.getNextSibling() != null) {
			n = n.getNextSibling();
			if (n.getNodeType() == Node.ELEMENT_NODE) {

				Element child = (Element) n;
				if (child.getNodeName() == "Itemlist") {
					printItem(child, tableArray);
				} else if (child.getNodeName() == "BoQCtgy") {
					printCateg(child, tableArray);

					NodeList bodyList = child.getElementsByTagName("BoQBody");
					Element bodyElement = (Element) bodyList.item(0);
					level += 1;
					parseBody(bodyElement, tableArray);
					levelCnt[level] = 0;
					level -= 1;
				}
			}
		}
	}

	// bekommt Element <Itemlist> mit 1-n Items
	static void printItem(Element e, ArrayList<String[]> tableArray) {
		String[] row;
		NodeList itemListe = e.getElementsByTagName("Item");
		for (int i = 0; i < itemListe.getLength(); i++) {
			row = new String[4];
			levelCnt[level] += 1;
			row[0] = ozCount();
			Element item = (Element) itemListe.item(i);

			NodeList qtyList = item.getElementsByTagName("Qty");
			Element eQty = (Element) qtyList.item(0);
			row[1] = eQty.getTextContent();

			NodeList quList = item.getElementsByTagName("QU");
			Element eQu = (Element) quList.item(0);
			row[2] = eQu.getTextContent();

			NodeList txtList = item.getElementsByTagName("TextOutlTxt");
			Element eTxt = (Element) txtList.item(0);
			NodeList spanList = eTxt.getElementsByTagName("span");
			Element eSpan = (Element) spanList.item(0);
			row[3] = eSpan.getTextContent().replace("\n", " ").replace("\r", " ");
			tableArray.add(row);
			System.out.println(row[0] + " " + row[1] + " " + row[2] + " " + row[3]);
		}

	}

	// bekommt Element <BoQBody>
	static void printCateg(Element e, ArrayList<String[]> tableArray) {
		levelCnt[level] += 1;

		String[] row = { ozCount(), "", "", "" };
		NodeList ctgList = e.getElementsByTagName("span");
		if (ctgList.getLength() > 0) {
			Element eSpan = (Element) ctgList.item(0);
			row[3] = eSpan.getTextContent();
		}
		tableArray.add(row);
		System.out.println(row[0] + " " + row[1] + " " + row[2] + " " + row[3]);
	}

	static String ozCount() {
		wrk = "";
		for (int i = 0; i <= level; i++) {
			wrk = wrk + Integer.toString(levelCnt[i]) + ".";
		}
		return wrk.substring(0, wrk.length() - 1);
	}
}