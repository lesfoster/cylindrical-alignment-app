/*
 CDDL HEADER START

 The contents of this file are subject to the terms of the
 Common Development and Distribution License (the "License").
 You may not use this file except in compliance with the License.

 You can obtain a copy of the license at
   https://opensource.org/licenses/CDDL-1.0.
 See the License for the specific language governing permissions
 and limitations under the License.

 When distributing Covered Code, include this CDDL HEADER in each
 file and include the License file at
    https://opensource.org/licenses/CDDL-1.0.
 If applicable, add the following below this CDDL HEADER, with the
 fields enclosed by brackets "[]" replaced with your own identifying
 information: Portions Copyright [yyyy] [name of copyright owner]

 CDDL HEADER END
*/



/*
 * General XSLT Converter
 * Created on Aug 5, 2005
 */
package self.lesfoster.cylindrical_alignment.data_source;

import self.lesfoster.cylindrical_alignment.utils.ConfigUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.XMLConstants;

/**
 * This converter will run an XSLT style sheet, to convert between
 * XML formats.
 * 
 * @author Leslie L Foster
 */
public class GeneralXsltConverter implements Converter {
	//  These are all the different choices of XSLT sheets.
	private static final String XSLT_MAP_PROPS = "self/lesfoster/cylindrical_alignment/data_source/xslt_map.properties";
	private static final String XSLT_EXTENSION_MAP_PROPS = "xslt_map.properties";
	private static final String XSLT_STRIP_PREFIX = "TYPE";
	private static final Properties TYPE_PROPS = ConfigUtils.getProperties(XSLT_MAP_PROPS);
	private static final Properties EXTENDED_TYPE_PROPS;
	static {
		// Has the effect of overriding internal settings with user extensions.
		EXTENDED_TYPE_PROPS = ConfigUtils.getProperties(XSLT_EXTENSION_MAP_PROPS);
		if (! EXTENDED_TYPE_PROPS.isEmpty()) {
			for (Iterator it = EXTENDED_TYPE_PROPS.keySet().iterator(); it.hasNext(); ) {
				String nextName = it.next().toString();
				TYPE_PROPS.setProperty(nextName, EXTENDED_TYPE_PROPS.getProperty(nextName));
				System.out.println("Added extended mapping "
						+ nextName
						+ " to "
						+ EXTENDED_TYPE_PROPS.getProperty(nextName)
						+ " as an XSLT conversion.");
			}
		}
	}

	//  Each converter converts a file using a stylesheet.
	private String filename;
	private String xslFile;

	/**
	 * Factory method on this converter.  Will determine whether the filename
	 * is something this converter can handle, and will return an instance
	 * if so.  If not, will return null.
	 * 
	 * @param filename filename to test.
	 * @return new instance of this converter, or null.
	 */
	public static GeneralXsltConverter getConverter(String filename) {
    	if (filename.endsWith(".xml")) {
    		for (Iterator it = TYPE_PROPS.keySet().iterator(); it.hasNext(); ) {
    			String nextKey = it.next().toString();
    			String suffix = nextKey.substring(XSLT_STRIP_PREFIX.length());
    			if (filename.endsWith(suffix)) {
    				return new GeneralXsltConverter(filename, (String)TYPE_PROPS.get(nextKey));
    			}
    		}
    	}
		
    	return null;
	}

	/**
	 * Caches the filename provided.
	 * 
	 * @param filename
	 */
	private GeneralXsltConverter(String filename, String xslFile) {
		this.filename = filename;
		this.xslFile = xslFile;
	}

	/**
	 * Picks up conversion result.
	 * 
	 * @return input stream that sources the target XML type.
	 */
	public InputStream getStream() {
		Transformer transformer;
		TransformerFactory factory = TransformerFactory.newInstance();
		factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "all");
		//factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "all");
        InputStream inStream = getClass().getClassLoader().getResourceAsStream(getXslFile());
		StreamSource stylesheet = new StreamSource(inStream);
		ByteArrayInputStream inputStream = null;
		//PipedOutputStream pout = new PipedOutputStream();
		//PipedInputStream pin;
		try {
			Templates templates = factory.newTemplates(stylesheet);
			transformer = templates.newTransformer();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		    transformer.transform(new StreamSource(getFilename()), new StreamResult(outputStream));
			byte[] bytesOfXml = outputStream.toByteArray();

			inputStream = new ByteArrayInputStream(bytesOfXml);
			// DEBUG
			//System.out.println(new String(bytesOfXml));

			//pin = new PipedInputStream(pout);

			// Now, in order that the piped input stream will be filled with
			// good things to read, its "pump" thread must be started.
			//Thread thread = new Thread(new Piper(transformer, pout));
			//thread.start();
		} catch (Exception e) {
		    System.out.println(getFilename() + " failed to convert to target XML required by this app.");
		    System.out.println(e.getMessage());
		    throw new RuntimeException(e);
		}
		//return pin;
		return inputStream;
	}

	/**
	 * Template Method: provides the name of the file to convert to
	 * internal format.
	 * 
	 * @return the filename.
	 */
    private String getFilename() {
    	return filename;
    }

    /**
     * Template Method: provides the name of the XSLT stylesheet file.
     * 
     * @return a valid XSLT spreadsheet.
     */
    private String getXslFile() {
    	return xslFile;
    }

	/**
	 * Inner class to take care of filling the pipe of the transformer.
	 * @author Leslie L Foster
	 *
	class Piper implements Runnable {
		private Transformer transformer;
		private OutputStream os;
		public Piper(Transformer transformer, OutputStream os) {
			this.transformer = transformer;
			this.os = os;
		}

		/**
		 * 'Run' will startup a transform.
		 *
		public void run() {
			try {
			    transformer.transform(new StreamSource(filename), new StreamResult(os));
			} catch (Exception ex) {
				System.out.println(filename + " failed to convert to target XML required by this app, during transform stage.");
				System.out.println(ex.getMessage());
			}
		}
	}*/

}
