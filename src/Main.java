import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length < 6) {
            System.out.println("Incorrect number of arguments");
            return;
        }

        String inFile = args[0];
        String opChoice = args[1];
        String outFile1 = args[2];
        String outFile2 = args[3];
        String outFile3 = args[4];
        String debugFile = args[5];

        int numRows = 0;
        int numCols = 0;
        int minVal = 0;
        int maxVal = 0;

        int[][] mirrorFramedArray = new int[numRows + 4][numCols + 4];
        int[][] avgArray = new int[numRows + 4][numCols + 4];
        int[][] CPavgArray = new int[numRows + 4][numCols + 4];
        int[] avg_histArray = new int[maxVal + 1];
        int[] CPavg_histArray = new int[maxVal + 1];

        try {
            Scanner scanner = new Scanner(new File(inFile));
            numRows = scanner.nextInt();
            numCols = scanner.nextInt();
            minVal = scanner.nextInt();
            maxVal = scanner.nextInt();
            scanner.close();
        } catch (FileNotFoundException e){
            System.out.println("Error: Input file not found. " + e.getMessage());
        }


        Enhancement enhancement = new Enhancement(numRows, numCols, minVal, maxVal);
        enhancement.loadImage(inFile, mirrorFramedArray);
        enhancement.mirrorFraming(mirrorFramedArray);

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(outFile1));
            writer.println("Below is the image reformatted mirrorFramedAry");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }

        enhancement.imgReformat(mirrorFramedArray, outFile1);

        try {
            // Process choice
            if (opChoice == "1") {
                enhancement.computeAvg5x5(/* parameters */);
                try {
                    PrintWriter writer = new PrintWriter(new FileWriter(outFile1));
                    writer.println("Below is the image reformatted mirrorFramedAry");
                } catch (IOException e) {
                    System.out.println("An error occurred while writing to the file: " + e.getMessage());
                }
                enhancement.imgReformat(enhancement.avgArray, outFile1);
                writeImagePropertiesAndArray(outFile2Path, enhancement.numRows, enhancement.numCols, enhancement.minVal, enhancement.maxVal, enhancement.avgAry);
                enhancement.computeHist(enhancement.avgAry, enhancement.Avg_histAry, debugFile);
                enhancement.dispHist(enhancement.Avg_histAry, outFile1Path);
                printHist(outFile3Path, enhancement.Avg_histAry, debugFile);
            } else if (opChoice == "2") {
                enhancement.cornerPreserveAvg(/* parameters */);
                try {
                    PrintWriter writer = new PrintWriter(new FileWriter(outFile1));
                    writer.println("Below is the image reformatted mirrorFramedAry");
                } catch (IOException e) {
                    System.out.println("An error occurred while writing to the file: " + e.getMessage());
                }
                enhancement.imgReformat(enhancement.CPavgAry, outFile1Path);
                writeImagePropertiesAndArray(outFile2Path, enhancement.numRows, enhancement.numCols, enhancement.minVal, enhancement.maxVal, enhancement.CPavgAry);
                enhancement.computeHist(enhancement.CPavgAry, enhancement.CPavg_histAry, debugFile);
                enhancement.dispHist(enhancement.CPavg_histAry, outFile1Path);
                printHist(outFile3Path, enhancement.CPavg_histAry, debugFile);
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    }
}