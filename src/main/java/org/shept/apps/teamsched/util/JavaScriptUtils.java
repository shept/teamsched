package org.shept.apps.teamsched.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

/** 
 * @version $$Id: JavaScriptUtils.java,v 1.1 2009/11/27 18:53:19 oops.oops Exp $$
 *
 * @author Andi
 *
 */
public class JavaScriptUtils {

	/*
	 * Transform an ArrayList (rowList) with ArrayList (columns)
	 * into Javascript 2-dimensional array syntax.
	 */
	public static String asJavaScript2DimArray(List l) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintWriter pw = new PrintWriter(baos);
		pw.write(" [");
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			List row = (List) iter.next();
			pw.write(" [");
			for (Iterator iterator = row.iterator(); iterator.hasNext();) {
				Object sel = iterator.next();
				pw.write(sel.toString());
				if (iterator.hasNext()) pw.write(", ");
			}
			pw.write("] ");
			if (iter.hasNext()) pw.write(" , ");
		}
		pw.write("] ");
		pw.close();
		return baos.toString();
	}

	//
	// I guess this one is unused (there is a ruled out usage in timeapp#enterhours
	//
	public static String asJavaScriptStringArray(List l) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintWriter pw = new PrintWriter(baos);
		pw.write(" [");
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			String s = (String) iter.next();
			pw.write("\"" + s.toString() + "\"");
			if (iter.hasNext()) pw.write(", ");
		}
		pw.write("] ");
		pw.close();
		return baos.toString();
	}

}
