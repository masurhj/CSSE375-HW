package Tests;

import static org.junit.Assert.assertEquals;

import javax.swing.JTextArea;

import org.junit.Test;

import main.FileManager;

public class FileManagerTests{

    @Test
    public void clearDirectoryTest(){
        JTextArea display = new JTextArea(1, 25); // with most formats, 5,25 is better!
        FileManager fm = new FileManager();
        fm.openTheFile("5", 5, display);
        fm.clearFiles(display);
        assertEquals(0, fm.getFileCount());
    }
}