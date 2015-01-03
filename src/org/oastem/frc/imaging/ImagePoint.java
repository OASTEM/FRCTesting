/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oastem.frc.imaging;

import com.sun.squawk.util.MathUtils;

/**
 * This class is gonna contain ALL of the possible data we can get
 * on each data point we get. 
 * 
 * Tons of variables. You have been warned.
 * 
 * @author KTOmega
 *
 * Going to do some randoms things in here. You have been warned.
 * @author joyhsu0504
 *
 */
public class ImagePoint {
    public double rectangularity;
    public double aspectRatioInner;
    public double aspectRatioOuter;
    public double xEdge;
    public double yEdge;
    public double x;
    public double y;
    public int label = 0;
    public int marked = -1;
    
    public boolean hot = false;
    
    // Oh dear, oh my.
    // The things I do to make code look nice.
    public static final int INVALID = -1;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    
    // 0 => left, 1 => right
    private int side = INVALID;
    
    // 0 => horizontal, 1 => vertical
    private int orientation = INVALID;
    
    public ImagePoint(int label, double x, double y) {
        this(x, y);
        this.setLabel(label);
    }
    
    public ImagePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public double getX() { return this.x; }
    public double getY() { return this.y; }
    
    public int getOrientation() { return this.orientation; }
    public int getSide() { return this.side; }
    
    public boolean isLeft() {
        return side == 0;
    }
    
    public boolean isRight() {
        return side == 1;
    }
    
    public double getAspectRatio() {
        return aspectRatioOuter;
    }
    
    public boolean hasAspect(double aspect, double error) {
        return Math.abs(this.getAspectRatio() - aspect) <= error;
    }
    
    public void setOrientation(int newOr) {
        this.orientation = newOr;
    }
    
    public void setSide(int newSide) {
        this.side = newSide;
    }
    
    public int getLabel() { return this.label; }
    public void setLabel(int newLabel) { this.label = newLabel; }
    
    public boolean isMarked() { return this.marked != -1; }
    public int getMarker() { return this.marked; }
    public void setMarker(int mark) { this.marked = mark; }
    
    public boolean isHot() { return hot; }
    public void setHot(boolean orNot) { this.hot = orNot; }
    
    public static double calculateDistance(ImagePoint p1, ImagePoint p2) {
        return Math.sqrt(MathUtils.pow(p2.getX() - p1.getX(), 2) + MathUtils.pow(p2.getY() - p1.getY(), 2));
    }
	public boolean isHigher(ImagePoint p1, ImagePoint p2) {
		if(p1.getY() > p2.getY()) {
			return true;
		}
		return false;
	}	
	public boolean hasSameHeight(ImagePoint p1, ImagePoint p2) {
		if(p1.getY() == p2.getY()) {
			return true;
		}
		return false;
	}	
	public boolean distanceAway(ImagePoint p1, ImagePoint p2, double goal, double errorMargin) {
		if((this.calculateDistance(p1, p2) >= goal-errorMargin) && (this.calculateDistance(p1, p2) <= goal+errorMargin) ) {
			return true;
		}
		return false;
	}
    public String toString() {
        return "(" + this.getX() + ", " + this.getY() + ")";
    }
}
