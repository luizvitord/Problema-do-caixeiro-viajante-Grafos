package algs4;

import java.util.Arrays;
import java.util.Comparator;

public class KdTree {

    private Node root;
    private int size;

    private static class Node {
        private final Point2D p;
        private final RectHV rect;
        private Node lb;
        private Node rt;

        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }

    public KdTree() {
        this.size = 0;
        this.root = null;
    }

    public KdTree(Point2D[] points) {
        if (points == null) throw new IllegalArgumentException("O array de pontos não pode ser nulo");
        Point2D[] pointsCopy = Arrays.copyOf(points, points.length);
        this.size = pointsCopy.length;
        this.root = buildBalancedTree(pointsCopy, 0, pointsCopy.length - 1, true, new RectHV(0, 0, 1000, 1000));
    }

    private Node buildBalancedTree(Point2D[] points, int start, int end, boolean vertical, RectHV rect) {
        if (start > end) {
            return null;
        }

        Comparator<Point2D> comparator = vertical ? Point2D.X_ORDER : Point2D.Y_ORDER;
        Arrays.sort(points, start, end + 1, comparator);

        int medianIndex = start + (end - start) / 2;
        Point2D medianPoint = points[medianIndex];
        Node node = new Node(medianPoint, rect);

        RectHV leftRect, rightRect;
        if (vertical) {
            leftRect = new RectHV(rect.xmin(), rect.ymin(), medianPoint.x(), rect.ymax());
            rightRect = new RectHV(medianPoint.x(), rect.ymin(), rect.xmax(), rect.ymax());
        } else {
            leftRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), medianPoint.y());
            rightRect = new RectHV(rect.xmin(), medianPoint.y(), rect.xmax(), rect.ymax());
        }

        node.lb = buildBalancedTree(points, start, medianIndex - 1, !vertical, leftRect);
        node.rt = buildBalancedTree(points, medianIndex + 1, end, !vertical, rightRect);

        return node;
    }


    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("O ponto não pode ser nulo");
        root = insert(root, p, true, new RectHV(0, 0, 1000, 1000));
    }

    private Node insert(Node x, Point2D p, boolean vertical, RectHV rect) {
        if (x == null) {
            size++;
            return new Node(p, rect);
        }
        if (x.p.equals(p)) return x;

        double cmp = vertical ? p.x() - x.p.x() : p.y() - x.p.y();

        if (cmp < 0) {
            RectHV newRect = vertical ? new RectHV(rect.xmin(), rect.ymin(), x.p.x(), rect.ymax())
                    : new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), x.p.y());
            x.lb = insert(x.lb, p, !vertical, newRect);
        } else {
            RectHV newRect = vertical ? new RectHV(x.p.x(), rect.ymin(), rect.xmax(), rect.ymax())
                    : new RectHV(rect.xmin(), x.p.y(), rect.xmax(), rect.ymax());
            x.rt = insert(x.rt, p, !vertical, newRect);
        }
        return x;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("O ponto não pode ser nulo");
        if (root == null) return null;
        return nearest(root, p, root.p, true);
    }

    private Point2D nearest(Node x, Point2D p, Point2D nearest, boolean vertical) {
        if (x == null) return nearest;

        if (x.p.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
            nearest = x.p;
        }

        Node first, second;
        if ((vertical && p.x() < x.p.x()) || (!vertical && p.y() < x.p.y())) {
            first = x.lb;
            second = x.rt;
        } else {
            first = x.rt;
            second = x.lb;
        }

        if (first != null) {
            nearest = nearest(first, p, nearest, !vertical);
        }

        if (second != null && second.rect.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
            nearest = nearest(second, p, nearest, !vertical);
        }
        return nearest;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("O ponto não pode ser nulo");
        return contains(root, p, true);
    }

    private boolean contains(Node x, Point2D p, boolean vertical) {
        if (x == null) {
            return false;
        }
        if (x.p.equals(p)) {
            return true;
        }

        double cmp;
        if (vertical) {
            cmp = p.x() - x.p.x();
        } else {
            cmp = p.y() - x.p.y();
        }

        if (cmp < 0) {
            return contains(x.lb, p, !vertical);
        } else {
            return contains(x.rt, p, !vertical);
        }
    }
}