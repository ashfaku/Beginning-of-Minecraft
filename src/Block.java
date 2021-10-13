import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.export.Savable;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;
public class Block extends Node implements Savable
{
	private Geometry block;
	private RigidBodyControl control;
	private static AssetManager assetManager;
	private static String[] names = {"grass", "stone",  "diamond", "oak_wood", "emerald_ore"};
	private static Material[][] materials;
	public static void initManager(AssetManager a)
	{
		assetManager = a;
	}
	public static void initMaterials()
	{
		materials = new Material[names.length][3];
		for (int i = 0; i < names.length; i++)
		{
			materials[i][0] = prepareMaterial(assetManager, "Top", names[i]);
			materials[i][1] = prepareMaterial(assetManager, "Side", names[i]);
			materials[i][2] = prepareMaterial(assetManager, "Bottom", names[i]);
		}
	}
	private int indexOf(String s)
	{
		for (int i = 0; i < names.length; i++)
			if (names[i].equals(s))
				return i;
		return -1;
	}
	public Block(Geometry block, CollisionShape c, float x, float y, float z, BulletAppState bulletAppState, int dimension, String blockName, AssetManager assetManager)
	{
		int index = indexOf(blockName);
		this.setCullHint(CullHint.Dynamic);
	    this.attachChild(prepareFlatFace(bulletAppState, blockName, dimension, true, x, y, z, index));
        this.attachChild(prepareFirstSide(x + .3f, y, z + .3f, dimension, index, bulletAppState));
        this.attachChild(prepareSecondSide(x + .3f, y + .3f, z + .3f, dimension, index, bulletAppState));
        this.attachChild(prepareThirdSide(x + .3f, y + .3f, z + .3f, dimension, index, bulletAppState));
        this.attachChild(prepareFourthSide(x + .3f, y + .3f, z + .3f, dimension, index, bulletAppState));
        this.attachChild(prepareFlatFace(bulletAppState, blockName, dimension, false, x, y, z, index));
	}
	private Geometry prepareFourthSide(float x, float y, float z, int dimension, int index, BulletAppState bas) 
	{
		Mesh first = new Mesh();
        Vector3f [] vertices = new Vector3f[4];
        vertices[0] = new Vector3f(x + dimension, y, z);
        vertices[1] = new Vector3f(x + dimension, y - dimension, z);
        vertices[2] = new Vector3f(x + dimension, y, z + dimension);
        vertices[3] = new Vector3f(x + dimension, y - dimension, z + dimension);
        Vector2f [] texCoord = new Vector2f[4];
        texCoord[0] = new Vector2f(0,0);
        texCoord[1] = new Vector2f(0,1);
        texCoord[2] = new Vector2f(1,0);
        texCoord[3] = new Vector2f(1,1);
        short[] indexes = {2, 0, 1, 1, 3, 2};
        first.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        first.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
        first.setBuffer(Type.Index, 1, BufferUtils.createShortBuffer(indexes));
        first.updateBound();
        Geometry firstSide = new Geometry("OurMesh", first);
        RigidBodyControl c = new RigidBodyControl(CollisionShapeFactory.createMeshShape(firstSide), 0f);
        firstSide.addControl(c);
        bas.getPhysicsSpace().add(c);

        firstSide.setMaterial(materials[index][1]);
        return firstSide;	
	}
	private Geometry prepareThirdSide(float x, float y, float z, int dimension, int index, BulletAppState bas)
	{
		Mesh first = new Mesh();
        Vector3f [] vertices = new Vector3f[4];
        vertices[0] = new Vector3f(x, y, z + dimension);
        vertices[1] = new Vector3f(x + dimension, y, z + dimension);
        vertices[2] = new Vector3f(x, y - dimension, z + dimension);
        vertices[3] = new Vector3f(x + dimension, y - dimension, z + dimension);
        Vector2f [] texCoord = new Vector2f[4];
        texCoord[0] = new Vector2f(0,0);
        texCoord[1] = new Vector2f(1,0);
        texCoord[2] = new Vector2f(0,1);
        texCoord[3] = new Vector2f(1,1);
        short[] indexes = {2, 0, 1, 1, 3, 2};
        first.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        first.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
        first.setBuffer(Type.Index, 1, BufferUtils.createShortBuffer(indexes));
        first.updateBound();
        Geometry firstSide = new Geometry("OurMesh", first);
        RigidBodyControl c = new RigidBodyControl(CollisionShapeFactory.createMeshShape(firstSide), 0f);
        firstSide.addControl(c);
        bas.getPhysicsSpace().add(c);

        firstSide.setMaterial(materials[index][1]);
        return firstSide;		
	}
	private Geometry prepareSecondSide(float x, float y, float z, int dimension, int index, BulletAppState bas)
	{
		Mesh first = new Mesh();
        Vector3f [] vertices = new Vector3f[4];
        vertices[0] = new Vector3f(x, y, z);
        vertices[1] = new Vector3f(x, y - dimension, z);
        vertices[2] = new Vector3f(x, y, z + dimension);
        vertices[3] = new Vector3f(x, y - dimension, z + dimension);
        Vector2f [] texCoord = new Vector2f[4];
        texCoord[0] = new Vector2f(0,0);
        texCoord[1] = new Vector2f(0,1);
        texCoord[2] = new Vector2f(1,0);
        texCoord[3] = new Vector2f(1,1);
        short[] indexes = {2, 0, 1, 1, 3, 2};
        first.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        first.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
        first.setBuffer(Type.Index, 1, BufferUtils.createShortBuffer(indexes));
        first.updateBound();
        Geometry firstSide = new Geometry("OurMesh", first);
        RigidBodyControl c = new RigidBodyControl(CollisionShapeFactory.createMeshShape(firstSide), 0f);
        firstSide.addControl(c);
        bas.getPhysicsSpace().add(c);
       
        firstSide.setMaterial(materials[index][1]);
        return firstSide;	
	}
	private Geometry prepareFirstSide(float x, float y, float z, int dimension, int index, BulletAppState bas)
	{
		Mesh first = new Mesh();
        Vector3f [] vertices = new Vector3f[4];
        vertices[0] = new Vector3f(x,y,z);
        vertices[1] = new Vector3f(x + dimension, y, z);
        vertices[2] = new Vector3f(x, y - dimension, z);
        vertices[3] = new Vector3f(x + dimension, y - dimension,z);
        Vector2f [] texCoord = new Vector2f[4];
        texCoord[0] = new Vector2f(0,0);
        texCoord[1] = new Vector2f(1,0);
        texCoord[2] = new Vector2f(0,1);
        texCoord[3] = new Vector2f(1,1);
        short[] indexes = {2, 0, 1, 1, 3, 2};
        first.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        first.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
        first.setBuffer(Type.Index, 1, BufferUtils.createShortBuffer(indexes));
        first.updateBound();
        Geometry firstSide = new Geometry("OurMesh", first);
        RigidBodyControl c = new RigidBodyControl(CollisionShapeFactory.createMeshShape(firstSide), 0f);
        firstSide.addControl(c);
        bas.getPhysicsSpace().add(c);
       
        firstSide.setMaterial(materials[index][1]);
        return firstSide;
	}
	private Geometry prepareFlatFace(BulletAppState bas, String block, int dimension, boolean top, float x, float y, float z, int index)
	{
		Mesh m = new Mesh();
		
		Vector3f[] vertices = new Vector3f[4];
		vertices[0] = new Vector3f(x, y, z);
		vertices[1] = new Vector3f(x, y, z + dimension); 
		vertices[2] = new Vector3f(x + dimension, y, z); 
		vertices[3] = new Vector3f(x + dimension, y, x + dimension); 
		
		vertices[0] = new Vector3f(0, 0, 0);
		vertices[1] = new Vector3f(0, 0, dimension); 
		vertices[2] = new Vector3f(dimension,  0, 0f); 
		vertices[3] = new Vector3f(dimension, 0, dimension); 
		
		Vector2f[] texCoord = new Vector2f[4];
		texCoord[0] = new Vector2f(0, 0);
		texCoord[1] = new Vector2f(1, 0);
		texCoord[2] = new Vector2f(0, 1);
		texCoord[3] = new Vector2f(1, 1);
		int[] indexes = 
		{
				2, 0, 1, 1, 3, 2
		};
		m.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
		m.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
		m.setBuffer(Type.Index, 1, BufferUtils.createIntBuffer(indexes));
		m.updateBound();
		Geometry geom = new Geometry("name", m);
        RigidBodyControl c = new RigidBodyControl(CollisionShapeFactory.createMeshShape(geom), 0f);
        geom.addControl(c);
        bas.getPhysicsSpace().add(c);
        c.setPhysicsLocation(new Vector3f(x, top ? y : y - dimension, z));
       	geom.setMaterial(materials[index][top ? 0 : 2]);     
        return geom;
	}
	private static Material prepareMaterial(AssetManager assetManager, String name, String whichBlock) 
	{
		Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		Texture texture = null;
		try
		{
			texture = assetManager.loadTexture(new TextureKey("blocks/" + whichBlock + "/" + name + ".PNG", false));
		}
		catch (AssetNotFoundException a)
		{
			texture = assetManager.loadTexture(new TextureKey("blocks/" + whichBlock + "/" + name + ".png", false));	
		}
		material.setTexture("ColorMap", texture);
		return material;
	}
}