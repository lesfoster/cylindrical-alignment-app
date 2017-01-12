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



/*
 * Table model for display of selection to table.
 * Created on Sep 21, 2004
 */
package self.lesfoster.cylindrical_alignment.inspector.table_model;

import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import javafx.application.Platform;
import javax.swing.SwingUtilities;

import javax.swing.table.AbstractTableModel;

/**
 * Table model builds table out of info about a blast hit from the BioJava package.
 * @author FosterLL
 */
public class SubHitTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = -1;

    private List<String> names = new ArrayList<>();
    private List<String> values = new ArrayList<>();

	/** Can see the model data on initialization-- or not. */
	public SubHitTableModel(Map<String, String> properties) {
		setModelInfo(properties);
	}

	/** Re-populate model info.  Empty model if null map. */
	public synchronized void setModelInfo(Map properties) {
        // Don't adjust model unless some table (at least) wants to know.
        if (this.getTableModelListeners().length == 0)
            return;
		if (properties == null)
			return;

        // Reset the model content.
        names.clear();
        values.clear();

    	for (Iterator it = properties.keySet().iterator(); it.hasNext(); ) {
    		Object nextKey = it.next();
    		names.add(nextKey.toString());
    		values.add(properties.get(nextKey).toString());
    	}
		Platform.runLater(() -> {
	        fireTableDataChanged();
		});
	}

	/** Return a header name for the column. */
	public String getColumnName(int col) {
	    if (col == 0)
	    	return "Property";
	    else if (col == 1)
	    	return "Value";
	    else
	    	return null;
	}
	/** How many columns here? */
    public int getColumnCount() {
		return 2;
	}
    
    /** Never editable. */
    public boolean isCellEditable(int col, int row) {
    	return false;
    }

    /** How long is this table? */
    public synchronized int getRowCount() {
    	if (names == null)
    		return 0;
    	return names.size();
    }

	/** Override the method from parent. */
	public synchronized Object getValueAt(int row, int column) {
		if (row < 0 || row > (names.size() - 1))
			return "";
		else if (column == 0) {
			return names.get(row);
		}
		else if (column == 1) {
			return values.get(row);
		}
		else {
			return null;
		}
	}
}
