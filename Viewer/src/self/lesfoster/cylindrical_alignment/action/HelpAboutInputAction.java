/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
			for (Effector effector: effected.getEffectors()) {
				if (effector instanceof HelpEffector) {
					HelpEffector he = (HelpEffector)effector;
					he.showInputData();
				}
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "Sorry, no information is available");
		}
	}
}
