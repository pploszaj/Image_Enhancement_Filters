import java.io.*;
import java.util.Objects;
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

        int[][] mirrorFramedArray = new int[numRows + 4][numCols + 4];
        int[][] avgArray = new int[numRows + 4][numCols + 4];
        int[][] CPavgArray = new int[numRows + 4][numCols + 4];
        int[] avg_histArray = new int[maxVal + 1];
        int[] CPavg_histArray = new int[maxVal + 1];


        Enhancement enhancement = new Enhancement(numRows, numCols, minVal, maxVal);
        enhancement.loadImage(inFile, mirrorFramedArray);
        enhancement.mirrorFraming(mirrorFramedArray);

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(outFile1, true));
            writer.println("Below is the image reformatted mirrorFramedAry");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }

        enhancement.imgReformat(mirrorFramedArray, outFile1);

        // Process choice
        if (Objects.equals(opChoice, "1")) {
            enhancement.computeAvg5x5(mirrorFramedArray, avgArray, debugFile);
            try {
                PrintWriter writer = new PrintWriter(new FileWriter(outFile1, true));
                writer.println("Below is the reformatted image of the result of 5x5 averaging on the input image");
                writer.close();
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file: " + e.getMessage());
            }
            enhancement.imgReformat(avgArray, outFile1);
            //print reformatted avgAry to outfile1
            try {
                PrintWriter writer = new PrintWriter(new FileWriter(outFile2, true));
                writer.println(numRows + " " + numCols + " " + minVal + " " + maxVal);
                for (int i = 2; i < avgArray.length - 2; i++) {
                    for (int j = 2; j < avgArray[i].length - 2; j++) {
                        writer.print(avgArray[i][j] + " ");
                    }
                    writer.println();
                }
                writer.close();
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file: " + e.getMessage());
            }
            enhancement.computeHist(avgArray, avg_histArray, debugFile);
            enhancement.dispHist(avg_histArray, outFile1);
            try {
                PrintWriter writer = new PrintWriter(new FileWriter(outFile3, true));
                writer.println(numRows + " " + numCols + " " + minVal + " " + maxVal);
                writer.close();
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file: " + e.getMessage());
            }
            enhancement.printHist(avg_histArray, outFile3, debugFile);
        } else if (Objects.equals(opChoice, "2")) {
            enhancement.cornerPreserveAvg(mirrorFramedArray, CPavgArray, debugFile);
            try {
                PrintWriter writer = new PrintWriter(new FileWriter(outFile1, true));
                writer.println("Below is the reformatted image of the result of corner-preserve averaging on the input image.");
                writer.close();
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file: " + e.getMessage());
            }
            enhancement.imgReformat(CPavgArray, outFile1);
            //print reformatted cpavgarray to outfile1
            try {
                PrintWriter writer = new PrintWriter(new FileWriter(outFile2, true));
                writer.println(numRows + " " + numCols + " " + minVal + " " + maxVal);
                for (int i = 2; i < CPavgArray.length - 2; i++) {
                    for (int j = 2; j < CPavgArray[i].length - 2; j++) {
                        writer.print(CPavgArray[i][j] + " ");
                    }
                    writer.println();
                }
                writer.close();
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file: " + e.getMessage());
            }
            enhancement.computeHist(CPavgArray, CPavg_histArray, debugFile);
            enhancement.dispHist(CPavg_histArray, outFile1);
            try {
                PrintWriter writer = new PrintWriter(new FileWriter(outFile3, true));
                writer.println(numRows + numCols + minVal + maxVal);
                writer.close();
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file: " + e.getMessage());
            }
            enhancement.printHist(CPavg_histArray, outFile3, debugFile);
        }
    }
}