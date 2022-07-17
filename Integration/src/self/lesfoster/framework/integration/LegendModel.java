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
/**
 * Legend Model
 * Created on Apr 9, 2005
 */
package self.lesfoster.framework.integration;

import java.awt.Color;
import java.util.*;

/**
 * Interface to allow representation of "color legend", to the view.
 *
 * @author Leslie L Foster
 */
public interface LegendModel {

    /**
     * All colors.
     */
    List getLegendColors();

    /**
     * All user-visible strings.
     */
    List<String> getLegendStrings();

    /**
     * All models.
     */
    List<Object> getModels();

    /**
     * Given a string, get its associated color.
     *
     * @param s a user-visible string assoc with color.
     */
    Color getColorForString(String s);

    /**
     * Given a color tell its user-visible string.
     *
     * @param c color to "tell about".
     */
    String getStringForColor(Color c);

    /**
     * Assigns color to specific object of interest.
     *
     * @param legendName how called
     * @param object what to label
     * @param r red component
     * @param g green component
     * @param b blue component
     */
    void addColorString(String legendName, Object object, float r, float g, float b);

    /**
     * Add a color mapped to specific object of interest.
     *
     * @param legendName how called
     * @param object what to label
     * @param legendColor color given
     */
    void addColorString(String legendName, Object object, Color legendColor);

    /**
     * Adds a listener to changes on this model.
     *
     * @param l whole model is changed.
     */
    void addListener(LegendModelListener l);

    /**
     * Register for selections within legend model.
     *
     * @param l listener to hear about selections.
     */
    void addLegendSelectionListener(LegendSelectionListener l);

    /**
     * Trigger the selection event: select something here.
     *
     * @param model got selected
     */
    void selectModel(Object model);

    /**
     * Allow programmatic setting of the selection's color.
     *
     * @param selectionColor
     */
    void setSelectionColor(float[] selectionColor);

    float[] getSelectionColor();

    /**
     * Call this if the old context becomes meaningless, or at dispose.
     */
    void clear();
}
