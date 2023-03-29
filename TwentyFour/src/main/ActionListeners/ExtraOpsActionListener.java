package main.ActionListeners;

import javax.swing.JTextField;

import main.FlagContainer;

public class ExtraOpsActionListener extends BasicInputActionListener {

    public ExtraOpsActionListener(JTextField input, JTextField inputDigitSize) {
        super(input, inputDigitSize);
    }

    @Override
    public void inputSpecificAction() {
        FlagContainer.extraOpsFlag = !FlagContainer.extraOpsFlag; // show / don't show results from extra operation
                                                                  // types
        System.out.println("Now extraOpsFlag = " + FlagContainer.extraOpsFlag);
        if (FlagContainer.extraOpsFlag) // This one turns all the other flags on or off!
        { // turn them all on
            FlagContainer.ToggleAllOps(true);
        } else // turn them all off
        {
            FlagContainer.ToggleAllOps(false);
        }
        FlagContainer.setrelated(); // set values related to extraOpsFlag being on or off
    }

}
