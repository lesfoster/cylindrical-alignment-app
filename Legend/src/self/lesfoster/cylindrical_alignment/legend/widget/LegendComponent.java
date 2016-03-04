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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JComponent;
import java.util.List;
import java.util.ArrayList;
import self.lesfoster.framework.integration.LegendModel;
import self.lesfoster.framework.integration.LegendModelListener;
import self.lesfoster.framework.integration.LegendSelectionListener;
import self.lesfoster.framework.integration.SelectionModel;
import self.lesfoster.framework.integration.SelectionModelListener;

/**
 * Panel to show the legend of string to color, to aid the user in understanding what
 * the colors mean.
 * @author Leslie L Foster
 */
public class LegendComponent extends JPanel implements LegendModelListener {
	private static final long serialVersionUID = -1L;
	
	public static final int MINIMUM_WIDTH = 150;
	public static final int PREFERRED_WIDTH = 150;
	private static final int HORIZ_OFFSET = 2;
	private static final int VERT_OFFSET = 10;

    private int height = -1;
	private LegendModel legendModel;
	private Object externallySelectedSubEntity;
	private List<LegendSelectionListener> legendSelectionListeners =
		new ArrayList<>();

	/**
	 * Construct with a model that contains mappings between colors and strings.
	 * @param model to read color/explanation from.
	 */
	public LegendComponent(LegendModel model) {
		legendModel = model;
		model.addListener(this);
		setBackground(Color.BLACK);   // Color background like java3D default.
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				// Must figure out what was pressed.
				LegendComponent component = (LegendComponent)me.getSource();
				if (component == null)
					return;
	            int pointY = component.getMousePosition().y;
				int heightOfFont = LegendComponent.this.getFont().getSize();
				int divisor = calcHeightOfOneLegendEntry(heightOfFont);
				int offsetPoint = pointY - VERT_OFFSET;
				// Check: within visible range.
				if (offsetPoint >= 0) {
				    int legendNumber = offsetPoint / divisor;

				    // Check: within extent of collection on screen (not
				    // within dead space below strings).
				    if (legendModel.getLegendStrings().size() > legendNumber) {
				        // Find the selected entity.
				    	Object subEntity = legendModel.getModels().get(legendNumber);
				    	synchronized (this) {
				    		for (int i = 0; i < legendSelectionListeners.size(); i++) {
				    			LegendSelectionListener nextListener = legendSelectionListeners.get(i);
				    			if (subEntity != null)
				    			    nextListener.selected(subEntity);
				    		}
				    	}
				    }
				}
			}
		});
		
		// Establish reaction to selection by other component.
		SelectionModel selectionModel = SelectionModel.getSelectionModel();
		selectionModel.addListener( new SelectionModelListener() {
			public void selected( Object obj ) {
				externallySelectedSubEntity = obj;
				updateLegendModel();
			}
		});
	}

	/**
	 * Add a listener for cases where something in the model has been selected.
	 * 
	 * @param listener what to tell when it happens.
	 */
	public synchronized void addListener(LegendSelectionListener listener) {
		legendSelectionListeners.add(listener);
	}

	/**
	 * Tells just how big we'd like to be ;-)
	 */
	public Dimension getPreferredSize() {
		if (height == -1) {
		    height = calcHeight();
		}
		return new Dimension(calculateMaxFontWidth(), height);
	}

	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	/**
	 * Hears events on the legend model, so it can repaint.
	 */
	public void updateLegendModel() {
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
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		super.paintComponent(g);
		Dimension preferredSize = getPreferredSize();
		g.fillRect(0, 0, preferredSize.width, preferredSize.height);
		int heightOfFont = this.getFont().getSize();
		int vertOffset = VERT_OFFSET;
		if (legendModel == null || legendModel.getLegendStrings() == null)
			return;
		for (int i = 0; i < legendModel.getLegendStrings().size(); i++) {
	    	Object subEntity = legendModel.getModels().get(i);

	    	vertOffset += heightOfFont;
			String nextString = (String)legendModel.getLegendStrings().get(i);
			g.setColor(Color.WHITE);
			g.drawString(nextString, HORIZ_OFFSET, vertOffset);

            vertOffset += 1;
            Color idColor = null; 
	    	if ( subEntity != null  &&  subEntity.equals( externallySelectedSubEntity ) ) {
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
		legendSelectionListeners.clear();
	}

	/**
	 * Allows calculation of the widest required string, for this pane.
	 * @return screen width required to show longest string in legend.
	 */
	private int calculateMaxFontWidth() {
		int maxWidth = 0;
		for (int i = 0; i < legendModel.getLegendStrings().size(); i++) {
			String nextLegend = (String)legendModel.getLegendStrings().get(i);
			int width = this.getFontMetrics(this.getFont()).charsWidth(nextLegend.toCharArray(), 0, nextLegend.length());
			if (width > maxWidth)
				maxWidth = width;
		}
		return maxWidth + HORIZ_OFFSET + 2;
	}

}

