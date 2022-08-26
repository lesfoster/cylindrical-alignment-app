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
package self.lesfoster.cylindrical_alignment.viewer.top_component;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import self.lesfoster.framework.integration.ResidueData;

/**
 * Neighborhood TopComponent.  Displays and allows interactions with
 * "neighborhoods", which are vertically-confined, isolated groups of
 * disagreeing residues occurring to some lower-bound frequency around
 * the cylinder.
 */
@ConvertAsProperties(
        dtd = "-//self.lesfoster.cylindrical_alignment.viewer.top_component//NeighborhoodSV//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "NeighborhoodSVTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "explorer", openAtStartup = false)
@ActionID(category = "Window", id = "self.lesfoster.cylindrical_alignment.viewer.top_component.NeighborhoodSVTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_NeighborhoodSVAction",
        preferredID = "NeighborhoodSVTopComponent"
)
@Messages({
    "CTL_NeighborhoodSVAction=NeighborhoodSV",
    "CTL_NeighborhoodSVTopComponent=NeighborhoodSV Window",
    "HINT_NeighborhoodSVTopComponent=This is a NeighborhoodSV window"
})
public final class NeighborhoodSVTopComponent extends TopComponent {
    private Lookup.Result<Map> mapResult;
    private Lookup.Result<ResidueData> rdResult;
    private LookupListener mapLookupListener;
    private ResidueDataLookupListener residueDataLookupListener; 
    
    public NeighborhoodSVTopComponent() {
        initComponents();
        setName(Bundle.CTL_NeighborhoodSVTopComponent());
        setToolTipText(Bundle.HINT_NeighborhoodSVTopComponent());
        putClientProperty(TopComponent.PROP_KEEP_PREFERRED_SIZE_WHEN_SLIDED_IN, Boolean.TRUE);
        establishLookups();
    }

    private ResidueData residueData;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        neighborhoodPanel = new javax.swing.JPanel();

        javax.swing.GroupLayout neighborhoodPanelLayout = new javax.swing.GroupLayout(neighborhoodPanel);
        neighborhoodPanel.setLayout(neighborhoodPanelLayout);
        neighborhoodPanelLayout.setHorizontalGroup(
            neighborhoodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        neighborhoodPanelLayout.setVerticalGroup(
            neighborhoodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(142, 142, 142)
                .addComponent(neighborhoodPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(158, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addComponent(neighborhoodPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(113, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel neighborhoodPanel;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        establishLookups();
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
    
    private void establishLookups() {
        if (mapResult == null || rdResult == null) {
            Lookup global = Utilities.actionsGlobalContext();
            mapResult = global.lookupResult(Map.class);
            if (mapResult != null) {
                mapLookupListener = new MapLookupListener();
                mapResult.addLookupListener(mapLookupListener);
            }
            rdResult = global.lookupResult(ResidueData.class);
            if (rdResult != null) {
                residueDataLookupListener = new ResidueDataLookupListener();
                rdResult.addLookupListener(residueDataLookupListener);
            }
        }
    }

    private class ResidueDataLookupListener implements LookupListener {
        @Override
        public void resultChanged(LookupEvent le) {
            residueData = rdResult.allInstances().stream().reduce((a, b) -> b)
                    .orElse(null);
        }        
    }
    
    private static final int MAX_CLUSTER = 20; // arbitrary.

    /** Map holder is from main view. */
    private class MapLookupListener implements LookupListener {

        @Override
        public void resultChanged(LookupEvent le) {            
            mapResult.allInstances().stream().reduce((a, b) -> b)
                    .filter(m -> m.containsKey("diff_residue") &&
                            m.get("diff_residue") != null &&
                            Boolean.parseBoolean(m.get("diff_residue").toString()))
                    .filter(lastModelInfo -> residueData != null)
                    .ifPresent(this::report);
        }
        
        private void report(Map<Object,Object> modelInfo) {
            System.out.println("Got new props map");
            int residuePos = (Integer)modelInfo.get("residue_pos");
            residueData.getDentilPosToIds().get(residuePos).forEach(System.out::println);
            System.out.println("Neighborhood counts:");
            Map<Integer,Integer> neighborhoodCounts = new TreeMap<>();
            for (int i = residuePos; i >= Math.max(0, residuePos - MAX_CLUSTER); i--) {
                final int pos = i;
                Optional.ofNullable(residueData.getDentilPosToIds()
                        .get(i)).map(l -> l.size())
                        .ifPresent(ct -> neighborhoodCounts.put(pos, ct));
            }
            for (int i = residuePos + 1; i < residuePos + 1 + MAX_CLUSTER; i++) {
                final int pos = i;
                Optional.ofNullable(residueData.getDentilPosToIds()
                        .get(i)).map(l -> l.size())
                        .ifPresent(ct -> neighborhoodCounts.put(pos, ct));
                
            }
            neighborhoodCounts.forEach((n,v) -> System.out.println(String.format("Pos: %d   Count: %d", n, v)));
        }
    }    
}
