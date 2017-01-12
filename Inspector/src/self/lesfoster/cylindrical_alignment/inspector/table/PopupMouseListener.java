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


package self.lesfoster.cylindrical_alignment.inspector.table;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

/**
 * Mouse listener to listen for popup events.
 *
 * @author lfoster
 */
public class PopupMouseListener extends MouseAdapter {

	private boolean isLinux() {
		return (System.getProperty("os.name").equalsIgnoreCase("Linux"));
	}

	private boolean isMac() {
		return (System.getProperty("os.name").toLowerCase().contains("mac"));
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		handleEvent(me);
	}
	
	public void handleEvent(MouseEvent me) {
		if (me.isPopupTrigger() || isLinux()  ||  isMac()) {
			JTable table = (JTable) me.getComponent();
			String[] values = getValueUnderClick(table, me.getX(), me.getY());
			JPopupMenu menu = null;
			if (isLongString(values[1])) {
				menu = new JPopupMenu();
				ViewStringAction action = new ViewStringAction(values);
				JMenuItem item = new JMenuItem(action);
				menu.add(item);
			}
			if (isLink(values[1])) {
				if (menu == null) {
					menu = new JPopupMenu();
				}
				LaunchLinkAction action = new LaunchLinkAction(values[1]);
				JMenuItem item = new JMenuItem(action);
				menu.add(item);

			}
			if (menu != null) {
				menu.setEnabled(true);
				menu.show(me.getComponent(), me.getX(), me.getY());

			}
		}
	}
	
	/**
	 * Tells what is in table, where user clicked.
	 */
	private String[] getValueUnderClick(JTable table, int x, int y) {
		Point p = new Point(x, y);
		int row = table.rowAtPoint(p);
		int col = table.columnAtPoint(p);
		String[] returnArray = new String[2];
		if (table.getValueAt(row, col) != null) {
			String nameString = table.getValueAt(row, 0).toString();
			String valString = table.getValueAt(row, 1).toString();
			returnArray[0] = nameString;
			returnArray[1] = valString;
		}
		return returnArray;
	}

	private boolean isLongString(String str) {
		return (str != null && str.length() > 5);
	}

	private boolean isLink(String str) {
		return str != null && str.trim().toLowerCase().startsWith("http://");
	}

}
