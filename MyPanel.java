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
ArrayList<Coord> activePeices;
Color[][] tetrColor;
 
  MyPanel(){

    defineVariables();
    this.setPreferredSize(new Dimension(width,height));
    timer = new Timer(5, this);
    timer.start();
   
  }
  public void defineVariables() {
    width = 800;
    height = 900;
    boardStatus = new int[10][20];
    bufferedBoard = new int[10][20];
    gridSize = (height - 100) / 20;
    //Test States
    boardStatus[2][3] = 1;
    boardStatus[9][19] = 2;
    //Colors
    tetrColor = new Color[5][2];
    //Reds
    tetrColor[1][0] = new Color(255,0,0);
    tetrColor[1][1] = new Color(100,0,0);
    //Blues
    tetrColor[2][0] = new Color(0,0,255);
    tetrColor[2][1] = new Color(0,0,100);
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
    for (int x = 0; x < 20; x++) {
      for (int i = 0; i < 10; i++) {
        if (boardStatus[i][x] == 0) {
          g2D.setPaint(Color.white);
          g2D.drawRect(200 + (gridSize * i), 100 + (x * gridSize), gridSize, gridSize);
        } else {
          g2D.setPaint(tetrColor[boardStatus[i][x]][0]);
          g2D.fillRect(200 + (gridSize * i), 100 + (x * gridSize), gridSize, gridSize);
          g2D.setPaint(tetrColor[boardStatus[i][x]][1]);
          g2D.drawRect(200 + (gridSize * i), 100 + (x * gridSize), gridSize, gridSize);
        }
      }
    }
  }
  @Override
	public void actionPerformed(ActionEvent e) {
    repaint();
  }

  public void copyBufferToBoard() {
    for (int x = 0; x < 10; ++x) {
      for (int y = 0; y < 20; ++y) {
        boardStatus[x][y] = bufferedBoard[x][y];
      }
    }
  }

  public void reset() {
    //Resets the game
    timer.stop();
    timer = new Timer(5, this);
    timer.start();
    defineVariables();
  }
}