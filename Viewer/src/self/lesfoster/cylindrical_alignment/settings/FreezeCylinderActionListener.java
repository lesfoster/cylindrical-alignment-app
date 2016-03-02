package self.lesfoster.cylindrical_alignment.settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import self.lesfoster.cylindrical_alignment.effector.CylinderPositioningEffector;

/**
 * Created by IntelliJ IDEA.
 * User: Leslie L Foster
 * Date: 4/24/12
 * Time: 12:23 AM
 *
 * This listener connects the button click to the freezing affector.
 */
public class FreezeCylinderActionListener implements ActionListener {
    private CylinderPositioningEffector cylinderPositioningAffector;
    public FreezeCylinderActionListener( CylinderPositioningEffector cylinderPositioningAffector ) {
        this.cylinderPositioningAffector = cylinderPositioningAffector;
    }
    public void actionPerformed(ActionEvent ae) {
        AbstractButton aButton = (AbstractButton) ae.getSource();
        boolean selected = aButton.getModel().isSelected();
        cylinderPositioningAffector.setFrozenMouseRotator(selected);
    }
}
