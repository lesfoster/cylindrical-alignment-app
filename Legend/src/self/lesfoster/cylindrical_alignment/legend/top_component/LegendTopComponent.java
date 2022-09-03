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
package self.lesfoster.cylindrical_alignment.legend.top_component;

import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import self.lesfoster.cylindrical_alignment.legend.widget.LegendComponent;
import self.lesfoster.framework.integration.LegendModel;
import self.lesfoster.framework.integration.LegendModelContainer;
import self.lesfoster.framework.integration.SharedObjectContainer.ContainerListener;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//self.lesfoster.cylindrical_alignment.legend.top_component//Legend//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = LegendTopComponent.PREFERRED_ID,
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
@ActionID(category = "Window", id = "self.lesfoster.cylindrical_alignment.legend.top_component.LegendTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_LegendAction",
        preferredID = LegendTopComponent.PREFERRED_ID
)
@Messages({
    "CTL_LegendAction=Legend",
    "CTL_LegendTopComponent=Legend",
    "HINT_LegendTopComponent=Clickable name/color combinations to select in other views."
})
public final class LegendTopComponent extends TopComponent {

    public static final String PREFERRED_ID = "LegendTopComponent";
    private ContainerListener<LegendModel> containerListener;
    private final LegendComponent legendComponent;

    public LegendTopComponent() {
        legendComponent = new LegendComponent();
        LegendTopComponent.this.associateLookup(legendComponent.getLookup());
        initComponents();
        setName(Bundle.CTL_LegendTopComponent());
        setToolTipText(Bundle.HINT_LegendTopComponent());

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        legendBasePanel = new javax.swing.JPanel();

        legendBasePanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(legendBasePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(legendBasePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel legendBasePanel;
    // End of variables declaration//GEN-END:variables
	@Override
    public void componentOpened() {
        containerListener = (value) -> {
            SwingUtilities.invokeLater(() -> {
                legendComponent.setModel(value);
                legendBasePanel.add(new JScrollPane(legendComponent), BorderLayout.CENTER);
                legendComponent.repaint();
            });
        };

        LegendModelContainer.getInstance().addListener(containerListener);
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    @SuppressWarnings("unused")
    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    @SuppressWarnings("unused")
    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}
