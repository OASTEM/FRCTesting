/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oastem.frc.imaging;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.LinearAverages;
import edu.wpi.first.wpilibj.image.NIVision;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.HSLImage;

/**
 * Moving all that template NI Vision junk into this new class.
 *
 * @author KTOmega
 *
 * Importing colorImage methods for testing
 * @author joyhsu0504
 */
public class ImageUtils {

    private static final int XMAXSIZE = 24;
    private static final int XMINSIZE = 24;
    private static final int YMAXSIZE = 24;
    private static final int YMINSIZE = 48;
    private static final double xMax[] = {1, 1, 1, 1, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, 1, 1, 1, 1};
    private static final double xMin[] = {.4, .6, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, 0.6, 0};
    private static final double yMax[] = {1, 1, 1, 1, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, 1, 1, 1, 1};
    private static final double yMin[] = {.4, .6, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05,
        .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05,
        .05, .05, .6, 0};
    private static final int RECTANGULARITY_LIMIT = 60;
    private static final int ASPECT_RATIO_LIMIT = 75;
    private static final int X_EDGE_LIMIT = 40;
    private static final int Y_EDGE_LIMIT = 60;
    private static final int X_IMAGE_RES = 320;          //X Image resolution in pixels, should be 160, 320 or 640
    private static final double VIEW_ANGLE = 43.5;       //Axis 206 camera
    //final double VIEW_ANGLE = 48;       //Axis M1011 camera
    public static final double HORIZONTAL_ASPECT = 23 / 4.5; // 4.8
    public static final double VERTICAL_ASPECT = 4.0 / 32;
    public static final double ASPECT_RANGE = 1.5;

    /**
     * Computes the estimated distance to a target using the height of the
     * particle in the image. For more information and graphics showing the math
     * behind this approach see the Vision Processing section of the
     * ScreenStepsLive documentation.
     *
     * @param image The image to use for measuring the particle estimated
     * rectangle
     * @param report The Particle Analysis Report for the particle
     * @param outer True if the particle should be treated as an outer target,
     * false to treat it as a center target
     * @return The estimated distance to the target in Inches.
     */
    public static double computeDistance(BinaryImage image, ParticleAnalysisReport report, int particleNumber, boolean outer) throws NIVisionException {
        double rectShort, height;
        int targetHeight;

        rectShort = NIVision.MeasureParticle(image.image, particleNumber, false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_SHORT_SIDE);
        //using the smaller of the estimated rectangle short side and the bounding rectangle height results in better performance
        //on skewed rectangles
        height = Math.min(report.boundingRectHeight, rectShort);
        targetHeight = outer ? 29 : 21;

        return X_IMAGE_RES * targetHeight / (height * 12 * 2 * Math.tan(VIEW_ANGLE * Math.PI / (180 * 2)));
    }

    /**
     * Computes a score (0-100) comparing the aspect ratio to the ideal aspect
     * ratio for the target. This method uses the equivalent rectangle sides to
     * determine aspect ratio as it performs better as the target gets skewed by
     * moving to the left or right. The equivalent rectangle is the rectangle
     * with sides x and y where particle area= x*y and particle perimeter= 2x+2y
     *
     * @param image The image containing the particle to score, needed to
     * performa additional measurements
     * @param report The Particle Analysis Report for the particle, used for the
     * width, height, and particle number
     * @param outer	Indicates whether the particle aspect ratio should be
     * compared to the ratio for the inner target or the outer
     * @return The aspect ratio score (0-100)
     */
    public static double scoreAspectRatio(BinaryImage image, ParticleAnalysisReport report, int particleNumber, boolean outer) throws NIVisionException {
        double rectLong, rectShort, aspectRatio, idealAspectRatio;

        rectLong = NIVision.MeasureParticle(image.image, particleNumber, false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_LONG_SIDE);
        rectShort = NIVision.MeasureParticle(image.image, particleNumber, false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_SHORT_SIDE);
        //idealAspectRatio = outer ? (62/29) : (62/20);	//Dimensions of goal opening + 4 inches on all 4 sides for reflective tape
        idealAspectRatio = outer ? (43 / 32) : (39 / 28);
        //Divide width by height to measure aspect ratio
        aspectRatio = report.boundingRectWidth / (double) report.boundingRectHeight;
        /*if(report.boundingRectWidth > report.boundingRectHeight){
         //particle is wider than it is tall, divide long by short
         aspectRatio = 100*(1-Math.abs((1-((rectLong/rectShort)/idealAspectRatio))));
         } else {
         //particle is taller than it is wide, divide short by long
         aspectRatio = 100*(1-Math.abs((1-((rectShort/rectLong)/idealAspectRatio))));
         }*/
        return aspectRatio;
        //return (Math.max(0, Math.min(aspectRatio, 100.0)));		//force to be in range 0-100
    }

    /**
     * Compares scores to defined limits and returns true if the particle
     * appears to be a target
     *
     * @param scores The structure containing the scores to compare
     * @param outer True if the particle should be treated as an outer target,
     * false to treat it as a center target
     *
     * @return True if the particle meets all limits, false otherwise
     */
    public static boolean scoreCompare(ImagePoint scores, boolean outer) {
        boolean isTarget = true;

        isTarget &= scores.rectangularity > RECTANGULARITY_LIMIT;
        if (outer) {
            isTarget &= scores.aspectRatioOuter > ASPECT_RATIO_LIMIT;
        } else {
            isTarget &= scores.aspectRatioInner > ASPECT_RATIO_LIMIT;
        }
        isTarget &= scores.xEdge > X_EDGE_LIMIT;
        isTarget &= scores.yEdge > Y_EDGE_LIMIT;

        return isTarget;
    }

    /**
     * Computes a score (0-100) estimating how rectangular the particle is by
     * comparing the area of the particle to the area of the bounding box
     * surrounding it. A perfect rectangle would cover the entire bounding box.
     *
     * @param report The Particle Analysis Report for the particle to score
     * @return The rectangularity score (0-100)
     */
    public static double scoreRectangularity(ParticleAnalysisReport report) {
        if (report.boundingRectWidth * report.boundingRectHeight != 0) {
            return 100 * report.particleArea / (report.boundingRectWidth * report.boundingRectHeight);
        } else {
            return 0;
        }
    }

    /**
     * Computes a score based on the match between a template profile and the
     * particle profile in the X direction. This method uses the the column
     * averages and the profile defined at the top of the sample to look for the
     * solid vertical edges with a hollow center.
     *
     * @param image The image to use, should be the image before the convex hull
     * is performed
     * @param report The Particle Analysis Report for the particle
     *
     * @return The X Edge Score (0-100)
     */
    public static double scoreXEdge(BinaryImage image, ParticleAnalysisReport report) throws NIVisionException {
        double total = 0;
        LinearAverages averages;

        NIVision.Rect rect = new NIVision.Rect(report.boundingRectTop, report.boundingRectLeft, report.boundingRectHeight, report.boundingRectWidth);
        averages = NIVision.getLinearAverages(image.image, LinearAverages.LinearAveragesMode.IMAQ_COLUMN_AVERAGES, rect);
        float columnAverages[] = averages.getColumnAverages();
        for (int i = 0; i < (columnAverages.length); i++) {
            if (xMin[(i * (XMINSIZE - 1) / columnAverages.length)] < columnAverages[i]
                    && columnAverages[i] < xMax[i * (XMAXSIZE - 1) / columnAverages.length]) {
                total++;
            }
        }
        total = 100 * total / (columnAverages.length);
        return total;
    }

    /**
     * Computes a score based on the match between a template profile and the
     * particle profile in the Y direction. This method uses the the row
     * averages and the profile defined at the top of the sample to look for the
     * solid horizontal edges with a hollow center
     *
     * @param image The image to use, should be the image before the convex hull
     * is performed
     * @param report The Particle Analysis Report for the particle
     *
     * @return The Y Edge score (0-100)
     *
     */
    public static double scoreYEdge(BinaryImage image, ParticleAnalysisReport report) throws NIVisionException {
        double total = 0;
        LinearAverages averages;

        NIVision.Rect rect = new NIVision.Rect(report.boundingRectTop, report.boundingRectLeft, report.boundingRectHeight, report.boundingRectWidth);
        averages = NIVision.getLinearAverages(image.image, LinearAverages.LinearAveragesMode.IMAQ_ROW_AVERAGES, rect);
        float rowAverages[] = averages.getRowAverages();
        for (int i = 0; i < (rowAverages.length); i++) {
            if (yMin[(i * (YMINSIZE - 1) / rowAverages.length)] < rowAverages[i]
                    && rowAverages[i] < yMax[i * (YMAXSIZE - 1) / rowAverages.length]) {
                total++;
            }
        }
        total = 100 * total / (rowAverages.length);
        return total;
    }

    /**
     * will discover if we are right or left of the field
     *
     * MODIFY METHOD WHEN WE HAVE MULTIPLE VERTICALS GOALS (we can only see one
     * set of vertical and horizontal goals)
     *
     * @param vertGoalX center of mass x normalized of vertical goal
     * @param vertGoalY center of mass y normalized of vertical goal
     * @param horzGoalX center of mass x noramlized of horizontal goal
     * @param horzGoalY center of mass y normalized of horizongtal goal
     * @return true means we are on Right side of field, false means we are on
     * left side
     */
    public static boolean isRightOrLeft(double vertGoalX, double vertGoalY, double horzGoalX, double horzGoalY) {
        vertGoalY = -1 * vertGoalY;
        horzGoalY = -1 * horzGoalY;
        if (horzGoalX > vertGoalX) {
            return true;
        } else if (horzGoalX < vertGoalX) {
            return false;
        } else {
            System.out.println("lol");
            return false;
        }
    }
    
    public static double getDistance(double pixelHeight) {
        return 129166.84601965 * MathUtils.pow(pixelHeight, -1.172464652462);
    }

	/** 
	 * ColorImage- getImage/getTarget/refresh/update
	 * should filter image and get details
	 * http://www.spectrum3847.org/frc2012api/edu/wpi/first/wpilibj/image/ColorImage.html
	 */
	public ColorImage getImage() throws AxisCameraException, NIVisionException {
	  ColorImage image=new HSLImage();
	  if (true) {//getImageFn.call1(image.image) == 0) {
	    image.free();
	    throw new AxisCameraException("No image available");
	  }
	  return image;
	}
	
	/*public void update(ColorImage image){
	  BinaryImage masked;
	  BinaryImage hulled;
	  BinaryImage filtered;
	  ParticleAnalysisReport[] all;
	  //targets.removeAllElements();
	  try {
	    //masked=image.thresholdRGB(redLow,redHigh,greenLow,greenHigh,blueLow,blueHigh);
	    hulled=masked.convexHull(true);
	    filtered=hulled.removeSmallObjects(true,2);
	    all=filtered.getOrderedParticleAnalysisReports(10);
	    image.free();
	    filtered.free();
	    hulled.free();
	    masked.free();
	    for (int i=0; i < all.length; i++) {
	      if (all[i].particleArea > areaThreshold) {
	        double rectangularityScore=rectangularityScore(all[i].particleArea,all[i].boundingRectWidth,all[i].boundingRectHeight);
	        double aspectRatioScore=aspectRatioScore(all[i].boundingRectWidth,all[i].boundingRectHeight);
	        if (rectangularityScore > rectangularityThreshold && aspectRatioScore > aspectThreshold) {
	          targets.addElement(all[i]);
	        }
	      }
	    }
	  }
	 catch (NIVisionException e) {
	    Output.queue("TargetFinder:: Failed to update()");
	    e.printStackTrace();
	  }
	  Output.queue("[TargetFinder] " + targets.size());
	}
	//not sure if Timer.delay would free up the thread or what
	public Target getTarget() throws NIVisionException, AxisCameraException {
	  while (true) {
	    ColorImage image=camera.getImage();
	    try {
	      return Target.getTarget(image,position,firstColor,secondColor);
	    }
	 catch (    Exception e) {
	      e.printStackTrace();
	    }
	 finally {
	      if (image != null) {
	        image.free();
	      }
	      image=null;
	    }
	    Timer.delay(.001);
	  }
	}
	 
	public void refreshTargets(){
	  try {
	    ColorImage image=camera.getImage();
	    //thresholdRGB(int redLow, int redHigh, int greenLow, int greenHigh, int blueLow, int blueHigh) yayay
	    BinaryImage thresholdImage=image.thresholdRGB(25,255,0,45,0,47);
	    BinaryImage bigObjectsImage=thresholdImage.removeSmallObjects(false,2);
	    BinaryImage convexHullImage=bigObjectsImage.convexHull(false);
	    BinaryImage filteredImage=convexHullImage.particleFilter(cc);
	    ParticleAnalysisReport[] reports=filteredImage.getOrderedParticleAnalysisReports();
	    System.out.println("\n\nFound " + filteredImage.getNumberParticles() + " boxes");
	    for (int i=0; i < reports.length; i++) {
	      ParticleAnalysisReport r=reports[i];
	      System.out.println("Square: ");
	      System.out.println("Center x: " + r.center_mass_x);
	      System.out.println("Center Y: " + r.center_mass_y);
	      System.out.println("Box Area: " + r.particleArea + " px^2");
	    }
	    System.out.println("Match Size: " + filteredImage.getOrderedParticleAnalysisReports(1)[0].particleToImagePercent);
	    filteredImage.free();
	    convexHullImage.free();
	    bigObjectsImage.free();
	    thresholdImage.free();
	    image.free();
	  }
	 catch (  Exception ex) {
	  }
	}*/
	 

    /**
     * Find the corresponding horiz/vert goal for the passed goal (point), with
     * aspect ratio aspect. Returns the index of the corresponding point.
     */
    public static int findCorrespondingGoal(ImagePoint[] points, int point, double aspect) {
        ImagePoint thePoint = points[point];
        double correspondingAspect = aspect == HORIZONTAL_ASPECT
                ? VERTICAL_ASPECT : HORIZONTAL_ASPECT;

        int leastIndex = -1;
        double leastDistance = Double.MAX_VALUE;
        for (int i = 0; i < points.length; i++) {
            if (i == point) {
                continue;
            }
            ImagePoint curGoal = points[i];
            if (curGoal.isMarked()) {
                continue;
            }
            double distance = ImagePoint.calculateDistance(curGoal, thePoint);

            if (curGoal.hasAspect(correspondingAspect, ASPECT_RANGE)
                    && distance < leastDistance) {
                leastIndex = i;
                leastDistance = distance;
            }
        }

        if (leastIndex != -1) {
            points[leastIndex].setMarker(point);
            thePoint.setMarker(leastIndex);
        }

        return leastIndex;
    }

    public static void determineGoals(ImagePoint[] points) {
        boolean markedLeft = false;
        boolean markedRight = false;
        int leftCount = 0;
        int rightCount = 0;
        for (int i = 0; i < points.length; i++) {
            ImagePoint curGoal = points[i];
            double aspect = curGoal.getAspectRatio();
            boolean isHoriz = curGoal.hasAspect(HORIZONTAL_ASPECT, ASPECT_RANGE);
            
            curGoal.setOrientation(isHoriz ? ImagePoint.HORIZONTAL : ImagePoint.VERTICAL);

            int correspIndex = ImageUtils.findCorrespondingGoal(points, i,
                    isHoriz ? HORIZONTAL_ASPECT : VERTICAL_ASPECT);

            //System.out.print(curGoal + " (" + (isHoriz ? "Horiz" : "Vert") + ") => ");
            if (correspIndex != -1) {
                ImagePoint correspGoal = points[correspIndex];
                
                if (isHoriz) {
                    correspGoal.setHot(true);
                    curGoal.setHot(true);
                }
                
                /*System.out.println(correspGoal + " ("
                        + (correspGoal.hasAspect(HORIZONTAL_ASPECT, ASPECT_RANGE)
                        ? "Horiz" : "Vert") + ")");*/

                if (isHoriz) {
                    if (correspGoal.getX() > curGoal.getX()) {
                        curGoal.setSide(ImagePoint.LEFT);
                        correspGoal.setSide(ImagePoint.LEFT);
                        markedLeft = true;
                        leftCount++;
                    } else {
                        curGoal.setSide(ImagePoint.RIGHT);
                        correspGoal.setSide(ImagePoint.RIGHT);
                        markedRight = true;
                        rightCount++;
                    }
                } else {
                    if (correspGoal.getX() > curGoal.getX()) {
                        curGoal.setSide(ImagePoint.RIGHT);
                        correspGoal.setSide(ImagePoint.RIGHT);
                        markedRight = true;
                        rightCount++;
                    } else {
                        curGoal.setSide(ImagePoint.LEFT);
                        correspGoal.setSide(ImagePoint.LEFT);
                        markedLeft = true;
                        leftCount++;
                    }
                }
            } else if (!curGoal.isHot()) {
                //System.out.println("Cannot find corresponding goal :( nawt hawt");
                curGoal.setHot(false);
                if (markedLeft) {
                    // current goal must be orphaned right
                    curGoal.setSide(ImagePoint.RIGHT);
                } else if (markedRight) {
                    curGoal.setSide(ImagePoint.LEFT);
                } else if (points.length == 2) {
                    if (i == 0) {
                        if (points[0].getX() > points[1].getX()) {
                            curGoal.setSide(ImagePoint.RIGHT);
                            points[1].setSide(ImagePoint.LEFT);
                        } else {
                            curGoal.setSide(ImagePoint.LEFT);
                            points[1].setSide(ImagePoint.RIGHT);
                        }
                    }
                } else {
                    curGoal.setSide(ImagePoint.INVALID);
                }
            }
        }
    }
}
