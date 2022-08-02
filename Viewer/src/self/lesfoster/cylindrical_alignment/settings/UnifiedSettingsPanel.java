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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import self.lesfoster.cylindrical_alignment.effector.Effector;
import self.lesfoster.cylindrical_alignment.effector.CylinderPositioningEffector;
import self.lesfoster.cylindrical_alignment.effector.Effected;
import self.lesfoster.cylindrical_alignment.effector.SettingsEffector;
import self.lesfoster.cylindrical_alignment.effector.SpeedEffector;
import self.lesfoster.cylindrical_alignment.viewer.top_component.EffectedContainer;
import self.lesfoster.cylindrical_alignment.viewer.top_component.EffectedContainer.EffectorContainerListener;

/**
 * Created by IntelliJ IDEA. User: Leslie L Foster Date: 4/22/12 Time: 8:41 PM
 * Updated 8/1/22
 *
 * A settings panel for the viewer.
 */
public class UnifiedSettingsPanel extends JPanel {

    private static final int USPWIDTH = 300;

    private EffectorContainerListener ecl;

    public UnifiedSettingsPanel() {
        initWhenReady();
    }

    public UnifiedSettingsPanel(
            SpeedEffector speedEffector,
            SettingsEffector settingsEffector,
            CylinderPositioningEffector cylinderPositioningEffector) {
        //super( "Adjust Settings" );
        initGui(speedEffector, settingsEffector, cylinderPositioningEffector);
    }

    private void initWhenReady() {
        EffectedContainer ec = EffectedContainer.getInstance();
        ecl = (Effected effected) -> {
            //close();
            launchInit(effected);
        };
        ec.addListener(ecl);
    }

    public void launchInit(Effected effected) {
        final Effector[] effectors = effected.getEffectors();
        SpeedEffector speedEffector = null;
        SettingsEffector settingsEffector = null;
        CylinderPositioningEffector cylPosEffector = null;
        for (Effector effector : effectors) {
            if (effector instanceof SpeedEffector) {
                speedEffector = (SpeedEffector) effector;
            } else if (effector instanceof SettingsEffector) {
                settingsEffector = (SettingsEffector) effector;
            } else if (effector instanceof CylinderPositioningEffector) {
                cylPosEffector = (CylinderPositioningEffector) effector;
            }
        }
        initGui(speedEffector, settingsEffector, cylPosEffector);
    }

    /**
     * Build out the GUI with all the effectors known here.
     */
    private void initGui(
            SpeedEffector speedEffector,
            SettingsEffector settingsEffector,
            CylinderPositioningEffector cylinderPositioningEffector) {

        removeAll();

        SpinSliderPanel spinSliderPanel = new SpinSliderPanel(speedEffector);
        SelectionEnvelopPanel selectionEnvelopPanel = new SelectionEnvelopPanel(settingsEffector);
        DragFactorSliderPanel dragFactorSliderPanel = new DragFactorSliderPanel(cylinderPositioningEffector);

        GridBagLayout gridBagLayout = new GridBagLayout();
        this.setLayout(gridBagLayout);

        int gridX = 1;
        int gridY = 1;

        int gridWidth = 2;
        int gridHeight = 1;

        double weightX = 4.0;
        double weightY = 4.0;

        int anchor = GridBagConstraints.WEST;
        int fill = GridBagConstraints.HORIZONTAL;

        int insetsTop = 0;
        int insetsLeft = 5;
        int insetsBottom = 0;
        int insetsRight = 5;
        Insets insets = new Insets(insetsTop, insetsLeft, insetsBottom, insetsRight);

        int ipadx = 0;
        int ipady = 0;

        GridBagConstraints spinSliderConstraints = new GridBagConstraints(
                gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor, fill, insets, ipadx, ipady
        );
        add(spinSliderPanel, spinSliderConstraints);

        gridY++;
        GridBagConstraints selectionEnvelopConstraints = new GridBagConstraints(
            gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor, fill, insets, ipadx, ipady
        );
        add(selectionEnvelopPanel, selectionEnvelopConstraints);

        gridY++;
        GridBagConstraints dragFactorConstraints = new GridBagConstraints(
            gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor, fill, insets, ipadx, ipady
        );
        add(dragFactorSliderPanel, dragFactorConstraints);

        JButton resetCylinderButton = new JButton("Re-set Cylinder Position");
        resetCylinderButton.addActionListener(e -> cylinderPositioningEffector.setDefaultCylinderPosition());
        gridY++;
        GridBagConstraints resetCylinderConstraints = new GridBagConstraints(
            gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor,
            GridBagConstraints.NONE, insets, ipadx, ipady
        );
        add(resetCylinderButton, resetCylinderConstraints);

        JCheckBox mismatchDentilsCheckbox = new JCheckBox("Only Differing Dentils");
        mismatchDentilsCheckbox.setSelected(false);
        mismatchDentilsCheckbox.addActionListener(
            e -> settingsEffector.setDifferingDentils(
                mismatchDentilsCheckbox.isSelected()
            )
        );
        gridY++;
        GridBagConstraints mismatchDentilsConstraints = new GridBagConstraints(
            gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor, fill, insets, ipadx, ipady
        );
        add(mismatchDentilsCheckbox, mismatchDentilsConstraints);

        JCheckBox freezeCylinderCheckbox = new JCheckBox("Freeze Cylinder Position");
        freezeCylinderCheckbox.addActionListener(
            e -> cylinderPositioningEffector.setFrozenMouseRotator(freezeCylinderCheckbox.isSelected())
        );
        gridY++;
        GridBagConstraints freezeCylinderConstraints = new GridBagConstraints(
            gridX, gridY, gridWidth/2, gridHeight, weightX / 2.0, weightY, anchor, fill, insets, ipadx, ipady
        );
        add(freezeCylinderCheckbox, freezeCylinderConstraints);

        JCheckBox dragAroundYCheckbox = new JCheckBox("Drag Cylinder around Y Axis Only");
        dragAroundYCheckbox.addActionListener(new DragAroundYActionListener(cylinderPositioningEffector));
        GridBagConstraints dragAroundYConstraints = new GridBagConstraints(
            gridX + 1, gridY, gridWidth/2, gridHeight, weightX / 2.0, weightY, anchor, fill, insets, ipadx, ipady
        );
        add(dragAroundYCheckbox, dragAroundYConstraints);

        gridY++;
        JPanel colorRankerPanel = new ColorRankerPanel(USPWIDTH);
        GridBagConstraints colorRankerConstraints = new GridBagConstraints(
            gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor, fill, insets, ipadx, ipady
        );
        add(colorRankerPanel, colorRankerConstraints);

        JCheckBox ambientLightCheckbox = new JCheckBox("Ambient Lighting");
        ambientLightCheckbox.setToolTipText(
            "Switching to ambient lighting ensures all objects show up more uniformly, although it may not show some of the more interesting effects"
        );
        ambientLightCheckbox.setSelected(false);  // Off by default.
        ambientLightCheckbox.addActionListener(e -> settingsEffector.setAmbientLightSource(ambientLightCheckbox.isSelected()));
        gridY++;
        GridBagConstraints ambilightConstraints = new GridBagConstraints(
            gridX, gridY, 1, gridHeight, weightX / 2.0, weightY, anchor, fill, insets, ipadx, ipady
        );
        add(ambientLightCheckbox, ambilightConstraints);

        JCheckBox darkLightCheckbox = new JCheckBox("Dark Background");
        darkLightCheckbox.setToolTipText(
                "A dark background is better for computer viewing.  A light background is better for printing"
        );
        darkLightCheckbox.setSelected(true);  // Dark by default.
        darkLightCheckbox.addActionListener(e -> settingsEffector.setDark(darkLightCheckbox.isSelected()));
        GridBagConstraints lightDarkConstraints = new GridBagConstraints(
            2, gridY, 1, gridHeight, weightX, weightY, GridBagConstraints.EAST, fill, insets, ipadx, ipady
        );
        add(darkLightCheckbox, lightDarkConstraints);

        /*
        GuiUtils.upperLeftLocation(this, WIDTH, HEIGHT);
        GuiUtils.setIcon(this);
        setResizable( false );
         */
        validate();
        invalidate();
        repaint();
    }

    /**
     * Close and init are opposite operations.
     */
    public void close() {
        EffectedContainer ec = EffectedContainer.getInstance();
        if (ecl != null) {
            ec.removeListener(ecl);
        }
    }
}
