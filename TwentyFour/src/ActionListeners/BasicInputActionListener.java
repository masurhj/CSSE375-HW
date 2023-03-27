package src.ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;

import src.FlagContainer;

public class BasicInputActionListener implements ActionListener {
    protected JTextField input;
    protected JTextField inputDigitSize;
    protected String inputValue;

    public BasicInputActionListener(JTextField input, JTextField inputDigitSize) {
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

        inputValue = input.getText(); // when user presses either "convert" button, we get it here
        System.out.println("input value = " + inputValue); // debugging display in DOS window
        if (inputValue.length() == 0)
            inputValue = "24"; // plug void value
        if (FlagContainer.debugFlag != 0) // do additional analysis & display
            System.out.println("FlagContainer.debugFlag = " + FlagContainer.debugFlag);
        inputSpecificAction();
    }

    public void inputSpecificAction(){
        return;
    }

}
