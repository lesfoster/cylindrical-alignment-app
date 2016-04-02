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
 * XML Appearance Source
 * Created on Mar 3, 2005
 */
package self.lesfoster.cylindrical_alignment.viewer.appearance_source;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import self.lesfoster.cylindrical_alignment.data_source.DataSource;
import self.lesfoster.cylindrical_alignment.data_source.SubEntity;
import self.lesfoster.cylindrical_alignment.viewer.utils.ConfigUtils;
import self.lesfoster.framework.integration.LegendModel;
import self.lesfoster.framework.integration.LegendModelContainer;
import self.lesfoster.framework.integration.SharedObjectContainer;

/**
 * Source for appearances to entities generated via the XML Data Source.
 * @author Leslie L. Foster
 */
public class XmlAppearanceSource implements AppearanceSource {
	private static final Color PERFORATION_COLOR = Color.RED;
	private static final Color SPECULAR_WHITE_COLOR = new Color( 0.5f, 0.5f, 0.5f, OPACITY );
	private static final Color DEFAULT_COLOR = new Color(0.3f, 0.3f, 1.0f, OPACITY);
	
	private static final String DEFAULT_ENTITY_TYPE = "Unknown Entity Type";
	private static final double SCORE_RESOLUTION = 0.005;

	private static final String APPEARANCE_PROPS = "self/lesfoster/cylindrical_alignment/viewer/appearance_source/XmlAppearanceSource.properties";
	private static final String NUM_COLORS_UNASSIGNED = "NumNonSpecificColors";
	private static final String NON_SPECIFIC_DOMAIN_NAME = "NonSpecificDomain";
	private Properties properties;
	private Map<String, Color> entityTypeToColor;
	private LegendModel legendModel;
	private int nextUnassignedDomain = 0;
	private int highestDomainNum = -1;

	private float scoreRed;
	private float scoreGreen;
	private float scoreBlue;

	/**
	 * Constructor prepares coloring properties for use.
	 */
	public XmlAppearanceSource() {
		//  Load the properties.
		properties = ConfigUtils.getProperties(APPEARANCE_PROPS);
		entityTypeToColor = new HashMap<>();

		//  Read the properties for coloring data.
		for (Iterator it = properties.keySet().iterator(); it.hasNext(); ) {
			String nextKey = it.next().toString();
			Color nextColor = parseForColor(properties.getProperty(nextKey));
			if (nextColor != null) {
				entityTypeToColor.put(nextKey, nextColor);
				// for score coloring
				if (isScoreColored(nextKey)) {
					scoreRed = (float)nextColor.getRed();
					scoreGreen = (float)nextColor.getGreen();
					scoreBlue = (float)nextColor.getBlue();
				}
			}
		}
		
		LegendModelContainer.getInstance().addListener(new SharedObjectContainer.ContainerListener<LegendModel>() {
			@Override
			public void setValue(LegendModel value) {
				XmlAppearanceSource.this.legendModel = value;
				Map residueColorMap = ResidueAppearanceHelper.getColorings();
				for (Iterator it = residueColorMap.keySet().iterator(); it.hasNext();) {
					String nextKey = (String) it.next();
					// Must translate between coloring schemes, to the legend model.
					final Color residueColor = (Color) residueColorMap.get(nextKey);
					java.awt.Color color2d = new java.awt.Color(
							(int) (255.0 * residueColor.getRed()),
							(int) (255.0 * residueColor.getGreen()),
							(int) (255.0 * residueColor.getBlue())
					);
					value.addColorString(nextKey, null, color2d);
				}
			}
			
		});

	}

	/* (non-Javadoc)
	 * @see self.lesfoster.cylindrical_viewer.appearance_source.AppearanceSource#createSubEntityAppearance(self.lesfoster.cylindrical_viewer.data_source.SubEntity)
	 */
	@Override
	public PhongMaterial createSubEntityAppearance(SubEntity subEntity) {
		return createMaterialAppearance(false, subEntity);
	}

	/* (non-Javadoc)
	 * @see self.lesfoster.cylindrical_viewer.appearance_source.AppearanceSource#createSubEntityInsertionAppearance(self.lesfoster.cylindrical_viewer.data_source.SubEntity)
	 */
	@Override
	public PhongMaterial createSubEntityInsertionAppearance(SubEntity subEntity) {
		return createMaterialAppearance(true, subEntity);
	}

	/* (non-Javadoc)
	 * @see self.lesfoster.cylindrical_viewer.appearance_source.AppearanceSource#createPerforatedAppearance(self.lesfoster.cylindrical_viewer.data_source.SubEntity)
	 */
	@Override
	public PhongMaterial createPerforatedAppearance(SubEntity subEntity) {
		return createPerforatedAppearance();
	}

	/* (non-Javadoc)
	 * @see self.lesfoster.cylindrical_viewer.appearance_source.AppearanceSource#createSubEntityAppearance(self.lesfoster.cylindrical_viewer.data_source.SubEntity)
	 */
	@Override
	public PhongMaterial createSubEntityAppearance(char residue, boolean isBase) {
		if (isBase)
		    return ResidueAppearanceHelper.getNtAppearance(residue);
	    else
		    return ResidueAppearanceHelper.getAAAppearance(residue);
	}
	
	/**
	 * Since this object knows legend model, it will be tasked with clearing it.
	 */
	@Override
	public void clear() {
		legendModel.clear();
	}

	/**
	 * Tells if this entity is to have its color affected by its scoring position or not.
	 * @param entityType
	 * @return true it is/false it is not.
	 */
	private boolean isScoreColored(String entityType) {
		return entityType.equalsIgnoreCase(DataSource.ENTITY_TYPE_BLAST_P_SUB_HIT)
		|| entityType.equalsIgnoreCase(DataSource.ENTITY_TYPE_BLAST_N_SUB_HIT)
	    || entityType.equalsIgnoreCase(DataSource.ENTITY_TYPE_BLAST_SUB_HIT)
	    || (entityType.indexOf("+[score]") >= 0);		
	}

    /**
     * Create the appearance to apply to the solids,
     * @param lightenColor true if wish to apply a slightly lighter shade to the color.
     */
    private PhongMaterial createMaterialAppearance(boolean lightenColor, SubEntity subEntity) {

        PhongMaterial materialAppear = getBaseMaterial();

        String entityType = getEntityType(subEntity);
        if (entityType == null)
        	entityType = DEFAULT_ENTITY_TYPE;

        Color diffuseColor = entityTypeToColor.get(entityType);
        if (diffuseColor == null) {
        	diffuseColor = generateDomainColor(entityType);
        	entityTypeToColor.put(entityType, diffuseColor);
        	System.out.println("Using " + diffuseColor + " coloring for type " + entityType + ".");
        }
        else if (lightenColor) {
        	Color oldColor = diffuseColor;
        	Color newColor = new Color(oldColor.getRed(), oldColor.getGreen(), oldColor.getBlue(), OPACITY);
        	diffuseColor = newColor.brighter().brighter();
        }
        else if (isScoreColored(entityType) && !lightenColor) {
        	if (scoreBlue >= SCORE_RESOLUTION) {
        		scoreBlue -= SCORE_RESOLUTION;
        	}
        	else if (scoreRed >= SCORE_RESOLUTION) {
        		scoreRed -= SCORE_RESOLUTION;
        	}
        	else if (scoreGreen >= SCORE_RESOLUTION) {
        		scoreGreen -= SCORE_RESOLUTION;
        	}

        	diffuseColor = new Color(scoreRed, scoreGreen, scoreBlue, OPACITY);
        	entityTypeToColor.put(entityType, diffuseColor);
        	
        }
        materialAppear.setDiffuseColor(diffuseColor);
    	legendModel.addColorString(buildLegendString(subEntity), subEntity,
    			(float)diffuseColor.getRed(), (float)diffuseColor.getGreen(), (float)diffuseColor.getBlue());
        return materialAppear;
    }

    /**
     * Build legend string.
     * @param subEntity what to present.
     * @return descriptive string.
     */
    private String buildLegendString(SubEntity subEntity) {
    	Map<String,Object> properties = subEntity.getProperties();
		String entityType = (String)properties.get(DataSource.SUB_ENTITY_TYPE_PROPERTY_NAME);
	    String acc = (String)properties.get(DataSource.ACCESSION_PROPERTY_NAME);
	    if (acc == null) {
	    	acc = (String)properties.get(DataSource.SUB_ENTITY_TYPE_PROPERTY_NAME);
	    }
		if ((entityType != null) && entityType.toLowerCase().equals(DataSource.ANCHOR_TYPE.toLowerCase())) {
			StringBuffer returnValue = new StringBuffer("Anchor");
			if (acc != null)
				returnValue.append(": ").append(acc);
			return returnValue.toString();
		}
		else {
        	String score = (String)properties.get(DataSource.ORDER_BY_PROPERTY_NAME);
        	if (score != null)
        	    return acc + ", "
		          + DataSource.BIT_SCORE_PROPERTY_NAME + ":" + score;
        	else
        		return acc;
		}
    }

    /**
     * Creates a Material with all base characteristics.
     * @return tweakable material.
     */
    private PhongMaterial getBaseMaterial() {
    	PhongMaterial material = new PhongMaterial();
        Color white = SPECULAR_WHITE_COLOR;
        material.setSpecularColor( white );
        material.setSpecularPower( 1.75f );

        return material;    	
    }

    /**
     * Read the props, and get the entity type.
     * @param subEntity
     * @return
     */
    private String getEntityType(SubEntity subEntity) {
    	String entityType = null;
    	if (subEntity != null) {
    		Map<String,Object> properties = subEntity.getProperties();
    		if (properties != null) {
    			Object entityTypeObj = properties.get(DataSource.SUB_ENTITY_TYPE_PROPERTY_NAME);
    			if (entityTypeObj != null)
    				entityType = entityTypeObj.toString();
    		}
    	}
    	return entityType;
    }

    /**
     * Parses a coloring property string, to produce a color object.
     * @param colorString in format r,g,b
     * @return color object with r,g,b as its colors. Or null if parse impossible.
     */
    private Color parseForColor(String colorString) {
    	Color returnColor = null;
    	StringTokenizer stk = new StringTokenizer(colorString, ",");
    	// Check format.
    	if (stk.countTokens() == 3) {
    		String red = stk.nextToken();
    		String green = stk.nextToken();
    		String blue = stk.nextToken();
    		// Attempt to make integers.
    		try {
    			int redInt = Integer.parseInt(red);
    			int greenInt = Integer.parseInt(green);
    			int blueInt = Integer.parseInt(blue);

    			returnColor = new Color(redInt / 255.0f, greenInt / 255.0f, blueInt / 255.0f, OPACITY); 
    		} catch (NumberFormatException nfe) {
    			System.out.println("Failed to parse a number string.");
    			nfe.printStackTrace();
    		}
    	}

    	return returnColor;
    }

    /** Create the appearance to apply to the solids. */
    private PhongMaterial createPerforatedAppearance(){

        PhongMaterial materialAppear = getBaseMaterial();
        materialAppear.setDiffuseColor(PERFORATION_COLOR);

		return materialAppear;
    }

    /**
     * Looks at non-specific domains, and makes a color, based on an offset into
     * an array of unassigned domain colors.
     * @param domainName name of some un-assigned domain.
     * @return color that is as unique as possible.
     */
    private Color generateDomainColor(String domainName) {
    	Color returnColor = DEFAULT_COLOR;
    	nextUnassignedDomain ++;
    	if (highestDomainNum == -1) {
    	    String highestDomain = properties.getProperty(NUM_COLORS_UNASSIGNED);
    	    if (highestDomain != null) {
    		   highestDomainNum = Integer.parseInt(highestDomain);
        	}
    	}
    	if (highestDomainNum > 0) {
    		String propName = NON_SPECIFIC_DOMAIN_NAME + (nextUnassignedDomain % (highestDomainNum + 1));
    		String colorString = properties.getProperty(propName);
        	returnColor = parseForColor(colorString);
    	}
    	return returnColor;
    }
    
}
