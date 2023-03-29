package main.ActionListeners;

import javax.swing.JTextField;

import main.FlagContainer;

public class SqrtActionListener extends BasicInputActionListener {

    public SqrtActionListener(JTextField input, JTextField inputDigitSize) {
        super(input, inputDigitSize);
    }

    @Override 
    public void inputSpecificAction(){
        FlagContainer.sqrtFlag = !FlagContainer.sqrtFlag;
        if (FlagContainer.sqrtFlag)
            FlagContainer.extraOpsFlag = true;
        FlagContainer.setrelated(); // set values related to extraOpsFlag being on or off
    }

}
