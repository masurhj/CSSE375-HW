package src.ActionListeners;

import javax.swing.JTextField;

import src.FlagContainer;

public class CubertActionListener extends BasicInputActionListener{

    public CubertActionListener(JTextField input, JTextField inputDigitSize) {
        super(input, inputDigitSize);
    }

    @Override 
    public void inputSpecificAction(){
        FlagContainer.cubertFlag = !FlagContainer.cubertFlag;
        if (FlagContainer.cubertFlag)
            FlagContainer.extraOpsFlag = true;
        FlagContainer.setrelated(); // set values related to extraOpsFlag being on or off
    }

}
