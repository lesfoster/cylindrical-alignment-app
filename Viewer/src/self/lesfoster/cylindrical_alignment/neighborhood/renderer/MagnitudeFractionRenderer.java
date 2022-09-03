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
package self.lesfoster.cylindrical_alignment.neighborhood.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import self.lesfoster.cylindrical_alignment.neighborhood.tablemodel.DepthFraction;

/**
 * Renderer to show graphically the fraction given, and display textually the 
 * magnitude provided in the incoming object.
 *
 * @author lesfo
 */
public class MagnitudeFractionRenderer implements TableCellRenderer {
    private final DefaultTableCellRenderer stdRenderer = new DefaultTableCellRenderer();

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (! (value instanceof DepthFraction)) {
            return stdRenderer;
        }
        return new GraphedLabel((DepthFraction) value);
    }

    private static class GraphedLabel extends JComponent {
        private final DepthFraction depthFraction;
        public GraphedLabel(DepthFraction depthFraction) {
            this.depthFraction = depthFraction;
            this.setToolTipText(toString());
        }
        
        @Override
        public void paint(Graphics graphics) {
            graphics.setColor(this.getBackground());
            graphics.clearRect(0, 0, this.getWidth(), this.getHeight());
            graphics.setColor(Color.pink);
            graphics.fillRect(0, 0, (int)((double)this.getWidth() * depthFraction.getFraction()), this.getHeight());
            graphics.setColor(this.getForeground());
            graphics.drawString(toString(), 0, this.getHeight() - 1);
        }
        
        @Override
        public final String toString() {            
            return String.format("%d: %5.4f", depthFraction.getDepth(), depthFraction.getFraction());
        }
    }
}
