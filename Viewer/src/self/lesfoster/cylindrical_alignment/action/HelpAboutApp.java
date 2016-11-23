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
