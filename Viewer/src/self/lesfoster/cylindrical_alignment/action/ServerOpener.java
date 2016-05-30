/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import self.lesfoster.cylindrical_alignment.components.SearchPopup;
import self.lesfoster.cylindrical_alignment.utils.GuiUtils;

@ActionID(
		category = "File",
		id = "self.lesfoster.cylindrical_alignment.model.action.ServerOpener"
)
@ActionRegistration(
		displayName = "#CTL_ServerOpener"
)
@ActionReference(path = "Menu/File", position = 1550)
@Messages("CTL_ServerOpener=Fetch from Server")
public final class ServerOpener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		SearchPopup searchPopup = new SearchPopup();
		GuiUtils.centerLocation(searchPopup, 800, 500);
		searchPopup.setVisible(true);
	}

}
