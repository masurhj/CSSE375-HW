package src.ActionListeners;

import javax.swing.JTextField;

import src.FlagContainer;

public class InsolublesActionListener extends BasicInputActionListener {

    public InsolublesActionListener(JTextField input, JTextField inputDigitSize) {
        super(input, inputDigitSize);
    }

    @Override 
    public void inputSpecificAction(){
        FlagContainer.insolublesFlag = !FlagContainer.insolublesFlag; // show / don't show results from extra operation
        // types
        System.out.println("insolublesFlag = " + FlagContainer.insolublesFlag);
        FlagContainer.extraOpsFlag = false; // Start with all these off -- change after first run
        FlagContainer.ToggleAllOps(false);
        FlagContainer.setrelated(); // set values related to extraOpsFlag being on or off
    }

}
