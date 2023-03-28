package src.Sorter;

public class BubbleSorter extends Sorter{
    
    @Override
    public void sort(int statsPointer, int statsArray[][]) {
        System.out.println(" Sorting table...");
        int temp0, temp1, temp2;
        for (int pass = 1; pass < statsPointer; pass++)
            for (int pair = 1; pair < statsPointer; pair++)
                if ((statsArray[pair - 1][1] - statsArray[pair - 1][2]) > (statsArray[pair][1] - statsArray[pair][2])) // compare
                                                                                                                       // the
                                                                                                                       // non-duplicated
                                                                                                                       // answers
                {
                    temp0 = statsArray[pair - 1][0];
                    temp1 = statsArray[pair - 1][1];
                    temp2 = statsArray[pair - 1][2];
                    statsArray[pair - 1][0] = statsArray[pair][0];
                    statsArray[pair - 1][1] = statsArray[pair][1];
                    statsArray[pair - 1][2] = statsArray[pair][2];
                    statsArray[pair][0] = temp0;
                    statsArray[pair][1] = temp1;
                    statsArray[pair][2] = temp2;
                } // if
        System.out.println(" Done sorting table...");
    } // bubbleSort()
}
