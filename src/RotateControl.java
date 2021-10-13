import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

public class RotateControl extends AbstractControl 
{
	@Override
	protected void controlUpdate(float tpf)
	{
		spatial.rotate(tpf, tpf, tpf);
	}

	@Override
	protected void controlRender(RenderManager arg0, ViewPort arg1) {
		
	}

}
