/*
Cylindrical Blast Viewer: allows viewing of Biomolecular
alignment data, in 3D, by forming the alignments into a
cylinder, such that the alignments are arranged like the staves
of a barrel. The cylinder can spin, and if it spins, it can
do so at two different speeds.  When stopped, properties can
be seen for the aligned values.
Copyright (C) 2005 Leslie L. Foster

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

/*
 * Residue PhongMaterial Helper.
 * Created on Apr 2, 2005
 */
package self.lesfoster.cylindrical_alignment.viewer.appearance_source;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import static self.lesfoster.cylindrical_alignment.viewer.appearance_source.AppearanceSource.OPACITY;

/**
 * Generates appearances for residue letters (amino acids or nucleotides).
 * @author Leslie L Foster
 */
public class ResidueAppearanceHelper {
	// Amino Acid classifications are from the book "Molecular Cell Biology", Lodish et al., 5th Ed., pg. 39
	private static final String HYDROPHOBIC_ACIDS = "AVILMFYW";
	private static final String HYDROPHILIC_ACIDS = "KRHENQ";
	private static final String CYSTEINE = "C";
	private static final String GLYCINE = "G";
	private static final String PROLINE = "P";

	private static PhongMaterial[] sBasePhongMaterial;
	private static Map<String, PhongMaterial> sAcidPhongMaterial;
	private static Map<String, Color> sStringToColor;

	/**
	 * Returns appearance assigned to a given DNA letter.
	 * @param base one of the 4 Nts seen in DNA.
	 * @return what it should look like.
	 */
	public static PhongMaterial getNtAppearance(char base) {
		if (sBasePhongMaterial == null) {
			generateBasePhongMaterial();
		}

        if (Character.toUpperCase(base) == 'A') {
        	return sBasePhongMaterial[0];
        }
        if (Character.toUpperCase(base) == 'C')
        	return sBasePhongMaterial[1];
        if (Character.toUpperCase(base) == 'G')
        	return sBasePhongMaterial[2];
        if (Character.toUpperCase(base) == 'T')
        	return sBasePhongMaterial[3];
		return null;
	}

	/**
	 * Returns appearance assigned to a given amino acid letter.
	 * @param base one of the 20 standard amino acids seen in peptides.
	 * @return what it should look like.
	 */
	public static PhongMaterial getAAAppearance(char residue) {
		if (sAcidPhongMaterial == null) {
			generateAcidPhongMaterial();
		}
		char testResidue = Character.toUpperCase(residue);
		for (Iterator it = sAcidPhongMaterial.keySet().iterator(); it.hasNext(); ) {
			String nextAASet = it.next().toString();
			if (nextAASet.indexOf(testResidue) > -1) {
				return (PhongMaterial)sAcidPhongMaterial.get(nextAASet);
			}
		}
		return null;
	}

	/**
	 * Returns color map generated here.  This depends on the residues having been generated previously.
	 * @param targetMap where to put the mappings.
	 */
	public static Map getColorings() {
    	if (sStringToColor == null)
    		return Collections.EMPTY_MAP;
		return sStringToColor;
	}

	/**
	 * Builds the array of appearances for Nt bases.
	 */
	private static void generateBasePhongMaterial() {
		// Coloring reference is:
		//  http://faculty.clintoncc.suny.edu/faculty/Michael.Gregory/files/Bio%20101/Bio%20101%20Laboratory/DNA%20Structure,%20DNA%20Synthesis/DNA%20Structure,%20DNA%20Synthesis.htm
		Color aColor = new Color(1.0f, 0.0f, 0.0f, OPACITY); // red
		Color cColor = new Color(0.0f, 0.0f, 1.0f, OPACITY); // blue 
		Color gColor = new Color(0.0f, 1.0f, 0.0f, OPACITY); // green
		Color tColor = new Color(1.0f, 1.0f, 0.0f, OPACITY); // yellow

		PhongMaterial a = createMaterial(aColor); // red
		PhongMaterial c = createMaterial(cColor); // blue 
		PhongMaterial g = createMaterial(gColor); // green
		PhongMaterial t = createMaterial(tColor); // yellow

		sBasePhongMaterial = new PhongMaterial[] { a, c, g, t };
		addToColorMap('A', aColor);
		addToColorMap('C', cColor);
		addToColorMap('G', gColor);
		addToColorMap('T', tColor);
	}

	/**
	 * Builds the array of appearances for Nt bases.
	 */
	private static void generateAcidPhongMaterial() {
		Color phobColor = new Color(1.0f, 0.0f, 1.0f, OPACITY); // pink
		Color philColor = new Color(0.0f, 0.0f, 1.0f, OPACITY); // blue
		Color cysColor = new Color(0.0f, 1.0f, 0.0f, OPACITY); // green
		Color glyColor = new Color(1.0f, 0.0f, 0.0f, OPACITY); // red
		Color proColor = new Color(1.0f, 1.0f, 0.0f, OPACITY); // yellow

		PhongMaterial hydrophobic = createMaterial(phobColor); // pink
		PhongMaterial hydrophilic = createMaterial(philColor); // blue
		PhongMaterial cysteine = createMaterial(cysColor); // green
		PhongMaterial glycine = createMaterial(glyColor); // red
		PhongMaterial proline = createMaterial(proColor); // yellow

		sAcidPhongMaterial = new HashMap<>();
		sAcidPhongMaterial.put(HYDROPHOBIC_ACIDS, hydrophobic);
		sAcidPhongMaterial.put(HYDROPHILIC_ACIDS, hydrophilic);
		sAcidPhongMaterial.put(CYSTEINE, cysteine);
        sAcidPhongMaterial.put(GLYCINE, glycine);
		sAcidPhongMaterial.put(PROLINE, proline);

		addToColorMap("Hydrophobic AA", phobColor);
		addToColorMap("Hydrophilic AA", philColor);
		addToColorMap("Cystein AA", cysColor);
		addToColorMap("Glycine AA", glyColor);
		addToColorMap("Proline AA", proColor);
	}

    /**
     * Create the appearance to apply to the solids,
     * @param lightenColor true if wish to apply a slightly lighter shade to the color.
     */
    private static PhongMaterial createMaterial(Color color) {

        PhongMaterial materialAppear = new PhongMaterial();
 		materialAppear.setDiffuseColor(Color.DODGERBLUE);
		materialAppear.setSpecularColor(Color.WHITE);
		materialAppear.setSpecularPower( 1.75f );

        return materialAppear;
    }

    /**
     * Maps a residue to its color, for later return.
     * 
     * @param residue acid letter.
     * @param color the assigned color for the appearance.
     */
    private static void addToColorMap(char residue, Color color) {
    	addToColorMap("" + residue, color);
    }

    /**
     * Maps a residue to its color, for later return.
     * 
     * @param residue acid descriptive string.
     * @param color the assigned color for the appearance.
     */
    private static void addToColorMap(String residue, Color color) {
    	if (sStringToColor == null)
    		sStringToColor = new HashMap<String, Color>();
    	sStringToColor.put(residue, new Color(color.getRed(), color.getGreen(), color.getBlue(), OPACITY));
    }
}
