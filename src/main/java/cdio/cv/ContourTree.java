package cdio.cv;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;

public class ContourTree {

    public ContourTree Sibling, Child;
    int Number;
    MatOfPoint contour;

    public ContourTree(Mat hir, ArrayList<MatOfPoint> contours, int Num) {
        Number = Num;
        contour = contours.get(Number);
        int sibNum = (int) hir.get(0, Number)[0];
        int childNum = (int) hir.get(0, Number)[2];

        if (sibNum != -1) {
            Sibling = new ContourTree(hir, contours, sibNum);
        } else {
            Sibling = null;
        }

        if (childNum != -1) {
            Child = new ContourTree(hir, contours, childNum);
        } else {
            Child = null;
        }
    }

    // Used for debugging.
    // Draws a certain level of contours with a certain color.
    // Used for visualizing the contour tree
    public void drawContourChildren(Mat img, Scalar color, int level, int desiredLevel) {
        ArrayList<Integer> alreadyDrawn = new ArrayList<>();
        for (ContourTree current = this; current != null; current = current.Sibling) {
            if (level == desiredLevel) {
                Imgproc.drawContours(img, Arrays.asList(current.contour), -1, color, 1);
            } else if (level < desiredLevel) {
                if (current.Child != null) {
                    current.Child.drawContourChildren(img, color, level + 1, desiredLevel);
                }
            }
        }
    }

    // See findRectIfChildren
    public void drawRectIfChildren(Mat img, int reqDepth) {
        for (ContourTree current = this; current != null; current = current.Sibling) {
            if (current.getDepth() >= reqDepth) {
                MatOfPoint2f newContour = new MatOfPoint2f(current.contour.toArray());

                RotatedRect rotatedRect = Imgproc.minAreaRect(newContour);
                Point[] points = new Point[4];
                rotatedRect.points(points);
                MatOfPoint a = new MatOfPoint(points);
                Imgproc.drawContours(img, Arrays.asList(a), -1, new Scalar(100, 255, 100), 2);

                current.Child.drawRectIfChildren(img, reqDepth);
            }
        }
    }

    // When using points for a perspective transform, it is important
    // that the points are ordered the same every time.
    // To ensure this we manually order the points using this function.
    // It's ugly, but it works
    private Point[] orderPoints(Point[] points) {
       /* Point ordering
                   X
    ----------------->
   |
   |   0----------1
   |   |          |
   |   |          |
 y |   3----------2
   v
        */

        Point[] res_point = new Point[4];
        double[] dists = new double[4];
        for (int i = 0; i < 4; i++) {
            dists[i] = points[i].x * points[i].x + points[i].y * points[i].y;
        }
        if (dists[0] < dists[1] && dists[0] < dists[2] && dists[0] < dists[3]) {
            res_point[0] = points[0];
            res_point[1] = points[1];
            res_point[2] = points[2];
            res_point[3] = points[4];
        } else if (dists[1] < dists[0] && dists[1] < dists[2] && dists[1] < dists[3]) {
            res_point[0] = points[1];
            res_point[1] = points[2];
            res_point[2] = points[3];
            res_point[3] = points[0];
        } else if (dists[2] < dists[1] && dists[2] < dists[0] && dists[2] < dists[3]) {
            res_point[0] = points[2];
            res_point[1] = points[3];
            res_point[2] = points[0];
            res_point[3] = points[1];
        } else if (dists[3] < dists[1] && dists[3] < dists[2] && dists[3] < dists[0]) {
            res_point[0] = points[3];
            res_point[1] = points[0];
            res_point[2] = points[1];
            res_point[3] = points[2];
        } else {
            res_point[0] = points[3];
            res_point[1] = points[0];
            res_point[2] = points[1];
            res_point[3] = points[2];
        }

        return res_point;
    }

    // Find the minAreaRect of all contours with reqDepth amount of children.
    // QR-codes often have a high (2-3) number of depth
    public ArrayList<MatOfPoint2f> findRectIfChildren(int reqDepth) {
        ArrayList<MatOfPoint2f> l = new ArrayList<>();
       for(ContourTree current = this; current != null; current = current.Sibling) {
           if(current.getDepth() >= reqDepth) {
               MatOfPoint2f newContour = new MatOfPoint2f(current.contour.toArray());
               RotatedRect rotatedRect = Imgproc.minAreaRect(newContour);
               Point[] points = new Point[4];
               rotatedRect.points(points);
               points = orderPoints(points);
               l.add(new MatOfPoint2f(points));
               l.addAll(current.Child.findRectIfChildren(reqDepth));
           }
       }
       return l;
    }

    // Find the maximum number of steps from the root of the tree to a leaf
    public int getDepth() {
        int depth = 0;
        if (Child != null) {
            for (ContourTree ct = Child; ct != null; ct = ct.Sibling) {
                if (ct.getDepth() > depth) {
                    depth = ct.getDepth();
                }
            }
            depth++;
        }
        return depth;
    }

}
