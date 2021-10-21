/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2001  Joerg Mueller <joergmueller@bigfoot.com>
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
// ConvexHull.java (c) fc
// 
// Adapted from Sedgewick, Algorithms
//
package freemind.view.mindmapview;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import static java.lang.Math.abs;

public class ConvexHull {

    protected int ccw(Point p0, Point p1, Point p2) {
        int dx1, dx2, dy1, dy2;
        dx1 = p1.x - p0.x;
        dy1 = p1.y - p0.y;
        dx2 = p2.x - p0.x;
        dy2 = p2.y - p0.y;
        int comp = dx1 * dy2 - dy1 * dx2;
        if (comp > 0)
            return 1;
        if (comp < 0)
            return -1;
        if ((dx1 * dx2 < 0) || (dy1 * dy2 < 0))
            return -1;
        if (dx1 * dx1 + dy1 * dy1 >= dx2 * dx2 + dy2 * dy2)
            return 0;
        return 1;
    }

    protected class ThetaComparator implements Comparator<Point> {
        Point p0;

        public ThetaComparator(Point p0) {
            this.p0 = new Point(p0);
        }

        /* the < relation. */
        public int compare(Point p1, Point p2) {
            double comp = theta(p0, p1) - theta(p0, p2);
            if (p1.equals(p2))
                return 0;
            if (comp > 0)
                return 1;
            if (comp < 0)
                return -1;
            // here, the points are collinear with p0 (i.e. p0,p1,p2 are on one
            // line). So we reverse the compare relation to get that nearer
            // points are greater.
            // we take the point that is nearer to p0:
            int dx1, dx2, dy1, dy2;
            dx1 = p1.x - p0.x;
            dy1 = p1.y - p0.y;
            dx2 = p2.x - p0.x;
            dy2 = p2.y - p0.y;
            int comp2 = (dx1 * dx1 + dy1 * dy1) - (dx2 * dx2 + dy2 * dy2);
            if (comp2 > 0)
                return -1;
            if (comp2 < 0)
                return 1;
            return 0;
        }

        double theta(Point p1, Point p2) {
            int dx, dy, ax, ay;
            double t;
            dx = p2.x - p1.x;
            ax = abs(dx);
            dy = p2.y - p1.y;
            ay = abs(dy);
            if ((dx == 0) && (dy == 0))
                t = 0;
            else
                t = ((double) dy) / ((double) (ax + ay));
            if (dx < 0)
                t = 2f - t;
            else if (dy < 0)
                t = 4f + t;

            return t * 90f;
        }
    }

    List<Point> doGraham(List<Point> points) {
        int i, min, M;
        Point t;
        min = 0;
        // search for minimum:
        for (i = 1; i < points.size(); ++i) {
            if (points.get(i).y < points.get(min).y)
                min = i;
        }
        // continue along the values with same y component
        for (i = 0; i < points.size(); ++i) {
            if ((points.get(i).y == points.get(min).y)
                    && (points.get(i).x > points.get(min).x)) {
                min = i;
            }
        }
        // swap:
        t = points.get(0);
        points.set(0, points.get(min));
        points.set(min, t);
        ThetaComparator comp = new ThetaComparator(points.get(0));
        points.sort(comp);
        // the first is the last.
        points.add(0, new Point(points.get(points.size() - 1)));
        M = 3;
        for (i = 4; i < points.size(); ++i) {
            while (ccw(points.get(M), points.get(M - 1), points.get(i)) >= 0) {
                M--;
            }
            M++;
            // swap:
            t = points.get(M);
            points.set(M, points.get(i));
            points.set(i, t);
        }
        points.remove(0);
        return points.subList(0, M);
    }

    public List<Point> calculateHull(List<Point> coordinates) {
        List<Point> q = new ArrayList<>();
        ListIterator<Point> coordinates_it = coordinates.listIterator();
        while (coordinates_it.hasNext()) {
            q.add(coordinates_it.next());
        }
        List<Point> res = doGraham(q);
        return res;
    }

}
