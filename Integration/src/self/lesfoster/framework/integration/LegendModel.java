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
	 * Assigns color to specific object of interest.
	 * 
	 * @param legendName how called
	 * @param object what to label
	 * @param r red component
	 * @param g green component
	 * @param b blue component
	 */
	void addColorString(String legendName, Object object, float r, float g, float b);
	
	/**
	 * Add a color mapped to specific object of interest.
	 * 
	 * @param legendName how called 
	 * @param object what to label
	 * @param legendColor color given
	 */
	void addColorString(String legendName, Object object, Color legendColor);
	
    /**
     * Adds a listener to changes on this model.
     * @param l whole model is changed.
     */
    void addListener(LegendModelListener l);
    
	/**
	 * Register for selections within legend model.
	 * @param l listener to hear about selections.
	 */
	void addLegendSelectionListener(LegendSelectionListener l);
	
	/**
	 * Trigger the selection event: select something here.
	 * @param model got selected
	 */
	void selectModel(Object model);
	
	/**
	 * Allow programmatic setting of the selection's color.
	 * @param selectionColor 
	 */
	void setSelectionColor(float[] selectionColor);
	
	float[] getSelectionColor();
	
	/**
	 * Call this if the old context becomes meaningless, or at dispose.
	 */
	void clear();
}