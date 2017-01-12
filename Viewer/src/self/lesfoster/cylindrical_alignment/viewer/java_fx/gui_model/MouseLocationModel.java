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


package self.lesfoster.cylindrical_alignment.viewer.java_fx.gui_model;

/**
 * Holds mouse position and relative location.
 * @author Leslie L Foster
 */
public class MouseLocationModel {
	public static final double DEFAULT_Y_ANGLE = -35.0;

    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;
	
	/**
	 * @return the mousePosX
	 */
	public double getMousePosX() {
		return mousePosX;
	}

	/**
	 * @param mousePosX the mousePosX to set
	 */
	public void setMousePosX(double mousePosX) {
		this.mousePosX = mousePosX;
	}

	/**
	 * @return the mousePosY
	 */
	public double getMousePosY() {
		return mousePosY;
	}

	/**
	 * @param mousePosY the mousePosY to set
	 */
	public void setMousePosY(double mousePosY) {
		this.mousePosY = mousePosY;
	}

	/**
	 * @return the mouseOldX
	 */
	public double getMouseOldX() {
		return mouseOldX;
	}

	/**
	 * @param mouseOldX the mouseOldX to set
	 */
	public void setMouseOldX(double mouseOldX) {
		this.mouseOldX = mouseOldX;
	}

	/**
	 * @return the mouseOldY
	 */
	public double getMouseOldY() {
		return mouseOldY;
	}

	/**
	 * @param mouseOldY the mouseOldY to set
	 */
	public void setMouseOldY(double mouseOldY) {
		this.mouseOldY = mouseOldY;
	}

	/**
	 * @return the mouseDeltaX
	 */
	public double getMouseDeltaX() {
		return mouseDeltaX;
	}

	/**
	 * @param mouseDeltaX the mouseDeltaX to set
	 */
	public void setMouseDeltaX(double mouseDeltaX) {
		this.mouseDeltaX = mouseDeltaX;
	}

	/**
	 * @return the mouseDeltaY
	 */
	public double getMouseDeltaY() {
		return mouseDeltaY;
	}

	/**
	 * @param mouseDeltaY the mouseDeltaY to set
	 */
	public void setMouseDeltaY(double mouseDeltaY) {
		this.mouseDeltaY = mouseDeltaY;
	}
}
