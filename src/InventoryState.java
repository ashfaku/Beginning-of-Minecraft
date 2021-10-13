import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;

public class InventoryState extends BaseAppState 
{
	private Node guiNode;
	private Player player;
	private Inventory inventory;
	public InventoryState(JME jme, Player p)
	{
		player = p;
		guiNode = jme.getGuiNode();
	}
	@Override
	protected void cleanup(Application arg0) 
	{
		guiNode.detachAllChildren();
		inventory.showHotbar();
	}
	@Override
	protected void initialize(Application arg0) 
	{
		inventory = player.getInventory();
		inventory.removeHotbar();
		inventory.showInventory();
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
