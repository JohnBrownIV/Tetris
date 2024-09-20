public class Coord {
  int x;
  int y;
  int color;
  public Coord(int inX, int inY, int inColor) {
    x = inX;
    y = inY;
    color = inColor;
  }
  public Coord(int inX, int inY) {
    x = inX;
    y = inY;
    color = 0;
  }
  public Coord() {
    x = 0;
    y = 0;
    color = 1;
  }
  public void changeX(double amount) {
    x += amount;
  }
  public void changeY(double amount) {
    y += amount;
  }
  public String toString() {
    return "(" + x + ", " + y + ")";
  }
}
