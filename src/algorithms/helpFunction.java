package algorithms;

import algorithms.Rectangle;

import java.util.*;

public class helpFunction {

    static int[] toInt(SortedSet<Integer> set){
        int[] arr = new int[set.size()];
        int i = 0;
        for(int value:set){
            arr[i++] = value;
        }

        return arr;
    }
    static Object[] compressCoordinates(Rectangle[] rectangles){
        SortedSet<Integer> setCompressX = new TreeSet<>();
        SortedSet<Integer> setCompressY = new TreeSet<>();

        for (Rectangle rect:rectangles){
            setCompressX.add(rect.firstCoordinate.x);
            setCompressY.add(rect.firstCoordinate.y);

            setCompressX.add(rect.secondCoordinate.x + 1);
            setCompressY.add(rect.secondCoordinate.y + 1);
        }

        int[] compressX = toInt(setCompressX);
        int[] compressY = toInt(setCompressY);

        return new Object[]{compressX, compressY};
    }

    static int findPos(int[] arr, int value){
        int left = 0, right = arr.length;
        while(left < right){
            int mid = left + (right - left) / 2;

            if(arr[mid] > value){
                right = mid;
            }else{
                left = mid + 1;
            }
        }
        return left - 1;
    }
}
