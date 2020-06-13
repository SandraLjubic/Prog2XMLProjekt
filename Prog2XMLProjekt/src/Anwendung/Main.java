package Anwendung;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class Main {
	static XMLInputFactory factory = XMLInputFactory.newInstance();
	static XMLStreamReader parser;
	static DefaultTableModel model = new DefaultTableModel();
	static JTable table = new JTable(model);

	private static List<LineDTO> list = new ArrayList<>();
	private static DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	private static XPathFactory xpathfactory = XPathFactory.newInstance();
	private static XPath xpath = xpathfactory.newXPath();
	
	private static Document doc;

	public static void main(String[] args) {

		start();

		for (LineDTO line : list) {
			System.out.println(line);
		}
	}

	private static void start() {
		try {
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			doc = docBuilder.parse(new File("Muster-Ausschreibungs-LV-ErdMauerBetonarbeiten-xml32.x83"));

			Node firstNode = doc.getElementsByTagName("BoQBody").item(0);

			NodeList firstLvl = ((Node) xpath.compile("*/Award/BoQ/BoQBody").evaluate(doc, XPathConstants.NODE)).getChildNodes();

			for (int i = 0; i < firstLvl.getLength(); i++) {
				list.add(parseDTOFirstLvl(firstLvl.item(i)));
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

	private static LineDTO parseDTOFirstLvl(Node item) throws XPathExpressionException {
		String oz = item.getAttributes().getNamedItem("RNoPart").getNodeValue();
		String posArt = "Bereich";
		String kurztext = null;
		String gp = null;
		
		NodeList secondLvl = ((Node) xpath.compile("*/Award/BoQ/BoQBody/BoQCtgy/BoQBody").evaluate(doc, XPathConstants.NODE)).getChildNodes();
		for (int i = 0; i < secondLvl.getLength(); i++) {
			list.add(parseDTOSecondLvl(secondLvl.item(i), oz + "."));
		}

		return new LineDTO(oz, posArt, kurztext, null, null, null, null, null, null, null, null, null, gp);
	}

	private static LineDTO parseDTOSecondLvl(Node item, String preOz) throws XPathExpressionException {
		String oz = preOz + item.getAttributes().getNamedItem("RNoPart").getNodeValue();
		String posArt = "Bereich";
		String kurztext = null;
		String gp = null;

		NodeList thridLvl = ((Node) xpath.compile("*/Award/BoQ/BoQBody/BoQCtgy/BoQBody/BoQCtgy/BoQBody/Itemlist").evaluate(doc, XPathConstants.NODE)).getChildNodes();

		for (int i = 0; i < thridLvl.getLength(); i++) {
			list.add(parseDTOEinheit(thridLvl.item(i), oz + "."));
		}

		return new LineDTO(oz, posArt, kurztext, null, null, null, null, null, null, null, null, null, gp);
	}

	private static LineDTO parseDTOEinheit(Node item, String preOz) throws XPathExpressionException {
		String oz = preOz + item.getAttributes().getNamedItem("RNoPart").getNodeValue();
		String posArt = "EPANTEILE";
		String kurztext = getKurztext(item);
		String menge = null;
		String einheit = null;
		String loehne = null;
		String stoffe = null;
		String betriebskosten = null;
		String geraetekosten = null;
		String sonstiges = null;
		String nachunternehmer = null;
		String ep = null;
		String gp = null;
		return new LineDTO(oz, posArt, kurztext, menge, einheit, loehne, stoffe, betriebskosten, geraetekosten,
				sonstiges, nachunternehmer, ep, gp);
	}

	private static String getKurztext(Node item) throws XPathExpressionException {
		Node test = (Node) xpath.compile("//Description/CompleteText/OutlineText/OutlTxt/TextOutlTxt").evaluate(item,
				XPathConstants.NODE);
		return test.getTextContent();
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

		// zum Test 2.row
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