package Anwendung;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.management.RuntimeErrorException;
import javax.sound.sampled.Line;
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
	private static int korrekturIndex = 0;

	private static Document doc;

	public static void main(String[] args) {

		start();

		Collections.sort(list, new Comparator<LineDTO>() {

			@Override
			public int compare(LineDTO o1, LineDTO o2) {
				
				String a = o1.getOz().replaceAll("\\.", "");
				String b = o2.getOz().replaceAll("\\.","");
				
				while(a.length()<3) {
					a = a + "0";
				}
				while(b.length()<3) {
					b = b+ "0";
				}
				
				Integer aInt = Integer.parseInt(a);
				Integer bInt= Integer.parseInt(b);
				
				return aInt.compareTo(bInt);
			}

		});
		for (LineDTO line : list) {
			System.out.println(line);
		}

	}

	private static void start() {
		try {
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			doc = docBuilder.parse(new File("Muster-Ausschreibungs-LV-ErdMauerBetonarbeiten-xml32.x83"));

			NodeList firstLvl = ((Node) xpath.compile("*/Award/BoQ/BoQBody").evaluate(doc, XPathConstants.NODE))
					.getChildNodes();

			for (int i = 0; i < firstLvl.getLength(); i++) {
				list.add(parseDTOFirstLvl(firstLvl.item(i), i + 1));
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

	private static LineDTO parseDTOFirstLvl(Node item, int ebene1) throws XPathExpressionException {
		String oz = item.getAttributes().getNamedItem("RNoPart").getNodeValue();
		String posArt = "BEREICH";
		String kurztext = null;
		String gp = null;

		NodeList secondLvl = ((Node) xpath.compile("*/Award/BoQ/BoQBody/BoQCtgy[" + ebene1 + "]/BoQBody").evaluate(doc,
				XPathConstants.NODE)).getChildNodes();
		for (int i = 0; i < secondLvl.getLength(); i++) {
			list.add(parseDTOSecondLvl(secondLvl.item(i), oz + ".", ebene1, i + 1));
		}

		return new LineDTO(oz, posArt, kurztext, null, null, null, null, null, null, null, null, null, gp);
	}

	private static LineDTO parseDTOSecondLvl(Node item, String preOz, int ebene1, int ebene2)
			throws XPathExpressionException {
		String oz = preOz + item.getAttributes().getNamedItem("RNoPart").getNodeValue();
		String posArt = "BEREICH";
		String kurztext = getKurztextEbene2(item, ebene1, ebene2);
		String gp = null;

		NodeList thridLvl = ((Node) xpath
				.compile("*/Award/BoQ/BoQBody/BoQCtgy[" + ebene1 + "]/BoQBody/BoQCtgy[" + ebene2 + "]/BoQBody/Itemlist")
				.evaluate(doc, XPathConstants.NODE)).getChildNodes();

		for (int i = 0; i < thridLvl.getLength(); i++) {
			Node item2 = ((Node) xpath
					.compile("*/Award/BoQ/BoQBody/BoQCtgy[" + ebene1 + "]/BoQBody/BoQCtgy[" + ebene2
							+ "]/BoQBody/Itemlist/Item[" + (i + 1 + korrekturIndex) + "]")
					.evaluate(doc, XPathConstants.NODE));
			if (item2 == null) {
				continue;
			}
			if (!item2.getNodeName().equalsIgnoreCase("item")) {
				korrekturIndex += 1;
			}

			list.add(parseDTOEinheit(oz + ".", ebene1, ebene2, i + 1 + korrekturIndex));
		}
		korrekturIndex = 0;
		return new LineDTO(oz, posArt, kurztext, null, null, null, null, null, null, null, null, null, gp);
	}

	private static LineDTO parseDTOEinheit(String preOz, int ebene1, int ebene2, int ebene3)
			throws XPathExpressionException {

		Node item = ((Node) xpath.compile("*/Award/BoQ/BoQBody/BoQCtgy[" + ebene1 + "]/BoQBody/BoQCtgy[" + ebene2
				+ "]/BoQBody/Itemlist/Item[" + ebene3 + "]").evaluate(doc, XPathConstants.NODE));

		String oz = preOz + item.getAttributes().getNamedItem("RNoPart").getNodeValue();
		String posArt = "EPANTEILE";
		String kurztext = getKurztextEbene3(item);
		String menge = getMenge(item);
		String einheit = getEinheit(item);
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

	private static String getEinheit(Node item) throws XPathExpressionException {
		Node einheit = (Node) xpath.compile("QU").evaluate(item, XPathConstants.NODE);
		return einheit.getTextContent();

	}

	private static String getMenge(Node item) throws XPathExpressionException {
		Node menge = (Node) xpath.compile("Qty").evaluate(item, XPathConstants.NODE);
		return menge.getTextContent();
	}

	private static String getKurztextEbene3(Node item) throws XPathExpressionException {
		Node kurztext = (Node) xpath.compile("Description/CompleteText/OutlineText/OutlTxt/TextOutlTxt").evaluate(item,
				XPathConstants.NODE);
		return kurztext.getTextContent();
	}

	private static String getKurztextEbene2(Node item, int ebene1, int ebene2) throws XPathExpressionException {
		Node kurztext = (Node) xpath
				.compile("*/Award/BoQ/BoQBody/BoQCtgy[" + ebene1 + "]/BoQBody/BoQCtgy[" + ebene2 + "]/LblTx")
				.evaluate(doc, XPathConstants.NODE);
		return kurztext.getTextContent();
	}

}