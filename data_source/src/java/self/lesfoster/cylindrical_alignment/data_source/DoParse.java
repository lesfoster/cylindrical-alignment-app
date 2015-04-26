/*
 * Uncomment if you want to test Schema files. 
 *
 * Created on Jul 18, 2005
 *
package self.lesfoster.cylindrical_alignment.data_source;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * @author Leslie L Foster
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 *
public class DoParse extends DefaultHandler {

	public static void main(String[] args) {
		try {
			String filename = args[0];
			SAXParser parser = new SAXParser();
			parser.setContentHandler(new DoParse());
			parser.setFeature("http://xml.org/sax/features/validation", true);
			parser.setFeature("http://apache.org/xml/features/validation/schema", true);
			parser.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
			parser.parse(filename);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	//public void startElement(String x, String y, String z, Attributes attrs) throws SAXException {
	//	System.out.println(x + "/" + y + "/" + z);
	//}
	public void warning(SAXParseException ex) throws SAXException {
		System.out.println(ex.getMessage());
	}
	public void error(SAXParseException ex) throws SAXException {
		System.out.println("ERROR:");
		System.out.println(ex.getMessage());
	}
	public void fatalError(SAXParseException ex) throws SAXException {
		System.out.println(ex.getMessage());
	}
}
*/