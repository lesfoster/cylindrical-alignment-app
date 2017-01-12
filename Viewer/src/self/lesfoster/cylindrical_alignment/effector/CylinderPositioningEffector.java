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


package self.lesfoster.cylindrical_alignment.effector;

/*
 * Define contract for a set of hooks to be implemented to change cylinder positioning
 * characteristics.
 */
public interface CylinderPositioningEffector extends Effector {
	public static final int FAST_DRAG = 20;
	public static final int SLOW_DRAG = 1;
	public static final double DEFAULT_MOUSE_ROTATE_FACTOR = 0.05;
	
	void setFrozenMouseRotator( boolean isFrozen );
	void setMouseRotatorFactor( double factor );
	void setYOnlyMouseRotator( boolean isXOnly );
	void setDefaultCylinderPosition();
}
