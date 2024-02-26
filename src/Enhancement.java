import java.io.*;
import java.util.Scanner;

public class Enhancement {
    private int numRows;
    private int numCols;
    private int minVal;
    private int maxVal;
    private int[][] mirrorFramedArray; // a 2D array, size numRows + 4 by numCols + 4. dynamically allocate (Initialized to 0).
    private int[][] avgArray;
    private int[][] CPavgArray;
    private int[][][] mask = new int[8][5][5];
    private int[] avg_histArray;
    private int[] CPavg_histArray;

    public Enhancement(int numRows, int numCols, int minVal, int maxVal) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.minVal = minVal;
        this.maxVal = maxVal;

        mask[0] = new int[][]{
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 1, 1, 1, 0},
                {1, 1, 1, 1, 1}
        };

        mask[1] = new int[][]{
                {1, 0, 0, 0, 0},
                {1, 1, 0, 0, 0},
                {1, 1, 1, 0, 0},
                {1, 1, 0, 0, 0},
                {1, 0, 0, 0, 0}
        };

        mask[2] = new int[][]{
                {1, 1, 1, 1, 1},
                {0, 1, 1, 1, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };

        mask[3] = new int[][]{
                {0, 0, 0, 0, 1},
                {0, 0, 0, 1, 1},
                {0, 0, 1, 1, 1},
                {0, 0, 0, 1, 1},
                {0, 0, 0, 0, 1}
        };

        mask[4] = new int[][]{
                {1, 1, 1, 0, 0},
                {1, 1, 1, 0, 0},
                {1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };

        mask[5] = new int[][]{
                {0, 0, 1, 1, 1},
                {0, 0, 1, 1, 1},
                {0, 0, 1, 1, 1},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };

        mask[6] = new int[][]{
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 1, 1, 1},
                {0, 0, 1, 1, 1},
                {0, 0, 1, 1, 1}
        };

        mask[7] = new int[][]{
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {1, 1, 1, 0, 0},
                {1, 1, 1, 0, 0},
                {1, 1, 1, 0, 0}
        };

    }

    public void loadImage(String inFile, int[][] mirrorFramedArray){
        try {
            Scanner scanner = new Scanner(new File(inFile));
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            for (int i = 2; i < numRows + 2; i++) {
                for (int j = 2; j < numCols + 2; j++) {
                    if (scanner.hasNextInt()) {
                        mirrorFramedArray[i][j] = scanner.nextInt();
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }
    public void mirrorFraming(int[][] mirrorFramedArray){
        for (int col = 0; col < numCols + 4; col++) {
            mirrorFramedArray[0][col] = mirrorFramedArray[2][col];
            mirrorFramedArray[1][col] = mirrorFramedArray[2][col];
        }

        for (int col = 0; col < numCols + 4; col++) {
            mirrorFramedArray[numRows + 2][col] = mirrorFramedArray[numRows + 1][col];
            mirrorFramedArray[numRows + 3][col] = mirrorFramedArray[numRows + 1][col];
        }

        for (int row = 0; row < numRows + 4; row++) {
            mirrorFramedArray[row][0] = mirrorFramedArray[row][2];
            mirrorFramedArray[row][1] = mirrorFramedArray[row][2];
        }

        for (int row = 0; row < numRows + 4; row++) {
            mirrorFramedArray[row][numCols + 2] = mirrorFramedArray[row][numCols + 1];
            mirrorFramedArray[row][numCols + 3] = mirrorFramedArray[row][numCols + 1];
        }
    }
    public int convolution5x5(int[][] mirrorFramedArray, int i, int j, int[][] mask, String debugFile){
        int result = 0;

        try {
            PrintWriter debugWriter = new PrintWriter(new FileWriter(debugFile, true));
            debugWriter.println("Entering convolution5x5 method");

            for (int r = -2; r <= 2; r++) {
                for (int c = -2; c <= 2; c++) {
                    result += mask[r + 2][c + 2] * mirrorFramedArray[i + r][j + c];
                }
            }

            debugWriter.println("Before leaving convolution5x5 method(): i= " + i + "; j= " + j + "; result= " + result);
            debugWriter.close();
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the debug file: " + e.getMessage());
        }

        return result;
    }
    public void cornerPreserveAvg(int[][] frameArray, int[][] outArray, String debugFile){
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(debugFile, true));
            writer.println("entering cornerPreserveAvg method");


            int newMax = frameArray[2][2];
            int newMin = frameArray[2][2];
            for (int i = 2; i < numRows + 2; i++) {
                for (int j = 2; j < numCols + 2; j++) {
                    int minAvg = frameArray[i][j];
                    int minDiff = 9999;
                    for (int maskIndex = 0; maskIndex < 8; maskIndex++) {
                        int result = convolution5x5(frameArray, i, j, mask[maskIndex], debugFile) / 9;
                        int diff = Math.abs(result - frameArray[i][j]);
                        if (diff < minDiff) {
                            minDiff = diff;
                            minAvg = result;
                        }
                    }
                    outArray[i][j] = minAvg;
                    if (newMax < minAvg) {
                        newMax = minAvg;
                    }
                    if (newMin > minAvg) {
                        newMin = minAvg;
                    }
                }
            }
            maxVal = newMax;
            minVal = newMin;

            writer.println("In cornerPreserveAvg(): newMax=" + newMax + " newMin=" + newMin);
            writer.println("Leaving cornerPreserveAvg() method");
            writer.close();
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file: " + e.getMessage());
            }
    }
    public int avg5x5(int[][] mirrorFramedArray, int i, int j){
        int sum = 0;
        int count = 0;

        for (int row = i - 2; row <= i + 2; row++) {
            for (int col = j - 2; col <= j + 2; col++) {
                if (row >= 0 && row < numRows + 4 && col >= 0 && col < numCols + 4) {
                    sum += mirrorFramedArray[row][col];
                    count++;
                }
            }
        }

        return count > 0 ? sum / count : 0;
    }
    public void computeAvg5x5(int[][] mirrorFramedArray, int[][] avgArray, String debugFile){
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(debugFile, true));
            writer.println("entering computeAvg5x5 method");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }

        int newMax = mirrorFramedArray[2][2];
        int newMin = mirrorFramedArray[2][2];

        for(int i = 2; i < numRows + 2; i++){
            for(int j = 2; j < numCols + 2; j++){
                avgArray[i][j] = avg5x5(mirrorFramedArray, i, j);
                if (newMax < avgArray[i][j]){
                    newMax = avgArray[i][j];
                }
                if(newMin > avgArray[i][j]){
                    newMin = avgArray[i][j];
                }
            }
        }
        maxVal = newMax;
        minVal = newMin;
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(debugFile, true));
            writer.println("In computeAvg5x5 newMax= " + maxVal + " newMin= " + minVal);
            writer.println("Leaving computeAvg5x5 method");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }

    }
    public void computeHist(int[][] inArray, int[] histArray, String debugFile){
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(debugFile, true));
            writer.println("entering computeHist method");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
        for (int i = 2; i < numRows + 2; i++){
            for(int j = 2; j < numCols + 2; j++) {
                histArray[inArray[i][j]]++;
            }
        }
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(debugFile, true));
            writer.println("Leaving computeHist method");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
    public void dispHist(int[] avghistArray, String outFile1){
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(outFile1, true));

            for (int i = 0; i <= maxVal; i++) {
                writer.print(i + " (" + avghistArray[i] + "):");
                for (int j = 0; j < avghistArray[i]; j++) {
                    writer.print("+");
                }
                writer.println();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
    public void printHist(int[] histArray, String histFile, String deBugFile){
        try {
            // Append to the debug file indicating the start of the method
            PrintWriter debugWriter = new PrintWriter(new FileWriter(deBugFile, true));
            debugWriter.println("Entering printHist method");
            debugWriter.close();

            PrintWriter histWriter = new PrintWriter(new FileWriter(histFile));
            histWriter.println(numRows + " " + numCols + " " + minVal + " " + maxVal);

            for (int index = 0; index <= maxVal; index++) {
                histWriter.println(index + " " + histArray[index]);
            }
            histWriter.close();

            debugWriter = new PrintWriter(new FileWriter(deBugFile, true));
            debugWriter.println("Leaving printHist method");
            debugWriter.close();

        } catch (IOException e) {
            System.err.println("An error occurred while writing to the files: " + e.getMessage());
        }
    }

    public void imgReformat(int[][] inArray, String outFile){
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(outFile, true));
            writer.println(numRows + " " + numCols + " " + minVal + " " + maxVal);

            String str = Integer.toString(maxVal);
            int width = str.length();

            for (int i = 2; i < numRows + 2; i++) {
                for (int j = 2; j < numCols + 2; j++) {
                    writer.print(inArray[i][j]);
                    String valueStr = Integer.toString(inArray[i][j]);
                    int valueWidth = valueStr.length();
                    while(valueWidth <= width) {
                        writer.print(" ");
                        valueWidth++;
                    }
                }
                writer.println();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the output file: " + e.getMessage());
        }
    }
}
