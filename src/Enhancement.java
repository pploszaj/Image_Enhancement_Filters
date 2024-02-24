import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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

    }

    public void loadImage(String inFile, int[][] mirrorFramedArray){}
    public void mirrorFraming(int[][] mirrorFramedArray){}
    public int convolution5x5(int[][] mirrorFramedArray, int i, int j, int[][] mask){}
    public void cornerPreserveAvg(int[][] frameArray, int[][] outArray, String debugFile){
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(debugFile, true));
            writer.println("entering cornerPreserveAvg method");


            int newMax = frameArray[2][2];
            int newMin = frameArray[2][2];
            for (int i = 2; i < numRows + 2; i++) {
                for (int j = 2; j < numCols + 2; j++) {
                    int maskIndex = 0;
                    int minAvg = frameArray[i][j];
                    int minDiff = 9999;
                    for (int maskIndex = 0; maskIndex < 8; maskIndex++) {
                        int result = convolution5x5(frameArray, i, j, mask[maskIndex]) / 9;
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
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file: " + e.getMessage());
            }
    }
    public int avg5x5(int[][] mirrorFramedArray, int i, int j){}
    public void computeAvg5x5(int[][] mirrorFramedArray, int[][] avgArray, String debugFile){
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(debugFile));
            writer.println("entering computeAvg5x5 method");
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
            PrintWriter writer = new PrintWriter(new FileWriter(debugFile));
            writer.println("In computeAvg5x5 newMax= " + maxVal + " newMin= " + minVal);
            writer.println("Leaving computeAvg5x5 method");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }

    }
    public void computeHist(int[][] inArray, int[] histArray, String debugFile){
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(debugFile, true));
            writer.println("entering computeHist method");
            for (int val : histArray) {
                writer.print(val + " ");
                writer.println();
            }
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
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
    public void dispHist(){}
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

    public void imgReformat(int[][] mirrorFramedArray, String outFile1){}
}
