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
 * Appearance Source
 * Created on Mar 3, 2005
 */
package self.lesfoster.cylindrical_alignment.viewer.appearance_source;

/**
 * Returns an appearance source.
 * @author Leslie L. Foster
 */
public class AppearanceSourceFactory {
	/**
	 * TODO change this to prop-file-driven.
	 * @param filename
	 * @return
	 */
    public static AppearanceSource getSourceForFile(String filename) {
    	//if (filename.endsWith(".xml")) {
		return new StandardAppearanceSource();
    	//}
    }

}
