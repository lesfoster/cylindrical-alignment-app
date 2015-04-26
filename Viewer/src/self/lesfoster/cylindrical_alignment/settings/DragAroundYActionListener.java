package self.lesfoster.cylindrical_alignment.settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import self.lesfoster.cylindrical_alignment.affector.CylinderPositioningAffector;

/**
 * Created by IntelliJ IDEA.
 * User: Leslie L Foster
 * Date: 4/24/12
 * Time: 12:19 AM
 *
 * This listener will hook a button to its affector, for fixing drag around the vertical axis only.
 */
public class DragAroundYActionListener implements ActionListener {
    private CylinderPositioningAffector cylinderPositioningAffector;

    public DragAroundYActionListener( CylinderPositioningAffector cylinderPositioningAffector ) {
        this.cylinderPositioningAffector = cylinderPositioningAffector;
    }

    public void actionPerformed(ActionEvent ae) {
        AbstractButton aButton = (AbstractButton) ae.getSource();
        boolean selected = aButton.getModel().isSelected();
        cylinderPositioningAffector.setXOnlyMouseRotator(selected);
    }
}
