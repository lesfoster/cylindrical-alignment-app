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
import javax.swing.AbstractAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import self.lesfoster.cylindrical_alignment.components.SearchPopup;
import self.lesfoster.cylindrical_alignment.utils.GuiUtils;

/**
 * This action has been carried out in past, but the target server is defunct.
 * In future, upon request, a working, available server might be launched.
 *
 * @author Leslie L Foster
 */
@ActionID(
        category = "File",
        id = "self.lesfoster.cylindrical_alignment.model.action.ServerOpener"
)
@ActionRegistration(
        displayName = "#CTL_ServerOpener",
        lazy = false
)
@ActionReference(path = "Menu/File", position = 1550)
@Messages("CTL_ServerOpener=Fetch from Server")
public final class ServerOpener extends AbstractAction {

    public static final int POPUP_HEIGHT = 600;
    public static final int POPUP_WID = 800;

    public ServerOpener() {
        super();
        super.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SearchPopup searchPopup = new SearchPopup(POPUP_WID, POPUP_HEIGHT);
        GuiUtils.centerLocation(searchPopup, POPUP_WID, POPUP_HEIGHT);
        searchPopup.setVisible(true);
    }

}
