package src.ActionListeners;

import javax.swing.JTextField;

import src.FlagContainer;

public class AbsvalActionListener extends BasicInputActionListener {

    public AbsvalActionListener(JTextField input, JTextField inputDigitSize) {
        super(input, inputDigitSize);
    }

    @Override 
    public void inputSpecificAction(){
        FlagContainer.absvalFlag = !FlagContainer.absvalFlag;
        if (FlagContainer.absvalFlag)
            FlagContainer.extraOpsFlag = true;
        FlagContainer.setrelated(); // set values related to extraOpsFlag being on or off
    }

}
