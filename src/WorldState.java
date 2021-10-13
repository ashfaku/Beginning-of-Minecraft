import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetLoadException;
import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.SkyFactory;
import com.simsilica.lemur.GuiGlobals;

import jme3tools.savegame.SaveGame;

public class WorldState extends BaseAppState implements AnalogListener, ActionListener, Savable
{
	private String worldName = "";
	private final int BLOCK_DIMENSION = 10;
	private JME jme;
	private AssetManager assetManager;
	private Node guiNode, rootNode;
	private AppSettings settings;
	private boolean cursor;
	private AppStateManager stateManager;
	private InputManager inputManager;
	private RenderManager renderManager;
	private Camera cam;	
	private FlyByCamera flyCam;
	private Player player;
	private int inventoryCounter = 0;
	private Boolean thirdPerson = null;
	private Item pickedUp;
	private Quaternion baseRotate;
	private BulletAppState bulletAppState;
	private int breakCounter = 0;
	private Geometry handModel;
	public WorldState(String s)
	{
		cursor = true;
		if (s != null)
			worldName = s;
	}
	public Quaternion getBaseRotation()
	{
		return baseRotate;
	}
	public Boolean checkPOV()
	{
		return thirdPerson;
	}
	private ActionListener action = new ActionListener() 
    {
    	public void onAction(String name, boolean isPressed, float tpf)
    	{
    		switch (name)
    		{
    			case "Inventory":
	    			if (!isPressed) 
		    		{
		    			inventoryCounter++;
		    			if (inventoryCounter % 2 == 1)
		    			{
		    				stateManager.attach(new InventoryState(jme, player));
		    				stateManager.getState(WorldState.class).setEnabled(false);
		    			}
		    			else
		    			{
		    				stateManager.detach(stateManager.getState(InventoryState.class));
		    				stateManager.getState(WorldState.class).setEnabled(true);
		    				if (player.getInventory().getPickedUp() != null)
		    				{
		    					dropItem();
		    					player.setPickedUp(null);				
		    				}
		    			}
		    		}
	    			break;
    			case "First Slot":
    				if (player.getInventory().getItems()[0][0] != null)
    					initHand(player.getInventory().getItems()[0][0].getModelURL(), 0, 0);		
    				else
    					handModel.removeFromParent();
    				break;
    			case "Second Slot":
    				if (player.getInventory().getItems()[0][1] != null)
    					initHand(player.getInventory().getItems()[0][1].getModelURL(), 0, 1);		
    				else
    					handModel.removeFromParent();
    				break;
    			case "Third Slot":
    				if (player.getInventory().getItems()[0][2] != null)
    					initHand(player.getInventory().getItems()[0][2].getModelURL(), 0, 2);		
    				else
    					handModel.removeFromParent();
    				break;
    			case "Fourth Slot":
    				if (player.getInventory().getItems()[0][3] != null)
    					initHand(player.getInventory().getItems()[0][3].getModelURL(), 0, 3);		
    				else
    					handModel.removeFromParent();
    				break;
    			case "Fifth Slot":
    				if (player.getInventory().getItems()[0][4] != null)
    					initHand(player.getInventory().getItems()[0][4].getModelURL(), 0, 4);		
    				else
    					handModel.removeFromParent();
    				break;
    			case "Sixth Slot":
    				if (player.getInventory().getItems()[0][5] != null)
    					initHand(player.getInventory().getItems()[0][5].getModelURL(), 0, 5);		
    				else
    					handModel.removeFromParent();
    				break;
    			case "Seventh Slot":
    				if (player.getInventory().getItems()[0][6] != null)
    					initHand(player.getInventory().getItems()[0][6].getModelURL(), 0, 6);		
    				else
    					handModel.removeFromParent();
    				break;
    			case "Eighth Slot":
    				if (player.getInventory().getItems()[0][7] != null)
    					initHand(player.getInventory().getItems()[0][7].getModelURL(), 0, 7);		
    				else
    					handModel.removeFromParent();
    				break;
    			case "Ninth Slot":
    				if (player.getInventory().getItems()[0][8] != null)
    					initHand(player.getInventory().getItems()[0][8].getModelURL(), 0, 8);		
    				else
    					handModel.removeFromParent();
    				
    				break;
    			
    			case "See yurself":
    				System.out.println("very suspicious");
    				if (thirdPerson == null)
    					thirdPerson = false;
    				else
    					thirdPerson = !thirdPerson;
    				break;
    			case "Save":
				try {
					save();
				} catch (IOException e) {
					e.printStackTrace();
				}
    				System.out.println("saved to " + worldName);
    				break;
  
    		}
    	}
    };
    private void initHand(String s, int r, int c)
    {
    	if (handModel == null)
    	{
	    	handModel = (Geometry) assetManager.loadModel(s);
	    	handModel.scale(.5f);
	    	handModel.setLocalTranslation(player.getInventory().getIcons()[r][c].getLocalTranslation().getX(),
	    			player.getInventory().getIcons()[r][c].getLocalTranslation().getY() + 40, player.getInventory().getIcons()[r][c].getLocalTranslation().getZ());
    			
    	}
    	else
    		handModel = (Geometry) assetManager.loadModel(s);
    	guiNode.attachChild(handModel);
    } 
    private void dropItem()
    {
		Item item = player.getInventory().getPickedUp();
		Geometry loadedItem = (Geometry) assetManager.loadModel(item.getModelURL());
		RigidBodyControl c = new RigidBodyControl(new BoxCollisionShape(new Vector3f(5, 5, 5)), .1f);
		loadedItem.addControl(c);	
		DirectionalLight itemSun = new DirectionalLight();
		bulletAppState.getPhysicsSpace().add(c);
		loadedItem.addControl(new RotateControl());
		loadedItem.getControl(RigidBodyControl.class).setPhysicsLocation(cam.getDirection().add(cam.getLocation()).subtract(new Vector3f(0, 30, 0)).add(new Vector3f(30, 0, 0)));
		itemSun.setDirection(cam.getDirection());
		itemSun.setColor(ColorRGBA.White);
		rootNode.addLight(itemSun);
		rootNode.attachChild(loadedItem);

    }
    public Player getPlayer()
    {
    	return player;
    }
	private void prepareManager()
    {
    	inputManager.addMapping("Inventory", new KeyTrigger(KeyInput.KEY_E));
    	inputManager.addMapping("See yurself", new KeyTrigger(KeyInput.KEY_C));
    	inputManager.addMapping("Save", new KeyTrigger(KeyInput.KEY_P));
    	inputManager.addMapping("First Slot", new KeyTrigger(KeyInput.KEY_1));
    	inputManager.addMapping("Second Slot", new KeyTrigger(KeyInput.KEY_2));
    	inputManager.addMapping("Third Slot", new KeyTrigger(KeyInput.KEY_3));
    	inputManager.addMapping("Fourth Slot", new KeyTrigger(KeyInput.KEY_4));
    	inputManager.addMapping("Fifth Slot", new KeyTrigger(KeyInput.KEY_5));
    	inputManager.addMapping("Sixth Slot", new KeyTrigger(KeyInput.KEY_6));
    	inputManager.addMapping("Seventh Slot", new KeyTrigger(KeyInput.KEY_7));
    	inputManager.addMapping("Eighth Slot", new KeyTrigger(KeyInput.KEY_8));
    	inputManager.addMapping("Ninth Slot", new KeyTrigger(KeyInput.KEY_9));
    	inputManager.addListener(action, new String[] {"Inventory", "See yurself", "Save",
    	"First Slot", "Second Slot", "Third Slot", "Fourth Slot", "Fifth Slot", "Sixth Slot",
    	"Seventh Slot", "Eighth Slot", "Ninth Slot"});
    	inputManager.addMapping("Mouse", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    	inputManager.addMapping("RightClick", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
    	inputManager.addListener(this, new String[] {"Mouse", "RightClick"});
    }
	@Override
	protected void cleanup(Application arg0)
	{
		
	}
	private void place()
	{
		breakCounter = 0;
		CollisionResults results = new CollisionResults();
		Ray r;
		if (cursor)
			r = new Ray(cam.getLocation(), cam.getDirection());
		else
		{
			Vector2f mouseCoors = inputManager.getCursorPosition();
			Vector3f worldCoors = cam.getWorldCoordinates(new Vector2f(mouseCoors.getX(), mouseCoors.getY()), 0f),
			direction = cam.getWorldCoordinates(new Vector2f(mouseCoors.getX(), mouseCoors.getY()), 1f).subtractLocal(worldCoors);
			r = new Ray(worldCoors, direction);
	    }
		rootNode.collideWith(r, results);
		if (results.size() == 0)
			System.out.println("Nothing");
		else
		{
			CollisionResult closest = results.getClosestCollision();
			
			if (closest != null && closest.getGeometry().getControl(RigidBodyControl.class) != null)
			{
				if (closest.getGeometry().getParent().getChild(0).getControl(RigidBodyControl.class).getPhysicsLocation().distance(cam.getLocation()) < 300)
				{
					Node item = closest.getGeometry().getParent();
					Vector3f loc = item.getChild(0).getControl(RigidBodyControl.class).getPhysicsLocation();
					Geometry block = Utility.prepareNode(assetManager, "sides", new Box(BLOCK_DIMENSION, BLOCK_DIMENSION, BLOCK_DIMENSION), ColorRGBA.Black, 1, false);
			    	CollisionShape c = CollisionShapeFactory.createMeshShape(block);
			    	rootNode.attachChild(new Block(block, c, (int) loc.getX(), (int) loc.getY() + BLOCK_DIMENSION*2, (int) loc.getZ(), bulletAppState, BLOCK_DIMENSION, "grass", assetManager));
				}	
			}
			else if (closest != null && closest.getGeometry().getControl(RigidBodyControl.class) == null)
			{
				Node item = closest.getGeometry().getParent();
				Geometry g = closest.getGeometry();
				Vector3f loc = g.getLocalTranslation();
				Geometry block = Utility.prepareNode(assetManager, "sides", new Box(BLOCK_DIMENSION, BLOCK_DIMENSION, BLOCK_DIMENSION), ColorRGBA.Black, 1, false);
		    	CollisionShape c = CollisionShapeFactory.createMeshShape(block);
		    	rootNode.attachChild(new Block(block, c, (int) loc.getX(), (int) loc.getY() + BLOCK_DIMENSION, (int) loc.getZ(), bulletAppState, BLOCK_DIMENSION, "grass", assetManager));	
			}
		}
	}
	private void mouseAction()
    {
		CollisionResults results = new CollisionResults();
		Ray r;
		if (cursor)
			r = new Ray(cam.getLocation(), cam.getDirection());
		else
		{
			Vector2f mouseCoors = inputManager.getCursorPosition();
			Vector3f worldCoors = cam.getWorldCoordinates(new Vector2f(mouseCoors.getX(), mouseCoors.getY()), 0f),
			direction = cam.getWorldCoordinates(new Vector2f(mouseCoors.getX(), mouseCoors.getY()), 1f).subtractLocal(worldCoors);
			r = new Ray(worldCoors, direction);

	    }
		rootNode.collideWith(r, results);
		if (results.size() == 0)
			System.out.println("Nothing");
		else
		{
			CollisionResult closest = results.getClosestCollision();
			if (!stateManager.getState(WorldState.class).isEnabled())
			{
				breakCounter = 0;
				return;
			}
			if (closest != null && !closest.getGeometry().getParent().equals(player.getAvatar()))
			{
				breakCounter++;
				System.out.println(breakCounter);
				if (breakCounter > 50 && closest.getGeometry().getParent().getChild(0).getControl(RigidBodyControl.class).getPhysicsLocation().distance(cam.getLocation()) < 300)
				{
					Node item = closest.getGeometry().getParent();
					Vector3f loc = item.getChild(0).getControl(RigidBodyControl.class).getPhysicsLocation();
					for (int i = 0; i < item.getChildren().size(); i++)
					{
						bulletAppState.getPhysicsSpace().remove(item.getChild(i).getControl(RigidBodyControl.class));
					}
					item.removeFromParent();
					item.scale(.2f);
					item.addControl(new RotateControl());
					item.addControl(new RigidBodyControl(new BoxCollisionShape(new Vector3f(1, 1, 1)), .1f));
					rootNode.attachChild(item);
					item.getControl(RigidBodyControl.class).setPhysicsLocation(loc);
					bulletAppState.getPhysicsSpace().remove(closest.getGeometry().getParent().getChild(0).getControl(RigidBodyControl.class));
					breakCounter = 0;
				}
				
			}
		}
    }
	@Override
	protected void initialize(Application arg0) 
	{
		GuiGlobals.getInstance().setCursorEventsEnabled(false);
		jme = (JME) arg0;
		jme.getViewPort().setClearFlags(true, true, true);
		rootNode = jme.getRootNode();
		assetManager = jme.getAssetManager();
		inputManager = jme.getInputManager();
		stateManager = jme.getStateManager();
		renderManager = jme.getRenderManager();
		renderManager.removePreView("background");
		guiNode = jme.getGuiNode();
		cam = jme.getCam();
        baseRotate = cam.getRotation();
		flyCam = jme.getFlyByCamera();
		settings = jme.getSettings();
		guiNode.detachAllChildren();
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);
		bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0, -9.81f, 0));
		prepareManager();
		Block.initManager(assetManager);
		Block.initMaterials();
		assetManager.registerLocator("./worlds", FileLocator.class);
		try 
		{
			Node loadedNode = (Node) assetManager.loadModel(worldName + ".j3o");
			loadedNode.setName("Loaded Node");
			rootNode.attachChild(loadedNode);
			System.out.println(worldName + " is loaded!");
		}
		catch (AssetNotFoundException a)
		{
			player = new Player(jme, pickedUp, "steve.blend", rootNode, guiNode, settings, assetManager, inputManager);
			load();
		}
		catch (AssetLoadException ae)
		{
			player = new Player(jme, pickedUp, "steve.blend", rootNode, guiNode, settings, assetManager, inputManager);
			System.out.println("File didn't work for some reason...........");
			load();	
		}
	}
	private void load()
	{
	//	loadNewWorld();	
		rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", SkyFactory.EnvMapType.CubeMap));
		jme.enqueue(new Runnable()
        {
			@Override
			public void run() 
			{
				for (int i = 0; i < 1; i++)
					Utility.generateChunk(new Vector3f(i*300, 0, i*300), 16, BLOCK_DIMENSION, rootNode, bulletAppState, assetManager);		
			}
        });
		
	}
	private void loadNewWorld()
	{
		rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", SkyFactory.EnvMapType.CubeMap));
        Texture heightMapImage = assetManager.loadTexture("Textures/Terrain/splat/mountains512.png");
		AbstractHeightMap heightmap = null;
        try 
        {
            heightmap = new ImageBasedHeightMap(heightMapImage.getImage(), 0.25f);
            heightmap.load();
        }
        catch (Exception e) {}

		TerrainQuad terrain = new TerrainQuad("terrain", 65, 513, heightmap.getHeightMap());
        List<Camera> cameras = new ArrayList<Camera>();
        cameras.add(cam);
        TerrainLodControl control = new TerrainLodControl(terrain, cameras);
        terrain.addControl(control);
        Texture grass = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
        grass.setWrap(WrapMode.Repeat);
        Material matRock = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
        matRock.setBoolean("useTriPlanarMapping", false);
        matRock.setBoolean("WardIso", true);
        matRock.setTexture("AlphaMap", assetManager.loadTexture("Textures/Terrain/splat/alphamap.png"));
        matRock.setTexture("DiffuseMap", grass);
        matRock.setFloat("DiffuseMap_0_scale", 64);
        Texture dirt = assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg");
        dirt.setWrap(WrapMode.Repeat);
        matRock.setTexture("DiffuseMap_1", dirt);
        matRock.setFloat("DiffuseMap_1_scale", 16);
        Texture rock = assetManager.loadTexture("Textures/Terrain/splat/road.jpg");
        rock.setWrap(WrapMode.Repeat);
        matRock.setTexture("DiffuseMap_2", rock);
        matRock.setFloat("DiffuseMap_2_scale", 128);
        Texture normalMap0 = assetManager.loadTexture("Textures/Terrain/splat/grass_normal.jpg");
        normalMap0.setWrap(WrapMode.Repeat);
        Texture normalMap1 = assetManager.loadTexture("Textures/Terrain/splat/dirt_normal.png");
        normalMap1.setWrap(WrapMode.Repeat);
        Texture normalMap2 = assetManager.loadTexture("Textures/Terrain/splat/road_normal.png");
        normalMap2.setWrap(WrapMode.Repeat);
        matRock.setTexture("NormalMap", normalMap0);
        matRock.setTexture("NormalMap_1", normalMap1);
        matRock.setTexture("NormalMap_2", normalMap2);

        terrain.setMaterial(matRock);
        terrain.setLocalScale(new Vector3f(2, 2, 2));

        RigidBodyControl terrainPhysicsNode = new RigidBodyControl(CollisionShapeFactory.createMeshShape(terrain), 0);
        terrain.addControl(terrainPhysicsNode);
        rootNode.attachChild(terrain);
        bulletAppState.getPhysicsSpace().add(terrainPhysicsNode);
    }
	@Override
	protected void onDisable() 
	{
		flyCam.unregisterInput();
		inputManager.setCursorVisible(true);
		GuiGlobals.getInstance().setCursorEventsEnabled(true);
	}
	@Override
	protected void onEnable() 
	{
		flyCam.registerWithInput(inputManager);
		inputManager.setCursorVisible(false);
		GuiGlobals.getInstance().setCursorEventsEnabled(false);
		
	}
	public void onAnalog(String name, float intensity, float tpf) 
	{
		switch (name)
		{
		}
	}
	private void save() throws IOException
	{
		BinaryExporter e = BinaryExporter.getInstance();
    		e.save(rootNode, new File("./worlds/" + worldName + ".j3o"));
		SaveGame.saveGame("./worlds", worldName + ".j3o", rootNode);
    	System.out.println("saved");
    	System.exit(0);
	}
	@Override
	public void onAction(String name, boolean isPressed, float tpf) 
	{
		switch (name)
		{
			case "Mouse":
				mouseAction();
				break;
			case "RightClick":
				place();
				break;
			default:
				break;
		}
		if (!isPressed)
			breakCounter = 0;
	}
	@Override
    public void write(JmeExporter ex) throws IOException
	{
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(worldName, "worldName", worldName);
        capsule.write(BLOCK_DIMENSION, "blockdimension", 1);
        capsule.write(jme, "jme", jme); 
        capsule.write(rootNode, "rootNode", rootNode);
        capsule.write(guiNode, "guiNode", guiNode);
        capsule.write(cursor, "cursor", true);
        capsule.write(cam, "cam", cam);
        capsule.write(player, "player", player);
        capsule.write(inventoryCounter, "inventoryCounter", -1);
        capsule.write(breakCounter, "breakCounter", -1);
	}
	@Override
    public void read(JmeImporter im) throws IOException 
	{
        InputCapsule capsule = im.getCapsule(this);
        player = (Player) capsule.readSavable("player", player);
        worldName = capsule.readString("worldName", worldName);
        jme = (JME) capsule.readSavable("jme", jme);
        rootNode = (Node) capsule.readSavable("rootNode", rootNode);
        guiNode = (Node) capsule.readSavable("guiNode", guiNode);
        cursor = capsule.readBoolean("cursor", true);
        cam = jme.getCam();
        inventoryCounter = capsule.readInt("inventoryCounter", -1);
        breakCounter = capsule.readInt("breakCounter", -1);
	}
}