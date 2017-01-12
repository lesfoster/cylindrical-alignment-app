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
				return sAcidPhongMaterial.get(nextAASet);
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
 		materialAppear.setDiffuseColor(color);
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
    		sStringToColor = new HashMap<>();
    	sStringToColor.put(residue, new Color(color.getRed(), color.getGreen(), color.getBlue(), OPACITY));
    }
}
