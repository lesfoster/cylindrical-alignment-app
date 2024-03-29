package self.lesfoster.cylindrical_alignment.settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import self.lesfoster.cylindrical_alignment.effector.CylinderPositioningEffector;

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
public class DragAroundYActionListener implements ActionListener {

    private final CylinderPositioningEffector cylinderPositioningAffector;

    public DragAroundYActionListener(CylinderPositioningEffector cylinderPositioningAffector) {
        this.cylinderPositioningAffector = cylinderPositioningAffector;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        AbstractButton aButton = (AbstractButton) ae.getSource();
        boolean selected = aButton.getModel().isSelected();
        cylinderPositioningAffector.setYOnlyMouseRotator(selected);
    }
}
