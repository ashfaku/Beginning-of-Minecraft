import java.io.File;
import java.io.IOException;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
/**
 * @author Monkeyman 
 */

public class JME extends SimpleApplication implements Savable
{
	private HomePageState homePage;
	private static AppSettings settings;
	public ViewPort pv;
	private float[] angles = new float[3];
	private Quaternion tmpQuat = new Quaternion();
	private int counter = 0;
	public Camera getCam()
    {
    	return cam;
    }
   	private static AppSettings prepareSettings()
   	{
   		AppSettings s = new AppSettings(true);
   		s.setTitle("Bootleg Minecraft");
   		//s.setFullscreen(true);
   		//s.setResolution(0, 0);
   		return s;
   	}
	public static void main(String[] args)
    {
        JME app = new JME();
        settings = prepareSettings();
        app.setSettings(settings); 
        Cheese.main(args);
    //    app.setShowSettings(false);
        app.start(); // start the game
        app.enqueue(new Runnable()
        {
			@Override
			public void run() 
			{
				
			}
        });
    }
	public AppSettings getSettings()
	{
		return settings;
	}
	@Override
    public void simpleInitApp() 
    {		
		cam.setFrustumFar(2000);
		//AudioNode music = new AudioNode(assetManager, "music.wav", DataType.Stream);
		//music.setDirectional(false);
		//music.setLooping(true);
		//music.play();
		homePage = new HomePageState();
		stateManager.attach(homePage);
		Picture p = new Picture("background");
		p.setImage(assetManager, "dirt.png", false);
		p.setWidth(settings.getWidth());
		p.setHeight(settings.getHeight());
		p.setPosition(0, 0);
		pv = renderManager.createPreView("background", cam);
		pv.setClearFlags(true, true, true);
		pv.attachScene(p);
		viewPort.setClearFlags(false, true, true);			
		p.updateGeometricState();
	}
    @Override
    public void simpleUpdate(float tpf)
    {	
    	if (stateManager.getState(WorldState.class) != null && stateManager.getState(WorldState.class).getPlayer() != null)
    	{
    		WorldState state = stateManager.getState(WorldState.class);
    		Vector3f camDir = cam.getDirection().clone().multLocal(0.6f);
	        Vector3f camLeft = cam.getLeft().clone().multLocal(0.4f);
	        boolean[] array = state.getPlayer().getWalkBooleans();
	        // forward, left, right, backwards, jump
	        state.getPlayer().getWalkDirection().set(0, 0, 0);
	        if(array[1])
	            state.getPlayer().getWalkDirection().addLocal(camLeft);
	        if(array[2])
	            state.getPlayer().getWalkDirection().addLocal(camLeft.negate());
	        if(array[0])
	            state.getPlayer().getWalkDirection().addLocal(camDir);
	        if(array[3])
	            state.getPlayer().getWalkDirection().addLocal(camDir.negate());
	        if (array[4] && state.getPlayer().getPhysicsCharacter().onGround())
	        	state.getPlayer().getPhysicsCharacter().jump();
	        cam.getRotation().toAngles(angles);
	        if(angles[0] > FastMath.HALF_PI)
	        {
		        angles[0] = FastMath.HALF_PI;
		        cam.setRotation(tmpQuat.fromAngles(angles));
	        }
	        else if(angles[0] < -FastMath.HALF_PI)
	        {
		        angles[0] = -FastMath.HALF_PI;
		        cam.setRotation(tmpQuat.fromAngles(angles));
	        }
	        if (state.checkPOV() != null && state.checkPOV())
	        {
	        }
	        else
	        {
	        	state.getPlayer().getPhysicsCharacter().setWalkDirection(state.getPlayer().getWalkDirection());
		        state.getPlayer().getAvatar().getControl(CharacterControl.class).setPhysicsLocation(cam.getLocation());
			     
	        	cam.setLocation(new Vector3f(state.getPlayer().getPhysicsCharacter().getPhysicsLocation().getX(),
		        state.getPlayer().getPhysicsCharacter().getPhysicsLocation().getY() + 30, 
		        state.getPlayer().getPhysicsCharacter().getPhysicsLocation().getZ()));
	        }
	        if (!state.getPlayer().getPhysicsCharacter().onGround())
	        {
	        	cam.setLocation(new Vector3f(state.getPlayer().getPhysicsCharacter().getPhysicsLocation().getX(),
	    		        state.getPlayer().getPhysicsCharacter().getPhysicsLocation().getY() + 30, 
	    		        state.getPlayer().getPhysicsCharacter().getPhysicsLocation().getZ()));
	        	counter++;
	        }
	        else
	        	counter = 0;
	    }
    }
	@Override
	public void read(JmeImporter arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void write(JmeExporter arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}
}
