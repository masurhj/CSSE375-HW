package src.ActionListeners;

import javax.swing.JTextField;

import src.FlagContainer;

public class FactorialActionListener extends BasicInputActionListener{

    public FactorialActionListener(JTextField input, JTextField inputDigitSize) {
        super(input, inputDigitSize);
    }

    @Override 
    public void inputSpecificAction(){
        FlagContainer.factorialFlag = !FlagContainer.factorialFlag;
        if (FlagContainer.factorialFlag)
            FlagContainer.extraOpsFlag = true;
        FlagContainer.setrelated(); // set values related to extraOpsFlag being on or off
    }

}
