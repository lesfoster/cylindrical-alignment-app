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


package self.lesfoster.cylindrical_alignment.utils;

/*
 * Gui Utils
 * Created on Feb 3, 2006
 */

import java.awt.*;
import java.net.URL;

import javax.swing.*;

/**
 * Utilities to aid in GUI construction.
 * 
 * @author Leslie L Foster
 */
public class GuiUtils {
    /**
     * Sets up the minimization/top-left-corner icon.
     */
    public static void setIcon(JFrame frame) {
    	// Heads off a problem with icon setup.
    	if (System.getProperty("os.name").toLowerCase().startsWith("linux"))
    		return;
    	try {
    		URL gifUrl = ClassLoader.getSystemResource("cyl_icon.GIF");
    		if (gifUrl != null) {
    		    ImageIcon imageIcon = new ImageIcon(Toolkit.getDefaultToolkit().
    				    getImage(gifUrl));
    		    if (imageIcon != null)
    		        frame.setIconImage(imageIcon.getImage());
    		}

    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }

    /**
     * Sets up frame to be at centered position on screen, as near as possible to the size
     * given without exceeding available area on screen.
     * 
     * @param frame what to position/size.
     * @param proposedWidth what caller wants.
     * @param proposedHeight what caller wants.
     */
    public static Dimension centerLocation(JFrame frame, int proposedWidth, int proposedHeight) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		Insets insets = toolkit.getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());

		int trueScreenWidth = (int)screenSize.getWidth() - insets.left - insets.right;
		int trueScreenHeight = (int)screenSize.getHeight() - insets.top - insets.bottom;
        int width = (int)Math.min(proposedWidth, trueScreenWidth);
        int height = (int)Math.min(proposedHeight, trueScreenHeight);
		frame.setSize(width, height);
		int xLoc = (int)((trueScreenWidth - width) / 2.0) + insets.left;
		int yLoc = (int)((trueScreenHeight - height) / 2.0) + insets.top;
        frame.setLocation(xLoc, yLoc);

		return new Dimension(width, height);
	}

    /**
     * Sets up frame to be at upper left position on screen, as near as possible to the size
     * given without exceeding available area on screen.
     * 
     * @param frame what to position/size.
     * @param proposedWidth what caller wants.
     * @param proposedHeight what caller wants.
     */
    public static Dimension upperLeftLocation(JFrame frame, int proposedWidth, int proposedHeight) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		Insets insets = toolkit.getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());

		int trueScreenWidth = (int)screenSize.getWidth() - insets.left - insets.right;
		int trueScreenHeight = (int)screenSize.getHeight() - insets.top - insets.bottom;
        int width = (int)Math.min(proposedWidth, trueScreenWidth);
        int height = (int)Math.min(proposedHeight, trueScreenHeight);
		frame.setSize(width, height);

		int xLoc = (int)((trueScreenWidth - width) / 10.0) + insets.left;
		int yLoc = (int)((trueScreenHeight - height) / 10.0) + insets.top;
        frame.setLocation(xLoc, yLoc);

		return new Dimension(width, height);
	}

    /** Establish both size and position of a dialog, given its desired width and height. */
    public static void setupScreenRealestate(Component c, int width, int height) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    width = (int)Math.min(width, screenSize.getWidth());
	    height = (int)Math.min(height, screenSize.getHeight());
        c.setSize(new Dimension(width, height));
        c.setLocation((int) ((screenSize.width - width) / 2.0), (int) ((screenSize.height - height) / 2.0));
    }
	
}
