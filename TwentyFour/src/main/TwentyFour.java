package main;
/*
 *  File: TwentyFourC.java
 * --> This is Steve's Twenty Four game, fourth version, with more options!
 *
 *Like other versions, this one always outputs detail as a file into a subdirectory called "datafiles"
 *and displays only the summary data for each combination tried, and grand totals.
 *
 *This fourth version successfully does even the unary operations!
 *Also added averaging of 2 operators, and the ability to show sample solutions using the additional
 *operations, for those problems insoluble using just the standard 4 arithmetic operations.
 *
 *Now also works with 3 operators averaged -- this is harder!  Didn't try for 4!
 *
 *And our last fix -- weeds out duplicates due to mult or addn associativity --
 * and also "extended commutativity" ...
 * and this code should now be added also into the unary routine, below it!
 *
 */

import javax.swing.*; // Packages used

import main.ActionListeners.AbsvalActionListener;
import main.ActionListeners.Average3ActionListener;
import main.ActionListeners.AverageActionListener;
import main.ActionListeners.ClearActionListener;
import main.ActionListeners.CubertActionListener;
import main.ActionListeners.DefaultInputActionListener;
import main.ActionListeners.ExpActionListener;
import main.ActionListeners.ExtraOpsActionListener;
import main.ActionListeners.FactorialActionListener;
import main.ActionListeners.GcdActionListener;
import main.ActionListeners.InsolublesActionListener;
import main.ActionListeners.LcmActionListener;
import main.ActionListeners.ModActionListener;
import main.ActionListeners.SqrtActionListener;

import java.awt.*;
import java.awt.event.*;

public class TwentyFour extends JFrame{

    private JLabel prompt = new JLabel(" Input magic number (def = 24) or 4 digits : ");
    private JLabel promptDigitSize = new JLabel(" Maximum digit size (def = 9) : ");
    private JLabel promptDone = new JLabel(" Click this button last --> ");
    private JLabel promptSteve = new JLabel(" by Steve Chenoweth");
    private JLabel promptExtras = new JLabel(" Click any extra ops:");

    private JTextField input = new JTextField(10);
    private JTextField inputDigitSize = new JTextField(10);
    private JTextArea display = new JTextArea(1, 25); // with most formats, 5,25 is better!
    private JScrollPane scrollPane = new JScrollPane(display); // try scrolling the text area
    private JButton calculate = new JButton(" Calculate! ");
    private JButton mod = new JButton(" mod ");
    private JButton exp = new JButton(" exp ");
    private JButton gcd = new JButton(" gcd ");
    private JButton lcm = new JButton(" lcm ");
    private JButton sqrt = new JButton(" sqrt ");
    private JButton cubert = new JButton(" cubert ");
    private JButton absval = new JButton(" abs val ");
    private JButton factorial = new JButton(" factorial ");
    private JButton average = new JButton(" average 2 ");
    private JButton average3 = new JButton(" average 3 ");
    private JButton extraOps = new JButton(" All Extra Ops! ");
    private JButton insolubles = new JButton(" Insolubles Only! ");
    private JButton clear = new JButton("Clear all datafiles");
    private FileManager fileManager = new FileManager();
    // private JButton write = new JButton(" Write display! ");
    // private JButton writeCalcBtn = new JButton(" Write detail! ");

    // Here's the constructor:
    public TwentyFour() {
        getContentPane().setLayout(new BorderLayout()); // border layout for the whole JFrame content pane
        JPanel inputPanel = new JPanel(); // Input panel
        inputPanel.setLayout(new GridLayout(3, 2));
        inputPanel.add(prompt);
        inputPanel.add(input);
        inputPanel.add(promptDigitSize);
        inputPanel.add(inputDigitSize);
        inputPanel.add(promptDone);
        inputPanel.add(calculate);
        calculate.setOpaque(true);
        calculate.setBackground(Color.cyan);
        getContentPane().add(inputPanel, "North");

        JPanel controlPanel = new JPanel();
        // controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS)); // was
        // this
        controlPanel.setLayout(new GridLayout(14, 1)); // now this layout
        controlPanel.add(promptExtras);
        controlPanel.add(mod);
        controlPanel.add(exp);
        controlPanel.add(gcd);
        controlPanel.add(lcm);
        controlPanel.add(sqrt);
        controlPanel.add(cubert);
        controlPanel.add(absval);
        controlPanel.add(factorial);
        controlPanel.add(average);
        controlPanel.add(average3);
        controlPanel.add(extraOps);
        controlPanel.add(insolubles);
        controlPanel.add(clear);
        // controlPanel.add(write);
        // controlPanel.add(writeCalcBtn);
        controlPanel.add(promptSteve);

        getContentPane().add(controlPanel, "East");

        getContentPane().add(scrollPane, "Center"); // Output display -- was add display
        display.setLineWrap(true);
        display.setEditable(false);
        display.setText(
                "This display tracks progress - results are in datafiles directory. Some messages are in console.");

        input.addActionListener(new DefaultInputActionListener(input, inputDigitSize, display, fileManager));
        calculate.addActionListener(new DefaultInputActionListener(input, inputDigitSize, display, fileManager));
        mod.addActionListener(new ModActionListener(input, inputDigitSize));
        exp.addActionListener(new ExpActionListener(input, inputDigitSize));
        gcd.addActionListener(new GcdActionListener(input, inputDigitSize));
        lcm.addActionListener(new LcmActionListener(input, inputDigitSize));
        sqrt.addActionListener(new SqrtActionListener(input, inputDigitSize));
        cubert.addActionListener(new CubertActionListener(input, inputDigitSize));
        absval.addActionListener(new AbsvalActionListener(input, inputDigitSize));
        factorial.addActionListener(new FactorialActionListener(input, inputDigitSize));
        average.addActionListener(new AverageActionListener(input, inputDigitSize));
        average3.addActionListener(new Average3ActionListener(input, inputDigitSize));
        extraOps.addActionListener(new ExtraOpsActionListener(input, inputDigitSize));
        insolubles.addActionListener(new InsolublesActionListener(input, inputDigitSize));
        clear.addActionListener(new ClearActionListener(fileManager, display));

    } // TwentyFour()

    /**
     * main() creates an instance of this class and sets
     * the size and visibility of its JFrame.
     */
    public static void main(String args[]) {
        TwentyFour f = new TwentyFour();
        f.setSize(560, 600);
        f.setVisible(true);
        f.addWindowListener(new WindowAdapter() { // Quit the application
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    } // main()

} // TwentyFour