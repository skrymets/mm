package freemind.view.mindmapview;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class MultipleImage extends ImageIcon {
    private final List<ImageIcon> mImages = new ArrayList<>();
    private double zoomFactor = 1;
    private boolean isDirty;

    public MultipleImage(double zoom) {
        zoomFactor = zoom;
        isDirty = true;
    }

    public int getImageCount() {
        return mImages.size();
    }

    public void addImage(ImageIcon image) {
        mImages.add(image);
        setImage(image.getImage());
        isDirty = true;
    }

    public Image getImage() {
        if (!isDirty)
            return super.getImage();
        int w = getIconWidth();
        int h = getIconHeight();
        if (w == 0 || h == 0) {
            return null;
        }
        BufferedImage outImage = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = outImage.createGraphics();

        for (ImageIcon currentIcon : mImages) {
            // py = /* center: */ ( myHeight -
            // (int)(currentIcon.getIconHeight()* zoomFactor)) /2;
            // int pheight = (int) (currentIcon.getIconHeight() * zoomFactor);
            double pwidth = (currentIcon.getIconWidth() * zoomFactor);
            AffineTransform inttrans = AffineTransform.getScaleInstance(
                    zoomFactor, zoomFactor);
            g.drawImage(currentIcon.getImage(), inttrans, null);
            g.translate(pwidth, 0);
        }
        g.dispose();
        setImage(outImage);
        isDirty = false;
        return super.getImage();
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (getImage() != null) {
            super.paintIcon(c, g, x, y);
        }
    }

    // public void paintIcon(Component c,
    // Graphics g,
    // int x,
    // int y)
    // {
    // int myX = x;
    // int myHeight = getIconHeight();
    // for(int i = 0 ; i < mImages.size(); i++) {
    // ImageIcon currentIcon = ((ImageIcon) mImages.get(i));
    // int px,py,pwidth, pheight;
    // px = myX;
    // py = y /* center: */ + ( myHeight - (int)(currentIcon.getIconHeight()*
    // zoomFactor)) /2;
    // pwidth = (int) (currentIcon.getIconWidth() * zoomFactor);
    // pheight = (int) (currentIcon.getIconHeight() * zoomFactor);
    // /* code from ImageIcon.*/
    // if(currentIcon.getImageObserver() == null) {
    // g.drawImage(currentIcon.getImage(), px, py, pwidth, pheight, c);
    // } else {
    // g.drawImage(currentIcon.getImage(), px, py, pwidth, pheight,
    // currentIcon.getImageObserver());
    // }
    // /* end code*/
    // myX += pwidth;
    // }
    // };

    public int getIconWidth() {
        int myX = 0;
        for (ImageIcon mImage : mImages) {
            myX += mImage.getIconWidth();
        }
        // System.out.println("width: "+myX);
        return (int) (myX * zoomFactor);
    }

    public int getIconHeight() {
        int myY = 0;
        for (ImageIcon mImage : mImages) {
            int otherHeight = mImage.getIconHeight();
            if (otherHeight > myY)
                myY = otherHeight;
        }
        // System.out.println("height: "+myY);
        return (int) (myY * zoomFactor);
    }

}
