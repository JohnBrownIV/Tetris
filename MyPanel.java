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
ArrayList<Coord> activePeices;
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
    activePeices = new ArrayList<Coord>();
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
  @Override
	public void actionPerformed(ActionEvent e) {
    if (updateDelay <= 0) {
      updateBuffer();
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
    for (int i = 0; i < activePeices.size(); i++) {
      bufferedBoard[activePeices.get(i).x][activePeices.get(i).y] = 0; 
    }
    boolean descended = descendActive();
    for (int i = 0; i < activePeices.size(); i++) {
      try {
      bufferedBoard[activePeices.get(i).x][activePeices.get(i).y] = activePeices.get(i).color; 
      } catch (IndexOutOfBoundsException e) {
        System.out.println("ERROR, attempted at " + activePeices.get(i).y);
      }
    }
    if (!descended) {
      activePeices.clear();
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
        activePeices.add(new Coord(4,5,3));
        activePeices.add(new Coord(5,5,3));
        activePeices.add(new Coord(6,5,3));
        activePeices.add(new Coord(5,4,3));
        break;
    }
  }
  public boolean descendActive() {
    boolean success = true;
    int tempX;
    int tempY;
    int tempColor;
    //Checking if we can update
    for (int i = 0; i < activePeices.size(); i++) {
      if (tileOccupied(activePeices.get(i).x, activePeices.get(i).y + 1)) {
        success = false;
        //System.out.println("Couldn't descend");
      }
    }
    if (success) {
      //updating
      for (int i = 0; i < activePeices.size(); i++) {
        tempX = activePeices.get(i).x;
        tempY = activePeices.get(i).y;
        tempColor = activePeices.get(i).color;
        activePeices.set(i, new Coord(tempX, tempY + 1, tempColor));
      }
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
}