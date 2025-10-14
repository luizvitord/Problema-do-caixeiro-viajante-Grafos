import algs4.KdTree;
import algs4.Point2D;
import algs4.StdDraw;
import algs4.StdOut;
import java.util.HashMap; // Importa o HashMap

public class Tour {

    private static class Node {
        private Point point;
        private Node next;
    }

    private Node start;
    private int count;
    private final boolean useKdTree;

    private KdTree kdTree;
    private HashMap<Point2D, Node> nodeMap;

    public Tour() {
        this(false);
    }

    public Tour(boolean useKdTree) {
        this.useKdTree = useKdTree;
        this.start = null;
        this.count = 0;
        if (useKdTree) {
            this.kdTree = new KdTree();
            this.nodeMap = new HashMap<>();
        }
    }

    public Tour(Point a, Point b, Point c, Point d) {
        this();
        insertNearest(a);
        insertNearest(b);
        insertNearest(c);
        insertNearest(d);
    }

    public int size() {
        return count;
    }

    public double length() {
        if (start == null || start.next == start)
            return 0.0;

        double total = 0.0;
        Node current = start;
        do {
            total += current.point.distanceTo(current.next.point);
            current = current.next;
        } while (current != start);
        return total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (start == null)
            return "(Tour vazio)";

        Node current = start;
        do {
            sb.append(current.point.toString()).append("\n");
            current = current.next;
        } while (current != start);

        return sb.toString();
    }

    public void draw() {
        if (start == null || start.next == start)
            return;

        Node current = start;
        do {
            current.point.drawTo(current.next.point);
            current = current.next;
        } while (current != start);
    }

    public void insertNearest(Point p) {
        if (useKdTree) {
            insertNearestKd(p);
        } else {
            insertNearestNaive(p);
        }
    }

    public void insertNearestNaive(Point p) {
        Node newNode = new Node();
        newNode.point = p;

        if (start == null) {
            start = newNode;
            newNode.next = newNode;
            count = 1;
            return;
        }

        Node current = start;
        Node nearestNode = start;
        double minDistance = p.distanceTo(current.point);

        do {
            double dist = p.distanceTo(current.point);
            if (dist < minDistance) {
                minDistance = dist;
                nearestNode = current;
            }
            current = current.next;
        } while (current != start);

        newNode.next = nearestNode.next;
        nearestNode.next = newNode;
        count++;
    }

    public void insertNearestKd(Point p) {
        Node newNode = new Node();
        newNode.point = p;
        Point2D p2d = new Point2D(p.x(), p.y());

        if (start == null) {
            start = newNode;
            newNode.next = newNode;
        } else {
            Point2D nearestP2d = kdTree.nearest(p2d);

            Node nearestNode = nodeMap.get(nearestP2d);

            newNode.next = nearestNode.next;
            nearestNode.next = newNode;
        }

        count++;
        kdTree.insert(p2d);
        nodeMap.put(p2d, newNode);
    }

    public static void main(String[] args) {
        Tour tourNaive = new Tour(false);
        tourNaive.insertNearest(new Point(1.0, 1.0));
        tourNaive.insertNearest(new Point(1.0, 4.0));
        tourNaive.insertNearest(new Point(4.0, 4.0));
        tourNaive.insertNearest(new Point(4.0, 1.0));

        StdOut.println("--- Versão Ingênua ---");
        StdOut.println("# de pontos = " + tourNaive.size());
        StdOut.println("Comprimento = " + tourNaive.length());
        StdOut.println(tourNaive);

        Tour tourKd = new Tour(true);
        tourKd.insertNearest(new Point(1.0, 1.0));
        tourKd.insertNearest(new Point(1.0, 4.0));
        tourKd.insertNearest(new Point(4.0, 4.0));
        tourKd.insertNearest(new Point(4.0, 1.0));

        StdOut.println("\n--- Versão com KdTree ---");
        StdOut.println("# de pontos = " + tourKd.size());
        StdOut.println("Comprimento = " + tourKd.length());
        StdOut.println(tourKd);

        StdDraw.setXscale(0, 6);
        StdDraw.setYscale(0, 6);
        tourKd.draw();
    }
}