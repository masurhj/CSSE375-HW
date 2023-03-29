package main.ActionListeners;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import main.Calculator;
import main.FileManager;
import main.FlagContainer;

public class DefaultInputActionListener extends BasicInputActionListener {

    private JTextArea display;
    private FileManager fileManager;

    public DefaultInputActionListener(JTextField input, JTextField inputDigitSize, JTextArea display,
            FileManager fileManager) {
        super(input, inputDigitSize);
        this.display = display;
        this.fileManager = fileManager;
    }

    @Override
    public void inputSpecificAction() {
        if (fileManager.openTheFile(inputValue, FlagContainer.maxNumber, display)) { // if we opened the output file,
            // then...
            Calculator c = new Calculator(display, fileManager, FlagContainer.maxNumber);
            c.showTheCalcs(inputValue); // go run the calcs & show in that file
        } else {
            System.out.println("Problem opening the output file");
        }
    }

}
