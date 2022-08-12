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
package self.lesfoster.cylindrical_alignment.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import self.lesfoster.cylindrical_alignment.effector.Effected;
import self.lesfoster.cylindrical_alignment.effector.Effector;
import self.lesfoster.cylindrical_alignment.effector.HelpEffector;

@ActionID(
        category = "Help",
        id = "self.lesfoster.cylindrical_alignment.action.HelpAction"
)
@ActionRegistration(
        displayName = "#CTL_HelpAction"
)
@ActionReference(path = "Menu/Help", position = 1300, separatorAfter = 1350)
@Messages("CTL_HelpAction=About Input")
public final class HelpAboutInputAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        //final Lookup actionsGlobalContext = Utilities.actionsGlobalContext();
        //Effected effected = actionsGlobalContext.lookup(Effected.class);
        // Get the effected, and get its help effector.
        Effected effected = Lookup.getDefault().lookup(Effected.class);
        if (effected != null) {
            for (Effector effector : effected.getEffectors()) {
                if (effector instanceof HelpEffector) {
                    HelpEffector he = (HelpEffector) effector;
                    he.showInputData();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Sorry, no information is available");
        }
    }
}
