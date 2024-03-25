package kapitel01.loesungen;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Model {
	public List<Vector3f> vertices = new ArrayList<Vector3f>();
	public List<Vector3f> normals = new ArrayList<Vector3f>();
	public List<Vector2f> texCoords = new ArrayList<Vector2f>();
	public List<FaceTriangle> faces = new ArrayList<FaceTriangle>();
	public List<FaceQuad> facesQuads = new ArrayList<FaceQuad>();	
	public float size;

	public Model() {
	}
}
