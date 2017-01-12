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
//import org.openide.util.Utilities;
import self.lesfoster.cylindrical_alignment.effector.Effected;
import self.lesfoster.cylindrical_alignment.effector.Effector;
import self.lesfoster.cylindrical_alignment.effector.HelpEffector;

@ActionID(
		category = "Help",
		id = "self.lesfoster.cylindrical_alignment.action.HelpAboutApp"
)
@ActionRegistration(
		displayName = "#CTL_HelpAboutApp"
)
@ActionReference(path = "Menu/Help", position = 350)
@Messages("CTL_HelpAboutApp=Application Help")
public final class HelpAboutApp implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		//Effected effected = Utilities.actionsGlobalContext().lookup(Effected.class);
		Effected effected = Lookup.getDefault().lookup(Effected.class);
		if (effected == null) {
			JOptionPane.showMessageDialog(null, "Sorry, no information is available.");			
		}
		else {
			for (Effector effector : effected.getEffectors()) {
				if (effector instanceof HelpEffector) {
					HelpEffector he = (HelpEffector) effector;
					he.showApplicationHelp();
				}
			}
		}
	}
}
