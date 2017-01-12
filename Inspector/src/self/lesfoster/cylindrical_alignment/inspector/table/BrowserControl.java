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


package self.lesfoster.cylindrical_alignment.inspector.table;

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

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JOptionPane;

/**
* A simple, static class to display a URL in the system browser.

  Found at:
  
  	http://www.javaworld.com/javaworld/javatips/jw-javatip66.html
  	
  	A JavaWorld article by Steven Spencer.

*
* Under Unix, the system browser is hard-coded to be 'netscape'.
* Netscape must be in your PATH for this to work.  This has been
* tested with the following platforms: AIX, HP-UX and Solaris.


*
* Under Windows, this will bring up the default browser under windows,
* usually either Netscape or Microsoft IE.  The default browser is
* determined by the OS.  This has been tested under Windows 95/98/NT.


*
* Examples:


* 
BrowserControl.displayURL("http://www.javaworld.com")
* 
BrowserControl.displayURL("file://c:\\docs\\index.html")
* 
BrowserContorl.displayURL("file:///user/joe/index.html");
* 

* Note - you must include the url type -- either "http://" or
* "file://".
*/
public class BrowserControl {
	// Used to identify the windows platform.
    private static final String WIN_ID = "Windows";

    /**
     * Display a file in the system browser.  If you want to display a
     * file, you must include the absolute path name.
     *
     * @param url the file's url (the url must start with either "http://"
or
     * "file://").
     */
    public static void displayURL(String url) {
		String cmd = null;
		try {
			try {
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().browse(new URI(url));
				} else {
					JOptionPane.showMessageDialog(null, "Cannot open URL.  Not supported.");
				}
			} catch (URISyntaxException use) {
				System.err.println("Error bringing up browser, cmd='"
						+ cmd + "'");
				System.err.println("Caught: " + use);
			}
		} catch (IOException x) {
			// couldn't exec browser
			System.err.println("Could not invoke browser, command=" + cmd);
			System.err.println("Caught: " + x);
		}
	}
    /**
     * Try to determine whether this application is running under Windows
     * or some other platform by examing the "os.name" property.
     *
     * @return true if this application is running under a Windows OS
     */
    public static boolean isWindowsPlatform() {
        String os = System.getProperty("os.name");
        if ( os != null && os.startsWith(WIN_ID))
            return true;
        else
            return false;

    }
    /**
     * Simple example.
     */
    public static void main(String[] args) {
        displayURL("http://www.javaworld.com");
    }
}

