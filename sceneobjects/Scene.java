https://tutorcs.com
WeChat: cstutorcs
QQ: 749389476
Email: tutorcs@163.com
https://tutorcs.com
WeChat: cstutorcs
QQ: 749389476
Email: tutorcs@163.com
package comp3170.ass3.sceneobjects;

import comp3170.SceneObject;
import comp3170.ass3.SceneData;

/**
 * This class is the root of the scene graph and creates the scene based
 * on the SceneData file provided.
 * 
 * It is a simple implementation of the Singleton pattern. A single instance of
 * the scene is created and stored in the static field theScene. This can be accessed
 * from other classes as Scene.theScene.
 * 
 * E.g. 
 * SceneData data = Scene.theScene.getData();
 * 
 * @author malcolmryan
 *
 */

public class Scene extends SceneObject {

	public static Scene theScene;
	private SceneData data;	
	private Lighthouse lighthouse;
	
	public Scene(SceneData data) {
		if (theScene != null) {
			throw new RuntimeException("Scene singleton is not unique.");
		}
		theScene = this;
		this.data = data;

		// Create the scene objects
		
		lighthouse = new Lighthouse();
		lighthouse.setParent(this);
		
		// Scale to NDC coordinates
		// FIXME: Don't do this in your final submission! Use a projection matrix instead.
		// You will lose correctness marks if you leave this here. 
		lighthouse.getMatrix().scale(1f/40);
	}	

	public SceneData getData() {
		return data;
	}
}
