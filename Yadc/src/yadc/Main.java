package yadc;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

public class Main {
	float x=400,y=300;
	float rotation=0;
	long lastFrame;
	int fps;
	long lastFPS;
	public void start(){
		try{
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.create();
		}catch(LWJGLException e){
			e.printStackTrace();
			System.exit(0);
		}
		
		initGL();
		getDelta();
		lastFPS=getTime();
		
		while(!Display.isCloseRequested()){
			int delta=getDelta();
			update(delta);
			renderGL();
			Display.update();
			Display.sync(60);
		}
	}
	public void update(int delta){
		rotation+=0.15f*delta;
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))x-=0.35f*delta;
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))x+=0.35f*delta;
		if(Keyboard.isKeyDown(Keyboard.KEY_UP))y-=0.35f*delta;
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN))y+=0.35f*delta;
		
		if(x<0)x=0;
		if(x>800)x=800;
		if(y<0)y=0;
		if(y>600)y=600;
		
		updateFPS();
	}
	public void updateFPS(){
		if(getTime()-lastFPS>1000){
			Display.setTitle("FPS: "+fps);
			fps=0;
			lastFPS+=1000;
		}
		fps++;
	}
	public void renderGL(){
		glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
		glColor3f(0.5f, 0.5f, 1.0f);
		
		glPushMatrix();
			glTranslatef(x, y, 0);
			glRotatef(rotation, 0, 0, 1);
			glTranslatef(-x, -y, 0);
			glBegin(GL_QUADS);
	}
	public void initGL(){
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 800, 0, 600, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}
	public int getDelta(){
		long time=getTime();
		int delta=(int)(time-lastFrame);
		lastFrame=time;
		return delta;
	}
	public long getTime(){
		return (Sys.getTime()*1000)/Sys.getTimerResolution();
	}
	public static void main(String[] args) {
		Main main=new Main();
		main.start();
	}

}
