https://tutorcs.com
WeChat: cstutorcs
QQ: 749389476
Email: tutorcs@163.com
https://tutorcs.com
WeChat: cstutorcs
QQ: 749389476
Email: tutorcs@163.com
package comp3170.ass3;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * DO NOT CHANGE THIS FILE.
 * 
 * This class will load the scene data from a given JSON file 
 * and store the values in the fields:
 * 
 * Height map:
 * 	mapWidth: number of vertices wide in the X direction
 * 	mapDepth: number of vertices wide in the Z direction
 *  mapHeights: the height of each vertex as a value between 0 and 1 
 *  mapScale: the world-dimensions of the map
 *  
 * Water:
 * 	waterHeight: the height of the water (from the bottom of the map) in world units
 * 
 * Lighthouse:
 * 	lighthouseIntensity: the RGB values for the intensity of the lighthouse light
 * 	lighthouseAmbient: the RGB values for the ambient intensity of the lighthouse light
 *  lighthousePosition: the (x,z) position of the lighthouse in world coordinates
 *  
 * Sun:
 * 	sunDirection: the source vector for the sun light source in world coordinates
 * 	sunIntensity: the RGB values for the intensity of the sun light
 * 	sunAmbient: the RGB values for the ambient intensity of the sun light
 * 
 * Plane:
 * 	planePosition: the (x,y,z) starting position of the plane in world coordinates
 * 	planeHeading: the starting heading of the plane in degrees from the world z axis
 * 
 * @author malcolmryan
 *
 */
public class SceneData {
	public int mapWidth;
	public int mapDepth;
	public float[] mapHeights;
	public Vector3f mapScale = new Vector3f(1,1,1);

	public float waterHeight;

	public Vector3f lighthouseIntensity = new Vector3f();
	public Vector3f lighthouseAmbient = new Vector3f();
	public Vector2f lighthousePosition = new Vector2f();
	
	public Vector3f sunDirection = new Vector3f();
	public Vector3f sunIntensity = new Vector3f();
	public Vector3f sunAmbient = new Vector3f();

	public Vector3f planePosition = new Vector3f();
	public float planeHeading;
	

	/**
	 * Load scene data from the given JSON file.
	 * See the assignment spec for details of the file format.
	 * 
	 * @param jsonFile
	 */
	public SceneData(File jsonFile) {
		try {
			JSONObject json = new JSONObject(Files.readString(jsonFile.toPath()));

			loadMap(json);		
			loadWater(json);
			loadPlane(json);
			loadLighthouse(json);
			loadSun(json);
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load the heightmap parameters.
	 * @param json
	 * @throws IOException
	 */
	private void loadMap(JSONObject json) throws IOException {
		JSONObject map = json.getJSONObject("map");		
		Object heights = map.get("heights");

		if (heights instanceof String) {
			// assume it's a filename for a heightmap image
			String filename = (String) heights;
			loadHeightsFromImage(filename);
		} else {
			JSONArray array = (JSONArray) heights;
			loadHeightsFromJSON(map, array);
		}
		
		getVector3(map, "scale", mapScale);
	}

	/**
	 * Load heightmap heights from a JSON array
	 * @param json
	 * @param array
	 */
	private void loadHeightsFromJSON(JSONObject json, JSONArray array) {
		// assume it's an array of height values
		mapWidth = json.getInt("width");
		mapDepth = json.getInt("depth");

		mapHeights = new float[array.length()];
		for (int i = 0; i < mapHeights.length; i++) {
			mapHeights[i] = array.getFloat(i);
		}

	}

	/**
	 * Load heightmap heights from an image
	 * @param json
	 * @param array
	 */
	private void loadHeightsFromImage(String filename) throws IOException {
		BufferedImage mapImage = ImageIO.read(new File(filename));

		mapWidth = mapImage.getWidth();
		mapDepth = mapImage.getHeight();

		mapHeights = new float[mapWidth * mapDepth];
		int n = 0;

		for (int x = 0; x < mapWidth; x++) {
			for (int z = 0; z < mapDepth; z++) {
				int rgb = mapImage.getRGB(x, z);
				int r = (rgb & 0xff0000) >> 16;
				int g = (rgb & 0xff00) >> 8;
				int b = rgb & 0xff;

				// scale to [0...1]
				mapHeights[n++] = (r + g + b) / 255f / 3;
			}
		}
	}

	/**
	 * Load water parameters.
	 * @param json
	 */
	private void loadWater(JSONObject json) {
		JSONObject water = json.getJSONObject("water");		
		if (water != null) {
			waterHeight = water.getFloat("height");			
		}
		else {
			waterHeight = 0;
		}
	}
	
	/**
	 * Load plane parameters
	 * @param json
	 */
	private void loadPlane(JSONObject json) {
		JSONObject plane = json.getJSONObject("plane");		
		getVector3(plane, "position", planePosition);
		planeHeading = (float) Math.toRadians(plane.getFloat("heading"));
	}

	/**
	 * Load lighthouse parameters
	 * @param json
	 */
	private void loadLighthouse(JSONObject json) {
		JSONObject lighthouse = json.getJSONObject("lighthouse");		
		getVector2(lighthouse, "position", lighthousePosition);
		getVector3(lighthouse, "intensity", lighthouseIntensity);
		getVector3(lighthouse, "ambient", lighthouseAmbient);
	}

	/**
	 * Load sun parameters
	 * @param json
	 */
	private void loadSun(JSONObject json) {
		JSONObject sun = json.getJSONObject("sun");		
		getVector3(sun, "direction", sunDirection);
		getVector3(sun, "intensity", sunIntensity);
		getVector3(sun, "ambient", sunAmbient);		
	}

	private static void getVector2(JSONObject json, String name, Vector2f dest) {
		JSONArray array = json.getJSONArray(name);
		dest.x = array.getFloat(0);
		dest.y = array.getFloat(1);
	}

	private static void getVector3(JSONObject json, String name, Vector3f dest) {
		JSONArray array = json.getJSONArray(name);
		dest.x = array.getFloat(0);
		dest.y = array.getFloat(1);
		dest.z = array.getFloat(2);		
	}

}
