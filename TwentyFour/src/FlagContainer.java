package src;

public class FlagContainer {
    public static boolean insolublesFlag; // do or don't do these just on otherwise insoluble problems
    public static boolean extraOpsFlag; // do or don't do sqrt, cubert, mod, etc.

    public static boolean modFlag; // do or don't do mod in particular
    public static boolean expFlag; // do or don't do exp in particular
    public static boolean gcdFlag; // do or don't do gcd in particular
    public static boolean lcmFlag; // do or don't do lcm in particular
    public static boolean sqrtFlag; // do or don't do sqrt in particular
    public static boolean cubertFlag; // do or don't do cubert in particular
    public static boolean absvalFlag; // do or don't do absval in particular
    public static boolean factorialFlag; // do or don't do factorial in particular
    public static boolean averageFlag; // do or don't do average operation
    public static boolean average3Flag; // do or don't do average-of-3 operands operation
    public static int debugFlag;// Set here to display lots of stuff in different ways & amounts!
    public static int maxNumber;
    // current values used - 0, 1, 2, 6, plus commented-out code for 4
    private static FlagContainer instance = null;

    static int nOperations = 4; // how many operations to try -- default is 4, not 12
    static int binaryOperations = 4; // how many of these are binary
    static int unaryOperations = 0; // and how many unary ones to do after that!

    public static FlagContainer getInstance() {
        if (instance == null) {
            instance = new FlagContainer();
        }
        return instance;
    }

    public FlagContainer() {
        averageFlag = false;
        average3Flag = false;
        extraOpsFlag = false;
        insolublesFlag = false;
        modFlag = false;
        expFlag = false;
        gcdFlag = false;
        lcmFlag = false;
        sqrtFlag = false;
        cubertFlag = false;
        absvalFlag = false;
        factorialFlag = false;
    }

    public static void ToggleAllOps(boolean value) {
        modFlag = value;
        expFlag = value;
        gcdFlag = value;
        lcmFlag = value;
        sqrtFlag = value;
        cubertFlag = value;
        absvalFlag = value;
        factorialFlag = value;
        averageFlag = value;
        average3Flag = value;
    }

    // set values related to whether or not we're doing any of those extra
    // operations:
    public static void setrelated() {
        if (FlagContainer.extraOpsFlag) // set for doing / not doing those pesky extra ops
        {
            nOperations = 13;
            binaryOperations = 9;
            // System.out.println("sqrtFlag = "+sqrtFlag); // debug test example
            if (FlagContainer.sqrtFlag || FlagContainer.cubertFlag || FlagContainer.absvalFlag
                    || FlagContainer.factorialFlag) // do unary ones
                unaryOperations = 4;
            else
                unaryOperations = 0;
        } else {
            nOperations = 4;
            binaryOperations = 4;
            unaryOperations = 0;
        }
        // System.out.println("Settings are: "+extraOpsFlag+" "+nOperations+"
        // "+binaryOperations+" "+unaryOperations); // debug test
    } // setrelated
}