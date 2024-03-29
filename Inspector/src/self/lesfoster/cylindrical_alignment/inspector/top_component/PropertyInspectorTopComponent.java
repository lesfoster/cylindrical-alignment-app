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
        final SubHitTableModel subHitTableModel = new SubHitTableModel(new HashMap<>());
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
        mapResult.removeLookupListener(mapLookupListener);
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

    /** Wrapper-holder is from sub-view. */
    private class ModelSelectionWrapperLookupListener implements LookupListener {

        private final SubHitTableModel subHitTableModel;

        public ModelSelectionWrapperLookupListener(SubHitTableModel subHitTableModel) {
            this.subHitTableModel = subHitTableModel;
        }

        @Override
        public void resultChanged(LookupEvent le) {
            selectionWrapperResult.allInstances().stream().reduce((a, b) -> b)
                    .map(SelectedObjectWrapper::getSelectedObject)
                    .filter(SubEntity.class::isInstance)
                    .map(SubEntity.class::cast)
                    .map(SubEntity::getProperties)
                    .ifPresent(subHitTableModel::setModelInfo);
        }
    }

    /** Map holder is from main view. */
    private class MapLookupListener implements LookupListener {

        private final SubHitTableModel subHitTableModel;

        public MapLookupListener(SubHitTableModel subHitTableModel) {
            this.subHitTableModel = subHitTableModel;
        }

        @Override
        public void resultChanged(LookupEvent le) {
            mapResult.allInstances().stream().reduce((a, b) -> b).ifPresent(
                lastModelInfo -> subHitTableModel.setModelInfo(lastModelInfo));
        }
    }
}
