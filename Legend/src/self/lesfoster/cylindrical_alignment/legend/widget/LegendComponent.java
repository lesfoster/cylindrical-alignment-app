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
 * Legend Component
 * Created on Apr 10, 2005
 * Updated on Mar 3, 2016
 */
package self.lesfoster.cylindrical_alignment.legend.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JComponent;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import self.lesfoster.framework.integration.LegendModel;
import self.lesfoster.framework.integration.LegendModelListener;
import self.lesfoster.framework.integration.SelectedObjectWrapper;
import self.lesfoster.framework.integration.SelectionModel;

/**
 * Panel to show the legend of string to color, to aid the user in understanding what
 * the colors mean.
 * @author Leslie L Foster
 */
public class LegendComponent extends JPanel implements LegendModelListener, Lookup.Provider {
	private static final long serialVersionUID = -1L;
	
	public static final int MINIMUM_WIDTH = 150;
	public static final int PREFERRED_WIDTH = 150;
	private static final int HORIZ_OFFSET = 2;
	private static final int VERT_OFFSET = 10;

    private int height = -1;
	private LegendModel legendModel;
	private InstanceContent instanceContent;

	private String externallySelectedId;
	private Object externallySelectedObject;
	private Lookup objectLookup;
	private SelectedObjectWrapper wrapper;
	
	private Map<Integer,Object> legendNumberToModel = new HashMap<>();

	public LegendComponent() {		
		wrapper = new SelectedObjectWrapper();
		instanceContent = new InstanceContent();
		instanceContent.add(wrapper);
		objectLookup = new AbstractLookup(instanceContent);
	}
	
	/**
	 * Construct with a model that contains mappings between colors and strings.
	 * @param model to read color/explanation from.
	 */
	public LegendComponent(LegendModel model) {
		this();
		setModel(model);
	}
	
	public final void setModel(LegendModel model) {

		legendModel = model;
		model.addListener(this);
		setBackground(Color.BLACK);   // Color background like java3D default.
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				// Must figure out what was pressed.
				LegendComponent component = (LegendComponent) me.getSource();
				if (component == null) {
					return;
				}
				Point mousePoint = component.getMousePosition();
				if (mousePoint == null) {
					return;
				}
				int pointY = mousePoint.y;
				int heightOfFont = LegendComponent.this.getFont().getSize();
				int divisor = calcHeightOfOneLegendEntry(heightOfFont);
				int offsetPoint = pointY - VERT_OFFSET;
				// Check: within visible range.
				if (offsetPoint >= 0) {
				    int legendNumber = offsetPoint / divisor;
					System.out.println("Legend Number: " + legendNumber);

				    // Check: within extent of collection on screen (not
				    // within dead space below strings).
				    if (legendModel.getLegendStrings().size() > legendNumber) {
				        // Find the selected entity.
						Object selectedObject = legendNumberToModel.get(legendNumber);
						if (selectedObject != null) {
							legendModel.selectModel(selectedObject);
							// Push this to the lookup.
							instanceContent.remove(wrapper);
							instanceContent.add(wrapper.setSelectedObject(selectedObject));
							externallySelectedObject = selectedObject;
							LegendComponent.this.validate();
							LegendComponent.this.repaint();
						}
				    }
				}
			}
		});
		
		// Establish reaction to selection by other component.
		SelectionModel selectionModel = SelectionModel.getSelectionModel();
		selectionModel.addListener((obj) -> {
			externallySelectedId = obj.toString();
			externallySelectedObject = selectionModel.getObjectForId(externallySelectedId);
			updateLegendModel();
		});

	}

	//==============================IMPLEMENTS Lookup.Provider
	@Override
	public Lookup getLookup() {
		return objectLookup;
	}

	/**
	 * Tells just how big we'd like to be ;-)
	 */
	@Override
	public Dimension getPreferredSize() {
//		if (height == -1) {
		    height = calcHeight();
//		}
		return new Dimension(calculateMaxFontWidth(), height);
	}

	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	/**
	 * Hears events on the legend model, so it can repaint.
	 */
	@Override
	public void updateLegendModel() {
		invalidate();
		validate();
		repaint();
	}

	private int calcHeight() {
		int hightOfFont = this.getFont().getSize();
		int vertOffset = VERT_OFFSET;
		if (legendModel == null || legendModel.getLegendStrings() == null)
			return -1;
		vertOffset += legendModel.getLegendStrings().size() * calcHeightOfOneLegendEntry(hightOfFont);
		return vertOffset;	
	}

	private int calcHeightOfOneLegendEntry(int heightOfFont) {
		return (heightOfFont * 2) +  3;
	}

	/**
	 * This override to JComponent, will allow this component to paint itself as it likes.
	 * Here is where the work of presenting the mappings to the user will be done.
	 */
	@Override
	public void paintComponent(Graphics g) {		
		g.setColor(Color.BLACK);
		super.paintComponent(g);
		Dimension preferredSize = getPreferredSize();
		g.fillRect(0, 0, preferredSize.width, preferredSize.height);
		int heightOfFont = this.getFont().getSize();
		int vertOffset = VERT_OFFSET;
		if (legendModel == null || legendModel.getLegendStrings() == null)
			return;
		legendNumberToModel.clear();		
		for (int i = 0; i < legendModel.getLegendStrings().size(); i++) {
	    	Object legendModelObj = legendModel.getModels().get(i);
			legendNumberToModel.put(i, legendModelObj);
			//System.out.println(i + " maps to " + legendModelObj);

	    	vertOffset += heightOfFont;
			String nextString = (String)legendModel.getLegendStrings().get(i);
			g.setColor(Color.WHITE);
			g.drawString(nextString, HORIZ_OFFSET, vertOffset);

            vertOffset += 1;
            Color idColor = null; 
	    	if ( legendModelObj != null  &&  legendModelObj.equals( externallySelectedObject ) ) {
	    		idColor = SelectionModel.SELECTION_COLOR_2D;
	    	}
	    	else {
	    		idColor = legendModel.getColorForString(nextString);
	    	}
			
            g.setColor( idColor );
			g.fillRect(HORIZ_OFFSET + 10, vertOffset, calculateMaxFontWidth() * 2 / 3, heightOfFont);
			vertOffset += heightOfFont + 2;

		}
		height = vertOffset;
	}

	/**
	 * Override to force avoidance of component-adding.
	 * @param c any old component some errant user might wish to add.
	 */
	public void addComponent(JComponent c) {
		throw new UnsupportedOperationException("Please do not add components to this object");
	}

	/**
	 * Can be called to release resources.
	 */
	public void destroy() {
		legendModel = null;
	}

	/**
	 * Allows calculation of the widest required string, for this pane.
	 * @return screen width required to show longest string in legend.
	 */
	private int calculateMaxFontWidth() {
		int maxWidth = 0;
		if (legendModel == null) {
			return maxWidth;
		}
		final FontMetrics fontMetrics = this.getFontMetrics(this.getFont());
		if (fontMetrics == null) {
			java.util.logging.Logger.getLogger("FontMetricsError").warning("Unable to obtain font metrics to calculate max font width.");
			return maxWidth;
		}
		for (int i = 0; i < legendModel.getLegendStrings().size(); i++) {
			String nextLegend = (String)legendModel.getLegendStrings().get(i);
			int width = fontMetrics.charsWidth(nextLegend.toCharArray(), 0, nextLegend.length());
			if (width > maxWidth)
				maxWidth = width;
		}
		return maxWidth + HORIZ_OFFSET + 2;
	}

}

