import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class MyPanel extends JPanel implements ActionListener {

Timer timer;
int[][] boardStatus;
int width;
int height;
int gridSize;
ArrayList<Coord> activePeices;
 
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
    gridSize = (height - 100) / 20;
    boardStatus[2][3] = 1;
  }
 
  public void paint(Graphics g) {
    
    Graphics2D g2D = (Graphics2D) g;
    
    g2D.setPaint(Color.black);
    g2D.fillRect(0, 0, width, height);
    g2D.setPaint(Color.gray);
    g2D.fillRect(0, 0, width, 100);
    g2D.fillRect(0, 0, 200, height);
    g2D.fillRect(width - 200, 0, 200, height);
    for (int x = 0; x < 20; x++) {
      for (int i = 0; i < 10; i++) {
        switch (boardStatus[i][x]) {
          case 0:
            g2D.setPaint(Color.white);
            g2D.drawRect(200 + (gridSize * i), 100 + (x * gridSize), gridSize, gridSize);
            break;
          case 1:
            g2D.setPaint(Color.red);
            g2D.fillRect(200 + (gridSize * i), 100 + (x * gridSize), gridSize, gridSize);
            g2D.setPaint(Color.darkGray);
            g2D.drawRect(200 + (gridSize * i), 100 + (x * gridSize), gridSize, gridSize);
            break;
          default:
        }
      }
    }

  }
  @Override
	public void actionPerformed(ActionEvent e) {
    repaint();
  }

  public void reset() {
    //Resets the game
    timer.stop();
    timer = new Timer(5, this);
    timer.start();
    defineVariables();
  }
}