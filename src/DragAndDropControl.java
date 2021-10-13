import com.jme3.math.Vector2f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

public class DragAndDropControl extends AbstractControl 
{
	private Vector2f mousePos;
	private Item item;
	public DragAndDropControl()
	{
		
	}
	public DragAndDropControl(Vector2f pos, Item i)
	{
		mousePos = pos;
		item = i;
	}
	@Override
	protected void controlRender(RenderManager arg0, ViewPort arg1) 
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void controlUpdate(float arg0) 
	{
		item.setLocalTranslation(mousePos.getX(), mousePos.getY(), item.getLocalTranslation().getZ());
	}
}
