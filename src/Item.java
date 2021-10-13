import java.io.IOException;

import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.ui.Picture;

public class Item extends Picture implements Savable
{
	private String itemName, itemURL;
	
	public Item(String s, AssetManager assetManager)
	{
		super("item");
		itemName = s;
		itemURL = "items/pictures/" + itemName + ".png";
		this.setHeight(30);
		this.setWidth(30);
		if (itemName.length() > 0)
			this.setImage(assetManager, itemURL, false);
	}
	public String getModelURL()
	{
		return "items/models/" + itemName + ".obj";
	}
	public String getURL()
	{
		return itemURL;
	}
	@Override
    public void write(JmeExporter ex) throws IOException 
	{
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(itemName, "itemName", itemName);
        capsule.write(itemURL, "itemURL", itemURL);
	}
	@Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        itemName = capsule.readString("itemName", itemName);
        itemURL = capsule.readString("itemURL", itemURL);
	}
}
