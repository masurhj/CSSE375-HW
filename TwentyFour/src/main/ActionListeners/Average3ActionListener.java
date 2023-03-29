package main.ActionListeners;

import javax.swing.JTextField;

import main.FlagContainer;

public class Average3ActionListener extends BasicInputActionListener {

    public Average3ActionListener(JTextField input, JTextField inputDigitSize) {
        super(input, inputDigitSize);
    }

    @Override 
    public void inputSpecificAction(){
        FlagContainer.average3Flag = !FlagContainer.average3Flag;
        if (FlagContainer.average3Flag)
            FlagContainer.extraOpsFlag = true;
        FlagContainer.setrelated(); // set values related to extraOpsFlag being on or off
    }

}
