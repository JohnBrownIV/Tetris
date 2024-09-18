public class Coord {
  int x;
  int y;
  public Coord(int inX, int inY) {
    x = inX;
    y = inY;
  }
  public Coord() {
    x = 0;
    y = 0;
  }
  public void changeX(double amount) {
    x += amount;
  }
  public void changeY(double amount) {
    y += amount;
  }
}
