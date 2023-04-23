class Point{
    public int x;
    public int y;

    Point(int _x, int _y){
        x = _x;
        y = _y;
    }
}

public class Rectangle{
    public Point firstCoordinate;
    public Point secondCoordinate;

    Rectangle(Point f, Point s){
        firstCoordinate = f;
        secondCoordinate = s;
    }
}
