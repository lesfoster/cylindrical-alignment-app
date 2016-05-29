/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.model.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.SwingWorker;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import self.lesfoster.cylindrical_alignment.model.server_interaction.ServerInteractor;

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
	private ServerInteractor interactor = new ServerInteractor();

	@Override
	public void actionPerformed(ActionEvent e) {
		final String id = "2";
		interactor.fetch(id);
	}

}
