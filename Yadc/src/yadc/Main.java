package yadc;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import yadc.Map.IntPair;
import static org.lwjgl.opengl.GL11.*;

public class Main {
	float x=400,y=300;
	float rotation=0;
	long lastFrame;
	int fps;
	long lastFPS;
	private Hero mainHero;
	private Map mainMap;
	public void start(){
		mainMap=new Map();
		mainHero=new Hero();
		mainHero.setMap(mainMap);
		mainHero.teleportTo(mainMap.startPoz);
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
		/*rotation+=0.15f*delta;
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))x-=0.35f*delta;
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))x+=0.35f*delta;
		if(Keyboard.isKeyDown(Keyboard.KEY_UP))y+=0.35f*delta;
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN))y-=0.35f*delta;
		
		if(x<0)x=0;
		if(x>800)x=800;
		if(y<0)y=0;
		if(y>600)y=600;
		*/
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))mainHero.moveLeft();
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))mainHero.moveRight();
		if(Keyboard.isKeyDown(Keyboard.KEY_UP))mainHero.moveUp();
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN))mainHero.moveDown();
		mainHero.update(delta);
		//collisionCheck();
		updateFPS();
	}
	/*
	private IntPair oldCell=new IntPair(-1,-1);
	private IntPair currentCell=new IntPair();
	private IntPair dummyCell=new IntPair();
	private void collisionCheck(){
		mainMap.getCellInsideCoords(
				mainHero.poz.x,
				mainHero.poz.y, 
				currentCell);
		//coltul stanga sus al eroului
		mainMap.getCellInsideCoords(
				mainHero.poz.x-mainHero.width/2,
				mainHero.poz.y+mainHero.height/2,
				dummyCell);
		if(mainMap.isWall(dummyCell)){
			mainHero.teleportBy(1,-1);
		}
		//coltul dreapta sus al eroului
		mainMap.getCellInsideCoords(
				mainHero.poz.x+mainHero.width/2,
				mainHero.poz.y+mainHero.height/2,
				dummyCell);
		if(mainMap.isWall(dummyCell)){
			mainHero.teleportBy(-1, -1);
		}
		//coltul stanga jos al eroului
		mainMap.getCellInsideCoords(
				mainHero.poz.x-mainHero.width/2,
				mainHero.poz.y-mainHero.height/2,
				dummyCell);
		if(mainMap.isWall(dummyCell)){
			mainHero.teleportBy(1,1);
		}
		//coltul dreapta jos al eroului
		mainMap.getCellInsideCoords(
				mainHero.poz.x+mainHero.width/2,
				mainHero.poz.y-mainHero.height/2,
				dummyCell);
		if(mainMap.isWall(dummyCell)){
			mainHero.teleportBy(-1, 1);
		}
		/*
		if(currentCell.x!=oldCell.x||currentCell.y!=oldCell.y){
			System.out.printf("Current cell [%d][%d] = %d\n",
					currentCell.y,currentCell.x,
					mainMap.mapMatrix[currentCell.y][currentCell.x]);
		}
		oldCell.x=currentCell.x;
		oldCell.y=currentCell.y;
		return;
	}*/
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
		
		mainMap.draw();
		mainHero.draw();
		/*glColor3f(0.5f, 0.5f, 1.0f);
		
		glPushMatrix();
			glTranslatef(x, y, 0);
			glRotatef(rotation, 0, 0, 1);
			//glTranslatef(-x, -y, 0);
			glBegin(GL_QUADS);
			glVertex2f(-50,-50);
			glVertex2f(+50,-50);
			glVertex2f(+50,+50);
			glVertex2f(-50,+50);
			glEnd();
		glPopMatrix();*/
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
