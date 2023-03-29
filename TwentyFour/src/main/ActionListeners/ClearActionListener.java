package main.ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;

import main.FileManager;

public class ClearActionListener implements ActionListener{
    FileManager fileManager;
    JTextArea display;

    public ClearActionListener(FileManager fileManager, JTextArea display){
        this.fileManager = fileManager;
        this.display = display;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fileManager.clearFiles(display);
    }

}
