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
import static java.util.stream.Collectors.joining;
import self.lesfoster.framework.integration.Polymorphism;

/**
 * One item in a neighborhood of clustered differing residues.
 * This should remain immutable.
 *
 * @author lesfo
 */
public class Neighbor {
    private final int residueQueryPos;
    private final List<String> residueIds;
    private final List<Polymorphism.Type> types;
    
    public Neighbor(int residueQueryPos, List<String> residueIds, List<Polymorphism.Type> types) {
        this.residueQueryPos = residueQueryPos;
        this.residueIds = residueIds;
        this.types = types;
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

    public List<Polymorphism.Type> getTypes() {
        return types;
    }
    
    public String getTypeDesc() {
        return types.stream().map(t -> t.toString()).collect(joining(","));
    }
}

