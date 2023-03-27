package src.ActionListeners;

import javax.swing.JTextField;

import src.FlagContainer;

public class LcmActionListener extends BasicInputActionListener{

    public LcmActionListener(JTextField input, JTextField inputDigitSize) {
        super(input, inputDigitSize);
    }

    @Override 
    public void inputSpecificAction(){
        FlagContainer.lcmFlag = !FlagContainer.lcmFlag;
        if (FlagContainer.lcmFlag)
            FlagContainer.extraOpsFlag = true;
        FlagContainer.setrelated(); // set values related to extraOpsFlag being on or off
    }

}
