/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.inspector.table;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

/**
 * Renderer to take very long property values into account.
 *
 * @author Leslie L Foster
 */
public class TextAreaRenderer implements TableCellRenderer {

	private final JTextArea area = new JTextArea();
	private final Color gColor = new Color(225, 225, 225);

	@Override
	public Component getTableCellRendererComponent(JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column) {
		String valueStr = value.toString();
		area.setText(valueStr);
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		area.setBorder(new EmptyBorder(0, 0, 0, 0));
		if (row % 2 == 0) {
			area.setBackground(Color.WHITE);
		} else {
			area.setBackground(gColor);
		}
		return area;
	}
}
