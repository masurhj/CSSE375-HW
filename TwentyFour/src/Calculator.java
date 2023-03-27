package src;

import java.io.IOException;

import javax.swing.JTextArea;

public class Calculator {
	int inputValueN = 24; // value we're trying to get with each calculation -- user can override
	// maybe we'll need to use arrays, maybe not! Here's an example of something
	// possibly useful:

	// int outArray[][][][] = new int[9][9][9][9];

	static final int statsMax = 25000; // increased for numbers > 9 on cards
	int statsArray[][] = new int[statsMax][3]; // saves how many of each number of solutions

	int statsPointer = 0; // start building stats table
	int oldStatsPointer = 0; // and one to remember end of first run, for insolubles routine
	int insolublesDoMax = 20; // how many solutions to show for insolubles, using extra operations

	static final int maxDepth = 10; // How deep is our search table? (see next line)
	int operandTbl[][] = new int[maxDepth][9]; // for selecting operands & recording answers
	/*
	 * Values of slots in this table are as follows, for each i = 1 to maxDepth:
	 * answer = operandTbl[i][0]; // answer, or original operand
	 * status flag = operandTbl[i][1]; // 0 = empty, kinda; 1 = ready to use, 2 =
	 * used
	 * op1 = operandTbl[i][2]; // recover pieces of each calc, for display
	 * op2 = operandTbl[i][3]; // 2nd operand or -2000 flag if unary operation
	 * opn = operandTbl[i][4]; // operation, or -1 = original operand with no
	 * operation
	 * o1 = operandTbl[i][5]; // first operand's index in this table
	 * o2 = operandTbl[i][6]; // second operand's index in this table -- if the
	 * same, this is a 1-operand operation.
	 * o3 = operandTbl[i][7]; // third operand's index in this table, if any (opn =
	 * 9 only)
	 * op3 = operandTbl[i][8]; // third operand, if any
	 */
	String operations[] = { "averaged with", "+", "-", "*", "/", "mod", "exp", "gcd", "lcm", "averaged", "sqrt",
			"cubert", "abs val", "factorial" }; // for printing operations in answers
	int maxExp = 6; // extra rule for exponentiation -- must have power < = 6.
	int maxFact = 6; // likewise for doing factorials -- must be a number < = 6.
	int maxAnswer = 1000; // Maximum multiple of final answer allowed in calcs
	int maxToCheck = 24000; // Resulting max number -- reset in calcs based on magic no. like 24

	int nUnaryMax = 3; // and how many total of these we are allowed to try -- 3 more fits with
	// the following other restrictions -- having a table size of 10 with 4
	// variables coming in
	// (the 4 numbers provided). These 4 take 3 binary operations to get "used up,"
	// making 7
	// entries in the table, leaving 3 more for unary operations.

	String answerShown = new String();

	// boolean showCalcs = true; // running calculations vs. setting special flags
	// boolean writeCalcs = false; // write detail to file vs. displaying

	// int answer = 0; // unversal answer to next computation! -- That didn't work!
	boolean gotAnswer = true;
	boolean isDuplicate = false;

	static final int dupTableMax = 100;
	String dupTable[] = new String[dupTableMax]; // for checking for exact duplicate answers
	int dupTablePointer = 0;

	String answerString = new String(); // for reporting answers to user

	static final int insolTableMax = 100;
	String insolTable[] = new String[insolTableMax]; // for saving interesting answers to insolvables
	int insolTablePointer = 0;

	int nAnswers = 0; // count how many for each little set of numbers we try
	int nduplicates = 0; // duplicates removed

	int minNumber = 1; // and where they start

	private int total = 0; // fields used for averaging
	private int totduplicates = 0; // duplicates removed
	private int nentries = 0; // how many combinations we look at

	private JTextArea display;
	private FileReader fileReader;
	private int maxNumber;

	public Calculator(JTextArea display, FileReader fileReader, int maxNumber) {
		this.display = display;
		this.fileReader = fileReader;
		this.maxNumber = maxNumber;
	}

	// we're doing the calculations, so do them!
	public void showTheCalcs(String inputValue) {
		try { // Number conversions of numeric fields :
			inputValueN = Integer.parseInt(inputValue);
			if (inputValueN > 1000) // they want us to run just a single set of numbers

			{
				if (FlagContainer.insolublesFlag) // uh oh, we haven't coded this routine for that option!
					display.append("Oops -- sorry -- we only have the logic to do insolubles for a full run!\n");
				else // ok to do
				{
					System.out.println("Calculating for a single number"); // calculate the quality:
					int k1 = Integer.parseInt(inputValue.substring(0, 1));
					int k2 = Integer.parseInt(inputValue.substring(1, 2));
					int k3 = Integer.parseInt(inputValue.substring(2, 3));
					int k4 = Integer.parseInt(inputValue.substring(3, 4));
					inputValueN = 24; // use standard
					maxToCheck = maxAnswer * inputValueN; // maximum we'll check for as intermediate answer
					System.out.println(" with values " + k1 + ", " + k2 + ", " + k3 + ", " + k4
							+ ", and using magic number " + inputValueN);
					// writeDetail(" with values "+k1+ ", "+k2+", "+k3+", "+k4+", and using magic
					// number "+ inputValueN + "\n");
					runnumberset(k1, k2, k3, k4); // find answers for just this combination of numbers
					display.append("------- A total of " + nAnswers + " answers with " + nduplicates
							+ " duplicates removed = " + (nAnswers - nduplicates) + " shown.\n");
					fileReader.writeDetail("------- A total of " + nAnswers + " answers with " + nduplicates
							+ " duplicates removed = " + (nAnswers - nduplicates) + " shown.\n", display);
				} // else ok to do
			} // if (inputValueN > 1000)

			else // do the full routine

			{
				maxToCheck = maxAnswer * inputValueN; // maximum we'll check for as intermediate answer
				total = 0; // how many answers altogether
				nentries = 0; // for how many problems
				totduplicates = 0; // total number of duplicates we removed
				System.out.println("Calculating for a batch"); // calculate the quality:
				display.setText("And the results for game using " + inputValue + " are \n");

				// try all solutions for generated numbers

				for (int k1 = minNumber; k1 <= maxNumber; k1++)
					for (int k2 = minNumber; k2 <= k1; k2++)
						for (int k3 = minNumber; k3 <= k2; k3++)
							for (int k4 = minNumber; k4 <= k3; k4++) { // first do operand table setup
								runnumberset(k1, k2, k3, k4); // do this set of numbers
								display.append("------- A total of " + nAnswers + " answers with " + nduplicates
										+ " duplicates removed = " + (nAnswers - nduplicates) + " shown.\n");
								fileReader.writeDetail(
										"------- A total of " + nAnswers + " answers with " + nduplicates
												+ " duplicates removed = " + (nAnswers - nduplicates) + " shown.\n",
										display);
								total = total + nAnswers;
								nentries = nentries + 1;
								totduplicates = totduplicates + nduplicates;
								if (statsPointer < statsMax) // add to statistics table
								{
									statsArray[statsPointer][0] = 1000000 * k1 + 10000 * k2 + 100 * k3 + k4; // save
																												// statistics
																												// in
																												// table
									statsArray[statsPointer][1] = nAnswers;
									statsArray[statsPointer][2] = nduplicates;
								} else if (statsPointer == statsMax) // print warning message
									System.out.println("Warning -- Statistics lost, table not big enough.");
								statsPointer++;

								if (FlagContainer.debugFlag == 2) // print what's supposed to be displaying, too
									System.out.println("+++------- A total of " + nAnswers + ".\n");
							} // all the top=level for's!
				display.append("Grand total number of answers is " + total + "\n for " + nentries
						+ " combinations with " + totduplicates + " duplicates removed giving "
						+ (total - totduplicates) + " answers in file. \n");
				fileReader.writeDetail("Grand total number of answers is " + total + "\n for " + nentries
						+ " combinations with " + totduplicates + " duplicates removed giving "
						+ (total - totduplicates) + " answers in file. \n", display);
				fileReader.writeSummary(display, statsPointer, statsMax, statsArray); // first gen statistical summary
																						// of combos we tried
				if ((FlagContainer.insolublesFlag) && (statsPointer < statsMax)) // do equally long routine to solve
																					// just those combinations using
																					// extra functions
				{
					System.out.println("... Now working on insolubles, which usually takes quite a bit longer! ...");
					display.append("\n------- Now looking at insoluble problems from first run: ---------\n");
					fileReader.writeDetail("\n------- Now looking at insoluble problems from first run: ---------\n",
							display);
					total = 0; // start statistics again -- how many answers altogether
					nentries = 0; // for how many problems
					totduplicates = 0; // total number of duplicates we removed

					oldStatsPointer = statsPointer; // to remember where we started on second run
					statsPointer = 0; // start at beginning of sorted table, rewriting numbers there

					FlagContainer.extraOpsFlag = true; // turn on all the flags, for this part of the run:
					FlagContainer.ToggleAllOps(true);
					FlagContainer.setrelated(); // set values related to extraOpsFlag being on or off

					int k0, k1, k2, k3, k4; // for use in loop, below
					while (statsArray[statsPointer][1] - statsArray[statsPointer][2] == 0) // no solutions, in pass 1
					{
						insolTablePointer = 0; // To save interesting solutions for file;
						for (int i = 0; i < insolTableMax; i++) // clear this table for next combo
							insolTable[i] = "";
						k0 = statsArray[statsPointer][0]; // pull out the operands, for display
						k4 = k0 % 100;
						k1 = k0 / 1000000;
						k0 = (k0 - 1000000 * k1 - k4) / 100;
						k3 = k0 % 100;
						k2 = k0 / 100;
						runnumberset(k1, k2, k3, k4); // do this set of numbers

						if (nAnswers - nduplicates > insolublesDoMax) // then say we only showed the first 20 of them
						{
							int j = Math.min(insolTablePointer, insolTableMax);
							if (insolTablePointer > 0) // then we saved some of these to display later, and now's the
														// time!
							{
								for (int i = 0; i < j; i++)
									fileReader.writeDetail(insolTable[i], display);
							}
							display.append("------- Now a total of " + nAnswers + " answers with " + nduplicates
									+ " duplicates removed = " + (nAnswers - nduplicates) + ", only "
									+ (insolublesDoMax + j) + " shown.\n");
							fileReader.writeDetail("------- Now a total of " + nAnswers + " answers with " + nduplicates
									+ " duplicates removed = " + (nAnswers - nduplicates) + ", only "
									+ (insolublesDoMax + j) + " shown.\n", display);
						} // else we showed all of them
						else // we showed all of them
						{
							display.append("------- Now a total of " + nAnswers + " answers with " + nduplicates
									+ " duplicates removed = " + (nAnswers - nduplicates) + " shown.\n");
							fileReader.writeDetail(
									"------- Now a total of " + nAnswers + " answers with " + nduplicates
											+ " duplicates removed = " + (nAnswers - nduplicates) + " shown.\n",
									display);
						} // else we showed all of them
						if (statsPointer < statsMax) // add to statistics table
						{
							statsArray[statsPointer][0] = 1000000 * k1 + 10000 * k2 + 100 * k3 + k4; // save statistics
																										// in table
							statsArray[statsPointer][1] = nAnswers;
							statsArray[statsPointer][2] = nduplicates;
						} else if (statsPointer == statsMax) // print warning message
							System.out.println("Warning -- Statistics lost, table not big enough.");
						statsPointer++;
					} // while (statsArray[ ...

					fileReader.writeSummary(display, statsPointer, statsMax, statsArray); // first gen statistical
																							// summary of combos we
																							// tried

					FlagContainer.extraOpsFlag = false; // And turn all the flags off at end!
					FlagContainer.ToggleAllOps(false);
					FlagContainer.setrelated(); // set values related to extraOpsFlag being on or off

				} // if (insolublesFlag)

			} // else do the full routine

			try {
				System.out.println("closing the file! -------------");
				fileReader.getOutStream().close();
			} catch (IOException e1) {
				display.setText("IOERROR on file close: " + e1.getMessage() + "\n");
				e1.printStackTrace();
			} // catch IOException

		} // try
		catch (NumberFormatException e1) {
			System.out.println("Error -- input a number -- Try again"); // user entered non-numerics
			display.append("Error -- input a number -- Try again\n"); // debugging line
		} // catch

	} // showTheCalcs()

	// subroutine to process a given set of 4 numbers

	public void runnumberset(int k1, int k2, int k3, int k4) {
		operandTbl[0][0] = k1; // populated and
		operandTbl[0][1] = 1; // ready for use
		operandTbl[0][4] = -1; // flag original operands as result of an illegal operation
		operandTbl[1][0] = k2;
		operandTbl[1][1] = 1;
		operandTbl[1][4] = -1;
		operandTbl[2][0] = k3;
		operandTbl[2][1] = 1;
		operandTbl[2][4] = -1;
		operandTbl[3][0] = k4;
		operandTbl[3][1] = 1;
		operandTbl[3][4] = -1;
		for (int k = 4; k < maxDepth; k++) {
			operandTbl[k][0] = 0; // nothing there so we say so!
			operandTbl[k][1] = 0;
		}
		display.append(
				"Given values of " + k1 + " " + k2 + " " + k3 + " " + k4 + "--> magic no. " + inputValueN + " :\n");
		fileReader.writeDetail(
				"\nGiven values of " + k1 + " " + k2 + " " + k3 + " " + k4 + "--> magic no. " + inputValueN + " :\n\n",
				display);
		if (FlagContainer.debugFlag == 2) // print what's supposed to be displaying, too
			System.out.println("+++Given values of " + k1 + " " + k2 + " " + k3 + " " + k4 + " :\n");
		nAnswers = 0; // reset count for this new set of numbers to try
		nduplicates = 0;
		if ((FlagContainer.debugFlag == 1) || (FlagContainer.debugFlag == 2))
			printOperandTbl(); // show current contents of table
		gotAnswer = calculatem(4, 4, 0); // call recursive calculation routine w 1 - 4 slots valid at beginning
		for (int i = 0; i < dupTableMax; i++) // clear duplicate check list from last run, if necessary
			dupTable[i] = "";
		dupTablePointer = 0;
	} // runnumberset()

	// Recursive routine to do calcs and return valid or not to higer levels
	// The depth param works against the depth limit of the shared table showing
	// operands & results.
	// The nOperands param tells when we can test for a solution (game rules say all
	// should be used).
	// The nUnary param limits how many unary operations can be done (if these are
	// used at all). This
	// limit also ensures the program doesn't loop in useless combinations of just
	// those operands.
	public boolean calculatem(int depth, int nOperands, int nUnary) {
		int answer = 0; // recursively defined field
		if (FlagContainer.debugFlag == 2) // do detailed printouts
			System.out.println("Starting level " + depth); // debug line
		for (int o1 = 0; o1 < depth; o1++) {
			if (operandTbl[o1][1] == 1) // we should process this operand
			{
				if (FlagContainer.debugFlag == 2) // do detailed printouts
					System.out.println("Starting first loop at level " + depth); // debug line
				for (int o2 = 0; o2 < depth; o2++) {
					if ((o2 != o1) && (operandTbl[o2][1] == 1)) // and we should process this operand
					{ // this clause has a giant else clause for unary operators, way below!
						// System.out.println("Ready to try binary operators for operand "+o1+" =
						// "+operandTbl[o1][0]+" & "+o2+" = "+operandTbl[o2][0] +" at depth "+depth+",
						// nOperands = "+nOperands);
						if (FlagContainer.debugFlag == 2) // do detailed printouts
							System.out.println("Starting second loop at level " + depth); // debug line
						for (int opn = 0; opn <= FlagContainer.binaryOperations; opn++) // now do just the binary ops
																						// here
						{
							if (FlagContainer.debugFlag == 2) // do detailed printouts
								System.out.println("Starting third loop at level " + depth); // debug line
							gotAnswer = true; // initially, we're not doing bad integer division!
							boolean trinaryOp = false; // may not need anymore!
							int o3 = 0; // set "not trinary" values by default
							int op3 = 0; // For opn = 9, we'll need these then & also later!
							int op1 = operandTbl[o1][0]; // get the operands
							int op2 = operandTbl[o2][0];
							if (FlagContainer.debugFlag == 2) // do detailed printouts
								System.out.println("  At depth " + depth + " Trying " + op1 + " op " + opn + " " + op2
										+ " gotAnswer = " + gotAnswer);
							// do this operation
							{ // a bracket just to group all these if (opn = ... tests, for security
								if ((opn == 0) && FlagContainer.averageFlag) // An extra op'n -- average the two
																				// operands!
								// answer = op1 + op2; // for debugging, make it just like "add"
								{
									answer = (op1 + op2) / 2;
									if ((answer + answer) != (op1 + op2)) // didn't come out even -- no answer
										gotAnswer = false; // a flag to say invalid!
									// System.out.println(" Now averaging -- gotAnswer = "+gotAnswer);
									// if (gotAnswer)
									// System.out.println(" with "+op1+" & "+op2+" --> "+answer);
								}

								else if (opn == 1) // addition
									answer = op1 + op2;
								else if (opn == 2) // subtraction
								{
									if (op2 != 0) // take out silly similar ones -- this case is covered by addition,
													// above
										answer = op1 - op2;
									else
										gotAnswer = false;
								} else if (opn == 3) // multiplication
									answer = op1 * op2;
								else if (opn == 4) // division, have to check validity first
								{
									if ((op2 != 0) && (op2 != 1) && (op1 % op2 == 0)) // ok to do -- op2 = 1 is a silly
																						// case also covered by
																						// multiplying by 1
										answer = op1 / op2;
									else {
										answer = -1000; // for debugging, for me to spot if not caught
										gotAnswer = false; // a flag to say invalid!
									} // else (can't divide evenly here -- the series of calculations fails)
								} // else opn == 4

								else if ((opn == 5) && FlagContainer.modFlag) // modulo operation -- 1st extra operation
								{
									if ((op2 != 0) && (op1 != op2)) // ok to do, take out silly ones
										answer = op1 % op2;
									else // can't do modulo here
									{
										answer = -1000; // for debugging, for me to spot if not caught
										gotAnswer = false; // a flag to say invalid!
									} // else (can't do modulo here -- the series of calculations fails)
								} // else if ((opn == 5) && modFlag)
								else if ((opn == 6) && FlagContainer.expFlag) // exponentiation
								{
									if ((op2 >= 0) && (op2 <= maxExp) && !((op1 == 2) && (op2 == 2))) // ok to do,
																										// remove silly
																										// case
									{
										answer = 1;
										// System.out.println("start loop, op2 = "+op2);
										for (int i = 1; i <= op2; i++) // do them powers by hand!
											answer = answer * op1;
										// System.out.println("end loop");
									} // if ((op2 >= 0) &&
									else {
										answer = -1000; // for debugging, for me to spot if not caught
										gotAnswer = false; // a flag to say invalid!
									} // else (can't exponentiate here -- the series of calculations fails)
								} // else if ((opn == 6) && expFlag)
								else if ((opn == 7) && FlagContainer.gcdFlag) // gcd
									if ((op1 >= op2) && (op2 > 0)) // ok to do
									{
										boolean moretodo = true;
										int tempVal = 1;
										int op1Test = op1;
										int op2Test = op2;
										while (moretodo) {
											tempVal = op1Test % op2Test;
											if (tempVal > 0) // do it again
											{
												op1Test = op2Test;
												op2Test = tempVal;
											} // if (tempVal > 0)
											else
												moretodo = false;
										} // while (moretodo)
										answer = op2Test;
										if (answer == 1) // throw it out, just like doing mod
										{
											answer = -1000; // for debugging, for me to spot if not caught
											gotAnswer = false; // a flag to say invalid!
										} // if (answer == 1)
									} // if ((op1 >= op2) && (op2 > 0))
									else // (can't do gcd calc here -- the series of calculations fails)
									{
										answer = -1000; // for debugging, for me to spot if not caught
										gotAnswer = false; // a flag to say invalid!
									} // else (can't do gcd calc here -- the series of calculations fails)

								else if ((opn == 8) && FlagContainer.lcmFlag) // lcm
									if ((op1 >= op2) && (op2 > 0)) // ok to do
									{
										boolean moretodo = true; // do gcd first
										int tempVal = 1;
										int op1Test = op1;
										int op2Test = op2;
										while (moretodo) {
											tempVal = op1Test % op2Test;
											if (tempVal > 0) // do it again
											{
												op1Test = op2Test;
												op2Test = tempVal;
											} // if (tempVal > 0)
											else
												moretodo = false;
										} // while (moretodo)
										answer = op1 * op2 / op2Test; // calculate lcm from gcd
										if (op2Test == 1) // throw it out, just like multiplying the numbers
										{
											answer = -1000; // for debugging, for me to spot if not caught
											gotAnswer = false; // a flag to say invalid!
										} // if (op2Test == 1)
									} // if ((op1 >= op2) && (op2 > 0))
									else // (can't do lcm calc here -- the series of calculations fails)
									{
										answer = -1000; // for debugging, for me to spot if not caught
										gotAnswer = false; // a flag to say invalid!
									} // else (can't do lcm calc here -- the series of calculations fails)

								// This one's so tricky, I switch to declaring gotAnswer = false at the
								// beginning, and
								// it's then only true if we happen to hit the right condition in the middle!

								else if ((opn == 9) && FlagContainer.average3Flag) // trinary+ averaging -- for now,
																					// just trinary -- bad enuf!
								{
									gotAnswer = false; // Change in strategy -- start with false, make true if it works
														// out here
									if ((op1 < op2) || ((op1 == op2) && (o1 < o2))) // this is a duplicate
									{
									} // gotAnswer = false;
									else // get 3rd operand and check it likewise, then process
									{
										for (o3 = 0; (o3 < depth) && !trinaryOp; o3++) {
											op3 = operandTbl[o3][0]; // get the operands -- very peculiarly the compiler
																		// WANTS this line here, not in if, below
											if ((o3 != o1) && (o3 != o2) && (operandTbl[o3][1] == 1)) // then we could
																										// process this
																										// 3rd operand
											{
												if ((op2 < op3) || ((op2 == op3) && (o2 < o3))) // this is a duplicate
												{
												} // gotAnswer = false;
												else // process
												{
													answer = (op1 + op2 + op3) / 3;
													if ((3 * answer) != (op1 + op2 + op3)) // didn't come out even -- no
																							// answer
													{
													} // gotAnswer = false; // a flag to say invalid!
													else // got a correct answer here
													{
														gotAnswer = true;
														trinaryOp = true; // uh oh, a real answer --
														if (FlagContainer.debugFlag == 6) // debug display of this
																							// troublesome little
																							// routine when it succeeds!
															System.out.println("Now doing trinary+ averaging on " + op1
																	+ " and " + op2 + " and " + op3 + " = " + answer
																	+ " at depth" + depth + "-------");
														// if ((op1 == 4) && (op2 == 3) && (op3 == 2)) // flag this one
														// to trace
														// FlagContainer.debugFlag = 7; // special setting to print
														// trace of what happens next
													} // else got a correct answer here
														// Note: we take the cheap way out, forget other possible
														// answers in this loop!
												} // else process
											} // if ((o3 != o1) ...
											else // thought this should work, but it blocked even good answers!
											{
											} // gotAnswer = false;
												// --> needs a whole lot of fixing here to really be complete -- we
												// don't
												// really
												// want to exit this loop as-is, but have to on "success," for now!
										} // for (int o3 = 0...
										if (trinaryOp) // we got a good answer here, and since we're now outside the
														// loop --
											o3 = o3 - 1; // that "for loop", above, makes this 1 too many!
									} // else get 3rd operand
								} // if (opn == 9)

								else if (opn >= 10) // don't do ops 10+ here -- shouldn't see these -- they're unary!
								{
									System.out.println("Oops -- Shouldn't be seeing opn = " + opn + " here!");
									answer = -1000; // for debugging, for me to spot if not caught
									gotAnswer = false; // a flag to say invalid!
								} // else (can't divide evenly here -- the series of calculations fails)

								else // an operation to ignore -- we didn't meet the conditions to do one of these --
										// falls through
									gotAnswer = false;
							} // bracket to group all the if (opn = ... binary ops & opn = 9

							if (FlagContainer.debugFlag == 2) // do detailed printouts
								System.out.println("    still at depth " + depth + " with answer = " + answer
										+ " gotAnswer now = " + gotAnswer); // debug

							if (Math.abs(answer) > maxToCheck) // it might be right but it's too big to be realistic
								gotAnswer = false; // flag it as invalid!

							if (gotAnswer) // we plan to at least investigate the following with this answer:
							// Is it the final answer for this path? If so, save it before going on.
							// If not, should we go deeper pursuing an answer here? If so, call this routine
							// recursively.
							{ // so,
								operandTbl[depth][0] = answer; // save the answer in next operand table slot
								operandTbl[depth][1] = 1; // set as valid operand for next round, if any
								operandTbl[depth][2] = op1; // And now record how we did this calc!
								operandTbl[depth][3] = op2; // just in case it turns out right at end!
								operandTbl[depth][4] = opn; // operation ID we just did, for reporting
								operandTbl[depth][5] = o1; // show source of operands to decide how to eliminate
															// duplicates
								operandTbl[depth][6] = o2; // when they are really the same value
								if (opn == 9) // save last 2 slots in table for operand 3, too:
								{
									operandTbl[depth][7] = o3;
									operandTbl[depth][8] = op3; // These last 2 only used with opn = 9
								} // if (opn = 9)
								else // put identifiable garbage in there
								{
									operandTbl[depth][7] = 0;
									operandTbl[depth][8] = -3000;
								}
								if ((nOperands <= 2) || ((nOperands <= 3) && (opn == 9))) // we're possibly done,
																							// because used 2 more just
																							// now, leaving 1 -- an
																							// answer
								{
									// if (FlagContainer.debugFlag == 4) // do detailed printouts
									// System.out.println(" & reached end of levels with value "+answer);
									if (answer == inputValueN) // Hey -- we got it right, who would've guessed!?
									{ // so, for parsimony, we can stop here, not cycle on unary operators, etc.
										nAnswers = nAnswers + 1;
										isDuplicate = false; // with testing for dupes to follow here
										answerString = ""; // to build answer for displaying or writing to file

										int op1a, op2a, opna, opnb, answera, o1a, o2a, o3a, op3a; // to distinguish from
																									// loop counters
										for (int i = 4; (i <= depth) && (!isDuplicate); i++) // for now, as results of
																								// calcs, leave 'em
																								// guessing how:
										{
											op1a = operandTbl[i][2]; // recover pieces of each calc, for display
											op2a = operandTbl[i][3];
											opna = operandTbl[i][4];
											answera = operandTbl[i][0];
											o1a = operandTbl[i][5];
											o2a = operandTbl[i][6];
											o3a = operandTbl[i][7];
											op3a = operandTbl[i][8];

											// Check for average op or add or multiply duplicates due to commutativity
											// or repeat of identical operands
											if ((opna == 0 || opna == 1 || opna == 3) && ((op1a < op2a)
													|| ((op1a == op2a) && (operandTbl[i][5] < operandTbl[i][6])))) // messy
																													// test!
												isDuplicate = true; // Note, we check for associative law duplication
																	// issues farther down here!
											// if not seen as a duplicate so far -- we build answer string at this
											// level:
											else if (opna == 9) // trinary averaging operation
												answerString = answerString + operations[opna] + " ( "
														+ String.valueOf(op1a) + ", " + String.valueOf(op2a) + ", "
														+ String.valueOf(op3a) + " ) = " + String.valueOf(answera)
														+ "\n ";
											else if (o1a != o2a) // Append binary operation to answerString :
												answerString = answerString + String.valueOf(op1a) + " "
														+ operations[opna] + " " + String.valueOf(op2a) + " = "
														+ String.valueOf(answera) + "\n ";
											else // Append unary operation to answerString:
												answerString = answerString + operations[opna] + " ( "
														+ String.valueOf(op1a) + " ) = " + String.valueOf(answera)
														+ "\n ";
										} // for
										for (int i = 4; (i < depth) && (!isDuplicate); i++) { // go through loop again,
																								// checking for assoc
																								// law dups --
											boolean gotADup = true; // hopefully, we'll find one here!
											op3a = -1; // define these here just to make the compiler happy
											o3a = -1000;
											opna = operandTbl[i][4];
											opnb = operandTbl[i + 1][4];
											if (((opna == 1) && (opnb == 1)) || ((opna == 3) && (opnb == 3))) // candidates
																												// to
																												// check
											{
												answera = operandTbl[i][0]; // see if following op builds on first
												if (answera == operandTbl[i + 1][2]) // yes -- first operand
												{
													op3a = operandTbl[i + 1][3]; // then we want to look at the other
																					// operand
													o3a = operandTbl[i + 1][6];
												} // if (answera == operandTbl[i+1][2])
												else if (answera == operandTbl[i + 1][3]) // yes -- second operand
												{
													op3a = operandTbl[i + 1][3];
													o3a = operandTbl[i + 1][5];
												} // else if (answera == operandTbl[i+1][3])
												else // it's not in the scope of this dupe test
													gotADup = false;
												if (gotADup) // keep the investigation going
													if ((op3a > operandTbl[i][3])
															|| ((op3a == operandTbl[i][3]) && o3a > operandTbl[i][6])) // yet
																														// another
																														// messy
																														// test!
														isDuplicate = true; // we say, this will appear in descending
																			// order elsewhere!
											} // if (((opna == 1) && ...
										} // second "for" -- for (int i = 4; (i < depth) && (!isDuplicate); i++)

										// Now test for "extended commutativity" -- answers like 6+6 = 12, 6*6 = 36, 36
										// - 12 = 24 ;
										// and also 6*6 = 36, 6+6 = 12, 36 - 12 = 24 ; in the same batch of answers:
										for (int i = 6; (i <= depth) && (!isDuplicate); i++) {
											o1a = operandTbl[i][5]; // first operand's index in this table
											o2a = operandTbl[i][6]; // second operand's index in this table or -2000 if
																	// unary
											opna = operandTbl[i][4]; // operation, or -1 = original operand with no
																		// operation
											if ((o1a != o2a) && (opna != 9)) // this is a binary operation
												if ((operandTbl[o1a][4] != -1) && (operandTbl[o2a][4] != -1)) // these
																												// values
																												// were
																												// the
																												// result
																												// of
																												// arithmetic
												{
													o3a = Math.max(o1a, o2a); // find which of calcs these happened
																				// later
													int o1o3a = operandTbl[o3a][5]; // get the two operands for o3a
																					// operation
													int o2o3a = operandTbl[o3a][6];
													if ((operandTbl[o1o3a][4] == -1) && (operandTbl[o2o3a][4] == -1)
															&& (operandTbl[o3a][4] != 9)) // has 2 original operands
														if (((o3a == o1a) && (operandTbl[o3a][4] < operandTbl[o2a][4]))
																|| ((o3a == o2a)
																		&& (operandTbl[o3a][4] < operandTbl[o1a][4])))
															isDuplicate = true; // reject this ordering, the right one
																				// will come along!
												} // if ((operandTbl[o1a][4] != -1) &&
										} // for (int i = 6; (i <= depth) && (!isDuplicate); i++)

										if (!isDuplicate) // This one deserves to be tested against recent history!
										{
											for (int i = 0; i < dupTableMax; i++) // check for exact matches against
																					// recent solutions
												if (answerString.equals(dupTable[i])) // if so, then it's also a
																						// duplicate!
													isDuplicate = true;
										} // if (!isDuplicate)
										if (isDuplicate) // Is it a duplicate, after all these tests?
										{
											nduplicates = nduplicates + 1; // if so, just list how many we "removed"
																			// from answer list
											answerString = "";
										} else // process this one
										{
											dupTable[dupTablePointer] = answerString; // save it to check against
																						// duplicates
											dupTablePointer = (dupTablePointer + 1) % dupTableMax;

											if (FlagContainer.insolublesFlag && FlagContainer.extraOpsFlag) // we're
																											// into
																											// second
																											// part of
																											// insolubles
																											// routine
											{
												if (nAnswers - nduplicates <= insolublesDoMax) // only do so many!
													fileReader.writeDetail("+>  " + answerString + "\n", display); // write
																													// the
																													// answer
																													// to
																													// file!
												else // save in "overflow" table, maybe, as interesting examples
												{
													insolTablePointer = (int) (Math
															.pow((nAnswers - nduplicates - insolublesDoMax + 3), (0.4)))
															- 1; // an awkward formula, at best!
													if (insolTablePointer < insolTableMax) // there's room
													{
														insolTable[insolTablePointer] = "+  " + answerString + "\n";
													} // if ... there's room
												} // else save in overflow
											} // if (insolublesFlag ...
											else // normal routine -- always write the answer
												fileReader.writeDetail(">  " + answerString + "\n", display); // write
																												// the
																												// answer
																												// to
																												// file!
										} // else process this one
									} // if (answer == inputValueN)

									else if (depth < maxDepth - 1) // slight chance a unary op will give right answer
																	// next
									{
										if (opn != 9) // setup to go one deeper for just having done a binary opn
										{
											operandTbl[o1][1] = 2; // These operands now used, just in case!
											operandTbl[o2][1] = 2;
											if (FlagContainer.debugFlag == 6)
												System.out.println("1. Calling calculatem at " + (depth + 1) + ", "
														+ (nOperands - 1) + " operands, nUnary = " + nUnary
														+ ", successful opn = " + opn + ".");
											gotAnswer = calculatem(depth + 1, nOperands - 1, nUnary); // go another
																										// round
										} // if (opn != 9)
										else // opn == 9
										{
											operandTbl[o1][1] = 2; // These operands now used, just in case!
											operandTbl[o2][1] = 2;
											operandTbl[o3][1] = 2;
											if (FlagContainer.debugFlag == 6)
												System.out.println("1a. Calling calculatem at " + (depth + 1) + ", "
														+ (nOperands - 2) + " operands, nUnary = " + nUnary
														+ ", successful opn = " + opn + ".");
											gotAnswer = calculatem(depth + 1, nOperands - 2, nUnary); // go another
																										// round
										} // else opn == 9
									} // else if (depth < maxDepth - 1)
									else // else just set this flag for good measure, saying we didn't get anything
											// worth saving
										gotAnswer = false;

								} // if (nOperands <=2)...

								else if (depth < maxDepth - 1) // let this thing call itself recursively
								{

									if (opn != 9) // setup to go one deeper for just having done a binary opn
									{
										operandTbl[o1][1] = 2; // These operands now used, just in case!
										operandTbl[o2][1] = 2;
										if (FlagContainer.debugFlag == 6)
											System.out.println("2. Calling calculatem at " + (depth + 1) + ", "
													+ (nOperands - 1) + "operands, nUnary = " + nUnary
													+ ", successful opn = " + opn + ".");
										gotAnswer = calculatem(depth + 1, nOperands - 1, nUnary); // go another round
									} // if (opn != 9)
									else // opn == 9
									{
										operandTbl[o1][1] = 2; // These operands now used, just in case!
										operandTbl[o2][1] = 2;
										operandTbl[o3][1] = 2;
										if (FlagContainer.debugFlag == 6)
											System.out.println("2a. Calling calculatem at " + (depth + 1) + ", "
													+ (nOperands - 2) + " operands, nUnary = " + nUnary
													+ ", successful opn = " + opn + ".");
										gotAnswer = calculatem(depth + 1, nOperands - 2, nUnary); // go another round
									} // else opn == 9
								} // else if (depth < maxDepth-1)
								operandTbl[o1][1] = 1; // Reset these flags for returning to keep cycling or a higher
														// level!
								operandTbl[o2][1] = 1;
								if (opn == 9) // also re-add operand 3
									operandTbl[o3][1] = 1;
								operandTbl[depth][1] = 2;// and invalidate the one we did, just for cleanliness!
							} // if (gotanswer)
						} // for (int opn = 1; opn <= 4 or whatever; opn++)
					} // if (o2 != o1) && (operandTbl[o2][1] == 1)

					// UNARY ---------------------------VVVVVVVVVVVVVVVVVVVVVVV---------------

					else if ((o2 == o1) && (nUnary < nUnaryMax)) // then we can try unary operators here!
					{
						if (FlagContainer.unaryOperations > 0) {
							for (int opn = FlagContainer.binaryOperations + 1; opn <= FlagContainer.binaryOperations
									+ FlagContainer.unaryOperations; opn++) // ... and just the unary ops here
							{
								if (FlagContainer.debugFlag == 6)
									System.out.println("Ready to try unary operators for operand " + o1 + " = "
											+ operandTbl[o1][0] + " at depth " + depth + ", nOperands = " + nOperands
											+ ", opn = " + opn);
								int op1 = operandTbl[o1][0]; // get the operand
								gotAnswer = true;
								if ((opn == 10) && FlagContainer.sqrtFlag) // sqrt
								{
									if (op1 > 1) // in my opinion, 0 and 1 are silly to do!
									{
										answer = (int) Math.sqrt(op1);
										if (answer * answer != op1) // then op1 was not a perfect sqr
											gotAnswer = false;
									} else // throw it out
										gotAnswer = false;
								} else if ((opn == 11) && FlagContainer.cubertFlag) // cubert
								{
									if (Math.abs(op1) > 1) // not silly to do, in my opinion
									{
										answer = (int) (Math.pow(op1, (1.0 / 3.0)) + 0.0000000000001);
										if (answer * answer * answer != op1) // then op1 was not a perfect cube
											gotAnswer = false;
									} else
										gotAnswer = false;
									/*
									 * if (gotAnswer)
									 * {
									 * System.out.println("Tried cubert of "+op1+" with gotAnswer = "+gotAnswer);
									 * System.out.println("Doing unary operators for operand "+o1+" = "+operandTbl[
									 * o1][0] +" at depth "+depth+", nOperands = "+nOperands+", opn = "+opn);
									 * }
									 */
								} else if ((opn == 12) && FlagContainer.absvalFlag) // abs val
								{
									if (op1 < 0) // avoid the silly ones that are already >=0
										answer = Math.abs(op1);
									else
										gotAnswer = false;
									// System.out.println("Tried abs val of "+op1+" with gotAnswer = "+gotAnswer);
								} else if ((opn == 13) && FlagContainer.factorialFlag) // factorial
								{
									if ((op1 == 0) || ((op1 > 2) && (op1 <= maxFact))) // don't do negs, and 1 & 2 are
																						// silly
									{
										answer = 1;
										for (int i = 2; i <= op1; i++)
											answer = answer * i;
									} // if ((op1 ==0) || (op1 > 1))
									else
										gotAnswer = false;
									// System.out.println("Tried factorial of "+op1+" with gotAnswer = "+gotAnswer);
								} else // fell thru because flag for this opn not set, so no answer for it
									gotAnswer = false;

								if (Math.abs(answer) > maxToCheck) // it might be right but it's too big to be realistic
									gotAnswer = false; // flag it as invalid!

								if (gotAnswer) // we plan to at least investigate the following with this answer:
								// Is it the final answer for this path? If so, save it before going on.
								// If not, should we go deeper pursuing an answer here? If so, call this routine
								// recursively.
								{ // so,
									operandTbl[depth][0] = answer; // save the answer in next operand table slot
									operandTbl[depth][1] = 1; // set as valid operand for next round, if any
									operandTbl[depth][2] = op1; // And now record how we did this calc!
									operandTbl[depth][3] = -2000; // Flag operand 2 as non-existing here
									operandTbl[depth][4] = opn; // operation ID we just did, for reporting
									operandTbl[depth][5] = o1; // show source of operands to show it's a unary operation
									operandTbl[depth][6] = o1; // & maybe weed out duplicates
									operandTbl[depth][7] = 0; // Be sure these last 2 slots are clear --
									operandTbl[depth][8] = 0; // only used with opn = 9, way up above!

									if (nOperands <= 1) // we're possibly done, because used 1 more just now, leaving 1
														// -- an answer
									{
										// if (FlagContainer.debugFlag == 4) // do detailed printouts
										// System.out.println(" & reached end of level 6 with value "+answer);
										if (answer == inputValueN) // Hey -- we got it right, who would've guessed!?
										{ // so, for parsimony, we can stop here, not cycle on unary operators, etc.
											nAnswers = nAnswers + 1;
											isDuplicate = false; // with testing for dupes to follow here
											answerString = ""; // to build answer for displaying or writing to file

											int op1a, op2a, opna, opnb, answera, o1a, o2a, o3a, op3a; // to distinguish
																										// from loop
																										// counters
											for (int i = 4; (i <= depth) && (!isDuplicate); i++) // for now, as results
																									// of calcs, leave
																									// 'em guessing how:
											{
												op1a = operandTbl[i][2]; // recover pieces of each calc, for display
												op2a = operandTbl[i][3];
												opna = operandTbl[i][4];
												answera = operandTbl[i][0];
												o1a = operandTbl[i][5];
												o2a = operandTbl[i][6];
												o3a = operandTbl[i][7];
												op3a = operandTbl[i][8];

												// Check for add or multiply duplicates due to commutativity or repeat
												// of identical operands
												if ((opna == 1 || opna == 3) && ((op1a < op2a)
														|| ((op1a == op2a) && (operandTbl[i][5] < operandTbl[i][6])))) // messy
																														// test!
													isDuplicate = true;
												else if (opna == 9) // trinary averaging operation
													answerString = answerString + operations[opna] + " ( "
															+ String.valueOf(op1a) + ", " + String.valueOf(op2a) + ", "
															+ String.valueOf(op3a) + " ) = " + String.valueOf(answera)
															+ "\n ";
												else if (o1a != o2a) // Append binary operation to answerString :
													answerString = answerString + String.valueOf(op1a) + " "
															+ operations[opna] + " " + String.valueOf(op2a) + " = "
															+ String.valueOf(answera) + "\n ";
												else // Append unary operation to answerString:
													answerString = answerString + operations[opna] + " ( "
															+ String.valueOf(op1a) + " ) = " + String.valueOf(answera)
															+ "\n ";
											} // for

											for (int i = 4; (i < depth) && (!isDuplicate); i++) { // go through loop
																									// again, checking
																									// for assoc law
																									// dups --
												boolean gotADup = true; // hopefully, we'll find one here!
												op3a = -1; // define these here just to make the compiler happy
												o3a = -1000;
												opna = operandTbl[i][4];
												opnb = operandTbl[i + 1][4];
												if (((opna == 1) && (opnb == 1)) || ((opna == 3) && (opnb == 3))) // candidates
																													// to
																													// check
												{
													answera = operandTbl[i][0]; // see if following op builds on first
													if (answera == operandTbl[i + 1][2]) // yes -- first operand
													{
														op3a = operandTbl[i + 1][3]; // then we want to look at the
																						// other operand
														o3a = operandTbl[i + 1][6];
													} // if (answera == operandTbl[i+1][2])
													else if (answera == operandTbl[i + 1][3]) // yes -- second operand
													{
														op3a = operandTbl[i + 1][3];
														o3a = operandTbl[i + 1][5];
													} // else if (answera == operandTbl[i+1][3])
													else // it's not in the scope of this dupe test
														gotADup = false;
													if (gotADup) // keep the investigation going
														if ((op3a > operandTbl[i][3]) || ((op3a == operandTbl[i][3])
																&& o3a > operandTbl[i][6])) // yet another messy test!
															isDuplicate = true; // we say, this will appear in
																				// descending order elsewhere!
												} // if (((opna == 1) && ...
											} // second "for" -- for (int i = 4; (i < depth) && (!isDuplicate); i++)

											// Now test for "extended commutativity" -- answers like 6+6 = 12, 6*6 = 36,
											// 36 - 12 = 24 ;
											// and also 6*6 = 36, 6+6 = 12, 36 - 12 = 24 ; in the same batch of answers:
											for (int i = 6; (i <= depth) && (!isDuplicate); i++) {
												o1a = operandTbl[i][5]; // first operand's index in this table
												o2a = operandTbl[i][6]; // second operand's index in this table or -2000
																		// if unary
												opna = operandTbl[i][4]; // operation, or -1 = original operand with no
																			// operation
												if ((o1a != o2a) && (opna != 9)) // this is a binary operation
													if ((operandTbl[o1a][4] != -1) && (operandTbl[o2a][4] != -1)) // these
																													// values
																													// were
																													// the
																													// result
																													// of
																													// arithmetic
													{
														o3a = Math.max(o1a, o2a); // find which of calcs these happened
																					// later
														int o1o3a = operandTbl[o3a][5]; // get the two operands for o3a
																						// operation
														int o2o3a = operandTbl[o3a][6];
														if ((operandTbl[o1o3a][4] == -1) && (operandTbl[o2o3a][4] == -1)
																&& (operandTbl[o3a][4] != 9)) // has 2 original operands
															if (((o3a == o1a)
																	&& (operandTbl[o3a][4] < operandTbl[o2a][4]))
																	|| ((o3a == o2a)
																			&& (operandTbl[o3a][4] < operandTbl[o1a][4])))
																isDuplicate = true; // reject this ordering, the right
																					// one will come along!
													} // if ((operandTbl[o1a][4] != -1) &&
											} // for (int i = 6; (i <= depth) && (!isDuplicate); i++)

											if (!isDuplicate) // This one deserves to be tested against recent history!
											{
												for (int i = 0; i < dupTableMax; i++) // check for exact matches against
																						// recent solutions
													if (answerString.equals(dupTable[i])) // if so, then it's also a
																							// duplicate!
														isDuplicate = true;
											} // if (!isDuplicate)
											if (isDuplicate) // Is it a duplicate?
											{
												nduplicates = nduplicates + 1; // if so, just list how many we "removed"
																				// from answer list
												answerString = "";
											} else // process this one
											{
												dupTable[dupTablePointer] = answerString; // save it to check against
																							// duplicates
												dupTablePointer = (dupTablePointer + 1) % dupTableMax;

												if (FlagContainer.insolublesFlag && FlagContainer.extraOpsFlag) // we're
																												// into
																												// second
																												// part
																												// of
																												// insolubles
																												// routine
												{
													if (nAnswers - nduplicates <= insolublesDoMax) // only do so many!
														fileReader.writeDetail("+>  " + answerString + "\n", display); // write
																														// the
																														// answer
																														// to
																														// file!

													else // save in "overflow" table, maybe, as interesting examples
													{
														insolTablePointer = (int) (Math.pow(
																(nAnswers - nduplicates - insolublesDoMax + 3), (0.4)))
																- 1; // an awkward formula, at best!
														if (insolTablePointer < insolTableMax) // there's room
														{
															insolTable[insolTablePointer] = "+  " + answerString + "\n";
														} // if ... there's room
													} // else save in overflow

												} // if (insolublesFlag...
												else // normal routine -- always write the answer
													fileReader.writeDetail(">  " + answerString + "\n", display); // write
																													// the
																													// answer
																													// to
																													// file!
											} // else process this one

										} // if (answer == inputValueN)

										else if (depth < maxDepth - 1) // slight chance another unary op will give right
																		// answer next
										{
											operandTbl[o1][1] = 2; // Mark this operand now used, just in case!
											if (FlagContainer.debugFlag == 6)
												System.out.println("3. Calling calculatem at " + (depth + 1) + ", "
														+ (nOperands) + "operands, nUnary = " + nUnary
														+ ", successful opn = " + opn + ".");
											gotAnswer = calculatem(depth + 1, nOperands, nUnary + 1); // go another
																										// round, same
																										// no. of
																										// operands
																										// here!
										} else // else just set this flag for good measure, saying we didn't get
												// anything worth saving
											gotAnswer = false;

									} // if (nOperands <=2)

									else if (depth < maxDepth - 1) // let this thing call itself recursively
									{
										operandTbl[o1][1] = 2; // Mark this operand now used, just in case!
										if (FlagContainer.debugFlag == 6)
											System.out.println("4. Calling calculatem at " + (depth + 1) + ", "
													+ (nOperands) + "operands, nUnary = " + nUnary
													+ ", successful opn = " + opn + ".");
										gotAnswer = calculatem(depth + 1, nOperands, nUnary + 1); // go another round,
																									// same no. of
																									// operands here!
									}

									operandTbl[o1][1] = 1; // Reset this flag for returning to keep cycling or a higher
															// level!
									operandTbl[depth][1] = 2;// and invalidate the one we did, just for cleanliness!
								} // if (gotanswer)

							} // for (int opn = binary...
						} // if (unaryOperations > 0)
					} // else if (o2 == o1)
				} // mid-level for!
			} // if (operandTbl[o1][1] == 1)
		} // top-level for!
		return (gotAnswer);
	} // calculatem()

	// --------------------------------------------------------------

	public void printOperandTbl() { // debug routine to display the whole table:
		for (int i = 0; i < 7; i++)
			System.out.println("     " + operandTbl[i][0] + " " + operandTbl[i][1]);
	} // printOperandTbl
}
