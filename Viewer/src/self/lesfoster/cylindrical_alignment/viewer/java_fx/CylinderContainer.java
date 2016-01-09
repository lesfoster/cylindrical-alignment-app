/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.viewer.java_fx;

import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import self.lesfoster.cylindrical_alignment.constants.Constants;
import self.lesfoster.cylindrical_alignment.data_source.DataSource;
import self.lesfoster.cylindrical_alignment.data_source.Entity;
import self.lesfoster.cylindrical_alignment.data_source.SubEntity;
import self.lesfoster.cylindrical_alignment.geometry.TexCoordGenerator;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.events.KeyEventHandler;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.events.MouseDraggedHandler;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.events.MousePressedHandler;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.group.TransformableGroup;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.gui_model.CameraModel;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.gui_model.MouseLocationModel;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.gui_model.SelectionModel;

/**
 * Contains all the domain-oriented objects, which are also created here.
 *
 * @author Leslie L Foster
 */
public class CylinderContainer extends JFXPanel {
//    final PerspectiveCamera camera = new PerspectiveCamera(true); //Omitted true argument for older Java FX
//    final TransformableGroup cameraXform = new TransformableGroup();
//    final TransformableGroup cameraXform2 = new TransformableGroup();
//    final TransformableGroup cameraXform3 = new TransformableGroup();

	final double cameraDistance = Constants.LENGTH_OF_CYLINDER * 3;
	final TransformableGroup moleculeGroup = new TransformableGroup();

	private int startRange = 0;
	private int endRange = 0;
	private float factor = 0;

	private Group root = new Group();
	private TransformableGroup world = new TransformableGroup();
	private TransformableGroup positionableObject = new TransformableGroup();
	private TransformableGroup cylinder;
	private TransformableGroup ruler;
	private TransformableGroup anchor;
	private Scene scene;

	private MouseLocationModel mouseLocationModel = new MouseLocationModel();
	private SelectionModel selectionModel = new SelectionModel();
	private CameraModel cameraModel = new CameraModel();
	private Text inSceneLabel;

	private TexCoordGenerator texCoordGenerator = new TexCoordGenerator();

	public CylinderContainer(DataSource dataSource) {
		this(dataSource, 0, dataSource.getAnchorLength());
	}

	public CylinderContainer(DataSource dataSource, Integer startRange, Integer endRange) {
		this.startRange = startRange;
		this.endRange = endRange;
		factor = Constants.LENGTH_OF_CYLINDER / (endRange - startRange);
		init(dataSource);
	}

	private void init(final DataSource dataSource) {
		Platform.runLater(() -> {
			createScene();
			createCamera();
			positionableObject = createPositionableObjectHierarchy(dataSource);
			inSceneLabel = createLabel();
			root.getChildren().add(positionableObject);
			root.getChildren().add(inSceneLabel);
			scene = new Scene(root, Color.LIGHTGRAY);
			scene.setCamera(cameraModel.getCamera());
			setScene(scene);

			handleMouse(scene);
			handleKeyboard(scene);
		});
	}

	private void createScene() {
		root.getChildren().add(world);
	}

	private void createCamera() {
		root.getChildren().add(cameraModel.getCameraXform());
		cameraModel.getCameraXform().getChildren().add(cameraModel.getCameraXform2());
		cameraModel.getCameraXform2().getChildren().add(cameraModel.getCameraXform3());
		cameraModel.getCameraXform3().getChildren().add(cameraModel.getCamera());
        //cameraXform3.setRotateZ(180.0);

		cameraModel.getCamera().setNearClip(1.0);
		cameraModel.getCamera().setFarClip(1000.0);
		cameraModel.getCamera().setTranslateZ(-cameraDistance);
		cameraModel.getCameraXform().ry.setAngle(32.0);
		cameraModel.getCameraXform().rx.setAngle(23.0);
	}

	/**
	 * This creates one large hierarchy of objects which may be moved using
	 * mouse drag operations. Within this hierarchy will lie other things which
	 * may or may not move/animate with the larger whole.
	 */
	private TransformableGroup createPositionableObjectHierarchy(DataSource dataSource) {
		TransformableGroup rtnVal = new TransformableGroup();
		rtnVal.getChildren().add(createCylinder(dataSource.getEntities()));
		rtnVal.getChildren().add(createRuler(dataSource.getAnchorLength()));
		rtnVal.getChildren().add(createAnchor(dataSource.getAnchorLength()));
		return rtnVal;
	}

	private TransformableGroup createCylinder(List<Entity> entities) {
		TransformableGroup rtnVal = new TransformableGroup();
		try {
			double rotOffs = 360.0 / entities.size();
			double rotatePos = 0;
			for (Entity entity : entities) {
				for (Object object : entity.getSubEntities()) {
					if (object instanceof SubEntity) {
						SubEntity nextEntity = (SubEntity) object;

						// Use inner hit (HSP?) for chunk solids.
						int startSH = nextEntity.getStartOnQuery();
						int endSH = nextEntity.getEndOnQuery();

                        // The overall glyph.
						//System.out.println("Generating seq solid from " + startSH + " to " + endSH);
						MeshView subHitView = null;
						if (nextEntity.getPriority() == 0) {
							subHitView = generateRectSolid(startSH, endSH + 1);
						} else {
							subHitView = generateRectSolid(startSH, endSH + 1, nextEntity.getPriority() * 0.001f);
						}
//                        part.setWorldPositioner( worldPositioner );
//                        if ( ! isAnchor( nextEntity ) ) {
//                            part.setPreAnimator( rotationAnimator );
//                        }
						rtnVal.getChildren().add(subHitView);
						if (!isAnchor(nextEntity)) {
							// Do a rotation.
							subHitView.getTransforms().add(new Rotate(rotatePos, 0, 0, 0, Rotate.X_AXIS));
							rotatePos += rotOffs;
						} else {
							// Have an anchor.  Place it separately.
							subHitView.setRotationAxis(Rotate.X_AXIS);
							subHitView.setRotate(4.0f / 7.0f * 360.0);
							subHitView.setTranslateY(Constants.ANCHOR_OFFSET);
							subHitView.setTranslateZ(-Constants.ANCHOR_OFFSET);

							// Add a label to the main screen.
							addAnchorLabel(entity, rtnVal);
						}
					}
				}
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		//Stubbed
		return rtnVal;
	}

	/**
	 * Makes a text-2D label explaining what anchor is being shown.
	 *
	 * @param entity
	 * @param parentGroup
	 */
	private void addAnchorLabel(Entity entity, TransformableGroup parentGroup) {
		boolean foundAnchor = false;
		for (int i = 0; i < entity.getSubEntities().size(); i++) {
			SubEntity subEnt = (SubEntity) entity.getSubEntities().get(i);
			if (isAnchor(subEnt)) {
				foundAnchor = true;
				addAnchorLabel(subEnt.getProperties(), parentGroup);
			}
		}
		if (! foundAnchor ) {
			SubEntity subEnt = (SubEntity)entity.getSubEntities().get(0);
			if (subEnt != null) {
				addAnchorLabel( subEnt.getProperties(), parentGroup );
			}
		}
	}

	public void addAnchorLabel(Map<String,Object> props, TransformableGroup parentGroup) {
		String name = (String) props.get(DataSource.NAME_PROPERTY);
		if (name == null) {
			name = (String) props.get(DataSource.ANCHOR_ID_PROPERTY);
		}
		if (name == null) {
			name = "Unknown";
		}
		System.out.println("Creating Anchor Label: " + name);
		Text text = new Text(-50, 50.0, name);
		text.setFill(Color.WHITE);
		text.setFont(Font.font("Verdana", FontPosture.REGULAR, 10));
		parentGroup.getChildren().add(text);
	}

	/**
	 * An override to support default offset.
	 */
	private MeshView generateRectSolid(int startSH, int endSH) {
		return generateRectSolid(startSH, endSH, 0.0f);
	}

	/**
	 * Builds a rectangular solid geometry based on always-same Y,Z extents.
	 */
	private MeshView generateRectSolid(int startSH, int endSH, float extraYDisp) {
		return generateRectSolid(startSH, endSH, extraYDisp, Constants.ZB, Constants.ZF);
	}

	/**
	 * Check: is this entity an anchor? TODO consider adding this behavior to
	 * all entities.
	 *
	 * @param entity what to check
	 * @return true if it is
	 */
	private boolean isAnchor(Entity entity) {
		for (Object nextObj : entity.getSubEntities()) {
			SubEntity nextSubEnt = (SubEntity) nextObj;
			if (isAnchor(nextSubEnt)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check: is this entity an anchor? TODO consider adding this behavior to
	 * all entities.
	 *
	 * @param subEntity what to check
	 * @return true if it is
	 */
	private boolean isAnchor(SubEntity subEntity) {
		Map<String, Object> props = subEntity.getProperties();
		String entityType = (String) props.get(DataSource.SUB_ENTITY_TYPE_PROPERTY_NAME);
		if (entityType == null) {
			return false;
		}
		if (entityType.toLowerCase().equals(DataSource.ANCHOR_TYPE.toLowerCase())) {
			return true;
		}
		return false;
	}

	/**
	 * Builds a rectangular solid geometry based on always-same Y,Z extents.
	 */
	private MeshView generateRectSolid(int startSH, int endSH, float extraYDisp, float zBack, float zFront) {

        // The translations: the start and end need to be normalized for
		// a certain meter length.  They also need to be centered in that length.
		// Assume query IS coordinate system, and starts at zero.
		float xl = translateToJava3dCoords(startSH); //((float)startSH * factor) - START_OF_CYLINDER;
		float xr = translateToJava3dCoords(endSH);   //((float)endSH * factor) - START_OF_CYLINDER;
		float[] coordinateData = new float[]{
			// The 'lid'
			xl, Constants.YT + extraYDisp, zBack, //0
			xl, Constants.YT + extraYDisp, zFront, //1
			xr, Constants.YT + extraYDisp, zFront, //2
			xl, Constants.YT + extraYDisp, zBack, //3
			xr, Constants.YT + extraYDisp, zFront, //4
			xr, Constants.YT + extraYDisp, zBack, //5
			// The 'left'
			xl, Constants.YT + extraYDisp, zBack, //6
			xl, Constants.YB + extraYDisp, zBack, //7
			xl, Constants.YT + extraYDisp, zFront, //8
			xl, Constants.YT + extraYDisp, zFront, //9
			xl, Constants.YB + extraYDisp, zBack, //10
			xl, Constants.YB + extraYDisp, zFront, //11
			// The 'right'
			xr, Constants.YT + extraYDisp, zBack, //12
			xr, Constants.YT + extraYDisp, zFront, //13
			xr, Constants.YB + extraYDisp, zBack, //14
			xr, Constants.YT + extraYDisp, zFront, //15
			xr, Constants.YB + extraYDisp, zFront, //16
			xr, Constants.YB + extraYDisp, zBack, //17
			// The 'front'
			xl, Constants.YT + extraYDisp, zFront, //18
			xl, Constants.YB + extraYDisp, zFront, //19
			xr, Constants.YT + extraYDisp, zFront, //20
			xl, Constants.YB + extraYDisp, zFront, //21
			xr, Constants.YB + extraYDisp, zFront, //22
			xr, Constants.YT + extraYDisp, zFront, //23
			// The 'bottom'
			xl, Constants.YB + extraYDisp, zBack, //24
			xr, Constants.YB + extraYDisp, zBack, //25
			xr, Constants.YB + extraYDisp, zFront, //26
			xl, Constants.YB + extraYDisp, zBack, //27
			xr, Constants.YB + extraYDisp, zFront, //28
			xl, Constants.YB + extraYDisp, zFront, //29
			// The 'back'
			xr, Constants.YT + extraYDisp, zBack, //30
			xr, Constants.YB + extraYDisp, zBack, //31
			xl, Constants.YB + extraYDisp, zBack, //32
			xr, Constants.YT + extraYDisp, zBack, //33
			xl, Constants.YB + extraYDisp, zBack, //34
			xl, Constants.YT + extraYDisp, zBack, //35
		};

		return createMesh(coordinateData, texCoordGenerator.generateTexCoords(coordinateData));
	}

	private float translateToJava3dCoords(int seqCoord) {
		return ((float) (seqCoord - startRange) * factor) - Constants.START_OF_CYLINDER;

	}

	private MeshView createMesh(float[] vertices, float[] texCoords) {
		TriangleMesh tm = new TriangleMesh();
		tm.getPoints().addAll(vertices);
		tm.getTexCoords().addAll(texCoords);
		// Only one tex coord.
		int[] faces = new int[]{
			0, 0, 1, 0, 2, 0,
			3, 0, 4, 0, 5, 0,
			6, 0, 7, 0, 8, 0,
			9, 0, 10, 0, 11, 0,
			12, 0, 13, 0, 14, 0,
			15, 0, 16, 0, 17, 0,
			18, 0, 19, 0, 20, 0,
			21, 0, 22, 0, 23, 0,
			24, 0, 25, 0, 26, 0,
			27, 0, 28, 0, 29, 0,
			30, 0, 31, 0, 32, 0,
			33, 0, 34, 0, 35, 0
		};
		tm.getFaces().addAll(faces);
		MeshView meshView = new MeshView(tm);
		meshView.setCullFace(CullFace.BACK);

		final PhongMaterial meshMaterial = new PhongMaterial();
		meshMaterial.setDiffuseColor(Color.DODGERBLUE);
		meshMaterial.setSpecularColor(Color.ALICEBLUE);
		//meshMaterial.setSpecularPower(10000);
		meshView.setMaterial(meshMaterial);

		meshView.setId("Homemade Shape");
		return meshView;
	}

	/**
	 * Creates the ruler or scale to be shown, imobile (not spinning), but which
	 * always remains hovering above the spinning cylinder.
	 *
	 * @return geometry for a ruler to span the cylinder.
	 */
	public float[] generateRuleGeometry() {
        // Establish end points, as being wherever the cylinder's lowest possible point would be,
		// up to wherever the highest possible point would be.
		float xl = -Constants.START_OF_CYLINDER;
		float xr = Constants.LENGTH_OF_CYLINDER - Constants.START_OF_CYLINDER;
		float yBottom = Constants.YT + 0.05f;
		float ruleYTop = Constants.YT + 0.2f;
		float zBack = -0.02f;
		float[] coordinateData = new float[]{
			// Front face
			xl, ruleYTop, 0.0f, //0
			xr, yBottom, 0.0f, //2
			xr, ruleYTop, 0.0f, //1
			xl, ruleYTop, 0.0f, //3
			xl, yBottom, 0.0f, //5
			xr, yBottom, 0.0f, //4
			// Left side
			xl, ruleYTop, 0.0f, //6
			xl, yBottom, 0.0f, //8
			xl, ruleYTop, zBack, //7
			xl, yBottom, 0.0f, //9
			xl, yBottom, zBack, //11
			xl, ruleYTop, zBack, //10
			// Top
			xl, ruleYTop, 0.0f, //12
			xr, ruleYTop, 0.0f, //14
			xl, ruleYTop, zBack, //13
			xl, ruleYTop, 0.0f, //15
			xr, ruleYTop, 0.0f, //17
			xr, ruleYTop, zBack, //16
			// Right side
			xr, ruleYTop, zBack, //18
			xr, yBottom, zBack, //20
			xr, ruleYTop, 0.0f, //19
			xr, ruleYTop, zBack, //21
			xr, yBottom, zBack, //23
			xr, yBottom, 0.0f, //22
			// Back face
			xr, ruleYTop, zBack, //24
			xr, Constants.RULE_Y_BOTTOM, zBack, //26
			xl, Constants.RULE_Y_BOTTOM, zBack, //25
			xl, ruleYTop, zBack, //27
			xl, Constants.RULE_Y_BOTTOM, zBack, //29
			xr, ruleYTop, zBack, //28
			// Bottom
			xl, Constants.RULE_Y_BOTTOM, zBack, //30
			xr, Constants.RULE_Y_BOTTOM, 0.0f, //32
			xl, Constants.RULE_Y_BOTTOM, 0.0f, //31
			xl, Constants.RULE_Y_BOTTOM, zBack, //33
			xr, Constants.RULE_Y_BOTTOM, zBack, //35
			xr, Constants.RULE_Y_BOTTOM, 0.0f, //34
		};
		return coordinateData;
	}

	//-----------------------------------HELPER METHODS
//    /** Builds a rectangular solid geometry with a single hole. */
//    private GeometryInfo generatePerforatedSolid(int startSH, int endSH, float extraYDisp) {
//
//        // The translations: the start and end need to be normalized for
//        // a certain meter length.  They also need to be centered in that length.
//        // Assume query IS coordinate system, and starts at zero.
//        float xl = translateToJava3dCoords(startSH); //((float)startSH * factor) - START_OF_CYLINDER;
//        float xr = translateToJava3dCoords(endSH);   //((float)endSH * factor) - START_OF_CYLINDER;
//        GeometryInfo gi = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
//        float[] coordinateData = new float[] {
//                // ONE 'hole in the top' to simulate a gap in the subject sequence.
//                xl, Constants.YT + extraYDisp, Constants.ZB + 0.01f,
//                xr, Constants.YT + extraYDisp, Constants.ZB + 0.01f,
//                xr, Constants.YT + extraYDisp, Constants.ZF - .01f,
//                xl, Constants.YT + extraYDisp, Constants.ZF - .01f,         
//                
//                // The 'hole in the bottom'
//                xl, Constants.YB + extraYDisp, Constants.ZB + 0.01f,
//                xl, Constants.YB + extraYDisp, Constants.ZF - .01f,
//                xr, Constants.YB + extraYDisp, Constants.ZF - .01f,
//                xr, Constants.YB + extraYDisp, Constants.ZB + 0.01f,
//
//                // The 'left'
//                xl, Constants.YT + extraYDisp - .01f, Constants.ZB + .01f,
//                xl, Constants.YT + extraYDisp - .01f, Constants.ZF - .01f,
//                xr, Constants.YB + extraYDisp - .01f, Constants.ZF - .01f,
//                xr, Constants.YB + extraYDisp - .01f, Constants.ZB + .01f,
//                // The 'right'
//                xr, Constants.YT + extraYDisp - .01f, Constants.ZB + .01f,
//                xr, Constants.YB + extraYDisp - .01f, Constants.ZB + .01f,
//                xr, Constants.YB + extraYDisp - .01f, Constants.ZF - .01f,
//                xr, Constants.YT + extraYDisp - .01f, Constants.ZF - .01f,
//                // The 'front'
//                xl, Constants.YT + extraYDisp - .01f, Constants.ZF - .01f,
//                xl, Constants.YT + extraYDisp - .01f, Constants.ZF - .01f,
//                xl, Constants.YB + extraYDisp - .01f, Constants.ZF - .01f,
//                xl, Constants.YB + extraYDisp - .01f, Constants.ZF - .01f,
//                // The 'back'   
//                xr, Constants.YT + extraYDisp - .01f, Constants.ZB + .01f,
//                xl, Constants.YT + extraYDisp - .01f, Constants.ZB + .01f,
//                xl, Constants.YB + extraYDisp - .01f, Constants.ZB + .01f,
//                xr, Constants.YB + extraYDisp - .01f, Constants.ZB + .01f,
//        };
//
//        gi.setCoordinates(coordinateData);
//        return gi;
//    }
//
//    /** Add shapes for any insertions in subject, relative to query. */
//    private void generateSubjectInsertions(SubEntity subEntity, BranchGroup hitGroup, int[][] queryGaps) {
//    	int startSH = subEntity.getStartOnQuery();
//    	for (int i = 0; i < queryGaps.length; i++) {
//    		int[] nextGap = queryGaps[i];
//            GeometryInfo gi = generateRectSolid(startSH + nextGap[0], startSH + nextGap[1], 0.025f, Constants.ZB + 0.01f, Constants.ZF - 0.01f);
//            finalizeGeometry(gi);
//            Shape3D part = new Shape3D();
//            part.setCapability(Node.ENABLE_PICK_REPORTING);
//            part.setPickable(true);
//            part.setBoundsAutoCompute(true);
//            part.setAppearance(appearanceSource.createSubEntityInsertionAppearance(subEntity));
//            part.setGeometry(gi.getGeometryArray());
//            part.getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
//            hitGroup.addChild(part);
//    		
//    	}
//    }
//
//    /** Add shapes for any gaps in subject, relative to query. */
//    private void generateSubjectGaps(SubEntity subEntity, BranchGroup hitGroup, int[][] subjectGaps) {
//    	int startSH = subEntity.getStartOnQuery();
//		for (int i = 0; i < subjectGaps.length; i++) {
//    		int[] nextGap = subjectGaps[i];
//            //  System.out.println("Generating perforation at " + (startSH + pos) + " through " + (startSH + endPos));
//            GeometryInfo gi = generatePerforatedSolid(startSH + nextGap[0],
//                    startSH + nextGap[1], 0.001f);
//            finalizeGeometry(gi);
//            Shape3D part = new Shape3D();
//            part.setAppearance(appearanceSource.createPerforatedAppearance(subEntity));
//            part.setGeometry(gi.getGeometryArray());
//            part.getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
//            hitGroup.addChild(part);
//    		
//    	}
//    }
//
//    /**
//     * Adds "dentils" to the subhit. Each represents a residue--either an amino acid or a base.
//     * 
//     * @param subEntity data model to drive the glyph-building
//     * @param hitGroup where to attach the new hit glyph.
//     */
//    private void generateResidueDentils(SubEntity subEntity, BranchGroup hitGroup) {
//        GeometryInfo gi = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
//
//    	Map props = subEntity.getProperties();
//    	if (props != null) {
//    		// NOTE: putting residues from subject, at position relative to query.
//        	String subjectResidues = (String)props.get(DataSource.SUBJECT_ALIGNMENT);
//        	String queryResidues = (String)props.get(DataSource.QUERY_ALIGNMENT);
//        	int startPos = subEntity.getStartOnQuery();
//	        NormalGenerator ng = new NormalGenerator();
//	        Stripifier st = new Stripifier();
//	        String dentilResidues = subjectResidues;
//	        boolean isAnchor = isAnchor(subEntity);
//	        boolean isBase = subEntity.getProperties().get(DataSource.SUB_ENTITY_TYPE_PROPERTY_NAME).equals(DataSource.ENTITY_TYPE_BLAST_N_SUB_HIT);
// 
//        	if (dentilResidues != null) {
//        		int[] stripCount = null;
//        		if (prominentDentils) {
//        			stripCount = new int[] {4,4,4,4,4,4};
//        		}
//        		else {
//        			stripCount = new int[] {4,4,4,};
//        		}
//
//        		// Make the glyphs--enough for all residues in the set.
//        		for (int i = 0; i < dentilResidues.length(); i++) {
//        			// When to do it and when not to do it.
//        			if ((!isAnchor) && diffResiduesOnly && (queryResidues != null) && (queryResidues.charAt(i) == subjectResidues.charAt(i)))
//            			continue;  // Diffs only
//        			if (dentilResidues.charAt(i) == '-')
//        				continue;
//
//        			if (prominentDentils)
//        			    gi = generateRectSolid(startPos + i, startPos + i + 1, 0.001f, Constants.ZB, Constants.ZB+0.01f);
//        			else
//        				gi = generateFacadeBox(startPos + i, startPos + i + 1, 0.001f, Constants.ZB-0.001f, Constants.ZB, gi);
// 
//        			Shape3D part = new Shape3D();
//        			Appearance materialAppearance = appearanceSource.createSubEntityAppearance(dentilResidues.charAt(i), isBase);
//        			if (materialAppearance == null)
//        				materialAppearance = createMaterialAppearance();
//        			part.setAppearance(materialAppearance);
//
//        	        gi.setStripCounts(stripCount);
//
//        	        ng.generateNormals(gi);
//
//        	        st.stripify(gi);
//        	        gi.recomputeIndices();
//
//        			part.setGeometry(gi.getGeometryArray());
//        			part.getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
//        			part.setCapability(Node.ALLOW_AUTO_COMPUTE_BOUNDS_READ);
//        			part.setBoundsAutoCompute(true);
//        			hitGroup.addChild(part);
//        		}
//        	}
//    	}
//    }
//    /** Builds a rectangular solid geometry based on always-same Y,Z extents. */
//    private GeometryInfo generateRectSolid(int startSH, int endSH, float extraYDisp, float zBack, float zFront) {
//
//        // The translations: the start and end need to be normalized for
//        // a certain meter length.  They also need to be centered in that length.
//        // Assume query IS coordinate system, and starts at zero.
//        float xl = translateToJava3dCoords(startSH); //((float)startSH * factor) - START_OF_CYLINDER;
//        float xr = translateToJava3dCoords(endSH);   //((float)endSH * factor) - START_OF_CYLINDER;
//        GeometryInfo gi = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
//        float[] coordinateData = new float[] {
//                // The 'lid'
//                xl, Constants.YT + extraYDisp, zBack,
//                xl, Constants.YT + extraYDisp, zFront,
//                xr, Constants.YT + extraYDisp, zFront,
//                xr, Constants.YT + extraYDisp, zBack,
//                // The 'left'
//                xl, Constants.YT + extraYDisp, zBack,
//                xl, Constants.YB + extraYDisp, zBack,
//                xl, Constants.YB + extraYDisp, zFront,
//                xl, Constants.YT + extraYDisp, zFront,
//                // The 'right'
//                xr, Constants.YT + extraYDisp, zBack,
//                xr, Constants.YT + extraYDisp, zFront,
//                xr, Constants.YB + extraYDisp, zFront,
//                xr, Constants.YB + extraYDisp, zBack,
//                // The 'front'
//                xl, Constants.YT + extraYDisp, zFront,
//                xl, Constants.YB + extraYDisp, zFront,
//                xr, Constants.YB + extraYDisp, zFront,
//                xr, Constants.YT + extraYDisp, zFront,
//                // The 'bottom'
//                xl, Constants.YB + extraYDisp, zBack,
//                xl, Constants.YB + extraYDisp, zFront,
//                xr, Constants.YB + extraYDisp, zFront,
//                xr, Constants.YB + extraYDisp, zBack,
//                // The 'back'   
//                xr, Constants.YT + extraYDisp, zBack,
//                xr, Constants.YB + extraYDisp, zBack,
//                xl, Constants.YB + extraYDisp, zBack,
//                xl, Constants.YT + extraYDisp, zBack,
//        };
//
//        gi.setCoordinates(coordinateData);
//        return gi;
//    }
//
	/**
	 * Builds a rectangular solid geometry based on always-same Y,Z extents.
	 */
	private float[] generateFacadeBox(int startSH, int endSH, float extraYDisp, float zBack, float zFront) {

        // The translations: the start and end need to be normalized for
		// a certain meter length.  They also need to be centered in that length.
		// Assume query IS coordinate system, and starts at zero.
		float xl = translateToJava3dCoords(startSH);
		float xr = translateToJava3dCoords(endSH);
		float yb = Constants.YT + extraYDisp - 0.01f;
		float[] coordinateData = new float[]{
			// The 'lid'
			xl, Constants.YT + extraYDisp, zBack, //0
			xl, Constants.YT + extraYDisp, zFront, //1
			xr, Constants.YT + extraYDisp, zBack, //3
			xl, Constants.YT + extraYDisp, zBack, //0
			xr, Constants.YT + extraYDisp, zFront, //2
			xr, Constants.YT + extraYDisp, zBack, //3
			// The 'front'
			xl, Constants.YT + extraYDisp, zFront, //4
			xl, yb, zFront, //5
			xr, Constants.YT + extraYDisp, zFront, //7
			xr, Constants.YT + extraYDisp, zFront, //7
			xl, yb, zFront, //5
			xr, yb, zFront, //6
			// The 'back'
			xr, Constants.YT + extraYDisp, zBack, //8
			xr, yb, zBack, //9
			xl, Constants.YT + extraYDisp, zBack, //11
			xr, Constants.YT + extraYDisp, zBack, //8
			xl, yb, zBack, //10
			xl, Constants.YT + extraYDisp, zBack, //11
		};
		return coordinateData;
	}

	private MeshView createRuler(long anchorLength) {
		float[] coordinateData = generateRuleGeometry();
		MeshView meshView = createMesh(coordinateData, texCoordGenerator.generateTexCoords(coordinateData));
		final PhongMaterial meshMaterial = new PhongMaterial();
		meshMaterial.setDiffuseColor(Color.WHITE);
		meshMaterial.setSpecularColor(Color.WHITE);
		//meshMaterial.setSpecularPower(10000);
		meshView.setMaterial(meshMaterial);
		
		return meshView;
	}

	private TransformableGroup createAnchor(long anchorLength) {
		// Stubbed
		return new TransformableGroup();
	}

	private Text createLabel() {
		// Stubbed.
		Text rtnVal = new Text("The Cylinder");
		rtnVal.setTranslateX(-Constants.LENGTH_OF_CYLINDER / 2.0);
		rtnVal.setTranslateY(+Constants.LENGTH_OF_CYLINDER / 2.0);
		return rtnVal;
	}

	//
	// The handleCameraViews file contains the handleMouse() and handleKeyboard() 
	// methods that are used in the MoleculeSampleApp application to handle the 
	// different 3D camera views.  These methods are used in the Getting Started with 
	// JavaFX 3D Graphics tutorial. 
	//
	private void handleMouse(Scene scene) {
		scene.setOnMousePressed(new MousePressedHandler(mouseLocationModel));
		scene.setOnMouseDragged(new MouseDraggedHandler(mouseLocationModel, selectionModel, cameraModel));
	}

	private void handleKeyboard(Scene scene) {
		scene.setOnKeyPressed(new KeyEventHandler(cameraModel));
	}

}
