package main.ActionListeners;

import javax.swing.JTextField;

import main.FlagContainer;

public class ModActionListener extends BasicInputActionListener{

    public ModActionListener(JTextField input, JTextField inputDigitSize) {
        super(input, inputDigitSize);
    }

    @Override 
    public void inputSpecificAction(){
        FlagContainer.expFlag = !FlagContainer.expFlag;
        if (FlagContainer.expFlag)
            FlagContainer.extraOpsFlag = true;
        FlagContainer.setrelated(); // set values related to extraOpsFlag being on or off
    }
}