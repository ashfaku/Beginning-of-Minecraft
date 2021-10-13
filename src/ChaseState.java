import java.io.IOException;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

public class ChaseState extends AbstractAppState implements Savable
{
	private Ray ray;
	private Camera cam;
	private Node root;
	private SimpleApplication app;
	private AssetManager assetManager;
	@Override
	public void update(float tpf)
	{
		CollisionResults r = new CollisionResults();
    	ray.setOrigin(cam.getLocation());
    	ray.setDirection(cam.getDirection());
		root.collideWith(ray, r);
   		if (r.size() > 0)
		{
   		/*	Geometry t = r.getClosestCollision().getGeometry();
   			if (t.getControl(ChaseControl.class) != null)
   			{
   				if (cam.getLocation().distance(t.getLocalTranslation()) < 10)
   				{
   					t.move(cam.getDirection());
   				}
   			}
		*/}
   	}
	@Override
	public void cleanup()
	{
		
	}
	@Override
	public void initialize(AppStateManager stateManager, Application app)
	{
		super.initialize(stateManager, app);
		this.app = (SimpleApplication) app;
		this.ray = new Ray();
		this.cam = app.getCamera();
		root = this.app.getRootNode();
		this.assetManager = this.app.getAssetManager();
		
		makeCubes(15);
	}
	private Geometry prepareNode(String name, Box b, ColorRGBA c, float scale, boolean isLocal) 
    {
    	Geometry g = new Geometry(name, b);
    	Material m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    	m.setColor("Color", c);
    	g.setMaterial(m);
    	if (isLocal)
    		g.setLocalScale(scale);
    	else
    		g.scale(scale);
    	return g;
    }
	private void makeCubes(int n)
   	{
   		for (int i = 0; i < n; i++)
   		{
   			Geometry g = prepareNode(i + "", new Box(1, 1, 1), ColorRGBA.randomColor(), 1, true);
   			Vector3f location = new Vector3f(FastMath.nextRandomInt(-20, 20), FastMath.nextRandomInt(-20, 20),
   			FastMath.nextRandomInt(-20, 20));
   			g.setLocalTranslation(location);
   		//	if (i % 4 == 0)
   			//	g.addControl(new ChaseControl(cam, root));
   			root.attachChild(g);
   		}
   	}
	@Override
	public void read(JmeImporter im) throws IOException 
	{
		InputCapsule capsule = im.getCapsule(this);
        ray = (Ray) capsule.readSavable("ray", new Ray());
        cam = (Camera) capsule.readSavable("cam", new Camera());
        root = (Node) capsule.readSavable("someJmeObject", new Node());
    //  app = (SimpleApplication) capsule.readSavable("app", new SimpleApplication());
       // assetManager = (DesktopAssetManager) capsule.readSavable("assetManager", new DesktopAssetManager());
	}
	@Override
	public void write(JmeExporter ex) throws IOException {
		OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(ray,   "ray",   new Ray(ray.getOrigin(), ray.getDirection()));
        capsule.write(cam, "cam", cam);
        capsule.write(root,  "root",  root);
        //capsule.write(app, "app", app);
       // capsule.write(assetManager,  "assetManager",  assetManager);    
	}
}
