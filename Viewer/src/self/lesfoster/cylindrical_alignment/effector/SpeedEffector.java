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
 * Speed Affector.
 * 
 * Created on Jan 30, 2005
 */
package self.lesfoster.cylindrical_alignment.effector;

/**
 * Interface to expose controls for speed of cylinder rotation.
 *  
 * @author Leslie L. Foster
 */
public interface SpeedEffector extends Effector {
	public static final int HALTED_DURATION = -1;
	public static final int FAST_SPEED_DURATION = 3000;
    public static final int SLOW_SPEED_DURATION = 50000;
	public static final int INITIAL_SPEED_DURATION = (FAST_SPEED_DURATION + SLOW_SPEED_DURATION) / 2; // TEMP

	void setSlow();
	void setFast();
	void setImmobile();
	void setDuration(int duration);
}

