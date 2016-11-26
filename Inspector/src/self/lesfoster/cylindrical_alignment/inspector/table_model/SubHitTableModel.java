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
 * Table model for display of selection to table.
 * Created on Sep 21, 2004
 */
package self.lesfoster.cylindrical_alignment.inspector.table_model;

import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
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
		if (SwingUtilities.isEventDispatchThread()) {
	        fireTableDataChanged();
		}
		else {
			SwingUtilities.invokeLater(() -> {
				fireTableDataChanged();
			});
		}
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
