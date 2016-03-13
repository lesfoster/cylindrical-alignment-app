/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.inspector.table;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 * An action to deal with launching URLs found in property values.
 *
 * @author lfoster
 */
public class LaunchLinkAction extends AbstractAction {

	private static final long serialVersionUID = -1L;
	private String value;

	public LaunchLinkAction(String value) {
		super("Go There");
		this.value = value;
	}

	public void actionPerformed(ActionEvent ae) {
		// Goes to URL.
		try {
			BrowserControl.displayURL(value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
