import java.io.IOException;

import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.input.InputManager;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.event.DefaultMouseListener;
import com.simsilica.lemur.event.MouseEventControl;

public class Inventory implements Savable
{	
	private InventorySlot[][] inventory;
	private Item[][] items;
	private int[][] itemCounts;
	private AssetManager assetManager;
	private InputManager inputManager;
	private AppSettings settings;
	private Node guiNode;
	private Item pickedUp;
	public Inventory(Item p, InputManager iM, Node gN, int rows, int cols, AssetManager a, AppSettings as)
	{
		inputManager = iM;
		pickedUp = p;
		guiNode = gN;
		assetManager = a;
		settings = as;
		inventory = new InventorySlot[rows][cols];
		items = new Item[rows][cols];
		itemCounts = new int[rows][cols];
	}
	public Item getPickedUp()
	{
		return pickedUp;
	}
	public void setPickedUp(Item s)
	{
		pickedUp = s;
	}
	public InventorySlot[][] getIcons()
	{
		return inventory;
	}
	public Item[][] getItems()
	{
		return items;
	}
	public void setUpInventory()
	{
		for (int i = 0; i < inventory.length; i++)
		{
			for (int j = 0; j < inventory[i].length; j++)
			{
				int x = settings.getWidth() / 2- 250 + j*50;
				int y = (int) (settings.getHeight() * .66) - i*50;
				if (i == 0)
				{
					items[i][j] = new Item("diamondSword", assetManager);
					inventory[i][j] = new InventorySlot(items[i][j], x, 50, 50, guiNode, "inventoryslot.PNG", itemCounts[i][j], assetManager, 50);		
					items[i][j].setLocalTranslation(x + 10, 10, 100);
				}
				else
				{
					inventory[i][j] = new InventorySlot(items[i][j], x, y, 50, guiNode, "inventoryslot.PNG", itemCounts[i][j], assetManager, 50);		
					if (items[i][j] != null)
						items[i][j].setLocalTranslation(x + 10, y + 10 - 50, 100);
				}
				final int ii = i, jj = j;
				MouseEventControl.addListenersToSpatial(items[ii][jj], new DefaultMouseListener()
				{
					@Override
					public void mouseButtonEvent(MouseButtonEvent e, Spatial t, Spatial c)
					{
						if (pickedUp == null && items[ii][jj] != null)
						{
							items[ii][jj].setWidth(50);
							items[ii][jj].setHeight(50);
							pickedUp = items[ii][jj];
							pickedUp.addControl(new DragAndDropControl(inputManager.getCursorPosition(), items[ii][jj]));
							items[ii][jj] = null;
						}
						else
						{
							putBackIn();
						}
					}
				});
			}
		}
		showHotbar();
	}
	private void putBackIn()
	{
		Vector2f position = inputManager.getCursorPosition();
		int x = (int) position.getX(), y = (int) position.getY();
		for (int i = 0; i < inventory.length; i++)
		{
			for (int j = 0; j < inventory[i].length; j++)
			{
				Vector3f p = inventory[i][j].getLocalTranslation();
				if (x > p.getX() - 10 && x < p.getX() + 50 && y > p.getY() - 50 && y < p.getY())
				{
					if (pickedUp != null && items[i][j] == null)
					{
						pickedUp.removeControl(DragAndDropControl.class);
						pickedUp.setWidth(30);
						pickedUp.setHeight(30);
						final int ii = i, jj = j;
						MouseEventControl.addListenersToSpatial(pickedUp, new DefaultMouseListener()
						{
							@Override
							public void mouseButtonEvent(MouseButtonEvent e, Spatial t, Spatial c)
							{
								if (pickedUp == null && items[ii][jj] != null)
								{
									items[ii][jj].setWidth(50);
									items[ii][jj].setHeight(50);
									pickedUp = items[ii][jj];
									pickedUp.addControl(new DragAndDropControl(inputManager.getCursorPosition(), items[ii][jj]));
									items[ii][jj] = null;
								}
								else
								{
									putBackIn();
								}
							}
						});
						pickedUp.setLocalTranslation(p.getX() + 10, p.getY() - 40, 100);
						items[i][j] = pickedUp;
						pickedUp = null;
						showInventory();
						return;
					
					}
				}
			}
		}
	}
	public void showInventory()
	{
		guiNode.detachAllChildren();
		for (int i = 0; i < inventory.length; i++)
		{
			for (int j = 0; j < inventory[i].length; j++)
			{
				if (items[i][j] != null)
					guiNode.attachChild(items[i][j]);
				guiNode.attachChild(inventory[i][j]);
			}
		}
	}
	public void removeHotbar()
	{
		int diff = (int) inventory[1][0].getLocalTranslation().getY() - (int) inventory[0][0].getLocalTranslation().getY() - 50;
		for (int i = 0; i < inventory[0].length; i++)
		{
			guiNode.detachChild(inventory[0][i]);
			inventory[0][i].move(0, diff + 100, 0);
			if (items[0][i] != null)
			{
				guiNode.detachChild(items[0][i]);	
				items[0][i].move(0, diff + 100 , 0);
			}
		}
	}
	private void centerMark()
   	{
   		Geometry g = Utility.prepareNode(assetManager, "Box", new Box(1, 1, 1), ColorRGBA.White, 4, false);
   		g.setLocalTranslation(Vector3f.ZERO);
   		g.scale(4);
   		g.setLocalTranslation(settings.getWidth() / 2, settings.getHeight() / 2, 0);
   		guiNode.attachChild(g);
   	}
	public void showHotbar()
	{
		centerMark();
		for (int i = 0; i < inventory[0].length; i++)
		{
		//	inventory[0][i].showLabel();
			int x = settings.getWidth() / 2 - 250 + i*50;
			if (items[0][i] != null)
			{
				items[0][i].setLocalTranslation(x + 10, 10, 100);
				guiNode.attachChild(items[0][i]);
			}
			inventory[0][i].setLocalTranslation(x, 50, 50);
			guiNode.attachChild(inventory[0][i]);
		}
	}
	@Override
	public void read(JmeImporter arg0) throws IOException 
	{
		InputCapsule capsule = arg0.getCapsule(this);
		inventory = (InventorySlot[][]) capsule.readSavableArray2D("inventory", inventory);   
		items = (Item[][]) capsule.readSavableArray2D("items", items);
		itemCounts = capsule.readIntArray2D("itemCounts", itemCounts);
		guiNode = (Node) capsule.readSavable("guiNode", guiNode);
		pickedUp = (Item) capsule.readSavable("pickedUp", pickedUp);
	}
	@Override
	public void write(JmeExporter arg0) throws IOException 
	{
		OutputCapsule capsule = arg0.getCapsule(this);
		capsule.write(inventory, "inventory", inventory);
		capsule.write(items, "items", items);
		capsule.write(itemCounts, "itemCounts", itemCounts);
		capsule.write(guiNode, "guiNode", guiNode);
		capsule.write(pickedUp, "pickedUp", pickedUp);
	}
}
