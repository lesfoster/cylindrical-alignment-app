/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.legend.top_component;

import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import self.lesfoster.cylindrical_alignment.legend.widget.LegendComponent;
import self.lesfoster.framework.integration.LegendModel;

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
	"CTL_LegendTopComponent=Legend Window",
	"HINT_LegendTopComponent=This is a Legend window"
})
public final class LegendTopComponent extends TopComponent {
	public static final String PREFERRED_ID = "LegendTopComponent";

	public LegendTopComponent() {
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

        jPanel1 = new javax.swing.JPanel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
	@Override
	public void componentOpened() {
		//LegendComponent legend = new LegendComponent();
	}

	@Override
	public void componentClosed() {
		// TODO add custom code on component closing
	}

	void writeProperties(java.util.Properties p) {
		// better to version settings since initial version as advocated at
		// http://wiki.apidesign.org/wiki/PropertyFiles
		p.setProperty("version", "1.0");
		// TODO store your settings
	}

	void readProperties(java.util.Properties p) {
		String version = p.getProperty("version");
		// TODO read your settings according to their version
	}
}
