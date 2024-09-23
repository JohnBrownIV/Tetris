import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;

public class MyFrame extends JFrame implements KeyListener{
 
 MyPanel panel;
 
 MyFrame(){
  
  panel = new MyPanel();
  
  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  
  this.add(panel);
  this.pack();
  this.setLocationRelativeTo(null);
  this.addKeyListener(this);
  this.setVisible(true);
  this.toFront();
  this.requestFocus();
  
 }
 @Override
	public void keyTyped(KeyEvent e) {
  }
 @Override
	public void keyPressed(KeyEvent e) {
    //System.out.println(e.getKeyCode());
    if (e.getKeyCode() == 69) {
      //E, clockwise turn.
      panel.rotationPress = -1;
    } else if (e.getKeyCode() == 81) {
      panel.rotationPress = 1;
    }
  }
  @Override
	public void keyReleased(KeyEvent e) {
    if (e.getKeyCode() == 69 || e.getKeyCode() == 81) {
      panel.rotationPress = 0;
      panel.performedRot = false;
    }
  }
}