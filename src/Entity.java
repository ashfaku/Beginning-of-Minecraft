import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Node;

public class Entity extends Node 
{
	public Entity()
	{
		
	}
	public Entity(JME application)
	{
		super("entity");
		this.addControl(new RigidBodyControl(0f));
		if (application.getStateManager().getState(BulletAppState.class) != null)
			application.getStateManager().getState(BulletAppState.class).getPhysicsSpace().add(this);	
	}
	public void setMass(float f)
	{
		this.getControl(RigidBodyControl.class).setMass(f);
	}
}
