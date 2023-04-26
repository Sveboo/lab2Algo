package test;
import algorithms.Point;
import algorithms.Rectangle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

import static test.generateData.getPoints;
import static test.generateData.getRectangles;

public class Main {
    static class Event {
        public int getX() {
            return x;
        }

        public int x;
        public int beginY;
        public int endY;
        public int status;


        public Event(int x, int beginY, int endY, int status) {
            this.x = x;
            this.beginY = beginY;
            this.endY = endY;
            this.status = status;
        }
    }


    static class Node {
        int value;
        Node left;
        Node right;
        int leftInd;
        int rightInd;

        public Node(int value, Node left, Node right, int leftInd, int rightInd) {
            this.value = value;
            this.left = left;
            this.right = right;
            this.leftInd = leftInd;
            this.rightInd = rightInd;
        }
    }

    static void printAnswer(int[] arr) {
        for (int value : arr) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

    static int[] toInt(SortedSet<Integer> set) {
        int[] arr = new int[set.size()];
        int i = 0;
        for (int value : set) {
            arr[i++] = value;
        }

        return arr;
    }

    static Object[] compressCoordinates(Rectangle[] rectangles) {
        SortedSet<Integer> setCompressX = new TreeSet<>();
        SortedSet<Integer> setCompressY = new TreeSet<>();

        for (Rectangle rect : rectangles) {
            setCompressX.add(rect.firstCoordinate.y);
            setCompressY.add(rect.firstCoordinate.y);

            setCompressX.add(rect.secondCoordinate.x + 1);
            setCompressY.add(rect.secondCoordinate.y + 1);
        }

        int[] compressX = toInt(setCompressX);
        int[] compressY = toInt(setCompressY);

        return new Object[]{compressX, compressY};
    }

    static int findPos(int[] arr, int value) {
        int left = 0, right = arr.length;
        while (left < right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] > value) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left - 1;
    }

    static boolean contain(Rectangle rectangle, Point p){
        return rectangle.firstCoordinate.x <= p.x && rectangle.firstCoordinate.y <= p.y &&
                rectangle.secondCoordinate.x >= p.x && rectangle.secondCoordinate.y >= p.y;
    }

    static int[][] createMap(Rectangle[] rectangles, int[] compressX, int[] compressY){
        int[][] matrixMap = new int[compressY.length][compressX.length];

        for(Rectangle rect:rectangles){
            int firstIndX = findPos(compressX, rect.firstCoordinate.x);
            int firstIndY = findPos(compressY, rect.firstCoordinate.y);
            int secondIndX = findPos(compressX, rect.secondCoordinate.x + 1);
            int secondIndY = findPos(compressY, rect.secondCoordinate.y + 1);

            for(int i = firstIndY; i < secondIndY; ++i){
                for(int j = firstIndX; j < secondIndX; ++j){
                    ++matrixMap[i][j];
                }
            }
        }

        return matrixMap;
    }

    static Node buildTree(int[] arr, int leftInd, int rightInd) {
        if (leftInd + 1 == rightInd) {
            return new Node(arr[leftInd], null, null, leftInd, rightInd);
        }

        int mid = (leftInd + rightInd) / 2;
        Node left = buildTree(arr, leftInd, mid);
        Node right = buildTree(arr, mid, rightInd);

        return new Node(left.value + right.value, left, right, left.leftInd, right.rightInd);
    }


    static Node insert(Node node, int begin, int end, int value) {
        if (begin <= node.leftInd && node.rightInd <= end) {
            return new Node(node.value + value, node.left, node.right, node.leftInd, node.rightInd);
        }

        if (node.rightInd <= begin || end <= node.leftInd) {
            return node;
        }

        Node newNode = new Node(node.value, node.left, node.right, node.leftInd, node.rightInd);

        newNode.left = insert(newNode.left, begin, end, value);
        newNode.right = insert(newNode.right, begin, end, value);

        return newNode;
    }

    static Node[] buildPersistentSegmentTree(Rectangle[] rectangles, int[] compressX, int[] compressY) {

        if (rectangles.length == 0) {
            return null;
        }

        Event[] events = new Event[rectangles.length * 2];
        Node[] roots = new Node[events.length];

        int ind = 0;
        for (Rectangle rect : rectangles) {
            events[ind++] = new Event(findPos(compressX, rect.firstCoordinate.x),
                    findPos(compressY, rect.firstCoordinate.y),
                    findPos(compressY, rect.secondCoordinate.y + 1),
                    1);

            events[ind++] = new Event(findPos(compressX, rect.secondCoordinate.x + 1),
                    findPos(compressY, rect.firstCoordinate.y),
                    findPos(compressY, rect.secondCoordinate.y + 1),
                    -1);
        }

        Arrays.sort(events, Comparator.comparing(Event::getX));

        Node root = buildTree(new int[compressY.length], 0, compressY.length);

        int endX = events[0].x;
        ind = 0;
        for (Event event : events) {
            if (endX != event.x) {
                roots[ind++] = root;
                endX = event.x;
            }

            root = insert(root, event.beginY, event.endY, event.status);
        }

        return roots;
    }

    static int getAnswer(Node node, int target) {
        if (node != null) {
            int mid = (node.leftInd + node.rightInd) / 2;

            if (target < mid) {
                return node.value + getAnswer(node.left, target);
            } else {
                return node.value + getAnswer(node.right, target);
            }
        }

        return 0;
    }

    static int[] bruteForceAlgo(Rectangle[] rectangles, Point[] points){
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


    static int[] mapAlgo(Point[] points, int[][] map, int[] compressX, int[] compressY){
        int[] answer = new int[points.length];

        for(int i = 0; i < points.length; ++i){
            int posX = findPos(compressX, points[i].x);
            int posY = findPos(compressY, points[i].y);

            if(posX == -1 || posY == -1){
                answer[i] = 0;
            }else{
                answer[i] = map[posY][posX];
            }

        }

        return answer;
    }

    static int[] segTreeAlgo(Point[] points, int[] compressX, int[] compressY, Node[] roots) {
        int[] answer = new int[points.length];

        if (roots == null) {
            return answer;
        }

        for (int i = 0; i < points.length; ++i) {
            int posX = findPos(compressX, points[i].x);
            int posY = findPos(compressY, points[i].y);

            if (posX == -1 || posY == -1) {
                answer[i] = 0;
            } else {
                answer[i] = getAnswer(roots[posX], posY);
            }
        }

        return answer;
    }

    static void testAlgoAndWriteRes(){
        int n = 14;
        long[] bruteForceAlgoTime = new long[n];
        long[] mapAlgoTime = new long[n];
        long[] segTreeAlgoTime = new long[n];
        long[] bruteForcePreparation = new long[n];
        long[] mapPreparation = new long[n];
        long[] segTreePreparation = new long[n];

        int cnt = 1;
        for(int i = 0; i < n; ++i){
            long start, end;
            int[] compressX, compressY;
            Object[] compress;

            Rectangle[] rectangles = getRectangles(cnt);
            Point[] points = getPoints(cnt, 10000);

            start = System.nanoTime();
            bruteForceAlgo(rectangles, points);
            end = System.nanoTime();
            bruteForceAlgoTime[i] = end - start;

            start = System.nanoTime();
            compress = compressCoordinates(rectangles);
            compressX = (int[]) compress[0];
            compressY = (int[]) compress[1];
            int[][] map = createMap(rectangles, compressX, compressY);
            end = System.nanoTime();
            mapPreparation[i] = end - start;

            start = System.nanoTime();
            mapAlgo(points, map, compressX, compressY);
            end = System.nanoTime();
            mapAlgoTime[i] = end - start;

            start = System.nanoTime();
            compress = compressCoordinates(rectangles);
            compressX = (int[]) compress[0];
            compressY = (int[]) compress[1];
            Node[] roots = buildPersistentSegmentTree(rectangles, compressX, compressY);
            end = System.nanoTime();
            segTreePreparation[i] = end - start;

            start = System.nanoTime();
            segTreeAlgo(points, compressX, compressY, roots);
            end = System.nanoTime();
            segTreeAlgoTime[i] = end - start;
            cnt *= 2;
        }

        try(Writer writer = new BufferedWriter(new FileWriter("resultsAlgo.txt"))){
            String stringResult = Arrays.toString(bruteForceAlgoTime).replaceAll("[\\[\\]]", "") + "\n";
            writer.write(stringResult, 0, stringResult.length());

            stringResult = Arrays.toString(mapAlgoTime).replaceAll("[\\[\\]]", "") + "\n";
            writer.write(stringResult, 0, stringResult.length());

            stringResult = Arrays.toString(segTreeAlgoTime).replaceAll("[\\[\\]]", "") + "\n";
            writer.write(stringResult, 0, stringResult.length());

            stringResult = Arrays.toString(bruteForcePreparation).replaceAll("[\\[\\]]", "") + "\n";
            writer.write(stringResult, 0, stringResult.length());

            stringResult = Arrays.toString(mapPreparation).replaceAll("[\\[\\]]", "") + "\n";
            writer.write(stringResult, 0, stringResult.length());

            stringResult = Arrays.toString(segTreePreparation).replaceAll("[\\[\\]]", "");
            writer.write(stringResult, 0, stringResult.length());

            writer.flush();

        }catch (IOException e){
            System.out.println("err");
        }
    }
    public static void main(String[] args) {
        testAlgoAndWriteRes();
    }
}

