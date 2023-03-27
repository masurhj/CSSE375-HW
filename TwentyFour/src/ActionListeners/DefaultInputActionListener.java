package src.ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import src.Calculator;
import src.FlagContainer;

public class DefaultInputActionListener implements ActionListener {

    private JTextField input;
    private JTextField inputDigitSize;
    private JTextArea display;
    private src.FileReader fileReader;

    public DefaultInputActionListener(JTextField input, JTextField inputDigitSize, JTextArea display,
            src.FileReader fileReader) {
        this.input = input;
        this.inputDigitSize = inputDigitSize;
        this.display = display;
        this.fileReader = fileReader;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String maxDigit = inputDigitSize.getText(); // did they prefer a max digit?
        if (maxDigit.length() == 0)
            FlagContainer.maxNumber = 9;
        else
            FlagContainer.maxNumber = Integer.parseInt(maxDigit);
        System.out.println("Max digit size is " + FlagContainer.maxNumber);

        String inputValue = input.getText(); // when user presses either "convert" button, we get it here
        System.out.println("input value = " + inputValue); // debugging display in DOS window
        if (inputValue.length() == 0)
            inputValue = "24"; // plug void value
        if (FlagContainer.debugFlag != 0) // do additional analysis & display
            System.out.println("FlagContainer.debugFlag = " + FlagContainer.debugFlag);

        if (fileReader.openTheFile(inputValue, FlagContainer.maxNumber, display)) { // if we opened the output file,
                                                                                    // then...
            Calculator c = new Calculator(display, fileReader, FlagContainer.maxNumber);
            c.showTheCalcs(inputValue); // go run the calcs & show in that file
        } else {
            System.out.println("Problem opening the output file");
        }
    }

}
