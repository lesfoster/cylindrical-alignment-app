/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
