/*
 * Project Post Processor
 * Created on Sep 19, 2005
 */
package self.lesfoster.cylindrical_alignment.data_source;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import javax.xml.parsers.*; 

/**
 * Post processor to take up where XSLT left off, on Microsoft Project (tm) files.
 * @TODO replace with XSLT extensions if possible.
 * @author Leslie L Foster
 */
public class ProjectPostProcessor implements Converter {

	private InputStream instream;
	private String anchorOffset;

	/**
	 * Takes and keeps the input stream for later read/wrap.
	 * @param instream read from this.
	 */
	public ProjectPostProcessor(InputStream instream) {
		this.instream = instream;
	}

	/* (non-Javadoc)
	 * @see self.lesfoster.cylindrical_alignment.data_source.Converter#getStream()
	 */
	public InputStream getStream() {
		// Will parse and return result.
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(false);
			factory.setValidating(false);
			SAXParser parser = factory.newSAXParser();
			InternalConversionHandler handler = new InternalConversionHandler();
			parser.parse(instream, handler);
			return handler.getStream();

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;

		}

	}

	/**
	 * This handler will take the XML input, make minor tweaks, and pass it on.
	 * @author Leslie L Foster
	 */
	class InternalConversionHandler extends DefaultHandler {
		private StringBuffer outputBuffer = new StringBuffer();

		/** This stream is the whole point of this conversion. */
		public InputStream getStream() {
			// DEBUG
			//System.out.println(outputBuffer);
			return new ByteArrayInputStream(outputBuffer.toString().getBytes());
		}

		/** Finds beginning element.  Only "modifying" call, this focuses on changing attribute values. */
		public void startElement(String uri, String localName, String qName, Attributes attributes) {
			outputBuffer.append("<");
			outputBuffer.append(qName);
			for (int i = 0; i < attributes.getLength(); i++) {
				String value = attributes.getValue(i);
				String name = attributes.getQName(i);
				if (name.equalsIgnoreCase("anchor_start")) {
					anchorOffset = value;
					continue;
				}
				else if (name.equalsIgnoreCase("anchor_end")) {
					name = "anchor_length";
					try {
					    value = calculateDays(value, anchorOffset);
					} catch (Exception ex) {
						value = "-1";
						ex.printStackTrace();
					}
				}
				else if (name.equalsIgnoreCase("sstart") ||
					name.equalsIgnoreCase("send") ||
					name.equalsIgnoreCase("qstart") ||
					name.equalsIgnoreCase("qend")) {

					try {
					    value = calculateDays(value, anchorOffset);
					} catch (Exception ex) {
						value = "-1";
						ex.printStackTrace();
					}
				}

				outputBuffer.append(" ").append(name).append("=\"").append(cleanup(value)).append("\"");

			}
			outputBuffer.append(">");
		}
		 
		public void endElement(String uri, String localName, String qName) {
			outputBuffer.append("</").append(qName).append(">");
		}

        public void characters(char[] ch, int start, int length) {
			outputBuffer.append(ch, start, length);
		}

        /** Some processing to make realistic attribute values. */
        private String cleanup(String value) {
        	String newValue = value.replaceAll("&", "&amp;");
        	newValue = newValue.replaceAll("\n", " ");
        	newValue = newValue.replaceAll("\"", "&quot;");
        	newValue = newValue.replaceAll("\'", "&quot;");
        	return newValue;
        }
        
		/**
		 * Given two dates in the obvious format, calculate the number of days
		 * between them, taking the first date as the later date.
		 * 
		 * @param yyyymmdd1 later date.
		 * @param yyyymmdd2 earlier date.
		 * @return days between date 1 and date 2.
		 */
		private String calculateDays(String yyyymmdd1, String yyyymmdd2) {
			return "" + (daysForDate(yyyymmdd1) - daysForDate(yyyymmdd2)); 
		}

		/**
		 * Given a date in the format yyyymmdd, translate that into days since
		 * start of counting.
		 * 
		 * @param yyyymmdd
		 * @return days since jan 1, 1970.
		 */
		private long daysForDate(String yyyymmdd) {
			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
			try {
				StringBuffer mmddyyyy = new StringBuffer(yyyymmdd.substring(5,7));
				mmddyyyy.append("/")
				        .append(yyyymmdd.substring(8,10))
						.append("/")
						.append(yyyymmdd.substring(0,4));
			    Date date = df.parse(mmddyyyy.toString());
			    // Time is in ms.
			    return date.getTime() / 1000 / 60 / 60 / 24; 
			} catch (ParseException pe) {
				throw new IllegalArgumentException(pe);
			}
		}
	}
}
