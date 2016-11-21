/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.inspector.top_component;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import self.lesfoster.cylindrical_alignment.inspector.table.TextAreaRenderer;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import self.lesfoster.cylindrical_alignment.data_source.SubEntity;
import self.lesfoster.cylindrical_alignment.inspector.table.PopupMouseListener;
import self.lesfoster.cylindrical_alignment.inspector.table_model.SubHitTableModel;
import self.lesfoster.framework.integration.SelectedObjectWrapper;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
		dtd = "-//self.lesfoster.cylindrical_alignment.inspector.top_component//PropertyInspector//EN",
		autostore = false
)
@TopComponent.Description(
		preferredID = PropertyInspectorTopComponent.PREFERRED_ID,
		//iconBase="SET/PATH/TO/ICON/HERE", 
		persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "properties", position = 100, openAtStartup = true)
@ActionID(category = "Window", id = "self.lesfoster.cylindrical_alignment.inspector.top_component.PropertyInspectorTopComponent")
@ActionReference(path = "Menu/Window", position = 330)
@TopComponent.OpenActionRegistration(
		displayName = "#CTL_PropertyInspectorAction",
		preferredID = PropertyInspectorTopComponent.PREFERRED_ID
)
@Messages({
	"CTL_PropertyInspectorAction=PropertyInspector",
	"CTL_PropertyInspectorTopComponent=Property Inspector",
	"HINT_PropertyInspectorTopComponent=All properties of the current selection"
})
public final class PropertyInspectorTopComponent extends TopComponent {
	public static final String PREFERRED_ID = "PropertyInspectorTopComponent";
	
	private Lookup.Result<Map> mapResult;
	private Lookup.Result<SelectedObjectWrapper> selectionWrapperResult;
	private LookupListener mapLookupListener;
	private LookupListener selectedObjLookupListener;

	public PropertyInspectorTopComponent() {
		initComponents();
		setName(Bundle.CTL_PropertyInspectorTopComponent());
		setToolTipText(Bundle.HINT_PropertyInspectorTopComponent());

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        inspectorPanel = new javax.swing.JPanel();

        inspectorPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(inspectorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(inspectorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel inspectorPanel;
    // End of variables declaration//GEN-END:variables
	@Override
	public void componentOpened() {
		final SubHitTableModel subHitTableModel = new SubHitTableModel(new HashMap<String,String>());
		JTable propsTable = new JTable(subHitTableModel);
		propsTable.addMouseListener(new PopupMouseListener());
		propsTable.setDefaultRenderer(Object.class, new TextAreaRenderer());
		inspectorPanel.add(new JScrollPane(propsTable), BorderLayout.CENTER);
		Lookup global = Utilities.actionsGlobalContext();
		mapResult = global.lookupResult(Map.class);
		if (mapResult != null) {
			mapLookupListener = new MapLookupListener(subHitTableModel);
			mapResult.addLookupListener(mapLookupListener);
		}
		selectionWrapperResult = global.lookupResult(SelectedObjectWrapper.class);
		if (selectionWrapperResult != null) {
			selectedObjLookupListener = new ModelSelectionWrapperLookupListener(subHitTableModel);
			selectionWrapperResult.addLookupListener(selectedObjLookupListener);
		}
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

	private class ModelSelectionWrapperLookupListener implements LookupListener {

		private SubHitTableModel subHitTableModel;

		public ModelSelectionWrapperLookupListener(SubHitTableModel subHitTableModel) {
			this.subHitTableModel = subHitTableModel;
		}

		@Override
		public void resultChanged(LookupEvent le) {
			if (selectionWrapperResult.allInstances().size() > 0) {
				SelectedObjectWrapper lastWrapper = null;
				for (SelectedObjectWrapper wrapper : selectionWrapperResult.allInstances()) {
					lastWrapper = wrapper;
				}
				if (lastWrapper != null) {
					Object obj = lastWrapper.getSelectedObject();
					if (obj instanceof SubEntity) {
						SubEntity se = (SubEntity) obj;
						subHitTableModel.setModelInfo(se.getProperties());
						subHitTableModel.fireTableDataChanged();
					}
				}
			}
		}
	}
	
	private class MapLookupListener implements LookupListener {

		private SubHitTableModel subHitTableModel;

		public MapLookupListener(SubHitTableModel subHitTableModel) {
			this.subHitTableModel = subHitTableModel;
		}

		@Override
		public void resultChanged(LookupEvent le) {
			if (mapResult.allInstances().size() > 0) {
				Map lastModelInfo = null;
				for (Map modelInfo : mapResult.allInstances()) {
					lastModelInfo = modelInfo;
				}
				if (lastModelInfo != null) {
					subHitTableModel.setModelInfo(lastModelInfo);
					subHitTableModel.fireTableDataChanged();
				}
			}
		}
	}
}
