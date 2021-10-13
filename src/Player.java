import java.io.IOException;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.objects.PhysicsCharacter;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl;
import com.jme3.system.AppSettings;

public class Player extends Entity implements ActionListener, Savable
{
	private Node guiNode, rootNode;
	private Inventory inventory;
	private int health = 20;
	private AppSettings settings;
	private Node avatar;
	private AssetManager assetManager;
	private InputManager inputManager;
	private CharacterControl control;
	private CameraNode cameraNode;
	private Vector3f walkDirection = new Vector3f(0, 0, 0), viewDirection = new Vector3f(0, 0, 1);
	private float speed = 8;
	// forward, left, right, backwards
	private boolean[] walking = {false, false, false, false, false};
	private float airTime = 0;
	private PhysicsCharacter physicsCharacter;
	public int getHealth()
	{
		return health;
	}
	public void setHealth(int t)
	{
		health -= t;
	}
	public Vector3f getWalkDirection()
	{
		return walkDirection;
	}
	public float getAirTime()
	{
		return airTime;
	}
	public void setAirTime(float t)
	{
		airTime += t;
	}
	public void resetAirTime()
	{
		airTime = 0;
	}
	public Vector3f getViewDirection()
	{
		return viewDirection;
	}
	public boolean[] getWalkBooleans()
	{
		return walking;
	}
	public float getSpeed()
	{
		return speed;
	}
	public CharacterControl getPlayerControl()
	{
		return control;
	}
	public PhysicsCharacter getPhysicsCharacter()
	{
		return physicsCharacter;
	}
	public Player()
	{
		
	}
	public Player(JME j, Item pickedUp, String blendFile, Node rootNode, Node guiNode, AppSettings settings, AssetManager assetManager, InputManager inputManager)
	{
		super(j);
		physicsCharacter = new PhysicsCharacter(new SphereCollisionShape(10), 10f);
        physicsCharacter.setJumpSpeed(40);
        physicsCharacter.setFallSpeed(40);
        avatar = (Node) assetManager.loadModel(blendFile);
		CapsuleCollisionShape capsule = new CapsuleCollisionShape(5f, 25f);
        control = new CharacterControl(capsule, 0.01f);
        avatar.addControl(control);
        control.setPhysicsLocation(new Vector3f(0, 0, 0));
        rootNode.attachChild(avatar);
        j.getStateManager().getState(BulletAppState.class).getPhysicsSpace().add(control);
		this.guiNode = guiNode;
		this.rootNode = rootNode;
		control.setPhysicsLocation(new Vector3f(1, 10, 0));	       
		physicsCharacter.setPhysicsLocation(control.getPhysicsLocation());
	    j.getStateManager().getState(BulletAppState.class).getPhysicsSpace().add(physicsCharacter);
	     
		avatar.scale(.2f);
	    this.settings = settings;
		this.inputManager = inputManager;
		this.assetManager = assetManager;
		this.rootNode.attachChild(avatar);
		inventory = new Inventory(pickedUp, inputManager, this.guiNode, 4, 9, this.assetManager, this.settings);
		inventory.setUpInventory();
		setUpControls(j);
	}
	private void setUpControls(JME j)
	{
		control.setGravity(new Vector3f(0, -9.81f, 0));
		//avatar.addControl(control);
		//j.getStateManager().getState(BulletAppState.class).getPhysicsSpace().add(control);
		inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("Backwards", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addListener(this, "Forward", "Backwards", "Jump");
		inputManager.addListener(this, "Left", "Right");
	}
    
    public Node getAvatar() 
	{
		return avatar;
	}
	public void setPickedUp(Item s)
	{
		inventory.setPickedUp(s);
	}
	public Inventory getInventory()
	{
		return inventory;
	}
	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		switch (name)
		{
			case "Forward":
				walking[0] = isPressed;
				break;
			case "Left":
				walking[1] = isPressed;
				break;
			case "Right":
				walking[2] = isPressed;
				break;
			case "Backwards":
				walking[3] = isPressed;
				break;
			case "Jump":
				walking[4] = isPressed;
		}
		return;
	}
	public void write(JmeExporter ex) throws IOException 
	{
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(inventory, "Inventory", inventory);
        capsule.write(avatar, "avatar", avatar);
        capsule.write(control, "control", control);
        capsule.write(speed, "speed", speed);
        capsule.write(walkDirection, "walkDirection", walkDirection);
        capsule.write(viewDirection, "viewDirection", viewDirection);
        capsule.write(walking, "walking", walking);
        capsule.write(airTime, "airTime", airTime);
        capsule.write(physicsCharacter, "physicsCharacter", physicsCharacter);
    }
    public void read(JmeImporter im) throws IOException 
    {
        InputCapsule capsule = im.getCapsule(this);   
        inventory = (Inventory) capsule.readSavable("inventory", inventory);
        avatar = (Node) capsule.readSavable("avatar", avatar);
        control = (CharacterControl) capsule.readSavable("control", control);
        speed = capsule.readFloat("speed", speed);
        walkDirection = (Vector3f) capsule.readSavable("walkDirection", walkDirection);
        viewDirection = (Vector3f) capsule.readSavable("viewDirection", viewDirection);
        walking = capsule.readBooleanArray("walking", walking);
        airTime = capsule.readFloat("airTime", airTime);
        physicsCharacter = (PhysicsCharacter) capsule.readSavable("physicsCharacter", physicsCharacter);
    }
}