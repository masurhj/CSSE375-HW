package main;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;


import javax.swing.JTextArea;

import main.Sorter.BubbleSorter;
import main.Sorter.Sorter;

public class FileManager {
    private int fileId = 1; // to make separate files of each request to save data
    private FileWriter outStream; // to write calcs, etc.

    public boolean openTheFile(String inputValue, int maxNumber, JTextArea display) {
        boolean fileOpenError = false;
        System.out.println("Opening next file! -------------");
        String fileName = "datafiles";
        String maxNumberValue = String.valueOf(maxNumber);
        if (FlagContainer.insolublesFlag)
            fileName = "datafiles/insolubles" + inputValue + "&" + maxNumberValue + "gameResults"
                    + String.valueOf(fileId) + ".txt";
        else if (FlagContainer.extraOpsFlag) // show in header that this was using extra operations
            fileName = "datafiles/extraOps" + inputValue + "&" + maxNumberValue + "gameResults" + String.valueOf(fileId)
                    + ".txt";
        else
            fileName = "datafiles/stdOps" + inputValue + "&" + maxNumberValue + "gameResults" + String.valueOf(fileId)
                    + ".txt";
        File tmpDir = new File(fileName);
        if (tmpDir.exists()) { // Ask if they want to write over this
            System.out.println("A file with name '" + fileName + "' already exists! Overwriting!");
        }
        try { // creating the output file in directory called "datafiles" :
            System.out.println(fileName);
            outStream = new FileWriter(fileName);
            fileId = fileId + 1; // to enable successive tests in one run
        } catch (FileNotFoundException e1) {
            display.setText("You have no datafiles dir, or another copy of this file open elsewhere, Try again!\n");
            System.out.println("You have another copy of this file open elsewhere, Try again!");
            fileOpenError = true;
        } // catch
        catch (IOException e1) {
            display.setText("IOERROR: " + e1.getMessage() + "\n");
            fileOpenError = true;
            e1.printStackTrace();
        } // catch
        return (!fileOpenError);
    } // openTheFile

    public void clearFiles(JTextArea display){
        System.out.println("Clearing Files! -------------");
        String fileName = "datafiles";
        File tmpDir = new File(fileName);
        if (tmpDir.exists()) {
            try{
                for(File f: tmpDir.listFiles()){
                    f.delete();
                }
            } catch (Exception e){
                System.out.println("ERROR: " + e);
            }
        }
    }

    public int getFileCount(){
        String fileName = "datafiles";
        File tmpDir = new File(fileName);
        if (tmpDir.exists()) {
            return tmpDir.listFiles().length;
        }
        return 0;
    }

    // writes a detail line if flag set to do this vs. displaying
    public void writeDetail(String detailLine, JTextArea display) {
        try {
            outStream.write(detailLine);
        } catch (IOException e) {
            display.setText("IOERROR: " + e.getMessage() + "\n");
            e.printStackTrace();
        } // catch

    } // writeDetail

    public FileWriter getOutStream() {
        return this.outStream;
    }

    // summarize stats for the combos we tried
    public void writeSummary(JTextArea display, int statsPointer, int statsMax, int statsArray[][]) {
        writeDetail("\n--------------------------------\n\n", display);
        writeDetail("Results sorted by difficulty = least # of non-duplicated answers first\n", display);
        writeDetail("Fields are:\n", display);
        writeDetail("1 - Relative number in array, 2 - combo ID, 3 - # of answers\n", display);
        writeDetail("4 - # of duplicates, 5 - # of non-duplicated answers\n\n", display);

        if (statsPointer < statsMax) // sort and write table
        {
            Sorter sorter = new BubbleSorter();
            sorter.sort(statsPointer, statsArray); // first, put them in ascending order by nAnswers
            System.out.println(" writing table ...");
            int k0, k1, k2, k3, k4;
            for (int i = 0; i < statsPointer; i++) {
                k0 = statsArray[i][0]; // pull out the operands, for display
                k4 = k0 % 100;
                k1 = k0 / 1000000;
                k0 = (k0 - 1000000 * k1 - k4) / 100;
                k3 = k0 % 100;
                k2 = k0 / 100;
                writeDetail(i + " : " + k1 + " " + k2 + " " + k3 + " " + k4 + ", " + statsArray[i][1] + " - "
                        + statsArray[i][2] + " = " + (statsArray[i][1] - statsArray[i][2]) + "\n", display);
            }
            System.out.println(" Done writing table ...");
            System.out.println(
                    "Stayed within stats table size. StatsPointer =" + statsPointer + " versus statsMax =" + statsMax);
        } else {
            System.out
                    .println("Overflowed stats table. StatsPointer =" + statsPointer + " versus statsMax =" + statsMax);
        }
    }



}
