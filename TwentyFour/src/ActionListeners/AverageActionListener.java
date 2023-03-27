package src.ActionListeners;

import javax.swing.JTextField;

import src.FlagContainer;

public class AverageActionListener extends BasicInputActionListener {

    public AverageActionListener(JTextField input, JTextField inputDigitSize) {
        super(input, inputDigitSize);
    }

    @Override 
    public void inputSpecificAction(){
        FlagContainer.averageFlag = !FlagContainer.averageFlag;
        if (FlagContainer.averageFlag)
            FlagContainer.extraOpsFlag = true;
        FlagContainer.setrelated(); // set values related to extraOpsFlag being on or off
    }

}