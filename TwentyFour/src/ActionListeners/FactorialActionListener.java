package src.ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import src.FlagContainer;

public class FactorialActionListener implements ActionListener {
    private JTextField input;
    private JTextField inputDigitSize;

    public FactorialActionListener(JTextField input, JTextField inputDigitSize) {
        this.input = input;
        this.inputDigitSize = inputDigitSize;
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

        FlagContainer.factorialFlag = !FlagContainer.factorialFlag;
        if (FlagContainer.factorialFlag)
            FlagContainer.extraOpsFlag = true;
        FlagContainer.setrelated(); // set values related to extraOpsFlag being on or off
    }

}
