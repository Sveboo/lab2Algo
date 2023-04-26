package algorithms;

import algorithms.Point;
import algorithms.Rectangle;
import algorithms.helpFunction;

public class mapAlgo {
    private final Rectangle[] rectangles;
    private final int[][] map;
    public final int[] compressX;
    public final int[] compressY;

    mapAlgo(Rectangle[] _rectangles){
        rectangles = _rectangles;
        Object[] compress = helpFunction.compressCoordinates(rectangles);
        compressX = (int[]) compress[0];
        compressY = (int[]) compress[1];
        map = createMap();
    }

    int[][] createMap(){
        int[][] matrixMap = new int[compressY.length][compressX.length];

        for(Rectangle rect:rectangles){
            int firstIndX = helpFunction.findPos(compressX, rect.firstCoordinate.x);
            int firstIndY = helpFunction.findPos(compressY, rect.firstCoordinate.y);
            int secondIndX = helpFunction.findPos(compressX, rect.secondCoordinate.x + 1);
            int secondIndY = helpFunction.findPos(compressY, rect.secondCoordinate.y + 1);

            for(int i = firstIndY; i < secondIndY; ++i){
                for(int j = firstIndX; j < secondIndX; ++j){
                    ++matrixMap[i][j];
                }
            }
        }

        return matrixMap;
    }

    int[] algo(Point[] points){
        int[] answer = new int[points.length];

        for(int i = 0; i < points.length; ++i){
            int posX = helpFunction.findPos(compressX, points[i].x);
            int posY = helpFunction.findPos(compressY, points[i].y);

            if(posX == -1 || posY == -1){
                answer[i] = 0;
            }else{
                answer[i] = map[posY][posX];
            }

        }

        return answer;
    }
}
