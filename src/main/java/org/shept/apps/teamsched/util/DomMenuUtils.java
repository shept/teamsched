/**
 * 
 */
package org.shept.apps.teamsched.util;

import java.util.ArrayList;
import java.util.List;

import org.shept.apps.teamsched.orm.Issue;
import org.shept.apps.teamsched.orm.Workgroup;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * @author lucid64
 *
 */
public class DomMenuUtils extends DomUtils {
	
	public static String getWorkgroupWithIssuesAsMenuXML (List<Issue> issues, List<Workgroup> workgroups) throws Exception {
		// XML Dokument erzeugen
		Document doc = createDocument("ul");
		// Root Element erzeugen.
		Element ul = doc.getDocumentElement();

		Attr clazz = doc.createAttribute("class");
		clazz.setNodeValue("sheptMultiLevelMenu");
		ul.setAttributeNode(clazz);
		
		for (Workgroup workgroup : workgroups) {
			Element li = createItem(doc, null, workgroup.getName());

			List<Issue> issuesForWorkgroup = getIssuesForWorkgroup(issues, String.valueOf(workgroup.getId()));
			createChildren(null, 0, issuesForWorkgroup, li, doc);
			ul.appendChild(li);
		}
		String xml = printDoc(doc);
		return xml;
	}

	/**
	 * @param doc
	 * @param parent
	 * @param workgroup
	 * @return
	 */
	private static Element createItem(Document doc, Integer identifier, String name) {
		Element li = doc.createElement("li");
		Element a = doc.createElement("a");
		li.appendChild(a);
		Attr href = doc.createAttribute("href");
		href.setNodeValue("#");
		a.setAttributeNode(href);
		
		if (identifier != null) {
			Attr id = doc.createAttribute("id");
			id.setNodeValue(identifier.toString());
			a.setAttributeNode(id);
		}
		
		Text text = doc.createTextNode(name);
		a.appendChild(text);
		return li;
	}

	private static void createChildren(Integer parentId, Integer level,
			List<Issue> issues, Element parent, Document doc) {
		// build a list of all issues on this level
		List<Issue> levelIssues = new ArrayList<Issue>();
		for (Issue issue : issues) {
			if (issue.getFdel().equals(0)) {
				if (level.equals(issue.getLevel())
						&& (parentId == null || (parentId.equals(issue.getIssueId())))) {
					levelIssues.add(issue);
				}
			}
		}
		// no children on this level ?
		if (levelIssues.size() == 0)
			return;
		
		// for all children lets recursively create children themselves
		Element ul = doc.createElement("ul");
		for (Issue issue : levelIssues) {
			Element li = createItem(doc, issue.getId(), issue.getName());
			createChildren(issue.getId(), level + 1, issues, li, doc);
			ul.appendChild(li);
		}
		parent.appendChild(ul);
	}

}
