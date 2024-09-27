import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class MyPanel extends JPanel implements ActionListener {

Timer timer;
int[][] boardStatus;
int[][] bufferedBoard;
int[] pieces; //0 = next, 1 = active, 2 = held.
int[][][][] rotationVariants;
int width;
int height;
int gridSize;
int updateDelay;
int activeType;
int rotationPress;
int shiftPress;
int shiftCoolDown;
int currentRotation;
int score;
boolean performedRot;
Coord rotCoord;
ArrayList<Coord> activePieces;
Color[][] tetrColor;
boolean fastDrop;
String scoreDisplay;
 
  MyPanel(){

    defineVariables();
    this.setPreferredSize(new Dimension(width,height));
    timer = new Timer(5, this);
    timer.start();
    //Test spawn;
    generatePeice(pieces[0]);
   
  }
  public void defineVariables() {
    width = 800;
    height = 900;
    updateDelay = 25;
    boardStatus = new int[10][25];
    bufferedBoard = new int[10][25];
    gridSize = (height - 100) / 20;
    activePieces = new ArrayList<Coord>();
    pieces = new int[3];
    pieces[0] = randPiece();
    pieces[1] = randPiece();
    shiftPress = 0;
    shiftCoolDown = 0;
    score = 0;
    scoreDisplay = "000000000";
    fastDrop = false;
    //Test States (remember 6 is when things are on the board)
    //boardStatus[2][6] = 1;
    //boardStatus[2][7] = 1;
    //boardStatus[9][19] = 2;
    //Colors
    tetrColor = new Color[8][2];
    //Reds
    tetrColor[1][0] = new Color(255,0,0);
    tetrColor[1][1] = new Color(100,0,0);
    //Blues
    tetrColor[2][0] = new Color(0,0,255);
    tetrColor[2][1] = new Color(0,0,100);
    //Purple
    tetrColor[3][0] = new Color(255,0,255);
    tetrColor[3][1] = new Color(100,0,100);
    //Orange
    tetrColor[4][0] = new Color(255,128,0);
    tetrColor[4][1] = new Color(153,65,0);
    //yellow
    tetrColor[5][0] = new Color(255,255,0);
    tetrColor[5][1] = new Color(100,100,0);
    //Green
    tetrColor[6][0] = new Color(0,255,0);
    tetrColor[6][1] = new Color(0,100,0);
    //Teal
    tetrColor[7][0] = new Color(0,255,255);
    tetrColor[7][1] = new Color(0,100,100);
    //Rotation variants (ugh)
    rotationVariants = new int[8][4][5][5];//[Piece][Rotation][X][Y]. 2 would be centered on coordinates
    //Piece 3 - T block
      //Default rotation
      rotationVariants[3][0][2][2] = 3;//Center
      rotationVariants[3][0][2][1] = 3;
      rotationVariants[3][0][1][2] = 3;
      rotationVariants[3][0][3][2] = 3;
      //Rotated right
      rotationVariants[3][1][2][2] = 3;//Center
      rotationVariants[3][1][3][2] = 3;
      rotationVariants[3][1][2][1] = 3;
      rotationVariants[3][1][2][3] = 3;
      //Rotated left
      rotationVariants[3][3][2][2] = 3;//Center
      rotationVariants[3][3][1][2] = 3;
      rotationVariants[3][3][2][1] = 3;
      rotationVariants[3][3][2][3] = 3;
      //Flipped
      rotationVariants[3][2][2][2] = 3;//Center
      rotationVariants[3][2][2][3] = 3;
      rotationVariants[3][2][1][2] = 3;
      rotationVariants[3][2][3][2] = 3;
    //Piece 1 - Red Lightning
      //Default rotation
      rotationVariants[1][1][2][2] = 1;
      rotationVariants[1][1][2][3] = 1;
      rotationVariants[1][1][3][2] = 1;
      rotationVariants[1][1][3][1] = 1;
      //Rotated right
      rotationVariants[1][2][2][2] = 1;
      rotationVariants[1][2][3][2] = 1;
      rotationVariants[1][2][1][1] = 1;
      rotationVariants[1][2][2][1] = 1;
      //Vertical flip
      rotationVariants[1][3][2][2] = 1;
      rotationVariants[1][3][2][1] = 1;
      rotationVariants[1][3][1][2] = 1;
      rotationVariants[1][3][1][3] = 1;
      //Rotated Left
      rotationVariants[1][0][2][2] = 1;
      rotationVariants[1][0][1][2] = 1;
      rotationVariants[1][0][2][3] = 1;
      rotationVariants[1][0][3][3] = 1;
    //Piece 6 - green Lightning
      //Default rotation
      rotationVariants[6][0][2][2] = 6;
      rotationVariants[6][0][2][1] = 6;
      rotationVariants[6][0][3][1] = 6;
      rotationVariants[6][0][1][2] = 6;
      //Rotated right
      rotationVariants[6][1][1][1] = 6;
      rotationVariants[6][1][1][2] = 6;
      rotationVariants[6][1][2][2] = 6;
      rotationVariants[6][1][2][3] = 6;
      //Vertical flip
      rotationVariants[6][2][2][2] = 6;
      rotationVariants[6][2][3][2] = 6;
      rotationVariants[6][2][1][3] = 6;
      rotationVariants[6][2][2][3] = 6;
      //Rotated Left
      rotationVariants[6][3][2][2] = 6;
      rotationVariants[6][3][1][1] = 6;
      rotationVariants[6][3][1][2] = 6;
      rotationVariants[6][3][2][3] = 6;
    //Piece 2 - Blue L
      //Default rotation
      rotationVariants[2][0][2][2] = 2;//Center
      rotationVariants[2][0][1][2] = 2;
      rotationVariants[2][0][3][2] = 2;
      rotationVariants[2][0][1][1] = 2;
      //Rotated Right
      rotationVariants[2][1][2][2] = 2;//Center
      rotationVariants[2][1][2][1] = 2;
      rotationVariants[2][1][2][3] = 2;
      rotationVariants[2][1][1][3] = 2;
      //Vertical Flip
      rotationVariants[2][2][2][2] = 2;//Center
      rotationVariants[2][2][1][2] = 2;
      rotationVariants[2][2][3][2] = 2;
      rotationVariants[2][2][3][3] = 2;
      //Rotated Left
      rotationVariants[2][3][2][2] = 2;//Center
      rotationVariants[2][3][2][1] = 2;
      rotationVariants[2][3][2][3] = 2;
      rotationVariants[2][3][3][1] = 2;
    //Piece 4 - Orange L
      //Default rotation
      rotationVariants[4][0][2][2] = 4;//Center
      rotationVariants[4][0][1][2] = 4;
      rotationVariants[4][0][3][2] = 4;
      rotationVariants[4][0][1][1] = 4;
      //Rotated Right
      rotationVariants[4][1][2][2] = 4;//Center
      rotationVariants[4][1][2][1] = 4;
      rotationVariants[4][1][2][3] = 4;
      rotationVariants[4][1][1][1] = 4;
      //Vertical Flip
      rotationVariants[4][2][2][2] = 4;//Center
      rotationVariants[4][2][1][2] = 4;
      rotationVariants[4][2][3][2] = 4;
      rotationVariants[4][2][1][3] = 4;
      //Rotated Left
      rotationVariants[4][3][2][2] = 4;//Center
      rotationVariants[4][3][2][1] = 4;
      rotationVariants[4][3][2][3] = 4;
      rotationVariants[4][3][3][3] = 4;
    //Piece 7 - The line
      //Default rotation
      rotationVariants[7][0][2][0] = 7;
      rotationVariants[7][0][2][1] = 7;
      rotationVariants[7][0][2][2] = 7;
      rotationVariants[7][0][2][3] = 7;
      //Right rotation
      rotationVariants[7][1][0][1] = 7;
      rotationVariants[7][1][1][1] = 7;
      rotationVariants[7][1][2][1] = 7;
      rotationVariants[7][1][3][1] = 7;
      //180
      rotationVariants[7][2][1][0] = 7;
      rotationVariants[7][2][1][1] = 7;
      rotationVariants[7][2][1][2] = 7;
      rotationVariants[7][2][1][3] = 7;
      //Left rotation
      rotationVariants[7][3][0][2] = 7;
      rotationVariants[7][3][1][2] = 7;
      rotationVariants[7][3][2][2] = 7;
      rotationVariants[7][3][3][2] = 7;
    //Piece 5 - the square
      for (int i = 0; i < 4; ++i) {
        rotationVariants[5][i][1][1] = 5;
        rotationVariants[5][i][1][2] = 5;
        rotationVariants[5][i][2][1] = 5;
        rotationVariants[5][i][2][2] = 5;
      }
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
    //tiles
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
    //Score
    g2D.setColor(Color.black);
    g2D.fillRect(10,100,175,60);
    g2D.setColor(Color.white);
    g2D.setFont(new Font("Times New Roman", 1, 35));
    g2D.drawString(scoreDisplay, 16, 145);
    //Next Piece
    //Black out
    g2D.setColor(Color.black);
    g2D.fillRect(10,200,140,140);
    //Piece:
    for (int x = 0; x < 5; ++x) {
      for (int y = 0; y < 5; ++y) {
        if (rotationVariants[pieces[0]][0][x][y] != 0) {
          g2D.setPaint(tetrColor[rotationVariants[pieces[0]][0][x][y]][0]);
          g2D.fillRect(10 + (28 * x), 214 + (27 * y), 28, 28);
          g2D.setPaint(tetrColor[rotationVariants[pieces[0]][0][x][y]][1]);
          g2D.drawRect(10 + (28 * x), 214 + (27 * y), 28, 28);
        }
      }
    }
    g2D.setColor(Color.white);
    g2D.setFont(new Font("Times New Roman", 1, 25));
    g2D.drawString("NEXT:", 12, 225);
    //Hold
    //Black out
    g2D.setColor(Color.black);
    g2D.fillRect(width - 170,200,140,140);
    //Piece:
    for (int x = 0; x < 5; ++x) {
      for (int y = 0; y < 5; ++y) {
        if (rotationVariants[pieces[2]][0][x][y] != 0) {
          g2D.setPaint(tetrColor[rotationVariants[pieces[2]][0][x][y]][0]);
          g2D.fillRect((width - 170) + (28 * x), 214 + (27 * y), 28, 28);
          g2D.setPaint(tetrColor[rotationVariants[pieces[2]][0][x][y]][1]);
          g2D.drawRect((width - 170) + (28 * x), 214 + (27 * y), 28, 28);
        }
      }
    }
    g2D.setColor(Color.white);
    g2D.setFont(new Font("Times New Roman", 1, 25));
    g2D.drawString("HELD:", width - 168, 225);
  }
  //NEW FRAME
  ///////\\\\\\\
  //////||\\\\\\
  ///// -- \\\\\
  ////______\\\\
  //Triangle guy^
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
      currentRotation += rotationPress;
      if (currentRotation < 0) {
        currentRotation = 3;
      } else if (currentRotation > 3) {
        currentRotation = 0;
      }
      //System.out.println("Current rotation: " + currentRotation);
      rotatePiece(currentRotation);
      performedRot = true;
    }
    //Updates
    if (updateDelay <= 0) {
      descend();
      updateDelay = 20;
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
      checkClears();
      generatePeice(pieces[0]);
      pieces[1] = pieces[0];
      pieces[0] = randPiece();
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
        //Red Lightning
        rotCoord = new Coord(5, 5, 1);
        break;
      case 6:
        //Red Lightning
        rotCoord = new Coord(5, 5, 6);
        break;
      case 2:
        //Blue L
        rotCoord = new Coord(5, 4,2);
        break;
      case 4:
        //Orange L
        rotCoord = new Coord(5, 4,4);
        break;
      case 3:
        //T-Block
        rotCoord = new Coord(5, 5,3);
        break;
      case 5:
        //Square
        rotCoord = new Coord(5, 4,5);
        break;
      case 7:
        //Line
        rotCoord = new Coord(5, 4,7);
        break;
    }
    for (int x = 0; x < 5; ++x) {
      for (int y = 0; y < 5; ++y) {
        if (rotationVariants[rotCoord.color][0][x][y] != 0) {
          activePieces.add(new Coord(x + (rotCoord.x - 2), y + (rotCoord.y - 2), rotCoord.color));
        }
      }
    }
  }
  public int randPiece() {
    return (int)(Math.random() * 7) + 1;
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
    ArrayList<Coord> temp = simulateRot(direction);
    if (rotatationCheck(direction, temp)) {
      activePieces.clear();
      for (int i = 0; i < temp.size(); ++i) {
        activePieces.add(temp.get(i));
      }
    }
    copyActiveToBuffer();
    copyBufferToBoard();
  }
  public boolean rotatationCheck(int direction, ArrayList<Coord> temp) {
    boolean safeToRotate = true;
    for (int i = 0; i < temp.size(); ++i) {
      if (tileOccupied(temp.get(i).x, temp.get(i).y)) {
        safeToRotate = false;
        break;
      }
    }
    return safeToRotate;
  }
  public ArrayList<Coord> simulateRot(int direction) {
    ArrayList<Coord> temp  = new ArrayList<Coord>();
    for (int x = 0; x < 5; ++x) {
      for (int y = 0; y < 5; ++y) {
        if (rotationVariants[rotCoord.color][direction][x][y] != 0) {
          temp.add(new Coord(x + (rotCoord.x - 2), y + (rotCoord.y - 2), rotCoord.color));
        }
      }
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
    int scoreAdd = 0;
    int multiplier = 1;
    int i = 24;
    while (i > 5) {
      if (checkLineClear(i)) {
        ++scoreAdd;
        ++multiplier;
        i = 24;
      } else {
        i--;
      }
    }
    score += scoreAdd * multiplier;
    scoreDisplay = String.format("%09d", score);
  }
  public boolean checkLineClear(int lineCheck) {
    updateBuffer();
    boolean cleared = true;
    for (int i = 0; i < 10; ++i) {
      if (!tileOccupied(i, lineCheck)) {
        cleared = false;
        break;
      }
    }
    //System.out.println(lineCheck + " " + cleared);
    if (cleared) {
      for (int y = lineCheck; y > 5; --y) {
        for (int x = 0; x < 10; ++x) {
          bufferedBoard[x][y] = bufferedBoard[x][y - 1];
        }
      }
    }
    copyBufferToBoard();
    return cleared;
  }
  public void hold() {
    updateBuffer();
    int temp = pieces[1];
    int temp2 = pieces[2];
    pieces[2] = temp;
    if (temp2 == 0) {
      pieces[1] = pieces[0];
      pieces[0] = randPiece();
    } else {
      pieces[1] = temp2;
    }
    activePieces.clear();
    generatePeice(pieces[1]);
    copyBufferToBoard();
  }
}