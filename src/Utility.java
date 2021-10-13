import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapFont;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Insets3f;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.Button.ButtonAction;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;

public class Utility 
{
    /**
    * creates an empty node with a RigidBodyControl
    *
    * @param manager for loading assets
    * @param shape a shape for the collision object
    * @param mass a mass for rigid body
    * @return a new Node
    */
    public static Node createPhysicsTestNode(AssetManager manager, CollisionShape shape, float mass) 
    {
        Node node = new Node("PhysicsNode");
        RigidBodyControl control = new RigidBodyControl(shape, mass);
        node.addControl(control);
        return node;
    }
    private static void generateLayer(Vector3f corner, int y, int BLOCK_DIMENSION, Node rootNode, BulletAppState bulletAppState, AssetManager assetManager)
    {
    	String[] s = {"grass", "stone", "diamond", "oak_wood", "emerald_ore"};
    	Geometry block = null;
  //  	CollisionShape c = CollisionShapeFactory.createMeshShape(block);
    	for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++)
				rootNode.attachChild(new Block(null, null, (i* BLOCK_DIMENSION) + (int) corner.getX() + .3f, y + (int) corner.getY() + .3f, (int) .3f + corner.getZ() + (j*BLOCK_DIMENSION), bulletAppState, BLOCK_DIMENSION, y == 0 ? "grass" : s[(int)(Math.random() * s.length)], assetManager));
    }
    public static void generateChunk(Vector3f corner, int layers, int dimension, Node rootNode, BulletAppState bas, AssetManager assetManager)
    {
    	for (int i = 0; i > -3; i--)
    	{
    		generateLayer(corner, i * dimension, dimension, rootNode, bas, assetManager);
    	}
    }
	public static Geometry prepareNode(AssetManager a, String name, Box b, ColorRGBA c, float scale, boolean isLocal) 
    {
    	Geometry g = new Geometry(name, b);
    	Material m = new Material(a, "Common/MatDefs/Misc/Unshaded.j3md");
    	m.setColor("Color", c);
    	g.setMaterial(m);
    	if (isLocal)
    		g.setLocalScale(scale);
    	else
    		g.scale(scale);
    	return g;
    }
	public static void makeCubes(int n, Node rootNode, AssetManager a)
   	{
   		for (int i = 0; i < n; i++)
   		{
   			Geometry g = prepareNode(a, i + "", new Box(1, 1, 1), ColorRGBA.randomColor(), 1, true);
   			Vector3f location = new Vector3f(FastMath.nextRandomInt(-20, 20), FastMath.nextRandomInt(-20, 20),
   			FastMath.nextRandomInt(-20, 20));
   			g.setLocalTranslation(location);
   			//if (i % 4 == 0)
   				//g.addControl(new ChaseControl(cam, root));
   			rootNode.attachChild(g);
   		}
   	}
	@SuppressWarnings("unchecked")
	public static Button prepareButton(AssetManager a, Container window, String name, ColorRGBA border, ColorRGBA over, ColorRGBA background, ColorRGBA text)
	{
		Button b = window.addChild(new Button(name));
		b.setBackground(new QuadBackgroundComponent(background));
		b.setColor(text);
		b.setHighlightColor(over);
		b.setTextHAlignment(HAlignment.Center);
		b.setTextVAlignment(VAlignment.Center);
		b.setInsets(new Insets3f(8, 8, 8, 8));
		Vector3f f = b.getLocalTranslation();
		b.setPreferredSize(new Vector3f(350, 70, 0));
		BitmapFont font = a.loadFont("Minecraft.ttf.fnt");
		Texture t = GuiGlobals.getInstance().loadDefaultIcon("bevel-quad.png");
		TbtQuadBackgroundComponent c = TbtQuadBackgroundComponent.create
		(t, 1.0f, 8, 8, 119, 119, 0.02f, false);
		b.setBorder(c);
		b.setFont(font);
		b.addCommands(ButtonAction.HighlightOn, new Command<Button>()
		{
			@Override
			public void execute(Button arg0) 
			{
				b.setColor(ColorRGBA.Orange);
				b.setBorder(new QuadBackgroundComponent(ColorRGBA.Blue));
			}		
		});
		b.addCommands(ButtonAction.HighlightOff, new Command<Button>()
		{
			@Override
			public void execute(Button arg0)
			{
				Texture to = GuiGlobals.getInstance().loadDefaultIcon("bevel-quad.png");
				TbtQuadBackgroundComponent co = TbtQuadBackgroundComponent.create
				(to, 1.0f, 8, 8, 119, 119, 0.02f, false);
				b.setBorder(co);
			}
		});	
		return b;
	}
}