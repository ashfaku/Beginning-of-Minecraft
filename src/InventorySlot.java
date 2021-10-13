import java.io.IOException;

import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.SpringGridLayout;

public class InventorySlot extends Container implements Savable
{
	private int count;
	private AssetManager assetManager;
	private String whichSlot;
	private Node guiNode;
	private Label itemCountLabel;
	public InventorySlot(Item i, int x, int y, int z, Node g, String background, int label, AssetManager a, int dimension)
	{
		super();
		//SpringGridLayout layout = (SpringGridLayout) this.getLayout();
		guiNode = g;
		count = label;
		assetManager = a;
		whichSlot = background;	
		this.setPreferredSize(new Vector3f(dimension, dimension, dimension));
		this.setLocalTranslation(x, y, z);
		IconComponent icon = new IconComponent(background), item;
		icon.setIconSize(new Vector2f(dimension, dimension));	
		if (i != null)
		{
			item = new IconComponent(i.getURL());
			item.setIconSize(new Vector2f(30, 30));
		}
		this.setBackground(icon);
		/*
		Container c = this.addChild(new Container());
		c.setPreferredSize(new Vector3f(30, 30, 30));
		layout.addChild(c, 0, 0);
		c.setBackground(item);
		
		itemCountLabel = new Label(count + "");
		itemCountLabel.setFontSize(64);
		itemCountLabel.setColor(ColorRGBA.Orange);
		itemCountLabel.setLocalTranslation(x, y + 10, 150);
		 */
	}
	@Override
    public void write(JmeExporter ex) throws IOException 
	{
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(count, "count", 1);
        capsule.write(whichSlot, "whichSlot", "1");
        capsule.write(guiNode, "guiNode", guiNode);
    }
	@Override
    public void read(JmeImporter im) throws IOException 
	{
        InputCapsule capsule = im.getCapsule(this);
        count = capsule.readInt("count", 1);
        whichSlot = capsule.readString("whichSlot", "1");
    }
	/*private int count;
	private AssetManager assetManager;
	private String whichSlot;
	private Node guiNode;
	private Label itemCountLabel;
*/	
	public void showLabel() 
	{
	//	guiNode.attachChild(itemCountLabel);
	}
}
