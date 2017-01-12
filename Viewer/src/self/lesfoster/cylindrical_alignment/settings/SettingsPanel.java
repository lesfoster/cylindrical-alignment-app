package self.lesfoster.cylindrical_alignment.settings;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import self.lesfoster.cylindrical_alignment.effector.CylinderPositioningEffector;
import self.lesfoster.cylindrical_alignment.effector.SettingsEffector;
import self.lesfoster.cylindrical_alignment.effector.SpeedEffector;

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


public class SettingsPanel extends JPanel {
    private SpeedEffector speedAffector;
    private SettingsEffector settingsAffector;
    private CylinderPositioningEffector positioningAffector;

    // These are the controls for the panel.

    /**
     * Takes the affectors: these can simply have messages called upon them to make things happen.
     *
     * @param speedAffector controller for speed of cylinder spin.
     * @param settingsAffector various simple settings.
     * @param positioningAffector how the rotation-by-mouse takes place, etc.
     */
    public SettingsPanel(
            SpeedEffector speedAffector,
            SettingsEffector settingsAffector,
            CylinderPositioningEffector positioningAffector) {
        this.speedAffector = speedAffector;
        this.settingsAffector = settingsAffector;
        this.positioningAffector = positioningAffector;
    }

    //todo look at invoke and wait: could that be done better by caller?
    /** Sets up the GUI. Callable here from client, to allow the setup burden to be lost only at use-time. */
    public void initGui() throws InterruptedException, InvocationTargetException{
        SwingUtilities.invokeAndWait( new Runnable() {
            public void run() {

            }
        });
    }
}
