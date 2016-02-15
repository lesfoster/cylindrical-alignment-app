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

package self.lesfoster.cylindrical_alignment.constants;

import javafx.scene.paint.Color;

/**
 * Group of constants used in one or more places in the viewer classes. 
 * @author Leslie L Foster
 */
public class Constants {

    public static final String REVERSE_TEXTURE = "self/lesfoster/cylindrical_viewer/viewer/textures/rev_arrow.gif";
    public static final String FORWARD_TEXTURE = "self/lesfoster/cylindrical_viewer/viewer/textures/fwd_arrow.gif";
	private static final float CYL_RATIO_CONSTANT = 1.7f;
	
	public static final float LENGTH_OF_CYLINDER = 200f;
	public static final float START_OF_CYLINDER = LENGTH_OF_CYLINDER / 2.0f;
	public static final float ANCHOR_OFFSET = (0.27f / CYL_RATIO_CONSTANT) * LENGTH_OF_CYLINDER;
	public static final float Y_DISP = (0.4f / CYL_RATIO_CONSTANT ) * LENGTH_OF_CYLINDER;
	public static final float YT = (0.01f / CYL_RATIO_CONSTANT) * LENGTH_OF_CYLINDER + Y_DISP;
	public static final float YB = (-0.01f / CYL_RATIO_CONSTANT) * LENGTH_OF_CYLINDER + Y_DISP;
    public static final float ZB = (-0.012f / CYL_RATIO_CONSTANT) * LENGTH_OF_CYLINDER;
    public static final float ZF = (0.012f / CYL_RATIO_CONSTANT) * LENGTH_OF_CYLINDER;
	public static final float CB_LABEL_HEIGHT = (.05f / CYL_RATIO_CONSTANT) * LENGTH_OF_CYLINDER;
	public static final float CB_OUTER_RADIUS = Constants.YB + 2.4f;  // Outside will reach beyond the outer surface of all solids.
	public static final float CB_INNER_RADIUS = Constants.YB - 0.8f;  // Inside will be just lower than the inner surface of all solids.
	public static final float YLABEL = (0.55f / CYL_RATIO_CONSTANT) * LENGTH_OF_CYLINDER;
	public static final float CENTER_X = 0.0f;
	public static final float CENTER_Y = 0.0f;
	public static final float CENTER_Z = 0.0f;
	public static final float RULE_Y_BOTTOM = YT + (0.05f / CYL_RATIO_CONSTANT) * LENGTH_OF_CYLINDER;
	public static final int   MAX_UNDIVIDED_RULE_DIVISION = 1000;
	public static final float LABEL_CHAR_WIDTH = (0.03f / CYL_RATIO_CONSTANT) * LENGTH_OF_CYLINDER;
	public static final float JUST_OFF_RULE_X = (0.846f / CYL_RATIO_CONSTANT) * LENGTH_OF_CYLINDER;
	public static final float LOW_LABEL_X = (-0.84f / CYL_RATIO_CONSTANT) * LENGTH_OF_CYLINDER;
    public static final String CODE_VERSION = "1.0";
	public static final String ABOUT_MESSAGE =
		    "Cylindrical BLAST Viewer.\n" +
			"Version " + CODE_VERSION + "\nWritten by Les Foster\n" +
			"MS, Biotechnology, JHU, 2005/2011/2012\n\n" +
			"Comments? Suggestions? Kudos? Concerns?\n" +
			"Please contact cylviewer@gmail.com\n" +
            "For more information, visit the \n" +
            "'Cylindrical BLAST Viewer Users' group on LinkedIn.";
    public static final String APPLICATION_HELP_MSG =
            "For issues, please visit the forum pages at \n" +
            "http://sourceforge.net/projects/cyl-viewer/forums/forum/473691\n" +
            "or email cylviewer@gmail.com.  A reasonable effort will be made.";
    public static final String URL_PING_ADVISORY =
            "To best serve the user base, statistics will be collected using\n" +
            "Google Analytics.  Only counts will be stored; no user identifying data.\n" +
            "The process of contacting the analytics server will be run at\n" +
            "program startup, in a low priority thread, and in the background.";
	public static final Color CIGAR_BAND_COLOR = Color.color(0.8f, 0.6f, 0.3f);
	public static final Color TICK_BAND_COLOR = Color.color(0.6f, 0.6f, 0.6f);
	public static final Color FILL_LIGHT_COLOR = Color.LIGHTGRAY;
	public static final Color FILL_DARK_COLOR = Color.BLACK;
	public static final Color INLABEL_LIGHT_TEXT_COLOR = Color.BLACK;
	public static final Color INLABEL_DARK_TEXT_COLOR = Color.WHITE;

}
