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
package self.lesfoster.cylindrical_alignment.neighborhood.tablemodel;

import java.util.Collections;
import java.util.List;
import javafx.application.Platform;
import javax.swing.table.AbstractTableModel;

/**
 * Data to represent neighborhoods in a table.
 *
 * @author lesfo
 */
public class NeighborhoodTableModel extends AbstractTableModel {
    private static final long serialVersionUID = -1;

    private static final int COL_COUNT = 4;
    private static final String[] COL_NAMES = {
        "Query Offset", "Count", "Relative Position", "Type"
    };

    private List<Neighbor> neighbors;
    private int selectedPos;
    
    public NeighborhoodTableModel() {
        neighbors = Collections.EMPTY_LIST;
        selectedPos = 0;
    }
    public NeighborhoodTableModel(List<Neighbor> neighbors, int selectedPos) {
        this.neighbors = neighbors;
        this.selectedPos = selectedPos;
    }
    
    public void setNeighbors(List<Neighbor> neighbors, int selectedPos) {
        // Don't adjust model unless some table (at least) wants to know.
        if (this.getTableModelListeners().length == 0) {
            return;
        }

        this.neighbors = neighbors;
        this.selectedPos = selectedPos;
        Platform.runLater(() -> {
            fireTableDataChanged();
        });
    }

    @Override
    public int getRowCount() {
        return neighbors.size();
    }

    @Override
    public int getColumnCount() {
        return COL_COUNT;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        final int residueQueryPos = neighbors.get(rowIndex).getResidueQueryPos();
        if (rowIndex >= neighbors.size() || columnIndex >= COL_COUNT) {
            return null;
        }
        switch (columnIndex) {
            case 0:
                return residueQueryPos;
            case 1:
                return neighbors.get(rowIndex).getDepth();
            case 2:
                if (residueQueryPos == selectedPos) {
                    return "selected";
                } else if (rowIndex == 0) {
                    return "leading";
                } else if (rowIndex == neighbors.size() - 1) {
                    return "trailing";
                } else {
                    return "interior";
                }
            case 3:
                return "indel";
        }
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public String getColumnName(int column) {
        if (column > COL_COUNT  ||  column < 0) {
            return null;
        }
        return COL_NAMES[column];
    }
}
