import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.RenderManager;

import com.jme3.app.state.BaseAppState;

public class WorldPageState extends BaseAppState {

	@Override
	protected void cleanup(Application arg0) 
	{
		
	}

	@Override
	protected void initialize(Application arg0) 
	{
		File dir = new File("./worlds");
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) 
		{
			for (File child : directoryListing) 
			{
				Path file = Paths.get(child.getName());
	            BasicFileAttributes attr;
				try {
					attr = Files.readAttributes(file, BasicFileAttributes.class);
		            System.out.println("creationTime: " + attr.creationTime());
		            System.out.println("lastAccessTime: " + attr.lastAccessTime());
		            System.out.println("lastModifiedTime: " + attr.lastModifiedTime());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			}
	    }  
	    else
	    {
	    	
	    }
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
