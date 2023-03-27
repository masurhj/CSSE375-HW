package src.ActionListeners;

import javax.swing.JTextField;

import src.FlagContainer;

public class ExpActionListener extends BasicInputActionListener {
    
    public ExpActionListener(JTextField input, JTextField inputDigitSize) {
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
