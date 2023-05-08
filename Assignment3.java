https://tutorcs.com
WeChat: cstutorcs
QQ: 749389476
Email: tutorcs@163.com
https://tutorcs.com
WeChat: cstutorcs
QQ: 749389476
Email: tutorcs@163.com

package comp3170.ass3;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import java.io.File;

import comp3170.IWindowListener;
import comp3170.OpenGLException;
import comp3170.Window;
import comp3170.ass3.sceneobjects.Scene;


public class Assignment3 implements IWindowListener {
	
	private int screenWidth = 800;
	private int screenHeight = 800;
	
	private Scene scene;
	private Window window;
	private File sceneFile;
	
	public Assignment3(File jsonFile) throws OpenGLException {
		sceneFile = jsonFile;
		window = new Window(jsonFile.getName(), screenWidth, screenHeight, this);
		window.run();
	}

	@Override
	public void init() {
		// Load the scene data from the JSON scene file and create the scene.
		SceneData data = new SceneData(sceneFile);
		scene = new Scene(data);
	}

	@Override
	public void draw() {
		// Clear the colour buffer 
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT);		

		// Draw the scene
		scene.draw();		
	}

	@Override
	public void resize(int width, int height) {
		// TODO
	}

	@Override
	public void close() {
		
	}

	public static void main(String[] args) throws OpenGLException {
		// The filename for the scene file should be specified in the command line arguments
		// Do not change this code, it will be used for marking
		
		if (args.length < 1) {
			throw new IllegalArgumentException("No scene file provided.");
		}
		new Assignment3(new File(args[0]));
	}


}
