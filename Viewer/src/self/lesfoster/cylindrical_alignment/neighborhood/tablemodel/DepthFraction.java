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

public class DepthFraction {
    private final Integer depth;
    private final Integer max;
    private final String ident;
    public DepthFraction(Integer depth, Integer max) {
        this.depth = depth;
        this.max = max;
        this.ident = String.format("%d: %4.3f%%", depth, 100.0 * ((double)depth/(double)max));
    }

    public double getFraction() {
        return (double)depth / (double)max;
    }

    public Integer getDepth() {
        return depth;
    }

    @Override
    public String toString() {
        return ident;
    }
}
