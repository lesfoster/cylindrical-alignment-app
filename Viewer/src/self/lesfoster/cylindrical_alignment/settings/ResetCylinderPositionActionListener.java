package self.lesfoster.cylindrical_alignment.settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import self.lesfoster.cylindrical_alignment.effector.CylinderPositioningEffector;

/**
 * Created by IntelliJ IDEA.
 * User: Leslie L Foster
 * Date: 5/12/12
 * Time: 1:02 AM
 *
 * This listener connects the button click to the position re-setting affector.
 */
public class ResetCylinderPositionActionListener implements ActionListener {
    private CylinderPositioningEffector cylinderPositioningAffector;
    public ResetCylinderPositionActionListener(CylinderPositioningEffector cylinderPositioningAffector) {
        this.cylinderPositioningAffector = cylinderPositioningAffector;
    }
    public void actionPerformed(ActionEvent ae) {
        cylinderPositioningAffector.setDefaultCylinderPosition();
    }
}
