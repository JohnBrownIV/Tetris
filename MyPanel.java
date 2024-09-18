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
  }
 
  public void paint(Graphics g) {
    
    Graphics2D g2D = (Graphics2D) g;
    
    g2D.setPaint(Color.black);
    g2D.fillRect(0, 0, width, height);
    g2D.setPaint(Color.gray);
    g2D.fillRect(0, 0, width, 100);
    g2D.fillRect(0, 0, 200, height);
    g2D.setPaint(Color.white);
    for (int x = 0; x < 20; x++) {
      for (int i = 0; i < 10; i++) {
        g2D.drawRect(200 + gridSize * i), 100 + (x * ((height - 100) / 20)), ((height - 100) / 20), ((height - 100) / 20));
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