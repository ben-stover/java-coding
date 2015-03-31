package marsrovers;

/*
 * Copyright (C) 2012 Ben Stover
 */

import java.io.*;
import java.util.regex.*;

public class MarsRoversCommander 
{   
    //-----------fields-------------    
    private int plateauCoordX = 0;
    private int plateauCoordY = 0;    
    private int numberOfRoverCommandSets = 0;
    private StringBuilder unparsedRoversCommandString = new StringBuilder(); // holds command file contents without extra blank lines
    private StringBuilder onlyRoverCommandSets = new StringBuilder(); // holds valid sets of rover commands ready to be assigned to an array for rovers
    private String commandFile = null;    
    private BufferedReader br = null;    
    private MarsRoversAppUI MRAppUI = new MarsRoversAppUI();
    
    private final char SPACE_CHAR_DELIMITER = ' '; // listed here to be available for future methods
    private final int MINIMUM_COMMAND_STRING_SIZE = 8; // ex.: '1 3 W L ' = eight(8) character positions including spaces
    private final String PLATEAU_COORDS_SET_REGEX = "\\d+?\\s\\d+?\\s"; // find plateau coords
    private final String ROVER_COORDS_BEARING_COMMANDS_SET_REGEX = "\\d+?\\s\\d+?\\s[NEWSnews]+?\\s[LRMlrm]+?\\s"; // find one rover command set - 
                                                                                                // includes postion coords, bearing/ heading
                                                                                                //  & movements for one rover
    //-----------methods------------
    public MarsRoversCommander() // Done.
    {
        System.out.println("Welcome To The Mars Rovers Command Application \n");
    } // end constructor function MarsRoversCommander
    
    
    private void openCommandFile() throws IOException // Done.
    {           
        String str;
        
        br = new BufferedReader(new FileReader(commandFile));        
        
        str = br.readLine();        
        while(str != null)
        {   
            unparsedRoversCommandString.append(str);
            str = br.readLine();
        }
        br.close();        
    } // end function openCommandFile
     
          
    private void prepareCommandsForRoverCommandSetProcessor() // Done.
    {           
        int startOfPatternMatch = 0;
        int endOfPatternMatch = 0;
        boolean matchSuccessStatus = false; // needed per Matcher's find() function return requirements                                        
        CharSequence cs = unparsedRoversCommandString.subSequence(0, unparsedRoversCommandString.length()); // needed for regex operations
        int csLen = cs.length();        
        Pattern plateauXY = Pattern.compile(PLATEAU_COORDS_SET_REGEX);
        Matcher plateauXY_Matcher = plateauXY.matcher(cs);
        matchSuccessStatus = plateauXY_Matcher.find();
        startOfPatternMatch = plateauXY_Matcher.start();
        endOfPatternMatch = plateauXY_Matcher.end();
        char[] unparsedPlateauCoordsCharArray = unparsedRoversCommandString.substring(startOfPatternMatch, endOfPatternMatch).toCharArray();
        String holdForConversionToInteger = "";
        int spaceDelimiterFoundState = 0; // one(1): now parse and store X coord - two(2): now parse and store Y coord
        final int ONE_CHAR_ELEMENT = 1;
        
        // parse XY coords then assign them to member fields: plateauCoordX and plateauCoordY
        for(int i = 0; i <= unparsedPlateauCoordsCharArray.length - 1; i++)
        {
            if(unparsedPlateauCoordsCharArray[i] != SPACE_CHAR_DELIMITER)
            {
                holdForConversionToInteger += String.valueOf(unparsedPlateauCoordsCharArray, i, ONE_CHAR_ELEMENT);                                
            }
            else
            {
                spaceDelimiterFoundState++;
                if(spaceDelimiterFoundState == 1)
                {
                    plateauCoordX = Integer.parseInt(holdForConversionToInteger);
                    holdForConversionToInteger = "";
                }
                else
                {
                    plateauCoordY = Integer.parseInt(holdForConversionToInteger);
                }
            }
        }
                        
        Pattern roverCommands = Pattern.compile(ROVER_COORDS_BEARING_COMMANDS_SET_REGEX);
        Matcher roverCommandsMatcher = roverCommands.matcher(cs);        
        
        // count up number of rovers based on correctly matching command sets
        do
        {                
            matchSuccessStatus = roverCommandsMatcher.find();                     
            startOfPatternMatch = roverCommandsMatcher.start();
            endOfPatternMatch = roverCommandsMatcher.end();        
            onlyRoverCommandSets.append(unparsedRoversCommandString.substring(startOfPatternMatch, endOfPatternMatch));            
            numberOfRoverCommandSets++;
        }
        while((csLen - endOfPatternMatch) >= MINIMUM_COMMAND_STRING_SIZE);       
        
    } // end function prepareCommandsForRoverCommandSetProcessor    
    
    
    private void sendCommandsToRoverCommandSetProcessor() // Done.
    {                
        String finalPositionAndBearing = "";
        RoverCommandSetProcessor rover = new RoverCommandSetProcessor(plateauCoordX, plateauCoordY, onlyRoverCommandSets, numberOfRoverCommandSets);
        finalPositionAndBearing = rover.executeCommands();
        MRAppUI.displayRoverPositionAndDirection(finalPositionAndBearing);                    
    } // end function sendCommandsToRoverCommandSetProcessor
    
           
    public void run() // Done.
    {        
        commandFile = MRAppUI.requestCommandFileNameFromUser();
        try
        {
            this.openCommandFile();
        }
        catch (IOException e)
        {
            System.out.println("\n\nSomething is wrong with the file path and/or file name.\n"
                    + "Please make sure they exist and are typed in correctly.\nProgram will now exit.\n\n");            
            System.exit(1);
        }        
        this.prepareCommandsForRoverCommandSetProcessor();
        this.sendCommandsToRoverCommandSetProcessor();
    }  // end function run
        
    
    
    //----------------MAIN------------------
    public static void main(String[] args) throws IOException // Done.
    {        
        MarsRoversCommander MR = new MarsRoversCommander();
        MR.run();
    }
        
    
} // end class MarsRoversCommander