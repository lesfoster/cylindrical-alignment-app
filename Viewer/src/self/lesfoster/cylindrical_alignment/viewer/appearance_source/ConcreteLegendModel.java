/*
Cylindrical Blast Viewer: allows viewing of Biomolecular
alignment data, in 3D, by forming the alignments into a
cylinder, such that the alignments area arranged like the staves
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
 * Concrete Legend Model
 * Created on Apr 9, 2005
 */
package self.lesfoster.cylindrical_alignment.viewer.appearance_source;
import java.util.*;
import javafx.scene.paint.Color;
import self.lesfoster.cylindrical_alignment.data_source.SubEntity;
import self.lesfoster.cylindrical_alignment.viewer.legend_model.LegendModel;
import self.lesfoster.cylindrical_alignment.viewer.legend_model.LegendModelListener;

/**
 * Implementation of Legend Model that stores mappings of colors.
 * @author Leslie L Foster
 */
public class ConcreteLegendModel implements LegendModel {

	private List<String> legendStringList;
	private List<SubEntity> subEntityList;
	private LegendModelListener legendModelListener;
    private Map<String, Color> legendMap;
    private Map<Color, String> colorMap;

    /**
     * Constructor builds collections.
     */
    public ConcreteLegendModel() {
    	this(null);
    }

    /**
     * Constructor builds collections, and adds a listener.
     */
    public ConcreteLegendModel(LegendModelListener listener) {
    	legendModelListener = listener;
        legendMap = new HashMap<>();
        legendStringList = new ArrayList<>();
        colorMap = new HashMap<>();
        subEntityList = new ArrayList<>();
    }

    /**
     * This is how to put new values.
     * @param legendName visible to user.
     * @param legendColor should match what is seen in 3d.
     */
    public void addColorString(String legendName, SubEntity subEntity, Color legendColor) {
    	if (legendStringList.contains(legendName))
    		return;
    	legendStringList.add(legendName);
        legendMap.put(legendName, legendColor);
        colorMap.put(legendColor, legendName);
        subEntityList.add(subEntity);
        if (legendModelListener != null)
        	legendModelListener.updateLegendModel();
    }

    /**
     * This is how to put new values.
     * @param legendName visible to user.
     * @param r red component.
     * @param g green component.
     * @param b blue component.
     */
    public void addColorString(String legendName, SubEntity subEntity, float r, float g, float b) {
    	if (legendName != null) {
    		try {
    			//System.out.println("Adding color String: " + legendName);
    	        Color legendColor = new Color(r, g, b, 1.0);
    	        addColorString(legendName, subEntity, legendColor);
    		} catch (RuntimeException rte) {
    			// Can happen if bad color range given.
    			rte.printStackTrace();
    		}
    	}
    }

    /**
     * All sub entities.
     */
    public List<SubEntity> getModels() {
    	return subEntityList;
    }

    /**
     * All colors.
     */
    public List<Color> getLegendColors() {
        return new ArrayList<>(legendMap.values());
    }

    /**
     * All user-visible strings.
     */
    public List<String> getLegendStrings() {
        return legendStringList;
    }

    /**
     * Given a string, get its associated color.
     * @param s a user-visible string assoc with color.
     */
    public Color getColorForString(String s) {
        return (Color)legendMap.get(s);
    }

    /**
     * Given a color tell its user-visible string.
     * @param c color to "tell about".
     */
    public String getStringForColor(Color c) {
        return (String)colorMap.get(c);
    }

    /**
     * Allows addition of the (one) listener, to changes.
     * @param lml external object interested in hearing about changes.
     */
    public void addListener(LegendModelListener lml) {
    	this.legendModelListener = lml;
    	legendModelListener.updateLegendModel();
    }
}