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
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import self.lesfoster.cylindrical_alignment.effector.SettingsEffector;

/**
 * Created by IntelliJ IDEA.
 * User: Leslie L Foster
 * Date: 04/22/2012
 * Time: 11:46 PM
 * The controls on this panel will operate against an affector when the user enters a value.
 */
public class SelectionEnvelopPanel extends JPanel {
    private SettingsEffector settingsAffector;
    public SelectionEnvelopPanel(SettingsEffector settingsAffector) {
        super();
        this.settingsAffector = settingsAffector;
        init();
    }

    private void init() {
        final JTextField envelopDistanceTF = new JTextField();
        envelopDistanceTF.setColumns( 5 );
        envelopDistanceTF.setBorder( new TitledBorder( "Envelope Width" ) );

        JButton goButton = new JButton( "Set" );
        goButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Integer envelopeDistance = Integer.parseInt( envelopDistanceTF.getText() );
                    settingsAffector.setSelectionEnvelope( envelopeDistance );
                } catch ( NumberFormatException nfe ) {
                    // nada
                }
            }
        });
        BorderLayout borderLayout = new BorderLayout();
        setLayout(borderLayout);
        add( envelopDistanceTF, BorderLayout.CENTER );
        add( goButton, BorderLayout.EAST );
    }

}
