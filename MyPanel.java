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
int shiftPress;
int shiftCoolDown;
int currentRotation;
boolean performedRot;
Coord rotCoord;
ArrayList<Coord> activePieces;
Color[][] tetrColor;
boolean fastDrop;
 
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
    shiftPress = 0;
    shiftCoolDown = 0;
    fastDrop = false;
    //Test States (remember 6 is when things are on the board)
    //boardStatus[2][6] = 1;
    //boardStatus[2][7] = 1;
    //boardStatus[9][19] = 2;
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
    //shifting
    if (shiftPress != 0) {
      if (shiftCoolDown <= 0) {
        updateDelay += 5;
        shiftActive(shiftPress);
        shiftCoolDown = 5;
        //shift
      } else {
        shiftCoolDown--;
      }
    }
    //rotation
    if (rotationPress != 0 && !performedRot) {
      updateDelay += 5;
      performedRot = true;
      //System.out.println("Rotation triggered");
      rotatePiece(currentRotation);
      currentRotation += rotationPress;
      if (currentRotation > 3) {
        currentRotation = 0;
      } else if (currentRotation < 0) {
        currentRotation = 3;
      }
      //System.out.println("Current rotation: " + currentRotation);
      rotatePiece(currentRotation);//temporarily uni-directional
    }
    //Updates
    if (updateDelay <= 0) {
      descend();
      updateDelay = 25;
    } else {
      updateDelay--;
      if (fastDrop) {
        updateDelay -= 3;
      }
    }
  }

  public void copyBufferToBoard() {
    for (int x = 0; x < 10; ++x) {
      for (int y = 0; y < 25; ++y) {
        boardStatus[x][y] = bufferedBoard[x][y];
      }
    }
    repaint();
  }
  public void copyActiveToBuffer() {
    for (Coord temp : activePieces) {
      bufferedBoard[temp.x][temp.y] = temp.color;
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
    updateBuffer();
    boolean descended = descendActive();
    for (int i = 0; i < activePieces.size(); i++) {
      bufferedBoard[activePieces.get(i).x][activePieces.get(i).y] = activePieces.get(i).color; 
    }
    if (!descended) {
      activePieces.clear();
      generatePeice(1);
    }
    copyActiveToBuffer();
    copyBufferToBoard();
  }

  public void reset() {
    //Resets the game
    timer.stop();
    timer = new Timer(5, this);
    timer.start();
    defineVariables();
  }
  public void generatePeice(int type) {
    currentRotation = 0;
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
        //System.out.println(inX + " " + inY + " was occupied with " + bufferedBoard[inX][inY]);
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
  public void rotatePiece(int direction) {
    //System.out.println("Rotation called");
    updateBuffer();
    if (rotatationCheck(direction)) {
      //System.out.println("Rotation passed");
      ArrayList<Coord> temp  = simulateRot(direction);
      activePieces.clear();
      for (Coord tempCoord : temp) {
        activePieces.add(tempCoord);
      }
    }
    copyActiveToBuffer();
    copyBufferToBoard();
  }
  public boolean rotatationCheck(int direction) {
    boolean safeToRotate = true;
    ArrayList<Coord> temp  = simulateRot(direction);
    for (Coord tempCoord : temp) {
      if (tileOccupied(tempCoord.x, tempCoord.y)) {
        safeToRotate = false;
        //System.out.println("Rotation failed at " + tempCoord.toString());
        break;
      }
    }
    return safeToRotate;
  }
  public ArrayList<Coord> simulateRot(int direction) {
    ArrayList<Coord> temp  = new ArrayList<Coord>();
    //Clone it over as relative
    for (int i = 0; i < activePieces.size(); ++i) {
      temp.add(new Coord(activePieces.get(i).x - rotCoord.x, activePieces.get(i).y - rotCoord.y, activePieces.get(i).color));
      int tempX = temp.get(i).x;
      int tempY = temp.get(i).y;
      //Perform the rotations
      switch (direction) {
        case 3://Right
          temp.get(i).x = tempY;
          temp.get(i).y = tempX;
          break;
        case 1://Left
          temp.get(i).x = -1 * tempY;
          temp.get(i).y = -1 * tempX;
          break;
        case 2:
          temp.get(i).y = -1 * tempY;
          break;
        case 0://Down (don't bother for rn)
          break;
      }
      //Revert to orignal version
      temp.get(i).x += rotCoord.x;
      temp.get(i).y += rotCoord.y;
    }
    return temp;
  }
  public void shiftActive(int direction) {
    updateBuffer();
    boolean clear = true;
    for (Coord temp : activePieces) {
      if (tileOccupied(temp.x + direction, temp.y)) {
        clear = false;
        break;
      }
    }
    if (clear) {
      rotCoord.x += direction;
      for (int i = 0; i < activePieces.size(); i++) {
        activePieces.get(i).x = activePieces.get(i).x + direction;
      }
    }
    copyActiveToBuffer();
    copyBufferToBoard();
  }
  public void checkClears() {
    
  }
}