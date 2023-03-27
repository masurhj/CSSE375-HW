package src.ActionListeners;

import javax.swing.JTextField;

import src.FlagContainer;

public class GcdActionListener extends BasicInputActionListener {

    public GcdActionListener(JTextField input, JTextField inputDigitSize) {
        super(input, inputDigitSize);
    }

    @Override 
    public void inputSpecificAction(){
        FlagContainer.gcdFlag = !FlagContainer.gcdFlag;
        if (FlagContainer.gcdFlag)
            FlagContainer.extraOpsFlag = true;
        FlagContainer.setrelated(); // set values related to extraOpsFlag being on or off
    }

}
