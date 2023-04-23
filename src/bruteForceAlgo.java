public class bruteForceAlgo {
    private final Rectangle[] rectangles;

    bruteForceAlgo(Rectangle[] _rectangles){
         rectangles = _rectangles;
    }
    boolean contain(Rectangle rectangle, Point p){
        return rectangle.firstCoordinate.x <= p.x && rectangle.firstCoordinate.y <= p.y &&
                rectangle.secondCoordinate.x >= p.x && rectangle.secondCoordinate.y >= p.y;
    }

    int[] algo(Point[] points){
        int[] answer = new int[points.length];

        for(Rectangle rect:rectangles){
            for(int i = 0; i < points.length; ++i){
                if(contain(rect, points[i])){
                    ++answer[i];
                }
            }
        }

        return answer;
    }
}
