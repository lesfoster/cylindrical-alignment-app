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
 * ClustalW Converter
 * 
 * Created on Jul 31, 2005
 */
package self.lesfoster.cylindrical_alignment.data_source;

import java.io.*;
import java.util.*;

/**
 * Converts ClustalW text files into the target XML for the viewer.
 * 
 * @author Leslie L Foster
 */
public class ClustalWConverter implements Converter {

    private int segmentNumber = 0;
    private Map<String, StringBuffer> alignmentMap = new HashMap<String, StringBuffer>();
    private String fileName;

    /**
     * Constructor takes name of file to parse.
     * @param fileName
     */
    public ClustalWConverter(String fileName) {
    	this.fileName = fileName;
    }

	/* (non-Javadoc)
	 * @see self.lesfoster.cylindrical_alignment.data_source.Converter#getStream()
	 */
    @Override
	public InputStream getStream() {
		return parseClustal();
	}

	/**
	 * Turns the input file of clustal alignment text, and into an input stream
	 * of XML.
	 * @return stream containing converted data.
	 */
	public InputStream parseClustal() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String inbuf = null;
			while(null != (inbuf = reader.readLine())) {
				// What kind of line?

				// CLUSTAL Header.
				if (inbuf.trim().startsWith("CLUSTAL W"))
					continue;
				
				// BLANK line.
				if (inbuf.trim().length() == 0) {
					segmentNumber ++;
					continue;
				}
				
				String name = inbuf.substring(0, 38).trim().intern();
				if (name == null || name.length() == 0)
					continue;
				
				String segmentOfAlignment = inbuf.substring(38).trim();
				
				StringBuffer alignment = alignmentMap.get(name);
				if (alignment == null) {
					if (segmentNumber > 1)
						throw new IllegalArgumentException("ERROR: file has an alignment, " + name + ", that does not begin at first position");
					alignment = new StringBuffer();
					alignmentMap.put(name, alignment);
				}
				
				alignment.append(segmentOfAlignment);
			}
		} catch (IOException ioe) {
			System.out.println("Failed to parse file " + fileName);
			ioe.printStackTrace();
		}

		return new ByteArrayInputStream(toXml().getBytes());
	}

	/**
	 * @return map of alignments. 
	 */
	public Map<String, StringBuffer> getAlignment() {
		return alignmentMap;
	}

	/**
	 * Create a string-of-XML
	 * @return valid xml for target format.
	 */
	public String toXml() {
		StringBuffer returnBuffer = new StringBuffer();
		boolean bAxisIssued = false;
		int alignmentCount = 0;
		int maxAlignment = 0;
		for (Iterator it = getAlignment().keySet().iterator(); (alignmentCount < 75) && it.hasNext(); ) {
			String nextKey = (String)it.next();
			StringBuffer alignment = (StringBuffer)getAlignment().get(nextKey);
			int alignmentLength = alignment.length() + 2;
			if (alignmentLength > maxAlignment)
				maxAlignment = alignmentLength;

			if (! bAxisIssued) {
				bAxisIssued = true;
				returnBuffer.append("<entity>");
				returnBuffer.append("<subentity strand='"+SubEntity.STRAND_NOT_APPLICABLE+"' send='"+alignmentLength+"' sstart='1' qend='"+alignmentLength+"' qstart='1'>");
				returnBuffer.append("<p value='Anchor' name='subEntityType'/>");
				returnBuffer.append("<p value='"+fileName+"' name='defline'/>");
				returnBuffer.append("</subentity>");
				returnBuffer.append("</entity>");
			}
			else {
				// Bypass all the outer hyphens.
				String alignString = alignment.toString();
				int alignmentStart = 0;
				for (int i = 0; i < alignString.length(); i++) {
					if (alignString.charAt(i) != '-') {
						alignmentStart = i+1;
						break;
					}
				}
				int alignmentEnd = alignString.length() + 1;
				for (int i = alignString.length() - 1; i >=0; i--) {
					if (alignString.charAt(i) != '-') {
						alignmentEnd = i+1;
						break;
					}
				}
				if (alignmentEnd <= alignmentStart)
					continue;
				returnBuffer.append("<entity>");
				returnBuffer.append("<subentity qend='"+alignmentEnd+"' qstart='"+alignmentStart+"' send='"+alignmentEnd+"' sstart='"+alignmentStart+"' strand='"+SubEntity.STRAND_NOT_APPLICABLE+"'><p value='"+alignment.toString().substring(alignmentStart - 1, alignmentEnd)+"' name='subject_align'/><p value='"+nextKey+"' name='Sequence Source' /><p value='CLUSTAL W Alignment' name='subEntityType'/></subentity>");
				returnBuffer.append("</entity>");
				
			}
			alignmentCount++;
			
		}
		returnBuffer.append("</cylinder>");

		// Prefix with axis length.
		returnBuffer.insert(0,
				"<?xml version='1.0' encoding='UTF-8'?><cylinder anchor_name='" +
				fileName +
				"' anchor_length='" +
				(maxAlignment + 1) +
				"' version='1.0'>");
				
		// DEBUG
		//System.out.println(returnBuffer);
		return returnBuffer.toString();
	}
	
}
