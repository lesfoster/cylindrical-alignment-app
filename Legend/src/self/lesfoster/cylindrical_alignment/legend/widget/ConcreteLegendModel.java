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
package self.lesfoster.cylindrical_alignment.legend.widget;
import java.util.*;
import java.awt.Color;
import self.lesfoster.framework.integration.LegendModel;
import self.lesfoster.framework.integration.LegendModelListener;
import self.lesfoster.framework.integration.LegendSelectionListener;

/**
 * Implementation of Legend Model that stores mappings of colors.
 * @author Leslie L Foster
 */
public class ConcreteLegendModel implements LegendModel {

	private List<String> legendStringList;
	private List<Object> labeledObjects;
	private LegendModelListener legendModelListener;
    private Map<String, Color> legendMap;
    private Map<Color, String> colorMap;
	
	private Map<Integer,Object> idToEntity = new HashMap<>();
	private List<LegendSelectionListener> legendSelectionListeners = new ArrayList<>();
	private float[] selectionColor;

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
        labeledObjects = new ArrayList<>();
    }

    /**
     * This is how to put new values.
     * @param legendName visible to user.
     * @param legendColor should match what is seen in 3d.
     */
	@Override
    public void addColorString(String legendName, Object subEntity, Color legendColor) {
    	if (legendStringList.contains(legendName))
    		return;
    	legendStringList.add(legendName);
        legendMap.put(legendName, legendColor);
        colorMap.put(legendColor, legendName);
        labeledObjects.add(subEntity);
        if (legendModelListener != null)
        	legendModelListener.updateLegendModel();
    }

	private int colorOffset = 0;
	
    /**
     * This is how to put new values.
     * @param legendName visible to user.
     * @param r red component.
     * @param g green component.
     * @param b blue component.
     */
	@Override
    public void addColorString(String legendName, Object subEntity, float r, float g, float b) {
    	if (legendName != null) {
    		try {
    			//System.out.println("Adding color String: " + legendName);
    	        Color legendColor = new Color(r, g, b);
    	        addColorString(legendName, subEntity, legendColor);
				idToEntity.put(colorOffset++, subEntity);
    		} catch (RuntimeException rte) {
    			// Can happen if bad color range given.
    			rte.printStackTrace();
    		}
    	}
    }

    /**
     * All sub entities.
     */
	@Override
    public List<Object> getModels() {
    	return labeledObjects;
    }

    /**
     * All colors.
     */
	@Override
    public List<Color> getLegendColors() {
        return new ArrayList<>(legendMap.values());
    }

    /**
     * All user-visible strings.
     */
	@Override
    public List<String> getLegendStrings() {
        return legendStringList;
    }

    /**
     * Given a string, get its associated color.
     * @param s a user-visible string assoc with color.
     */
	@Override
    public Color getColorForString(String s) {
        return (Color)legendMap.get(s);
    }

    /**
     * Given a color tell its user-visible string.
     * @param c color to "tell about".
     */
	@Override
    public String getStringForColor(Color c) {
        return (String)colorMap.get(c);
    }

	/**
	 * Add a listener for cases where something in the model has been selected.
	 *
	 * @param listener what to tell when it happens.
	 */
	@Override
	public synchronized void addLegendSelectionListener(LegendSelectionListener listener) {
		legendSelectionListeners.add(listener);
	}

    /**
     * Allows addition of the (one) listener, to changes.
     * @param lml external object interested in hearing about changes.
     */
    public void addListener(LegendModelListener lml) {
    	this.legendModelListener = lml;
    	legendModelListener.updateLegendModel();
    }

	public Object getSubEntity(Integer id) {
		return idToEntity.get(id);
	}
	
	@Override
	public void clear() {
		colorMap.clear();
		legendMap.clear();
		legendStringList.clear();
		labeledObjects.clear();
	}

	@Override
	public void selectModel(Object model) {
		if (model != null) {
			for (int i = 0; i < legendSelectionListeners.size(); i++) {
				LegendSelectionListener nextListener = legendSelectionListeners.get(i);
				nextListener.selected(model);
			}
		}
	}

	@Override
	public void setSelectionColor(float[] selectionColor) {
		this.selectionColor = selectionColor;
	}

	@Override
	public float[] getSelectionColor() {
		return selectionColor;
	}
}