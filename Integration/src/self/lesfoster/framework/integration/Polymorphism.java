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
package self.lesfoster.framework.integration;

/**
 * Holds metadata about a differing residue or contiguous series of residues
 * when compared against the query.
 *
 * @author lesfo
 */
public class Polymorphism {
    public enum Type {
        Insertion, Deletion, InDel
    }
    private final int startPos;
    private final int endPos; // May be same as start.
    private final Type type;
    public Polymorphism(int startPos, int endPos, Type type) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.type = type;
    }
    
    public int getStart() {
        return startPos;
    }
    public int getEnd() {
        return endPos;
    }
    public Type getType() {
        return type;
    }
}
