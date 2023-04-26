package test;
import algorithms.Rectangle;
import algorithms.Point;

import java.util.Scanner;

public class generateData {
    public static Rectangle[] getRectangles(int n){
        Rectangle[] rectangles = new Rectangle[n];

        for(int i = 0; i < n; ++i){
            rectangles[i] = new Rectangle(new Point(10 * i, 10 * i), new Point(10 * (2 * n - i), 10 * (2 * n - i)));
        }

        return rectangles;
    }

    public static Point[] getPoints(int n, int m){
        Point[] points = new Point[m];
        for(int i = 0; i < m; ++i){
            points[i] = new Point((int)Math.pow(2999 * i, 31) % (20 * n), (int)Math.pow(5323 * i, 31) % (20 * n));
        }

        return points;
    }

    public Rectangle[] getRectanglesFromUser(){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Rectangle[] rectangles = new Rectangle[n];

        for (int i = 0; i < n; ++i) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            Point p1 = new Point(x, y);

            x = sc.nextInt();
            y = sc.nextInt();
            Point p2 = new Point(x, y);

            rectangles[i] = new Rectangle(p1, p2);
        }

        return rectangles;
    }

    public Point[] getPointsFromUser(){
        Scanner sc = new Scanner(System.in);
        int m = sc.nextInt();
        Point[] points = new Point[m];
        for (int i = 0; i < m; ++i) {
            int x = sc.nextInt();
            int y = sc.nextInt();

            points[i] = new Point(x, y);
        }
        return points;
    }
}