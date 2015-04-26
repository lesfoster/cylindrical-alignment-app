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
 * Config Utils.
 * Created on Sep 23, 2005
 */
package self.lesfoster.cylindrical_alignment.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utilities to help with configuration.
 * 
 * @author Leslie L Foster
 */
public class ConfigUtils {

	/**
	 * Use this to turn a filename into a properties collection.
	 * 
	 * @param file relative path to properties file.  No drive letter, should be
	 * in class path, but otherwise fully-qualified down to extension.
	 * 
	 * @return all props from file.
	 */
	public static Properties getProperties(String file) {
		InputStream inStream = null;
		Properties properties = new Properties();
		try {
	        inStream = ConfigUtils.class.getClassLoader().getResourceAsStream(file);
			properties.load(inStream);
		} catch (Exception ex) {
			// Do nothing.  Fail silently.  The properties are optional.
		} finally {
			if (inStream != null) {
				try {
				    inStream.close();
				} catch (IOException ioe2) {
					// Nada.
				}
			}
		}
		return properties;
	}
}
