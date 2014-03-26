package yadc;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector2f;

import yadc.Map.IntPair;
public class Hero {
	public long uniqueId;
	public int currentHp,maxHp;
	private float hpBarYOffset=-10;
	private float hpBarLength=80;
	private float hpBarHeight=8;
	public Vector2f poz;
	public float width,height;
	private float walkSpeed;
	private Vector2f moveDirection=new Vector2f(0,0);
	private Map map;
	
	public Hero(){
		uniqueId=System.nanoTime();
		currentHp=100;
		maxHp=100;
		poz=new Vector2f(0, 0);
		width=32;
		height=32;
		walkSpeed=0.2f;
	}
	public void draw(){
		glPushMatrix();
			glTranslatef(poz.x, poz.y, 0);
			glBegin(GL_QUADS);
			glColor3f(0.5f, 0.5f, 1.0f);
			glVertex2f(-width/2,-height/2);
			glVertex2f(width/2, -height/2);
			glVertex2f(width/2,height/2);
			glVertex2f(-width/2, height/2);
			glColor3f(1.0f,0.0f,0.0f);
			glVertex2f(-hpBarLength/2, -height/2+hpBarYOffset+hpBarHeight/2);
			glVertex2f(+hpBarLength/2, -height/2+hpBarYOffset+hpBarHeight/2);
			glVertex2f(+hpBarLength/2, -height/2+hpBarYOffset-hpBarHeight/2);
			glVertex2f(-hpBarLength/2, -height/2+hpBarYOffset-hpBarHeight/2);
			glEnd();
		glPopMatrix();
	}
	public void update(float deltaTime){
		if(moveDirection.length()>0){
			moveDirection.normalise();
			moveDirection.scale(walkSpeed*deltaTime);
			poz.x+=moveDirection.x;
			collisionCheck(deltaTime);
			if(colBL||colBR||colTL||colTR){
				poz.x-=moveDirection.x;
			}
			poz.y+=moveDirection.y;
			collisionCheck(deltaTime);
			if(colBL||colBR||colTL||colTR){
				poz.y-=moveDirection.y;
			}
			//Vector2f.add(poz, moveDirection, poz);
			//pozX+=moveDirection.x*deltaTime;
			//pozY+=moveDirection.y*deltaTime;
			//checkCollision();
		}
		//collisionCheck(deltaTime);
		moveDirection.set(0, 0);
	}
	public void moveUp(){
		moveDirection.y+=1;
	}
	public void moveDown(){
		moveDirection.y-=1;
	}
	public void moveLeft(){
		moveDirection.x-=1;
	}
	public void moveRight(){
		moveDirection.x+=1;
	}
	public void teleportTo(Vector2f newPoz){
		poz.set(newPoz);
	}
	public void teleportTo(float x,float y){
		poz.x=x;
		poz.y=y;
	}
	public void teleportBy(float x,float y){
		poz.x+=x;
		poz.y+=y;
	}
	public void teleportBy(Vector2f by){
		Vector2f.add(poz, by, poz);
	}
	public void setMap(Map map){
		this.map=map;
	}
	private IntPair oldCell=new IntPair(-1,-1);
	private IntPair currentCell=new IntPair();
	private IntPair dummyCell=new IntPair();
	private boolean colTL,colTR,colBL,colBR;
	private int colCount;
	private void collisionCheck(float delta){
		map.getCellInsideCoords(
				poz.x,
				poz.y, 
				currentCell);
		colCount=0;
		//coltul stanga sus al eroului
		map.getCellInsideCoords(
				poz.x-width/2,
				poz.y+height/2,
				dummyCell);
		if((colTL=map.isWall(dummyCell)))colCount++;
		//coltul dreapta sus al eroului
		map.getCellInsideCoords(
				poz.x+width/2,
				poz.y+height/2,
				dummyCell);
		if((colTR=map.isWall(dummyCell)))colCount++;
		//coltul stanga jos al eroului
		map.getCellInsideCoords(
				poz.x-width/2,
				poz.y-height/2,
				dummyCell);
		if((colBL=map.isWall(dummyCell)))colCount++;
		//coltul dreapta jos al eroului
		map.getCellInsideCoords(
				poz.x+width/2,
				poz.y-height/2,
				dummyCell);
		if((colBR=map.isWall(dummyCell)))colCount++;
		/*
		if(colCount==4){
			System.out.println("Hero should be dead");
			teleportBy(-moveDirection.x, -moveDirection.y);
		}else if(colCount==3){
			if(!colBR){
				teleportTo(
						currentCell.x*map.cellSize+width/2+1,
						(currentCell.y+1)*map.cellSize-height/2-1);
			}else if(!colBL){
				teleportTo(
						(currentCell.x+1)*map.cellSize-width/2-1,
						(currentCell.y+1)*map.cellSize-height/2-1);
			}else if(!colTL){
				teleportTo(
						(currentCell.x+1)*map.cellSize-width/2+1,
						currentCell.y*map.cellSize+height/2+1);
			}else if(!colTR){
				teleportTo(
						currentCell.x*map.cellSize+width/2-1,
						currentCell.y*map.cellSize+height/2+1);
			}
		}else if(colCount==2){
			if(colTL&&colTR){
				teleportTo(poz.x, (currentCell.y+1)*map.cellSize-height/2);
			}else if(colBL&&colBR){
				teleportTo(poz.x, currentCell.y*map.cellSize+height/2);
			}else if(colTL&&colBL){
				teleportTo(currentCell.x*map.cellSize+width/2, poz.y);
			}else if(colTR&&colBR){
				teleportTo((currentCell.x+1)*map.cellSize-width/2, poz.y);
			}
		}else if(colCount==1){
			//if(colTL)
		}
		*/
	}
}
