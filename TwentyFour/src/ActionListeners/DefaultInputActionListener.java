package src.ActionListeners;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import src.Calculator;
import src.FlagContainer;

public class DefaultInputActionListener extends BasicInputActionListener {

    private JTextArea display;
    private src.FileReader fileReader;

    public DefaultInputActionListener(JTextField input, JTextField inputDigitSize, JTextArea display,
            src.FileReader fileReader) {
        super(input, inputDigitSize);
        this.display = display;
        this.fileReader = fileReader;
    }

    @Override
    public void inputSpecificAction() {
        if (fileReader.openTheFile(inputValue, FlagContainer.maxNumber, display)) { // if we opened the output file,
            // then...
            Calculator c = new Calculator(display, fileReader, FlagContainer.maxNumber);
            c.showTheCalcs(inputValue); // go run the calcs & show in that file
        } else {
            System.out.println("Problem opening the output file");
        }
    }

}
