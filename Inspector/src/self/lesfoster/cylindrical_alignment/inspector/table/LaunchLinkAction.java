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
    private final String value;

    public LaunchLinkAction(String value) {
        super("Go There");
        this.value = value;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        // Goes to URL.
        try {
            BrowserControl.displayURL(value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
