package algorithms;

import java.util.Arrays;
import java.util.Comparator;

class Event{

    public int x;
    public int beginY;
    public int endY;
    public int status;

        public int getX() {
            return x;
        }

    public Event(int x, int beginY, int endY, int status) {
        this.x = x;
        this.beginY = beginY;
        this.endY = endY;
        this.status = status;
    }
}

class Node{
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
public class segmentTreeAlgo {
    int[] compressX;
    int[] compressY;
    Node[] roots;

    public segmentTreeAlgo(Rectangle[] rectangles) {
        Object[] compress = helpFunction.compressCoordinates(rectangles);
        compressX = (int[]) compress[0];
        compressY = (int[]) compress[1];
        roots = buildPersistentSegmentTree(rectangles);
    }

    Node buildTree(int[] arr, int leftInd, int rightInd){
        if (leftInd + 1 == rightInd){
            return new Node(arr[leftInd], null, null, leftInd, rightInd);
        }

        int mid = (leftInd + rightInd) / 2;
        Node left = buildTree(arr, leftInd, mid);
        Node right = buildTree(arr, mid, rightInd);

        return new Node(left.value + right.value, left, right, left.leftInd, right.rightInd);
    }

    Node insert(Node node, int begin, int end, int value){
        if (begin <= node.leftInd && node.rightInd <= end){
            return new Node(node.value + value, node.left, node.right, node.leftInd, node.rightInd);
        }

        if(node.rightInd <= begin || end <= node.leftInd){
            return node;
        }

        Node newNode = new Node(node.value, node.left, node.right, node.leftInd, node.rightInd);

        newNode.left = insert(newNode.left, begin, end, value);
        newNode.right = insert(newNode.right, begin, end, value);

        return newNode;
    }

    Node[] buildPersistentSegmentTree(Rectangle[] rectangles){
        if(rectangles.length == 0){
            return null;
        }

        Event[] events = new Event[rectangles.length * 2];
        Node[] roots = new Node[events.length];
        int ind = 0;

        for(Rectangle rect:rectangles){
            events[ind++] = new Event(helpFunction.findPos(compressX, rect.firstCoordinate.x),
                    helpFunction.findPos(compressY, rect.firstCoordinate.y),
                    helpFunction.findPos(compressY, rect.secondCoordinate.y + 1),
                    1);

            events[ind++] = new Event(helpFunction.findPos(compressX, rect.secondCoordinate.x + 1),
                    helpFunction.findPos(compressY, rect.firstCoordinate.y),
                    helpFunction.findPos(compressY, rect.secondCoordinate.y + 1),
                    -1);
        }

        Arrays.sort(events, Comparator.comparing(Event::getX));

       Node root = buildTree(new int[compressY.length], 0, compressY.length);

        int endX = events[0].x;
        ind = 0;
        for(Event event:events){
            if(endX != event.x){
                roots[ind++] = root;
                endX = event.x;
            }

            root = insert(root, event.beginY, event.endY, event.status);
        }

        return roots;
    }

    int getAnswer(Node node, int target){
        if (node != null){
            int mid = (node.leftInd + node.rightInd) / 2;

            if(target < mid){
                return node.value + getAnswer(node.left, target);
            }else{
                return node.value + getAnswer(node.right, target);
            }
        }

        return 0;
    }

    int[] algo(Point[] points){
        int[] answer = new int[points.length];

        if(roots == null){
            return answer;
        }

        for(int i = 0; i < points.length; ++i){
            int posX = helpFunction.findPos(compressX, points[i].x);
            int posY = helpFunction.findPos(compressY, points[i].y);

            if(posX == -1 || posY == -1){
                answer[i] = 0;
            }else{
                answer[i] = getAnswer(roots[posX], posY);
            }
        }

        return answer;
    }
}
