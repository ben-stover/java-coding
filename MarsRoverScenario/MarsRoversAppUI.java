package marsrovers;

/*
 * Copyright (C) 2012 Ben Stover
 */

import java.util.Scanner;

public class MarsRoversAppUI 
{        
    public String requestCommandFileNameFromUser() // Done.
    {   
        String comFile = null;
        
        Scanner scnr = new Scanner(System.in);

        System.out.print("Please enter the path and file name of the rovers command file: ");

        if(scnr.hasNext())
        {
            comFile = scnr.nextLine();
        }            

        scnr.close();
        
        return comFile;
    } // end function requestCommandFile
    
    
    public void displayRoverPositionAndDirection(String roverPosBear)  // Done.
    {
        System.out.println("\nFinal rover positions and bearings:\n" + roverPosBear);
    }
} // end class MarsRoversAppUI
