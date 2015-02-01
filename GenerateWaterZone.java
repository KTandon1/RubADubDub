import java.awt.event.*;
import java.awt.*;
import java.applet.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.*;
public class GenerateWaterZone extends JFrame {

    //display
    protected BufferedImage img2;

    public static void main(String[] args) { //main
        try {
            String backgroundDir = args[0];
            String backgroundShampooDir = args[1];
            int displayResult = Integer.parseInt(args[2]);
            if (displayResult != 0 && displayResult != 1) {
                throw new Exception("Bad Input");
            }
            try {
                GenerateWaterZone bic = new GenerateWaterZone(backgroundDir, backgroundShampooDir, displayResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("\nUSAGE: java GenerateWaterZone [/path/to/background/files] [/path/to/background/object/dir] [0/1 where 1=display result]");
        }
    }

    public GenerateWaterZone(String backgroundDir, String backgroundShampooDir, int displayResult) {
        super("Image Displayer"); //create frame

        //display if needed
        if (displayResult == 1) {

            //run display
            setSize(330, 250);
            setDefaultCloseOperation(EXIT_ON_CLOSE); //How frame is closed
            setResizable(true);
            setVisible(true);//frame visible

            segmentation(backgroundDir, backgroundShampooDir, displayResult);

        } else {


            //quit
            System.exit(0);

            segmentation(backgroundDir, backgroundShampooDir, displayResult);

        }
    }

    protected void segmentation(String backgroundDir, String backgroundShampooDir, int displayResult) {

        //read in and store background image
        System.out.println("Loading Background Image...");
        double[][] backgroundImage = new double[320][240];
        double[][] backgroundShampooImage = new double[320][240];
        //load background image
        String fileName = backgroundDir + "/background.csv";
        Scanner fromFile = OpenFile.openToRead(new File(fileName));
        while (fromFile.hasNext()) {
            String temp = fromFile.nextLine();
            int x = Integer.parseInt(temp.substring(0, temp.indexOf(",")));
            int y = Integer.parseInt(temp.substring(temp.indexOf(",") + 1, temp.lastIndexOf(",")));
            double z = Double.parseDouble(temp.substring(temp.lastIndexOf(",") + 1, temp.length()));
            backgroundImage[y][x] = z;
        }

        System.out.println("Background image loaded.");
        //load shampoo image
        System.out.println("Loading Shampoo Image" + " ...");
        String filePath  = backgroundShampooDir + "/background.csv";
        fromFile = OpenFile.openToRead(new File(filePath));
        while (fromFile.hasNext()) {
            String temp = fromFile.nextLine();
            int x = Integer.parseInt(temp.substring(0, temp.indexOf(",")));
            int y = Integer.parseInt(temp.substring(temp.indexOf(",") + 1, temp.lastIndexOf(",")));
            double z = Double.parseDouble(temp.substring(temp.lastIndexOf(",") + 1, temp.length()));
            backgroundShampooImage[y][x] = z;
        }


        System.out.println();
        double[][] waterZone = Utility.subtractBackground(backgroundImage, backgroundShampooImage);
        filePath = backgroundShampooDir + "/waterZone.csv";
        Utility.d2ArrToCSV(waterZone, filePath);

        //display if needed
        if (displayResult == 1) {
            img2 = Utility.d2ArrToBufferedImage(waterZone);
            repaint();
            Utility.goToSleep();
        }
        System.out.println("...");
        System.out.println("Completed");
    }

    public void paint(Graphics g) {
        g.drawImage(img2, 0, 0, 320, 240, null);
    }
}