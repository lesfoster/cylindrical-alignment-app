/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.viewer.java_fx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Line;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import javax.swing.SwingUtilities;

import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.lookup.InstanceContent;

import self.lesfoster.cylindrical_alignment.effector.Effected;
import self.lesfoster.cylindrical_alignment.effector.Effector;
import self.lesfoster.cylindrical_alignment.effector.ConcreteSpeedEffector;
import self.lesfoster.cylindrical_alignment.effector.SpeedEffectorTarget;
import self.lesfoster.cylindrical_alignment.constants.Constants;
import self.lesfoster.cylindrical_alignment.data_source.DataSource;
import self.lesfoster.cylindrical_alignment.data_source.Entity;
import self.lesfoster.cylindrical_alignment.data_source.SubEntity;
import self.lesfoster.cylindrical_alignment.effector.ConcreteCylinderPositioningEffector;
import self.lesfoster.cylindrical_alignment.effector.ConcreteHelpEffector;
import self.lesfoster.cylindrical_alignment.effector.ConcreteSettingsEffector;
import self.lesfoster.cylindrical_alignment.effector.CylinderPositioningEffectorTarget;
import self.lesfoster.cylindrical_alignment.effector.HelpEffectorTarget;
import self.lesfoster.cylindrical_alignment.effector.SettingsEffectorTarget;
import self.lesfoster.cylindrical_alignment.effector.SpeedEffector;
import self.lesfoster.cylindrical_alignment.geometry.TexCoordGenerator;
import self.lesfoster.cylindrical_alignment.viewer.appearance_source.AppearanceSource;
import self.lesfoster.cylindrical_alignment.viewer.appearance_source.AppearanceSourceFactory;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.events.KeyEventHandler;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.events.MouseDraggedHandler;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.events.MousePressedHandler;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.group.TransformableGroup;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.gui_model.CameraModel;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.gui_model.MouseLocationModel;
import self.lesfoster.framework.integration.SelectionModel;
import static self.lesfoster.cylindrical_alignment.viewer.appearance_source.AppearanceSource.OPACITY;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.events.GlyphSelector;
import self.lesfoster.framework.integration.SelectedObjectWrapper;
import self.lesfoster.framework.integration.SelectionModelListener;

/**
 * Contains all the domain-oriented objects, which are also created here.
 *
 * @author Leslie L Foster
 */
public class CylinderContainer extends JFXPanel  
        implements SpeedEffectorTarget, CylinderPositioningEffectorTarget,
		           HelpEffectorTarget, SettingsEffectorTarget, 
				   Effected {
	public static final String SPIN_GROUP_ID = "SPIN_GROUP";
	private static final double CAMERA_DISTANCE = Constants.LENGTH_OF_CYLINDER * 3;
	private static final int BAND_CIRCLE_VERTEX_COUNT = 100;

	private int startRange = 0;
	private int endRange = 0;
	private int selectionEnvelope = 0;
	
	private boolean prominentDentils;
	private boolean diffResiduesOnly;
	private boolean usingResidueDentils = true;
	private float factor = 0;

	private final Group world = new Group();
	private final TransformableGroup root = new TransformableGroup();
	private final TransformableGroup positionableObject = new TransformableGroup();
	private final TransformableGroup lowCigarBandSlide = new TransformableGroup();
	private final TransformableGroup highCigarBandSlide = new TransformableGroup();
	private Text lowCigarBandLabel;
	private Text highCigarBandLabel;
	
	// TODO decide if these should be used or eliminated.
	private TransformableGroup cylinder;
	//private TransformableGroup ruler;
	//private TransformableGroup anchor;
	private Scene scene;
	private final Map<String,SubEntity> idToSubEntity = new HashMap<>();
	private final Map<String,Node> idToShape = new HashMap<>();
	private GlyphSelector subEntitySelector;
	private int latestGraphId = 1;
	private int duration = SpeedEffector.INITIAL_SPEED_DURATION;
	private double naturalSpinRate = 0;
	private RotateTransition rotateTransform;

	private final MouseLocationModel mouseLocationModel = new MouseLocationModel();
	private MouseDraggedHandler mouseDraggedHandler;
	private final SelectionModel selectionModel = SelectionModel.getSelectionModel();
	private final CameraModel cameraModel = new CameraModel();	
	private AppearanceSource appearanceSource;
	private Text inSceneLabel;
	private boolean dark = true;
	private DataSource dataSource;
	private Map<String, Object> propMap = new HashMap<>();
	private InstanceContent instanceContent;
	private SelectionModelListener selectionListener;
	private LookupListener selectedObjLookupListener;
	private Lookup.Result<SelectedObjectWrapper> selectionWrapperResult;
	
	private final TexCoordGenerator texCoordGenerator = new TexCoordGenerator();
	private final Logger log = Logger.getLogger(CylinderContainer.class.getName());

	public CylinderContainer(DataSource dataSource, InstanceContent instanceContent) {
		this(dataSource, 0, dataSource.getAnchorLength(), instanceContent);
	}

	public CylinderContainer(DataSource dataSource, Integer startRange, Integer endRange, InstanceContent instanceContent) {
		this.dataSource = dataSource;
		this.startRange = startRange;
		this.endRange = endRange;
		this.instanceContent = instanceContent;
		factor = Constants.LENGTH_OF_CYLINDER / (endRange - startRange);
		init(dataSource);
		instanceContent.add(propMap);
		selectionListener = (Object obj) -> {
			SwingUtilities.invokeLater(() -> {
				instanceContent.remove(propMap);
				SubEntity se = idToSubEntity.get(obj.toString());
				if (se != null) {
					//System.out.println("Got selected sub-entity " + se.toString());                    
					propMap = se.getProperties();
					instanceContent.add(propMap);
				}
			});
			
			positionCigarBands(obj);
		};
		selectionModel.addListener(selectionListener);
		Lookup global = Utilities.actionsGlobalContext();
		selectionWrapperResult = global.lookupResult(SelectedObjectWrapper.class);
		if (selectionWrapperResult != null) {
			selectedObjLookupListener = new ModelSelectionWrapperLookupListener();
			selectionWrapperResult.addLookupListener(selectedObjLookupListener);
		}
	}

	//----------------------------------------IMPLEMENTS Effected
	/**
	 * Provides "the hookup" for interacting with aspects of this component.
	 *
	 * @return array of effectors which can be called in response to events.
	 */
	@Override
	public Effector[] getEffectors() {
		return new Effector[]{
			new ConcreteSpeedEffector(this),
			new ConcreteHelpEffector(this, this),
			new ConcreteSettingsEffector(this),
			new ConcreteCylinderPositioningEffector(this),
		};
	}
	
	//----------------------------------------IMPLEMENTS SpeedEffectorTarget
	@Override
	public int getDuration() {
		return duration;
	}

	@Override
	public void setDuration(int duration) {
		if (duration == -1) {
			rotateTransform.setRate(0);
		}
		else {
			rotateTransform.setDuration(Duration.millis(duration));			
			rotateTransform.setRate(naturalSpinRate);
			rotateTransform.stop();
			rotateTransform.play();
		}
		this.duration = duration;
	}

	//----------------------------------------IMPLEMENTS CylinderPositioningEffectorTarget
	@Override
	public MouseLocationModel getMouseLocationModel() {
		return mouseLocationModel;
	}
	
	@Override
	public MouseDraggedHandler getMouseDraggedHandler() {
		return mouseDraggedHandler;
	}
	
	//----------------------------------------IMPLEMENTS HelpEffectorTarget
	@Override
	public String getInputFile() {
		return dataSource.toString();
	}

	//----------------------------------------IMPLEMENTS SettingsEffectorTarget
	@Override
	public Scene getUniverse() {
		return scene;
	}

	@Override
	public void setEnvelopeDistance(int envelopeDistance) {
		this.selectionEnvelope = envelopeDistance;
	}

	public void addAnchorLabel(Map<String, Object> props, TransformableGroup parentGroup) {
		String name = (String) props.get(DataSource.NAME_PROPERTY);
		if (name == null) {
			name = (String) props.get(DataSource.ANCHOR_ID_PROPERTY);
		}
		if (name == null) {
			name = "Unknown";
		}
		System.out.println("Creating Anchor Label: " + name);
		inSceneLabel.setText(name);
	}
	
	public void dispose() {
		SelectionModel.getSelectionModel().removeListener(selectionListener);
		SelectionModel.getSelectionModel().clear();
		// Must clear the legend model.
		appearanceSource.clear();
		instanceContent.remove(propMap);
	}
	
	private void init(final DataSource dataSource) {
		Platform.runLater(() -> {
			//root.ry.setAngle(180.0);
			scene = new Scene(world, this.getWidth(), this.getHeight(), true, SceneAntialiasing.BALANCED);
			scene.setCamera(cameraModel.getCamera());
			if (dark) {
				scene.setFill(Constants.FILL_DARK_COLOR);
			} else {
				scene.setFill(Constants.FILL_LIGHT_COLOR);
			}
			setScene(scene);
			appearanceSource = AppearanceSourceFactory.getSourceForFile(null);
			world.getChildren().add(root);
			createCamera();
			inSceneLabel = createLabel();
			createPositionableObjectHierarchy(dataSource);
			root.getChildren().add(positionableObject);
			root.getChildren().add(inSceneLabel);

			// This must be populated after entities all created.
			subEntitySelector = new GlyphSelector(selectionModel, idToShape, idToSubEntity);
			handleMouse(scene);
			handleKeyboard(scene);
		});
	}

	/**
	 * Camera will establish the default orientation for Java FX: Y progresses
	 * downward.  Much monkeying here, as convinced me it will be easier for
	 * me to invert the geometry calculations, than to attempt to get the right
	 * camera transform to make this scenegraph look like OpenGL or Java3D.
	 */
	private void createCamera() {
		root.getChildren().add(cameraModel.getCameraXform());
		// Doing this gives correct mouse-move behavior, and to push things
		// to upward when I use higher numbered y coords,
		// but it also causes text to be reflected top/bottom.
		//cameraModel.getCameraXform().ry.setAngle(180.0);
		//cameraModel.getCameraXform().rz.setAngle(180.0);
		cameraModel.getCameraXform().getChildren().add(cameraModel.getCamera());

		cameraModel.getCamera().setNearClip(1.0);
		cameraModel.getCamera().setFarClip(1000.0);
		cameraModel.getCamera().setTranslateZ(-CAMERA_DISTANCE);
		cameraModel.getCameraXform().ry.setAngle(MouseLocationModel.DEFAULT_Y_ANGLE);
		// Moves to left/centered at midpoint anyway. cameraModel.getCamera().setTranslateX(100.0);
		
	}

	/**
	 * This creates one large hierarchy of objects which may be moved using
	 * mouse drag operations. Within this hierarchy will lie other things which
	 * may or may not move/animate with the larger whole.
	 */
	private TransformableGroup createPositionableObjectHierarchy(DataSource dataSource) {
		cylinder = createCylinder(dataSource.getEntities());
		TransformableGroup spinGroup = null;
		for (Node child: cylinder.getChildren()) {
			String id = child.getId();
			if (id != null  &&  id.equals(SPIN_GROUP_ID)) {
				spinGroup = (TransformableGroup)child;
			}
		}
		rotateTransform = new RotateTransition();
		this.naturalSpinRate = rotateTransform.getRate();
		rotateTransform.setFromAngle(360.0);
		rotateTransform.setToAngle(0.0);
		rotateTransform.setDuration(Duration.millis(duration));
		rotateTransform.setInterpolator(Interpolator.LINEAR); //Runs smoothly.
		rotateTransform.setCycleCount(Animation.INDEFINITE);  //Runs forever.
		rotateTransform.setByAngle(0.01);
		rotateTransform.setAxis(new Point3D(1.0, 0.0, 0.0));
		positionableObject.getChildren().add(cylinder);
		final int anchorLength = dataSource.getAnchorLength();
		positionableObject.getChildren().add(createRuler(anchorLength));
		positionableObject.getChildren().addAll(createTickBands());
		lowCigarBandSlide.getChildren().add(createCigarBandGroup(true));
		highCigarBandSlide.getChildren().add(createCigarBandGroup(false));
		lowCigarBandLabel.setText("" + startRange);
		highCigarBandLabel.setText("" + endRange);
		positionableObject.getChildren().add(lowCigarBandSlide);
		positionableObject.getChildren().add(highCigarBandSlide);

		rotateTransform.setNode(spinGroup);
		rotateTransform.play();
		return positionableObject;
	}

	private TransformableGroup createCylinder(List<Entity> entities) {
		TransformableGroup rtnVal = new TransformableGroup();
		TransformableGroup spinGroup = new TransformableGroup();
		spinGroup.setId(SPIN_GROUP_ID);
		rtnVal.getChildren().add(spinGroup);
		try {
			double rotOffs = 360.0 / entities.size();
			double rotatePos = 0;
			SelectionModel selectionModel = SelectionModel.getSelectionModel();
			for (Entity entity : entities) {
				for (Object object : entity.getSubEntities()) {
					if (object instanceof SubEntity) {
						SubEntity nextEntity = (SubEntity) object;
						final boolean anchorFlag = isAnchor(nextEntity);

						// Use inner hit (HSP?) for chunk solids.
						int startSH = nextEntity.getStartOnQuery();
						int endSH = nextEntity.getEndOnQuery();

                        // The overall glyph.
						//log.debug("Generating seq solid from " + startSH + " to " + endSH);
						Group subHitGroup = new Group();
						MeshView subHitView = null;
						final String lookupId = Integer.toString(latestGraphId);
						if (!anchorFlag  &&  usingResidueDentils) {
							generateResidueDentils(nextEntity, subHitGroup);
						}
						if (nextEntity.getPriority() == 0) {
							subHitView = generateRectSolid(startSH, endSH + 1, nextEntity);
						} else {
							subHitView = generateRectSolid(startSH, endSH + 1, nextEntity.getPriority() * 0.01f, nextEntity);
						}
						subHitView.setId(lookupId);
						idToSubEntity.put(lookupId, nextEntity);
						idToShape.put(lookupId, subHitView);
						selectionModel.setIdToObject(lookupId, nextEntity);
						latestGraphId ++;

						if (!anchorFlag) {
							spinGroup.getChildren().add(subHitGroup);
							subHitGroup.getChildren().add(subHitView);
							if (nextEntity.getGapsOnQuery().length > 0) {
								generateSubjectInsertions(nextEntity, subHitGroup, nextEntity.getGapsOnQuery());
							}
							if (nextEntity.getGapsOnSubject().length > 0) {
								generateSubjectGaps(nextEntity, subHitGroup, nextEntity.getGapsOnSubject());
							}
							// Do a rotation.
							subHitGroup.getTransforms().add(new Rotate(rotatePos, 0, 0, 0, Rotate.X_AXIS));
							rotatePos += rotOffs;
						} else {
							// Have an anchor.  Place it separately.
							rtnVal.getChildren().add(subHitGroup);
							createAnchor(subHitView, entity, rtnVal);
							subHitGroup.getChildren().add(subHitView);
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

	/**
	 * An override to support default offset.
	 */
	private MeshView generateRectSolid(int startSH, int endSH, SubEntity subEntity) {
		return generateRectSolid(startSH, endSH, 0.0f, subEntity);
	}

	/**
	 * Builds a rectangular solid geometry based on always-same Y,Z extents.
	 */
	private MeshView generateRectSolid(int startSH, int endSH, float extraYDisp, SubEntity subEntity) {
		return generateRectSolid(startSH, endSH, extraYDisp, Constants.ZB, Constants.ZF, subEntity);
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
	private MeshView generateRectSolid(int startSH, int endSH, float extraYDisp, float zBack, float zFront, SubEntity subEntity) {

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

		return createMesh(coordinateData, texCoordGenerator.generateTexCoords(coordinateData), subEntity);
	}

	private float translateToJava3dCoords(int seqCoord) {
		return ((float) (seqCoord - startRange) * factor) - Constants.START_OF_CYLINDER;

	}

	private MeshView createMesh(float[] vertices, float[] texCoords, SubEntity subEntity) {
		TriangleMesh tm = new TriangleMesh();
		tm.getPoints().addAll(vertices);
		tm.getTexCoords().addAll(texCoords);
		// Only one tex coord.
		// 36 coords represented.  12 faces.
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

		if (subEntity != null) {
			PhongMaterial meshMaterial = createAppearance(subEntity);
			//meshMaterial.setSpecularPower(10000);
			meshView.setMaterial(meshMaterial);
		}

		meshView.setId("Homemade Shape");
		return meshView;
	}

	private MeshView createArbitrarySizedMesh(float[] vertices, float[] texCoords) {
		TriangleMesh tm = new TriangleMesh();
		tm.getPoints().addAll(vertices);
		tm.getTexCoords().addAll(texCoords);
		
		int[] faces = new int[ (vertices.length / 3) * 4 ];
		for (int i = 0; i < faces.length / 2; i+=2) {
			// Odd-numbered face-array members are all left at zero.
			faces[i] = i/2;
		}
		
		tm.getFaces().addAll(faces);
		MeshView meshView = new MeshView(tm);

		meshView.setId("Mesh");
		return meshView;
	}

	private MeshView createFacadeMesh(float[] vertices, float[] texCoords) {
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
		};
		tm.getFaces().addAll(faces);
		MeshView meshView = new MeshView(tm);
		meshView.setCullFace(CullFace.BACK);

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
		float xl = getCylLeftX() - 10.0f; // Extend to make room for start-range
		float xr = getCylRightX() + 10.0f; // Balance left.		
		float yBottom = Constants.BOTTOM_OF_CB_LABEL;
		float ruleYTop = -(Constants.YB + 24.0f);
		float zBack = -0.5f;
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
			xr, yBottom, zBack, //26
			xl, yBottom, zBack, //25
			xr, ruleYTop, zBack, //27
			xl, yBottom, zBack, //29
			xl, ruleYTop, zBack, //28
			// Bottom
			xl, yBottom, zBack, //30
			xr, yBottom, 0.0f, //32
			xl, yBottom, 0.0f, //31
			xl, yBottom, zBack, //33
			xr, yBottom, zBack, //35
			xr, yBottom, 0.0f, //34
		};
		return coordinateData;
	}

	private static float getCylRightX() {
		return Constants.LENGTH_OF_CYLINDER - Constants.START_OF_CYLINDER;
	}

	private static float getCylLeftX() {
		return -Constants.START_OF_CYLINDER;
	}
	
	private float[] generateBandGeometry(double x, float outerRadius, float innerRadius) {
		// How many vertices will be in entire geometry?
		//  100 points inner and outer lips of ring.  200
		//  4 points will have 2 triangles between them.  Multiple by 6 points.
		//    so far, 6 * 100 = 600.
		//  3 coords for each point.
		// Finally: must repeat all coordinates in opposite direction, to
		//  get both sides' normals populated.
		float[] coords = new float[2 * 3 * ((BAND_CIRCLE_VERTEX_COUNT) * 6)];
		
		// Now fill in the points.  Take circle to be 2*PI radians (Java Math).
		double increment = //360.0 / BAND_CIRCLE_VERTEX_COUNT;
				(Math.PI * 2.0) / (double)BAND_CIRCLE_VERTEX_COUNT;
		double angle = 0.0;

		Float prevOuterY = null;
		Float prevInnerY = null;
		Float prevOuterZ = null;
		Float prevInnerZ = null;
		float floatX = (float)x;
		int coordsPerPoint = 3;
		int coordsPerQuad = 6 * coordsPerPoint;
		int nextCoord = 0;
		for (int i = 0; i < BAND_CIRCLE_VERTEX_COUNT + 1; i++) {	
			final double cos = Math.cos(angle);
			final double sin = Math.sin(angle);
			
			// X remains the same.
			// Y for outer lip.
			float outerY = (float)(cos * outerRadius);
			// Y for inner lip.
			float innerY = (float)(cos * innerRadius);
			// Z for outer lip.
			float outerZ = (float)(sin * outerRadius);
			// Z for inner lip.
			float innerZ = (float)(sin * innerRadius);

			// Make next pair of triangles.
			if (prevOuterY != null) {
				final int quadCoordOffset = (i - 1) * coordsPerQuad;
				// First Triangle
				// X/Inner/Prev
				coords[ quadCoordOffset ] = floatX;
				coords[ quadCoordOffset + 1 ] = prevInnerY;
				coords[ quadCoordOffset + 2 ] = prevInnerZ;
				// X/Outer/Curr
				coords[ quadCoordOffset + 3 ] = floatX;
				coords[ quadCoordOffset + 4 ] = outerY;
				coords[ quadCoordOffset + 5 ] = outerZ;
				// X/Outer/Prev
				coords[ quadCoordOffset + 6 ] = floatX;
				coords[ quadCoordOffset + 7 ] = prevOuterY;
				coords[ quadCoordOffset + 8 ] = prevOuterZ;
				
				// Second Triangle
				// X/Inner/Prev
				coords[ quadCoordOffset + 9 ] = floatX;
				coords[ quadCoordOffset + 10 ] = prevInnerY;
				coords[ quadCoordOffset + 11 ] = prevInnerZ;
				// X/Inner/Curr
				coords[ quadCoordOffset + 12 ] = floatX;
				coords[ quadCoordOffset + 13 ] = innerY;
				coords[ quadCoordOffset + 14 ] = innerZ;
				// X/Outer/Curr
				coords[ quadCoordOffset + 15 ] = floatX;
				coords[ quadCoordOffset + 16 ] = outerY;
				coords[ quadCoordOffset + 17 ] = outerZ;
			}
			
			prevOuterY = outerY;
			prevOuterZ = outerZ;
			prevInnerY = innerY;
			prevInnerZ = innerZ;
			
			angle += increment;
			
			nextCoord = i;
		}
		
		angle = 0.0;
		floatX += 0.1;
		prevOuterY = null;
		prevOuterZ = null;
		prevInnerY = null;
		prevInnerZ = null;
		for (int i = nextCoord; i < 2*BAND_CIRCLE_VERTEX_COUNT + 1; i++) {
			final double cos = Math.cos(angle);
			final double sin = Math.sin(angle);

			// X remains the same.
			// Y for outer lip.
			float outerY = (float) (cos * outerRadius);
			// Y for inner lip.
			float innerY = (float) (cos * innerRadius);
			// Z for outer lip.
			float outerZ = (float) (sin * outerRadius);
			// Z for inner lip.
			float innerZ = (float) (sin * innerRadius);

			// Make next pair of triangles.
			if (prevOuterY != null) {
				final int quadCoordOffset = (i - 1) * coordsPerQuad;
				// First Triangle
				// X/Inner/Prev
				coords[quadCoordOffset] = floatX;
				coords[quadCoordOffset + 1] = prevInnerY;
				coords[quadCoordOffset + 2] = prevInnerZ;
				// X/Outer/Curr
				coords[quadCoordOffset + 3] = floatX;
				coords[quadCoordOffset + 4] = prevOuterY;
				coords[quadCoordOffset + 5] = prevOuterZ;
				// X/Outer/Prev
				coords[quadCoordOffset + 6] = floatX;
				coords[quadCoordOffset + 7] = outerY;
				coords[quadCoordOffset + 8] = outerZ;

				// Second Triangle
				// X/Inner/Prev
				coords[quadCoordOffset + 9] = floatX;
				coords[quadCoordOffset + 10] = innerY;
				coords[quadCoordOffset + 11] = innerZ;
				// X/Inner/Curr
				coords[quadCoordOffset + 12] = floatX;
				coords[quadCoordOffset + 13] = prevInnerY;
				coords[quadCoordOffset + 14] = prevInnerZ;
				// X/Outer/Curr
				coords[quadCoordOffset + 15] = floatX;
				coords[quadCoordOffset + 16] = outerY;
				coords[quadCoordOffset + 17] = outerZ;
			}

			prevOuterY = outerY;
			prevOuterZ = outerZ;
			prevInnerY = innerY;
			prevInnerZ = innerZ;

			angle += increment;
		}

		return coords;
	}

	//-----------------------------------HELPER METHODS
    /** Builds a rectangular solid geometry with a single hole. */
    private float[] generatePerforatedSolid(int startSH, int endSH, float extraYDisp) {
        // The translations: the start and end need to be normalized for
        // a certain meter length.  They also need to be centered in that length.
        // Assume query IS coordinate system, and starts at zero.
        float xl = translateToJava3dCoords(startSH); //((float)startSH * factor) - START_OF_CYLINDER;
        float xr = translateToJava3dCoords(endSH);   //((float)endSH * factor) - START_OF_CYLINDER;
		final float yTop = (Constants.YT + extraYDisp);
		final float zBack = (Constants.ZB + PERFORATION_HALF_WIDTH);
		final float zFront = (Constants.ZF - PERFORATION_HALF_WIDTH);
		final float yBottom = (Constants.YB + extraYDisp);
        //GeometryInfo gi = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
        float[] coordinateData = new float[] {
			// ONE 'hole in the top' to simulate a gap in the subject sequence.
			xl, yTop, zBack,
			xr, yTop, zFront,
			xl, yTop, zFront,
			xl, yTop, zBack,
			xr, yTop, zBack,
			xr, yTop, zFront,
			// The 'hole in the bottom'
			xl, yBottom, zFront,
			xr, yBottom, zFront,
			xl, yBottom, zBack,
			xr, yBottom, zFront,
			xr, yBottom, zBack,
			xl, yBottom, zBack,
			// The 'left'
			xl, yTop, zBack,
			xl, yBottom, zFront,
			xl, yTop, zFront,
			xl, yTop, zBack,
			xl, yBottom, zBack,
			xl, yBottom, zFront,
			// The 'right'
			xr, yTop, zBack,
			xr, yTop, zFront,
			xr, yBottom, zFront,
			xr, yTop, zBack,
			xr, yBottom, zFront,
			xr, yBottom, zBack,
			// The 'front'
			xl, yTop, zFront,
			xr, yTop, zFront,
			xr, yBottom, zFront,			
			xr, yBottom, zFront,
			xl, yBottom, zFront,
			xl, yTop, zFront,
			// The 'back'   
			xl, yTop, zBack,
			xr, yTop, zBack,
			xr, yBottom, zBack,
			xr, yBottom, zBack,
			xl, yBottom, zBack,
			xl, yTop, zBack
		};

        return coordinateData;
    }
	public static final float PERFORATION_HALF_WIDTH = 2f;
	
    /** Add shapes for any insertions in subject, relative to query. */
    private void generateSubjectInsertions(SubEntity subEntity, Group hitGroup, int[][] queryGaps) {
    	int startSH = subEntity.getStartOnQuery();
		float extraYDisp = 1.3f;
		float back = Constants.ZB + 0.5f;
		float front = Constants.ZF - 0.5f;
    	for (int i = 0; i < queryGaps.length; i++) {
    		int[] nextGap = queryGaps[i];
            MeshView insertion = generateRectSolid(startSH + nextGap[0], startSH + nextGap[1], extraYDisp, back, front, subEntity);
			PhongMaterial meshMaterial = appearanceSource.createSubEntityInsertionAppearance(subEntity);
			insertion.setMaterial(meshMaterial);
			hitGroup.getChildren().add(insertion);
    	}
    }

	public PhongMaterial createAppearance(SubEntity subEntity) {
		return appearanceSource.createSubEntityAppearance(subEntity);
	}

    /** Add shapes for any gaps in subject, relative to query. */
    private void generateSubjectGaps(SubEntity subEntity, Group hitGroup, int[][] subjectGaps) {
    	int startSH = subEntity.getStartOnQuery();
		for (int i = 0; i < subjectGaps.length; i++) {
    		int[] nextGap = subjectGaps[i];
            //  System.out.println("Generating perforation at " + (startSH + pos) + " through " + (startSH + endPos));
            float[] coordinateData = generatePerforatedSolid(startSH + nextGap[0],
                    startSH + nextGap[1], 0.4f);
		    MeshView gap = createMesh(coordinateData, texCoordGenerator.generateTexCoords(coordinateData), null);
			PhongMaterial meshMaterial = appearanceSource.createPerforatedAppearance(subEntity);
			gap.setMaterial(meshMaterial);
			hitGroup.getChildren().add(gap);
    		
    	}
    }

    /**
     * Adds "dentils" to the subhit. Each represents a residue--either an amino acid or a base.
     * 
     * @param subEntity data model to drive the glyph-building
     * @param hitGroup where to attach the new hit glyph.
     */
    private void generateResidueDentils(SubEntity subEntity, Group hitGroup) {
		MeshView gi; // Geometry Info
    	Map<String,Object> props = subEntity.getProperties();
    	if (props != null) {
    		// NOTE: putting residues from subject, at position relative to query.
        	String subjectResidues = (String)props.get(DataSource.SUBJECT_ALIGNMENT);
        	String queryResidues = (String)props.get(DataSource.QUERY_ALIGNMENT);
        	int startPos = subEntity.getStartOnQuery();
	        //NormalGenerator ng = new NormalGenerator();
	        //Stripifier st = new Stripifier();
	        String dentilResidues = subjectResidues;
	        boolean isAnchor = isAnchor(subEntity);
	        boolean isBase = subEntity.getProperties().get(DataSource.SUB_ENTITY_TYPE_PROPERTY_NAME).equals(DataSource.ENTITY_TYPE_BLAST_N_SUB_HIT);
 
        	if (dentilResidues != null) {
        		// Make the glyphs--enough for all residues in the set.
        		for (int i = 0; i < dentilResidues.length(); i++) {
        			// When to do it and when not to do it.
        			if ((!isAnchor) && diffResiduesOnly && (queryResidues != null) && (queryResidues.charAt(i) == subjectResidues.charAt(i)))
            			continue;  // Diffs only
        			if (dentilResidues.charAt(i) == '-')
        				continue;
					
					char residue = dentilResidues.charAt(i);

					//generateRectSolid(int startSH, int endSH, float extraYDisp, float zBack, float zFront, SubEntity subEntity)
        			if (prominentDentils)
        			    gi = generateRectSolid(startPos + i, startPos + i + 1, 0.01f, Constants.ZF - 0.02f, Constants.ZF + 0.02f, null);
        			else
        				gi = generateFacadeBox(startPos + i, startPos + i + 1, 0.01f, Constants.ZF, Constants.ZF + 0.01f);
 
        			//Shape3D part = new Shape3D();
        			PhongMaterial materialAppearance = appearanceSource.createSubEntityAppearance(dentilResidues.charAt(i), isBase);
        			if (materialAppearance == null)
        				materialAppearance = appearanceSource.createSubEntityAppearance(residue, isBase);
					gi.setMaterial(materialAppearance);

        			hitGroup.getChildren().add(gi);
        		}
        	}
    	}
    }
	
	/**
	 * Builds a rectangular solid geometry based on always-same Y,Z extents.
	 */
	private MeshView generateFacadeBox(int startSH, int endSH, float extraYDisp, float zBack, float zFront) {

        // The translations: the start and end need to be normalized for
		// a certain meter length.  They also need to be centered in that length.
		// Assume query IS coordinate system, and starts at zero.
		float xl = translateToJava3dCoords(startSH);
		float xr = translateToJava3dCoords(endSH);
		//float yb = Constants.YT + extraYDisp - 0.01f;
		float[] coordinateData = new float[]{
			// The 'lid'
			xl, Constants.YT + extraYDisp, zBack, //0
			xl, Constants.YT + extraYDisp, zFront, //1
			xr, Constants.YT + extraYDisp, zFront, //2
			xl, Constants.YT + extraYDisp, zBack, //3
			xr, Constants.YT + extraYDisp, zFront, //4
			xr, Constants.YT + extraYDisp, zBack, //5
			// The 'front'
			xl, Constants.YT + extraYDisp, zFront, //18
			xl, Constants.YB + extraYDisp, zFront, //19
			xr, Constants.YT + extraYDisp, zFront, //20
			xl, Constants.YB + extraYDisp, zFront, //21
			xr, Constants.YB + extraYDisp, zFront, //22
			xr, Constants.YT + extraYDisp, zFront, //23
			// The 'back'
			xr, Constants.YT + extraYDisp, zBack, //30
			xr, Constants.YB + extraYDisp, zBack, //31
			xl, Constants.YB + extraYDisp, zBack, //32
			xr, Constants.YT + extraYDisp, zBack, //33
			xl, Constants.YB + extraYDisp, zBack, //34
			xl, Constants.YT + extraYDisp, zBack, //35
		};
		MeshView meshView = createFacadeMesh(coordinateData, texCoordGenerator.generateTexCoords(coordinateData));
		return meshView;	
	}
	
	private Group createCigarBandGroup(boolean low) {
		Group rtnVal = new Group();
		Text bandLabel = null;
		if (low) {
			lowCigarBandLabel = new Text(getCylLeftX() - Constants.LENGTH_OF_BAND_LABEL, Constants.BOTTOM_OF_CB_LABEL, "0");
			bandLabel = lowCigarBandLabel;
		}
		else {
			highCigarBandLabel = new Text(getCylRightX(), Constants.BOTTOM_OF_CB_LABEL, "LARGE");
			bandLabel = highCigarBandLabel;
		}
		bandLabel.setTranslateZ(Constants.BANDLABEL_Z_FRONT - 0.2);
		bandLabel.setFill(Color.BLACK);
		bandLabel.setFont(new Font(7.0));
		//bandLabel.setSmooth(true);
		bandLabel.setCache(true);

		rtnVal.getChildren().addAll(createCigarBand(low));
		rtnVal.getChildren().add(bandLabel);
		return rtnVal;
	}
	
	/**
	 * Generates a cigar-band view.  It will have a tab pointing to the left
	 * or to the right.
	 * 
	 * @param leftward tab points away to left?  false-> to the right.
	 * @return a cigar band of appropriate tab-direction.
	 */
	private MeshView[] createCigarBand(boolean leftward) {
		MeshView[] rtnVal = new MeshView[2];
				
		double xCoord = leftward ? getCylLeftX() : getCylRightX();

		final PhongMaterial meshMaterial = new PhongMaterial();
		meshMaterial.setDiffuseColor(Constants.CIGAR_BAND_COLOR);
		meshMaterial.setSpecularColor(Constants.CIGAR_BAND_COLOR);

		// Build up the band mesh.
		float[] bandCoordinateData = generateBandGeometry(xCoord, Constants.CB_OUTER_RADIUS, Constants.CB_INNER_RADIUS);
		MeshView bandMesh = createArbitrarySizedMesh(bandCoordinateData, texCoordGenerator.generateTexCoords(bandCoordinateData));
		bandMesh.setOpacity(OPACITY);
		bandMesh.setCullFace(CullFace.NONE);
		
		bandMesh.setMaterial(meshMaterial);
		
		// Build up the label mesh.
		float lengthOfBandLabel = Constants.LENGTH_OF_BAND_LABEL;
		float startXLabel = 0;
		float endXLabel = 0;
		if (leftward) {
			endXLabel = getCylLeftX();
			startXLabel = endXLabel - lengthOfBandLabel;
		}
		else {
			startXLabel = getCylRightX();
			endXLabel = startXLabel + lengthOfBandLabel;
		}

		float yBottom = Constants.BOTTOM_OF_CB_LABEL;
		float yTop = Constants.TOP_OF_CB_LABEL;
		float zFront = Constants.BANDLABEL_Z_FRONT;
		float zBack = Constants.BANDLABEL_Z_BACK;
		float[] labelCoordinateData = new float[]{
			// The 'front'
			startXLabel, yTop, zFront,
			startXLabel, yBottom, zFront,
			endXLabel, yTop, zFront,
			startXLabel, yBottom, zFront,
			endXLabel, yBottom, zFront,
			endXLabel, yTop, zFront,

			// The 'back'   
			startXLabel, yTop, zBack,
			endXLabel, yTop, zBack,
			endXLabel, yBottom, zBack,
			endXLabel, yBottom, zBack,
			startXLabel, yBottom, zBack,
			startXLabel, yTop, zBack
		};
		MeshView labelMesh = createArbitrarySizedMesh(labelCoordinateData, texCoordGenerator.generateTexCoords(bandCoordinateData));
		labelMesh.setOpacity(OPACITY);
		labelMesh.setCullFace(CullFace.BACK);
		labelMesh.setMaterial(meshMaterial);
		rtnVal[0] = bandMesh;
		rtnVal[1] = labelMesh;
		return rtnVal;
	}

	private List<MeshView> createTickBands() {
		List<MeshView> tickBands = new ArrayList<>();
    	double stepSize = (this.endRange - this.startRange) / 10f;
		float outerRadius = Constants.YB + 0.4f;  // Outside will reach into solids.
		float innerRadius = Constants.YB - 0.4f;  // Inside will be just lower than the inner surface of all solids.
		final PhongMaterial meshMaterial = new PhongMaterial();
		meshMaterial.setDiffuseColor(Constants.TICK_BAND_COLOR);
		meshMaterial.setSpecularColor(Constants.TICK_BAND_COLOR);

		for (int i = this.startRange; i < this.endRange; i+=stepSize) {
            int nextTickX = i - startRange;
			double xCoord = getCylLeftX() + nextTickX * (Constants.LENGTH_OF_CYLINDER / (this.endRange - this.startRange));
			float[] coordinateData = generateBandGeometry(xCoord, outerRadius, innerRadius);
			MeshView meshView = createArbitrarySizedMesh(coordinateData, texCoordGenerator.generateTexCoords(coordinateData));
			meshView.setOpacity(OPACITY);
			meshView.setCullFace(CullFace.NONE);
			meshView.setMaterial(meshMaterial);
			tickBands.add(meshView);
			meshView.setMouseTransparent(true);
        }
		
		return tickBands;
	}

	//http://stackoverflow.com/questions/14780228/javafx-drawing-lines-and-text
	
	/**
	 * Adds tick marks to the ruler.
	 *
	 * @param rulerGroup what gets the lines?
	 */
	private void addRuleTicks(Group rulerGroup, int anchorLength) {
		int lengthOfQuery = anchorLength + 1;

		// Make tick mark lines.
		double tickYBottom = Constants.BOTTOM_OF_CB_LABEL;
		double tickYTop = tickYBottom - 4.7;
		double labelYBottom = tickYTop - 0.5; //getBottomOfCbLabel() + 2.0; //tickYTop + 1.0;
		double multiplier = factor;
		double stepSize = (endRange - startRange) / 10f;
		double innerStepSize = stepSize / 10f;
		int labelDenominator = computeTensDenominator(lengthOfQuery);
		float labelZOffset = -0.1f;

		// Tell the world what the numbers mean!
		if (lengthOfQuery >= Constants.MAX_UNDIVIDED_RULE_DIVISION) {
			rulerGroup.getChildren().add(generateRulerLabel("x" + labelDenominator, 0.0f, Constants.YLABEL, labelZOffset));
		}

		for (int i = this.startRange; i <= this.endRange; i += stepSize) {
			double nextTickX = -Constants.START_OF_CYLINDER + ((i - startRange) * multiplier);
			Line tick = new Line(nextTickX, tickYTop, nextTickX, tickYBottom);
			tick.setTranslateZ(labelZOffset);
			tick.setFill(Color.BLACK);
			//rulerGroup.getChildren().add(tick);
			float xPos = (float) (nextTickX - Constants.LABEL_CHAR_WIDTH);
			if (i + stepSize > endRange) {
				xPos -= (2 * Constants.LABEL_CHAR_WIDTH);
			} else if (i > 0) {
				xPos -= Constants.LABEL_CHAR_WIDTH;
			}
			String labelStr = null;
			if (endRange >= Constants.MAX_UNDIVIDED_RULE_DIVISION) {
				float labelNumber = ((float) i / (float) labelDenominator);
				labelStr = formatLabelNumber(labelNumber);
			} else {
				labelStr = "" + i;
			}
			rulerGroup.getChildren().add(generateRulerLabel(labelStr, xPos, (float) labelYBottom, labelZOffset));
			rulerGroup.getChildren().add(tick);
			//rulerGroup.getChildren().add(new Shape3D(tick));
            // DEBUG: System.out.println("Stepsize " + stepSize + ", next point is " + i);
			// Shorter tick marks at ten points within outer ticks.
			if (i + stepSize <= endRange && endRange >= 100) {
				for (float j = i; j < i + stepSize; j += innerStepSize) {
					double innerNextTickX = nextTickX + ((j - i) * multiplier);
					Line innerTick = new Line(innerNextTickX, tickYTop + 2.0f, innerNextTickX, tickYBottom);
					innerTick.setTranslateZ(-0.1d);
					//LineArray innerTick = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
					//innerTick.setCoordinate(0, new Point3d(innerNextTickX, tickYTop - 0.02f, 0.01d));
					//innerTick.setCoordinate(1, new Point3d(innerNextTickX, Constants.RULE_Y_BOTTOM, 0.01d));
					rulerGroup.getChildren().add(innerTick);
				}
			}
		}
	}
	
	private Text generateRulerLabel(String labelStr, float x, float y, float z) {
		Text bandLabel = new Text(x, y, labelStr);
		bandLabel.setFill(Color.BLACK);
		bandLabel.setFont(new Font(7.0));
		bandLabel.setCache(true);
		bandLabel.setTranslateZ(z);
		return bandLabel;
	}
	/* */

	/**
	 * Spacing for outer ticks, etc.
	 *
	 * @return how far apart?
	 */
	private int computeTensDenominator(int lengthOfQuery) {
		return (int) Math.pow(10, (int) (Math.log(lengthOfQuery) / Math.log(10)) - 1);
	}

	/**
	 * Ensures that label string does not have too many trailing decimals.
	 *
	 * @param labelNumber number to appear on a ruler.
	 * @return formatted to XXX.X
	 */
	private String formatLabelNumber(float labelNumber) {
		String startStr = "" + labelNumber;
		int pointPos = startStr.indexOf(".");
		// No '.'
		if (pointPos == -1) {
			return startStr;
		}
		// '.' on end of string.
		if (pointPos == startStr.length() - 1) {
			return startStr;
		}
		// '.' has digits following it.
		return startStr.substring(0, pointPos + 2);
	}

	private Group createRuler(int anchorLength) {
		Group rulerGroup = new Group();
		float[] coordinateData = generateRuleGeometry();
		MeshView meshView = createMesh(coordinateData, texCoordGenerator.generateTexCoords(coordinateData), null);
		meshView.setOpacity(OPACITY);
		final PhongMaterial meshMaterial = new PhongMaterial();
		meshMaterial.setDiffuseColor(Color.WHITE);
		meshMaterial.setSpecularColor(Color.WHITE);
		//meshMaterial.setSpecularPower(10000);
		meshView.setMaterial(meshMaterial);
		meshView.setMouseTransparent(true);
		rulerGroup.getChildren().add(meshView);
		addRuleTicks(rulerGroup, anchorLength);
		return rulerGroup;
	}

	private void createAnchor(MeshView subHitView, Entity entity, TransformableGroup parentGroup) {
		subHitView.setRotationAxis(Rotate.X_AXIS);
		subHitView.setRotate(4.0f / 7.0f * 360.0);
		subHitView.setTranslateY(Constants.ANCHOR_OFFSET);
		subHitView.setTranslateZ(-Constants.ANCHOR_OFFSET);
		subHitView.setMouseTransparent(true);

		// Add a label to the main screen.
		addAnchorLabel(entity, parentGroup);
	}

	private Text createLabel() {
		// Seeing some problem whereby, text with negative X coordinate
		// is being clipped on the left side.
		Text label = new Text(-Constants.LENGTH_OF_CYLINDER/2.0, Constants.LENGTH_OF_CYLINDER / 2.5, "The Cylinder");
		label.setCache(true);
		if (dark)
			label.setFill(Constants.INLABEL_DARK_TEXT_COLOR);
		else
			label.setFill(Constants.INLABEL_LIGHT_TEXT_COLOR);
		label.setFont(new Font(8.0));
		label.setSmooth(false);
		return label;
	}

	private void positionCigarBands(Object obj) {
		// Reposition the cigar bands.
		SubEntity subEntity = idToSubEntity.get(obj.toString());
		positionCigarBands(subEntity);
	}

	private void positionCigarBands(SubEntity subEntity) {
		if (subEntity != null) {
			int startSH = subEntity.getStartOnQuery();
			int endSH = subEntity.getEndOnQuery();
			float xl = translateToJava3dCoords(startSH);
			float xr = translateToJava3dCoords(endSH);
			Runnable runnable = new Runnable() {
				public void run() {
					lowCigarBandSlide.setTranslate(xl - getCylLeftX() - selectionEnvelope, 0, 0);
					lowCigarBandLabel.setText("" + startSH);
					highCigarBandSlide.setTranslate(xr - getCylRightX() + selectionEnvelope, 0, 0);
					highCigarBandLabel.setText("" + endSH);
				}
			};
			if (Platform.isFxApplicationThread()) {
				runnable.run();
			} else {
				Platform.runLater(runnable);
			}
		}
	}
	
	//
	// The handleCameraViews file contains the handleMouse() and handleKeyboard() 
	// methods that are used in the MoleculeSampleApp application to handle the 
	// different 3D camera views.  These methods are used in the Getting Started with 
	// JavaFX 3D Graphics tutorial. 
	//
	private void handleMouse(Scene scene) {
		scene.setOnMousePressed(new MousePressedHandler(mouseLocationModel));
		this.mouseDraggedHandler = new MouseDraggedHandler(mouseLocationModel, cameraModel, idToShape, subEntitySelector);
		scene.setOnMouseDragged(mouseDraggedHandler);
	}

	private void handleKeyboard(Scene scene) {
		scene.setOnKeyPressed(new KeyEventHandler(cameraModel));
	}

	private class ModelSelectionWrapperLookupListener implements LookupListener {

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
						positionCigarBands(se);
						if (subEntitySelector != null) {
							subEntitySelector.select(se);
						}
						else {
							log.warning("No sub entity selector available.");
						}
					}
				}
			}
		}
	}
	
}
