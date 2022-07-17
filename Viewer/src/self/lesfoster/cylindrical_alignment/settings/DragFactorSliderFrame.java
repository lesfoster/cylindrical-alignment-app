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
package self.lesfoster.cylindrical_alignment.settings;

/*
 * Spin Slider Frame
 * 
 * Created on Feb 2, 2006
 */
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import self.lesfoster.cylindrical_alignment.effector.CylinderPositioningEffector;
import self.lesfoster.cylindrical_alignment.utils.GuiUtils;

/**
 * Special control to popup and let the user control the spin rate of the
 * cylinder.
 *
 * @author Leslie L Foster
 */
public class DragFactorSliderFrame extends JFrame {

    private static final long serialVersionUID = -1;

    private static final int DFSF_WIDTH = 300;
    private static final int DFSF_HEIGHT = 100;

    /**
     * Will keep the instance hidden here
     */
    private static DragFactorSliderFrame instance;

    /**
     * Private constructor. Forces singleton.
     *
     * @param affector what to call to change the speed of spin.
     */
    private DragFactorSliderFrame(CylinderPositioningEffector affector) {
        JPanel panel = new DragFactorSliderPanel(affector);
        getContentPane().add(panel, BorderLayout.CENTER);

        GuiUtils.upperLeftLocation(this, DFSF_WIDTH, DFSF_HEIGHT);
        GuiUtils.setIcon(this);
    }

    /**
     * Return only possible instance.
     *
     * @return the instance. Create here if needed.
     * @param affector what to call to change the rate of cyl drag.
     */
    public static DragFactorSliderFrame getInstance(CylinderPositioningEffector affector) {
        if (affector == null) {
            throw new RuntimeException("Need instance of affector");
        }

        if (instance == null) {
            instance = new DragFactorSliderFrame(affector);
        }
        return instance;
    }

}
