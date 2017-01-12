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


package self.lesfoster.cylindrical_alignment.settings;

import self.lesfoster.cylindrical_alignment.utils.GuiUtils;

import javax.swing.*;
import java.awt.*;
import self.lesfoster.cylindrical_alignment.effector.SettingsEffector;

/**
 * Created by IntelliJ IDEA.
 * User: Leslie L Foster
 * Date: 12/7/11
 * Time: 11:46 PM
 * This popup will operate against an affector when the user enters a value.
 */
public class SelectionEnvelopDialog extends JDialog {
    private SettingsEffector settingsAffector;
    public SelectionEnvelopDialog( SettingsEffector settingsAffector ) {
        super();
        this.setTitle( "Selection Envelope" );
        this.settingsAffector = settingsAffector;
        init();
    }

    private void init() {
        GuiUtils.setupScreenRealestate(this, 200, 82);
        setResizable( false );
        getContentPane().setLayout( new BorderLayout() );
        JPanel selectionEnvelopPanel = new SelectionEnvelopPanel( settingsAffector );
        getContentPane().add( selectionEnvelopPanel, BorderLayout.CENTER );
    }

}
