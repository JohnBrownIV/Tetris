public class Coord {
  int x;
  int y;
  boolean checked;
  public Coord(int inX, int inY) {
    x = inX;
    y = inY;
    checked = false;
  }
  public Coord() {
    x = 0;
    y = 0;
    checked = false;
  }
  public void changeX(double amount) {
    x += amount;
  }
  public void changeY(double amount) {
    y += amount;
  }
}
