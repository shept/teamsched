package org.shept.apps.teamsched.util;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.shept.apps.teamsched.orm.Issue;
import org.shept.apps.teamsched.orm.Workgroup;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.qos.logback.classic.Logger;

/** 
 * @version $$Id: DomUtils.java,v 1.1 2009/11/27 18:53:19 oops.oops Exp $$
 *
 * @author Andi
 *
 */
public class DomUtils {
	
	/** Logger that is available to subclasses */
	protected final static Log logger = LogFactory.getLog(DomUtils.class);
	
	public static Document createDocument (String docName)  throws ParserConfigurationException {
//		 Create XML DOM document (Memory consuming).
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		DOMImplementation impl = builder.getDOMImplementation();
		return impl.createDocument(null, docName, null);
	}
	
	public static String printDoc(Document doc) throws TransformerException {
		StringWriter w = new StringWriter();
		DOMSource domSource = new DOMSource(doc);
		StreamResult streamResult = new StreamResult(w);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer = tf.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
		//serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"users.dtd");
		serializer.setOutputProperty(OutputKeys.INDENT,"no");
		//serializer.setOutputProperty(OutputKeys.METHOD, "html");
		serializer.transform(domSource, streamResult);
		String stringResult = streamResult.getWriter().toString();
		stringResult = stringResult.replaceAll("\"", "'");
		return stringResult;		
	}

	public static List getIssuesForWorkgroup(List issues, String workgroupId) {
		
		Vector issuesList = new Vector();

		for (Iterator iter = issues.iterator(); iter.hasNext();) {
			// naechste Issue holen
			Issue issue = (Issue) iter.next();
			// Workgroup-Id pruefen
			if (String.valueOf(issue.getWorkgroupId()).equals(workgroupId)) {
				// bei Gleichheit in die Liste eintragen
				issuesList.add(issue);
			}
		}
		// Die Liste mit den zugehoerigen issues zurueckgeben
		return issuesList;
	}
	
	

//============================================================
	
	public static Element createWorkgroupElement(Workgroup workgroup, Document doc) {
		// id ermitteln
		String id = String.valueOf(workgroup.getId());
		// name ermitteln
		String name = workgroup.getName();
		// Workgroup-Element erzeugen	
		Element workgroupElement = doc.createElement("workgroup");
		// id Element erzeugen
		Attr idAttribute = doc.createAttribute("id");
		idAttribute.setNodeValue(id);
		workgroupElement.setAttributeNode(idAttribute);
		//Element idElement = doc.createElement("id");
		//Text idText = doc.createTextNode(id);
		//idElement.appendChild(idText);
		//workgroupElement.appendChild(idElement);
		// name Element erzeugen
		Attr nameAttribute = doc.createAttribute("name");
		nameAttribute.setNodeValue(name);
		workgroupElement.setAttributeNode(nameAttribute);
		//Element nameElement = doc.createElement("name");
		//Text nameText = doc.createTextNode(name);
		//nameElement.appendChild(nameText);
		//workgroupElement.appendChild(nameElement);
		
		String test = "";
		try {
			test = printDoc(doc);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return workgroupElement;
	}
	
	public static Element createIssueElement(Issue issue, Document doc) {
		// id ermitteln
		String id = String.valueOf(issue.getId());
		// name ermitteln
		String name = issue.getName();
		// Workgroup-Element erzeugen	
		Element issueElement = doc.createElement("issue");
		// id Element erzeugen
		Attr idAttribute = doc.createAttribute("id");
		idAttribute.setNodeValue(id);
		issueElement.setAttributeNode(idAttribute);
		//Element idElement = doc.createElement("id");
		//Text idText = doc.createTextNode(id);
		//idElement.appendChild(idText);
		//issueElement.appendChild(idElement);
		// name Element erzeugen
		Attr nameAttribute = doc.createAttribute("name");
		nameAttribute.setNodeValue(name);
		issueElement.setAttributeNode(nameAttribute);
		//Element nameElement = doc.createElement("name");
		//Text nameText = doc.createTextNode(name);
		//nameElement.appendChild(nameText);
		//issueElement.appendChild(nameElement);
		return issueElement;
	}
	
	public static Element getDomIssuesForWorkgroupXML (List issues, Element workgroup, Document doc) throws Exception {
		 XPath xpath = XPathFactory.newInstance().newXPath(); 
		
		for (Iterator iter = issues.iterator(); iter.hasNext();) {
			Issue issue = (Issue) iter.next();
			String parentTagId = String.valueOf(issue.getIssueId());
		    String expression = "//issue[@id='"+parentTagId+"']"; 
		    NodeList nodes = (NodeList) xpath.evaluate(expression, workgroup, XPathConstants.NODESET); 
			Node parent;
			if (nodes.getLength() > 0) {
				parent = nodes.item(0);
			} else {
				parent = workgroup;
			}
			parent.appendChild(createIssueElement(issue, doc));
		}
		return workgroup;
	}
	
	public static String getWorkgroupWithIssuesXML (List issues, List workgroups) throws Exception {
		// XML Dokument erzeugen
		Document doc = createDocument("xml");
		// Root Element erzeugen.
		Element root = doc.getDocumentElement();
		// Ueber die Workgroups iterieren
		for (Iterator iter = workgroups.iterator(); iter.hasNext();) {
			// naechste Workgroup holen
			Workgroup workgroup = (Workgroup) iter.next();
			// Element erzeugen
			Element workgroupElement = createWorkgroupElement(workgroup, doc);
			// Zugehoerige issues anhaengen
			String id = String.valueOf(workgroup.getId());
			workgroupElement = getDomIssuesForWorkgroupXML(getIssuesForWorkgroup(issues, id), workgroupElement, doc);
			// Workgroup-Element an root anhaengen
			root.appendChild(workgroupElement);
		}
		String xml = printDoc(doc);
		logger.info(xml);
		return xml;
	}
	
}
