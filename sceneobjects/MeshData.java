https://tutorcs.com
WeChat: cstutorcs
QQ: 749389476
Email: tutorcs@163.com
https://tutorcs.com
WeChat: cstutorcs
QQ: 749389476
Email: tutorcs@163.com
package comp3170.ass3.sceneobjects;

import static org.lwjgl.assimp.Assimp.aiImportFile;
import static org.lwjgl.assimp.Assimp.aiReleaseImport;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;

/**
 * A simple interface to the Assimp (Asset Importer) library, to import models
 * from Wavefront OBJ files. 
 * 
 * Example usage:
 * 
 *    MeshData data = new MeshData("path/filename.obj");
 *    int vertexBuffer = GLBuffers.createBuffer(data.vertices);
 *    int indexBuffer = GLBuffers.createIndexBuffer(data.indices);
 * 
 * Note: this is a barebones implementation that assumes the file contains only
 * one model with no subparts.
 * 
 * @author malcolmryan
 *
 */

public class MeshData {
	public Vector4f[] vertices;
	public Vector4f[] normals;
	public Vector2f[] uvs;
	public int[] indices;

	public MeshData(String filename) {
		AIScene scene = aiImportFile(filename, Assimp.aiProcess_Triangulate);
		PointerBuffer meshes = scene.mMeshes();

		// Assuming the file contains a single model
		// Load all meshes in the file into one set of buffers
		
		List<Vector4f> vList = new ArrayList<Vector4f>();
		List<Vector4f> nList = new ArrayList<Vector4f>();
		List<Vector2f> uvList = new ArrayList<Vector2f>();
		List<Integer> iList = new ArrayList<Integer>();

		for (int i = 0; i < meshes.limit(); i++) {
			AIMesh mesh = AIMesh.create(meshes.get(i));
			processMesh(mesh, vList, nList, uvList, iList);

		}

		vertices = vList.toArray(new Vector4f[vList.size()]);
		normals = nList.toArray(new Vector4f[nList.size()]);
		uvs = uvList.toArray(new Vector2f[uvList.size()]);
		
		// Can't convert List<Integer> to int[] automatically
		indices = new int[iList.size()];
		for (int i = 0; i < indices.length; i++) {
			indices[i] = iList.get(i);
		}
		
		aiReleaseImport(scene);

	}

	private static void processMesh(AIMesh mesh, 
			List<Vector4f> vertices, 
			List<Vector4f> normals, 
			List<Vector2f> uvs, 
			List<Integer> indices) {

		AIVector3D.Buffer mVertices = mesh.mVertices();
		for (int i = 0; i < mVertices.limit(); i++) {
			AIVector3D v = mVertices.get(i);
			vertices.add(new Vector4f(v.x(), v.y(), v.z(), 1));
		}

		AIVector3D.Buffer mNormals = mesh.mNormals();
		for (int i = 0; i < mNormals.limit(); i++) {
			AIVector3D n = mNormals.get(i);
			normals.add(new Vector4f(n.x(), n.y(), n.z(), 0));
		}

		AIVector3D.Buffer mTextureCoords = mesh.mTextureCoords(0);
		for (int i = 0; i < mTextureCoords.limit(); i++) {
			AIVector3D uv = mTextureCoords.get(i);
			uvs.add(new Vector2f(uv.x(), uv.y()));
		}
		
		AIFace.Buffer mFaces = mesh.mFaces();
		for (int i = 0; i < mFaces.limit(); i++) {
			AIFace aiFace = mFaces.get(i);
			IntBuffer mIndices = aiFace.mIndices();
			for (int j = 0; j < mIndices.limit(); j++) {
				indices.add(mIndices.get(j));
			}
		}
	}
}
