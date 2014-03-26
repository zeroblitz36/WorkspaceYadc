package yadc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.lwjgl.util.vector.Vector2f;

import static org.lwjgl.opengl.GL11.*;

public class Map {
	public int mapMatrix[][];
	public float cellSize=64;
	public int width,height;
	//public int startPozX,startPozY;
	public Vector2f startPoz=new Vector2f();
	/*
	0 zona nedefinita : negru
	1 perete : gri
	2 spatiu gol : verde
	3 startul : albastru
	4 finish : alb
	 */
	//enum CellTypes{UNDEFINED,WALL,EMPTY,START,FINISH};
	public Map(){
		try {
			Scanner s=new Scanner(new File("mapMatrix.txt"));
			height=s.nextInt();
			width=s.nextInt();
			mapMatrix=new int[height][width];
			for(int i=0;i<height;i++)
				for(int j=0;j<width;j++){
					mapMatrix[height-i-1][j]=s.nextInt();
					if(mapMatrix[height-i-1][j]==3){
						//startPozY=height-i-1;
						//startPozX=j;
						startPoz.set(j+0.5f, height-i-1+0.5f);
						startPoz.scale(cellSize);
					}
				}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	public void getCellInsideCoords(float x,float y,IntPair ip){
		ip.x=(int) Math.floor((x/(width*cellSize))*width);
		ip.y=(int) Math.floor((y/(height*cellSize))*height);
	}
	public boolean isWall(int x,int y){
		return mapMatrix[y][x]==1;
	}
	public boolean isWall(IntPair ip){
		return mapMatrix[ip.y][ip.x]==1;
	}
	public void draw(){
		glBegin(GL_QUADS);
		for(int i=0;i<height;i++)
			for(int j=0;j<width;j++){
				switch (mapMatrix[i][j]) {
				case 0:glColor3f(0.0f, 0.0f, 0.0f);break;
				case 1:glColor3f(0.5f, 0.5f, 0.5f);break;
				case 2:glColor3f(0.0f, 1.0f, 0.0f);break;
				case 3:glColor3f(0.0f, 0.0f, 1.0f);break;
				case 4:glColor3f(1.0f, 1.0f, 1.0f);break;
				default:glColor3f(1.0f, 0.0f, 1.0f);break;
				}
				glVertex2f(j*cellSize,i*cellSize);
				glVertex2f(j*cellSize,(i+1)*cellSize);
				glVertex2f((j+1)*cellSize,(i+1)*cellSize);
				glVertex2f((j+1)*cellSize,i*cellSize);
			}
		glEnd();
	}
	public static class IntPair{
		public int x=0,y=0;
		public IntPair(){}
		public IntPair(int x,int y){
			this.x=x;
			this.y=y;
		}
	}
}
