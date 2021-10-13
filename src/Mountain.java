import java.util.ArrayList;

import com.jme3.asset.AssetManager;
public class Mountain 
{
	private ArrayList<ArrayList<Block>> content;
	public Mountain(AssetManager a)
	{
		content = new ArrayList<ArrayList<Block>>();
		int[] height = new int[(int)(Math.random() * 20) + 10];
		height[0] = (int)(Math.random() * 100);
		for (int i = 0; i < height.length; i++)
		{
			content.add(new ArrayList<Block>());
			if (i != 0)
				height[i] = (int)(Math.random() * height[i-1] * .85);
			for (int j = 0; j < height[i]; j++)
			{
//				content.get(i).add(new Block());
			}
		}
	}
}
