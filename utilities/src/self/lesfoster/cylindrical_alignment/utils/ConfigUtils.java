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
