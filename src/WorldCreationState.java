import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.component.TextEntryComponent;
import com.simsilica.lemur.event.DefaultMouseListener;
import com.simsilica.lemur.event.KeyAction;
import com.simsilica.lemur.event.KeyActionListener;
import com.simsilica.lemur.event.MouseEventControl;
import com.simsilica.lemur.event.PopupState;

public class WorldCreationState extends BaseAppState
{
	private JME jme;
	private Node guiNode, rootNode;
	private String worldName;
	@Override
	protected void cleanup(Application arg0) 
	{
		
	}
	
	@Override
	protected void initialize(Application arg0) 
	{
		jme = (JME) arg0;
		AppSettings settings = jme.getSettings();
		guiNode = jme.getGuiNode();
		guiNode.detachAllChildren();
		TextField getName = new TextField("Please enter your world's name here.");
		MouseEventControl.addListenersToSpatial(getName, new DefaultMouseListener()
		{
			@Override
			public void mouseButtonEvent(MouseButtonEvent m, Spatial target, Spatial capture)
			{
				if (getName.getText().equals("Please enter your world's name here."))
					getName.setText("");
			}	
		});
		getName.setPreferredWidth(500);
		getName.setTextHAlignment(HAlignment.Center);
		getName.setBorder(new QuadBackgroundComponent(ColorRGBA.White));
		guiNode.attachChild(getName);
		getName.getActionMap().put(new KeyAction(KeyInput.KEY_C), new KeyActionListener() 
		{
            @Override
            public void keyAction(TextEntryComponent arg0, KeyAction arg1) 
            {
       /*     	if (getName.getText().length() > 8)
            	{
            		System.out.println(getName.getText());
            		PopupState popup = GuiGlobals.getInstance().getPopupState();
            		popup.showModalPopup(c, ColorRGBA.Blue);
            	}
            	else
            	{
            	}
          */
            	worldName = getName.getText();
            	jme.getStateManager().detach(jme.getStateManager().getState(WorldCreationState.class));
            	jme.getStateManager().attach(new WorldState(worldName));
           }
		});
		getName.setBackground(new QuadBackgroundComponent(ColorRGBA.Blue));
		getName.setLocalTranslation(settings.getWidth() / 2 - getName.getPreferredWidth() / 2, settings.getHeight() / 2, 0);
	}
	@Override
	protected void onDisable() 
	{
		
	}

	@Override
	protected void onEnable() 
	{
		
	}

}
