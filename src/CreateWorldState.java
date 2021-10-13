import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.ui.Picture;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Insets3f;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.Button.ButtonAction;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;
import com.simsilica.lemur.event.DefaultMouseListener;
import com.simsilica.lemur.event.MouseEventControl;
import com.simsilica.lemur.event.MouseListener;

public class CreateWorldState extends BaseAppState
{
	private Container window;
	private Node rootNode;
	private SimpleApplication app;
	private AssetManager assetManager;
	private String nameOfWorld;
	public CreateWorldState(Container c, SimpleApplication a)
	{
		window = c;
		app = a;
	}	
	private boolean checkFileName(String s)
	{
		File dir = new File("./worlds");
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) 
		{
			for (File child : directoryListing) 
			{
				if (child.getName().substring(0, child.getName().lastIndexOf(".")).equals(s))
					return false;
			}
	    }
		return true;
	}
	@Override
	protected void cleanup(Application arg0) 
	{
	
	}
	private Picture makePack(int i)
	{
		Picture p = new Picture("pack");
		p.setImage(assetManager, "pack.png", false);
		p.setWidth(50);
		p.setLocalTranslation(window.getLocalTranslation().getX() - 50, window.getLocalTranslation().getY() - 55 - i * 60, window.getLocalTranslation().getZ());
		p.setHeight(50);
		return p;
	}
	private void setUpWorldList(Container window, JME j)
	{
		System.out.println(1);
		File dir = new File("./worlds");
		File[] directoryListing = dir.listFiles();
		Container middle = window.addChild(new Container(new SpringGridLayout()));
		middle.setBackground(new QuadBackgroundComponent(new ColorRGBA(.05f, .05f, .05f, .05f)));
		ArrayList<Container> worldList = new ArrayList<Container>();
		SpringGridLayout buttonGrid = new SpringGridLayout();
		Container buttons = new Container(buttonGrid);
		Button play = Utility.prepareButton(assetManager, window, "Play Selected World", ColorRGBA.White, ColorRGBA.Blue, ColorRGBA.Gray, ColorRGBA.White);
		buttonGrid.addChild(0, 0, play);
		Button generate = Utility.prepareButton(assetManager, window, "Generate New World", ColorRGBA.White, ColorRGBA.Blue, ColorRGBA.Gray, ColorRGBA.White);
		buttonGrid.addChild(0, 1, generate);
		generate.addClickCommands(new Command<Button>()
		{
			@Override
			public void execute(Button arg0)
			{
				j.getStateManager().detach(app.getStateManager().getState(CreateWorldState.class));
				j.getStateManager().attach(new WorldCreationState());				
			}	
		});
		if (directoryListing != null) 
		{
			for (int i = 0; i < directoryListing.length; i++)
			{
				File child = directoryListing[i];
				Path path = null;
				try 
				{
					path = Paths.get(child.getCanonicalPath());
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
				FileTime fileTime = null;
				try 
				{
				    fileTime = Files.getLastModifiedTime(path);
				} 
				catch (IOException e) 
				{
				    System.err.println("Cannot get the last modified time - " + e);
				}
				Container world = new Container(new SpringGridLayout());
				middle.addChild(world);
				worldList.add(world);
				
				j.getGuiNode().attachChild(makePack(i));
				
				Label worldName = world.addChild(new Label("World Name: " + child.getName().substring(0, child.getName().indexOf("."))));
				Label date = world.addChild(new Label("Date: " + fileTime.toString().substring(0, 10)));
				
				BitmapFont font = assetManager.loadFont("Minecraft.ttf.fnt");
				
				worldName.setFont(font);
				worldName.setFontSize(worldName.getFontSize() - 5);
				worldName.setColor(ColorRGBA.Black);
				worldName.setInsets(new Insets3f(3, 3, 3, 3));
				
				date.setFont(font);
				date.setFontSize(worldName.getFontSize());
				date.setColor(ColorRGBA.Black);
					
				world.setPreferredSize(new Vector3f((int) (j.getSettings().getWidth() * .6), 60, 50));
				world.setInsets(new Insets3f(5, 5, 5, 5));
				world.setBackground(new QuadBackgroundComponent(ColorRGBA.LightGray));
				
				MouseEventControl.addListenersToSpatial(world, new DefaultMouseListener()
				{
					@Override
					public void mouseButtonEvent(MouseButtonEvent m, Spatial target, Spatial capture)
					{
						QuadBackgroundComponent border = new QuadBackgroundComponent(ColorRGBA.White);
						border.setMargin(2, 2);
						world.setBorder(border);
						String h = worldName.getText();
						nameOfWorld = h.substring(h.indexOf(":") + 2);
						for (int i = 0; i < worldList.size(); i++)
						{
							Container c = worldList.get(i);
							c.setBorder(!c.equals(world) ? null : c.getBorder());	
						}
					}	
				});
			}
	    }
		play.addClickCommands(new Command<Button>()
		{
			@Override
			public void execute(Button arg0) 
			{
				if (nameOfWorld != null)
				{
					j.getStateManager().detach(app.getStateManager().getState(CreateWorldState.class));
					j.getStateManager().attach(new WorldState(nameOfWorld));			
				}
			}
		});
		buttons.setLocalTranslation(j.getSettings().getWidth() / 2 - play.getPreferredSize().getX(), window.getLocalTranslation().getY() + 75, 0);
		play.setInsets(new Insets3f(5, 5, 5, 5));
		generate.setPreferredSize(play.getPreferredSize());
		app.getGuiNode().attachChild(buttons);
	}
	@Override
	protected void initialize(Application arg0) 
	{
		rootNode = this.app.getRootNode();
		this.assetManager = this.app.getAssetManager();
		JME j = (JME) app;
		window.detachAllChildren();
		window.setLocalTranslation(j.getSettings().getWidth() / 4, (int) (j.getSettings().getHeight() * .75), 0);
		setUpWorldList(window, j);
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
