import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class MyPanel extends JPanel implements ActionListener {

Timer timer;
int[][] boardStatus;
int[][] bufferedBoard;
int width;
int height;
int gridSize;
int updateDelay;
int activeType;
int rotationPress;
boolean performedRot;
Coord rotCoord;
ArrayList<Coord> activePieces;
Color[][] tetrColor;
 
  MyPanel(){

    defineVariables();
    this.setPreferredSize(new Dimension(width,height));
    timer = new Timer(5, this);
    timer.start();
    //Test spawn;
    generatePeice(1);
   
  }
  public void defineVariables() {
    width = 800;
    height = 900;
    updateDelay = 25;
    boardStatus = new int[10][25];
    bufferedBoard = new int[10][25];
    gridSize = (height - 100) / 20;
    activePieces = new ArrayList<Coord>();
    //Test States (remember 6 is when things are on the board)
    boardStatus[2][6] = 1;
    boardStatus[2][7] = 1;
    boardStatus[9][19] = 2;
    //Colors
    tetrColor = new Color[5][2];
    //Reds
    tetrColor[1][0] = new Color(255,0,0);
    tetrColor[1][1] = new Color(100,0,0);
    //Blues
    tetrColor[2][0] = new Color(0,0,255);
    tetrColor[2][1] = new Color(0,0,100);
    //Purple
    tetrColor[3][0] = new Color(255,0,255);
    tetrColor[3][1] = new Color(100,0,100);
  }
 
  public void paint(Graphics g) {
    
    Graphics2D g2D = (Graphics2D) g;
    g2D.setStroke(new BasicStroke(3));
    g2D.setPaint(Color.black);
    g2D.fillRect(0, 0, width, height);
    g2D.setPaint(Color.gray);
    g2D.fillRect(0, 0, width, 100);
    g2D.fillRect(0, 0, 200, height);
    g2D.fillRect(width - 200, 0, 200, height);
    for (int x = 5; x < 25; x++) {
      for (int i = 0; i < 10; i++) {
        if (boardStatus[i][x] == 0) {
          g2D.setPaint(Color.white);
          g2D.drawRect(200 + (gridSize * i), 100 + ((x - 5) * gridSize), gridSize, gridSize);
        } else {
          g2D.setPaint(tetrColor[boardStatus[i][x]][0]);
          g2D.fillRect(200 + (gridSize * i), 100 + ((x - 5) * gridSize), gridSize, gridSize);
          g2D.setPaint(tetrColor[boardStatus[i][x]][1]);
          g2D.drawRect(200 + (gridSize * i), 100 + ((x - 5) * gridSize), gridSize, gridSize);
        }
      }
    }
  }
  //NEW FRAME
  /////
  ////
  /////
  ////
  ///
  ///
  @Override
	public void actionPerformed(ActionEvent e) {
    //Key presses
    if (rotationPress != 0 && !performedRot) {
      performedRot = true;
      updateBuffer();
      rotatePiece();//temporarily uni-directional
      copyBufferToBoard();
      updateDelay += 5;
    }
    //Updates
    if (updateDelay <= 0) {
      updateBuffer();
      descend();
      copyBufferToBoard();
      updateDelay = 25;
    } else {
      updateDelay--;
    }
    repaint();
  }

  public void copyBufferToBoard() {
    for (int x = 0; x < 10; ++x) {
      for (int y = 0; y < 25; ++y) {
        boardStatus[x][y] = bufferedBoard[x][y];
      }
    }
  }
  public void updateBuffer() {
    for (int x = 0; x < 10; ++x) {
      for (int y = 0; y < 25; ++y) {
        bufferedBoard[x][y] = boardStatus[x][y];
      }
    }
    for (int i = 0; i < activePieces.size(); i++) {
      bufferedBoard[activePieces.get(i).x][activePieces.get(i).y] = 0; 
    }
  }

  public void descend() {
    boolean descended = descendActive();
    for (int i = 0; i < activePieces.size(); i++) {
      bufferedBoard[activePieces.get(i).x][activePieces.get(i).y] = activePieces.get(i).color; 
    }
    if (!descended) {
      activePieces.clear();
      generatePeice(1);
    }
  }

  public void reset() {
    //Resets the game
    timer.stop();
    timer = new Timer(5, this);
    timer.start();
    defineVariables();
  }
  public void generatePeice(int type) {
    switch(type) {
      case 1:
        //T-Block
        rotCoord = new Coord(5, 4);
        activePieces.add(new Coord(4,5,3));
        activePieces.add(new Coord(5,5,3));
        activePieces.add(new Coord(6,5,3));
        activePieces.add(new Coord(5,4,3));
        break;
    }
  }
  public boolean descendActive() {
    boolean success = true;
    int tempX;
    int tempY;
    int tempColor;
    //Checking if we can update
    for (int i = 0; i < activePieces.size(); i++) {
      if (tileOccupied(activePieces.get(i).x, activePieces.get(i).y + 1)) {
        success = false;
        //System.out.println("Couldn't descend");
      }
    }
    if (success) {
      //updating
      for (int i = 0; i < activePieces.size(); i++) {
        tempX = activePieces.get(i).x;
        tempY = activePieces.get(i).y;
        tempColor = activePieces.get(i).color;
        activePieces.set(i, new Coord(tempX, tempY + 1, tempColor));
      }
      rotCoord.changeY(1);
    }
    return success;
  }
  public boolean tileOccupied(int inX, int inY) {
    //System.out.println("Checked occupation | " + inX + ", " + inY);
    //THIS CHECKS THE BUFFER
    if (inY < 25 && inX >= 0 && inX < 10) {
      if (bufferedBoard[inX][inY] != 0) {
        //System.out.println("It was occupied");
        return true;
      } else {
        //System.out.println("It was open");
        return false;
      }

    } else {
      //System.out.println("It was out of bounds");
      return true;
    }
  }
  public void rotatePiece() {
    if (rotatationCheck()) {
      for (int i = 0; i < activePieces.size(); ++i) {
        //Getting the coordinate relative to it's rotation point
        Coord tempRelative = new Coord(activePieces.get(i).x - rotCoord.x, activePieces.get(i).y - rotCoord.y);
        //Rotating those rotation points (Reflecting around origin, is that rotation?), and then adding back to the original coord
        tempRelative = new Coord(tempRelative.y + rotCoord.x, tempRelative.x + rotCoord.y);
        System.out.println("Orignal: " + activePieces.get(i).toString() + " final: " + tempRelative.toString());
        activePieces.set(i, new Coord(tempRelative.x, tempRelative.y, activePieces.get(i).color));
        bufferedBoard[activePieces.get(i).x][activePieces.get(i).y] = activePieces.get(i).color; 
      }
    }
  }
  public boolean rotatationCheck() {
    boolean safeToRotate = true;
    //System.out.println("rot point = " + rotCoord.toString());
    for (int i = 0; i < activePieces.size(); ++i) {
      //Getting the coordinate relative to it's rotation point
      Coord tempRelative = new Coord(activePieces.get(i).x - rotCoord.x, activePieces.get(i).y - rotCoord.y);
      //System.out.println("Orignal: " + activePeices.get(i).toString() + " relative:" + tempRelative.toString());
      //Rotating those rotation points (Reflecting around origin, is that rotation?)
      tempRelative = new Coord(tempRelative.y, tempRelative.x);
      //Turning the relative value into a true coordinate value
      tempRelative = new Coord(tempRelative.x + rotCoord.x, tempRelative.y + rotCoord.y);
      //Now we check if that space is open. (finally)
      //System.out.println("Orignal: " + activePeices.get(i).toString() + " final: " + tempRelative.toString());
      if (tileOccupied(tempRelative.x, tempRelative.y)) {
        safeToRotate = false;
        System.out.println("couldn't rotate " + tempRelative.toString());
      }
    }
    return safeToRotate;
  }
  public ArrayList<Coord> simulateRot(int direction) {
    ArrayList<Coord> temp  = new ArrayList<Coord>();
    for (int i = 0; i < activePieces.size(); ++i) {
      temp.add(new Coord(activePieces.get(i).x,activePieces.get(i).y,activePieces.get(i).color));
    }
    return temp;
  }
}