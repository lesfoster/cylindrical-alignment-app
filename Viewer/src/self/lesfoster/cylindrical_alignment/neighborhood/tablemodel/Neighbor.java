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

import java.util.List;

/**
 * One item in a neighborhood of clustered differing residues.
 * This should remain immutable.
 *
 * @author lesfo
 */
public class Neighbor {
    private final int residueQueryPos;
    private final List<String> residueIds;
    
    public Neighbor(int residueQueryPos, List<String> residueIds) {
        this.residueQueryPos = residueQueryPos;
        this.residueIds = residueIds;
    }
    
    public int getResidueQueryPos() {
        return residueQueryPos;
    }
    
    public List<String> getResidueIds() {
        return residueIds;
    }
    
    public int getDepth() {
        return residueIds.size();
    }
}

