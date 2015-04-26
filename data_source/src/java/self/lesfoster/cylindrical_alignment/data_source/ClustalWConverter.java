/*
Cylindrical Blast Viewer: allows viewing of Biomolecular
alignment data, in 3D, by forming the alignments into a
cylinder, such that the alignments are arranged like the staves
of a barrel. The cylinder can spin, and if it spins, it can
do so at two different speeds.  When stopped, properties can
be seen for the aligned values.
Copyright (C) 2005 Leslie L. Foster

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
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
	 * @param file the file to parse.
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
