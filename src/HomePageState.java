import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapAxis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Insets3f;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.Button.ButtonAction;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;
public class HomePageState extends BaseAppState 
{
	private Node rootNode;
	private SimpleApplication app;
	private AssetManager assetManager;
	@Override
	protected void initialize(Application app)
	{
		this.app = (SimpleApplication) app;
		rootNode = this.app.getRootNode();
		this.assetManager = this.app.getAssetManager();
		setMenu();
	}
	@Override
	protected void cleanup(Application app)
	{
	}
	private void setMenu()
	{
		JME j = (JME) app;
		GuiGlobals.initialize(app);
		GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
		Container window = new Container(new SpringGridLayout());
		Button load = Utility.prepareButton(assetManager, window, "Singleplayer", ColorRGBA.Black, ColorRGBA.Blue, ColorRGBA.Gray, ColorRGBA.White);
		window.setLocalTranslation(j.getSettings().getWidth() / 2 - load.getPreferredSize().getX()/2, j.getSettings().getHeight()/2, 0);
		Button newWorld = Utility.prepareButton(assetManager, window, "Multiplayer", ColorRGBA.Black, ColorRGBA.Blue, ColorRGBA.Gray, ColorRGBA.White);
		Button quit = Utility.prepareButton(assetManager, window, "Quit", ColorRGBA.Black, ColorRGBA.Blue, ColorRGBA.Gray, ColorRGBA.White);
		quit.addClickCommands(new Command<Button>()
		{
			@Override
			public void execute(Button arg0)
			{
				j.getStateManager().detach(app.getStateManager().getState(HomePageState.class));
				System.exit(0);
			}	
		});
		load.addClickCommands(new Command<Button>()
		{
			@Override
			public void execute(Button arg0) 
			{
				j.getStateManager().detach(app.getStateManager().getState(HomePageState.class));
				j.getStateManager().attach(new CreateWorldState(window, app));	
			}
		});
		newWorld.addClickCommands(new Command<Button>()
		{
			@Override
			public void execute(Button arg0) 
			{
				System.out.println("In progress...");
			}
		});
		j.getGuiNode().attachChild(window);
	}
	@Override
	protected void onDisable() {
		// TODO Auto-generated method stub
	}
	@Override
	protected void onEnable() {
		// TODO Auto-generated method stub
		
	}
}
