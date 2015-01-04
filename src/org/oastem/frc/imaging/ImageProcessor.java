package org.oastem.frc.imaging;

import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

public class ImageProcessor {
    /*private void imageProcessing() {
        try {
            // 43:32
            //ColorImage image = camera.getImage();     // comment if using stored images
            ColorImage image;                           // next 2 lines read image from flash on cRIO
            image = camera.getImage();
            //image = new RGBImage("/testImage.jpg");		// get the sample image from the cRIO flash
            BinaryImage thresholdImage = image.thresholdRGB(0, 50, 100, 255, 0, 50);
            //BinaryImage thresholdImage = image.thresholdHSV(60, 100, 90, 255, 20, 255);   // keep only red objects
            //thresholdImage.write("/threshold.bmp");
            BinaryImage convexHullImage = thresholdImage.convexHull(false);          // fill in occluded rectangles
            //convexHullImage.write("/convexHull.bmp");
            BinaryImage filteredImage = convexHullImage.particleFilter(cc);           // filter out small particles
            //filteredImage.write("/filteredImage.bmp");
            //SmartDashboard.

            //iterate through each particle and score to see if it is a target
            ImagePoint scores[] = new ImagePoint[filteredImage.getNumberParticles()];
            for (int i = 0; i < scores.length; i++) {
                ParticleAnalysisReport report = filteredImage.getParticleAnalysisReport(i);
                scores[i] = new ImagePoint(i, report.center_mass_x_normalized, report.center_mass_y_normalized);

                scores[i].rectangularity = ImageUtils.scoreRectangularity(report);
                scores[i].aspectRatioOuter = ImageUtils.scoreAspectRatio(filteredImage, report, i, true);
                scores[i].aspectRatioInner = ImageUtils.scoreAspectRatio(filteredImage, report, i, false);
                scores[i].xEdge = ImageUtils.scoreXEdge(thresholdImage, report);
                scores[i].yEdge = ImageUtils.scoreYEdge(thresholdImage, report);

                if (scores[i].aspectRatioOuter > 1.0) {
                    // Width > height, it's the horizontal goal
                    horzCenterMassX = report.center_mass_x_normalized;
                    horzCenterMassY = report.center_mass_y_normalized;
                    System.out.println(i + ": HorizGoal cx: " + report.center_mass_x_normalized + " cy: "
                            + report.center_mass_y_normalized);

                } else {
                    // Height > width, it's the vertical goal
                    vertCenterMassX = report.center_mass_x_normalized;
                    vertCenterMassY = report.center_mass_y_normalized;
                    System.out.println(i + ": VertGoal cx: " + report.center_mass_x_normalized + " cy: "
                            + report.center_mass_y_normalized
                            + " h: " + (report.boundingRectHeight / (double) report.imageHeight));
                    vertHeight = report.boundingRectHeight;
                    // This function seems to work only between 2-5 meters.
                    //double dist = 129166.84601965 * ( MathUtils.pow(report.boundingRectHeight, -1.172464652462));
                    System.out.println(report.boundingRectHeight);
                    //System.out.println( (347.5 * report.boundingRectHeight) / 92.0 );
                }



                // The following code will only store the initial readings.
                /*
                 * if (massCenters == null) { // We'll only take in the initial
                 * reading. massCenters = scores; }//
                 */

                // in discovering distance. ...
                // y = distance to target (to find)
                // x = sample distance (i.e. 10 meters)
                // h = sample height of target (corresponding to sample distance)
                // z = height of target
                // y = hz / x

                // h = 92 px
                // x = 347.5 cm
                // ----------------------------------
                // DISTANCE til full view of vision targets: 78.9 inches == 200 cm!!! 2 m
                // x = FOV/140 //credits to Spring
                // x = distance from wall to robot
                // FOV = bounding rect width --> width from edge of horzgoal to other edge 
                // TEST THIS
                // Put robot 2 meters from the vision targets and measure the pixel width of the image
                // (or boundingRectWidth) (from the edges of the goals



                /*
                 * if(scoreCompare(scores[i], false)) {
                 * System.out.println("particle: " + i + "is a High Goal
                 * centerX: " + report.center_mass_x_normalized + "centerY: " +
                 * report.center_mass_y_normalized);
                 * System.out.println("Distance: " +
                 * computeDistance(thresholdImage, report, i, false)); } else if
                 * (scoreCompare(scores[i], true)) {
                 * System.out.println("particle: " + i + "is a Middle Goal
                 * centerX: " + report.center_mass_x_normalized + "centerY: " +
                 * report.center_mass_y_normalized);
                 * System.out.println("Distance: " +
                 * computeDistance(thresholdImage, report, i, true)); } else {
                 * System.out.println("particle: " + i + "is not a goal centerX:
                 * " + report.center_mass_x_normalized + "centerY: " +
                 * report.center_mass_y_normalized); }
                
                //System.out.println("rect: " + scores[i].rectangularity + "ARinner: " + scores[i].aspectRatioInner);
                //System.out.println("ARouter: " + scores[i].aspectRatioOuter + "xEdge: " + scores[i].xEdge + "yEdge: " + scores[i].yEdge);	
            }

            massCenters = scores;
            ImageUtils.determineGoals(scores);

            for (int i = 0; i < scores.length; i++) {
                Point cur = scores[i];
                String o = cur.getOrientation() == Point.INVALID ? "Invalid"
                        : (cur.getOrientation() == Point.HORIZONTAL ? "Horiz" : "Vert");
                String s = cur.getSide() == Point.INVALID ? "Invalid"
                        : (cur.getSide() == Point.LEFT ? "Left" : "Right");

                String h = cur.isHot() ? "Hot" : "NotHot";

                if (cur.isHot()) {
                    currentHotGoal = cur.getSide();
                }

                System.out.println("Goal " + i + ": " + o + " " + s + " " + h);
            }

            //public void checkRegion(
            //System.out.println(ImageUtils.isRightOrLeft(vertCenterMassX, vertCenterMassY, horzCenterMassX, horzCenterMassY) + "");

            /**
             * all images in Java must be freed after they are used since they
             * are allocated out of C data structures. Not calling free() will
             * cause the memory to accumulate over each pass of this loop.
             //
            filteredImage.free();
            convexHullImage.free();
            thresholdImage.free();
            image.free();
            System.out.println("-------");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        lastUpdate = System.currentTimeMillis();
    }*/
}
