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

/**
 * Legend Model
 * Created on Apr 9, 2005
 */
package self.lesfoster.framework.integration;

import java.awt.Color;
import java.util.*;

/**
 * Interface to allow representation of "color legend", to the view.
 * @author Leslie L Foster
 */
public interface LegendModel {

    /**
     * All colors.
     */
    List getLegendColors();
    /**
     * All user-visible strings.
     */
    List<String> getLegendStrings();
    /**
     * All models.
     */
    List<Object> getModels();
    /**
     * Given a string, get its associated color.
     * @param s a user-visible string assoc with color.
     */
    Color getColorForString(String s);
    /**
     * Given a color tell its user-visible string.
     * @param c color to "tell about".
     */
    String getStringForColor(Color c);

    /**
     * Adds a listener to changes on this model.
     * @param l
     */
    void addListener(LegendModelListener l);
    
}