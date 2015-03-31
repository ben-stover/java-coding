package marsrovers;

/*
 * Copyright (C) 2012 Ben Stover
 */

public class RoverCommandSetProcessor 
{   
    //-----------fields-------------
    private int numberOfRoverCommandSets;
    private int plateauUpperRightXCoord;
    private int plateauUpperRightYCoord;    
    private StringBuilder commandString;    
    private String posBearing = "";
    private final char SPACE_CHAR_DELIMITER = ' '; 
    private final int X_ORIG = 0;
    private final int Y_ORIG = 0;
    private static int lastCommandIndex = 0;    
    private static int numberOfRoverCommandSetsProcessed = 0; // good to know for future enhancements
    private int spaceDelimiters = 0;
    private int coordXState = 0;
    private int coordYState = 0;
    private String holdForConversionToInteger = "";
    
    
    //-----------methods------------
    public RoverCommandSetProcessor(int platX, int platY, StringBuilder comStr, int numRovCommSets) 
    {
        numberOfRoverCommandSets = numRovCommSets;
        plateauUpperRightXCoord = platX;
        plateauUpperRightYCoord = platY;        
        commandString = comStr;                
    }
    
    
    public String executeCommands()
    {   
        int roverX = 0;
        int roverY = 0;
        char bearing = ' '; // a none important value setting for handling an initialization warning
        int len = commandString.length() - 1;
        
        
        //System.out.println(commandString); //  comment this testing code line out later
        
        for(; lastCommandIndex <= len; lastCommandIndex++)
        {
            switch(commandString.charAt(lastCommandIndex))
            {                
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':                    
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    
                   if(coordXState == 0 && spaceDelimiters == 0)
                        {                   
                            holdForConversionToInteger += String.valueOf(commandString.charAt(lastCommandIndex));
                            //System.out.println("command " + lastCommandIndex + "> " + " Rover " + numberOfRoverCommandSetsProcessed + " : first value from commandString = " + String.valueOf(commandString.charAt(lastCommandIndex))); //  comment this testing code line out later                            
                            
                        }

                   else if(coordYState == 0 && coordXState == 0 && spaceDelimiters == 1)
                        {                   
                            roverX = Integer.parseInt(holdForConversionToInteger);
                            holdForConversionToInteger = ""; // reset for roverY coord                           
                            holdForConversionToInteger += String.valueOf(commandString.charAt(lastCommandIndex));
                            //System.out.println("command " + lastCommandIndex + "> " + " Rover " + numberOfRoverCommandSetsProcessed + ": Starting x coord = " + roverX); //  comment this testing code line out later                                                                                                                
                            coordYState = 1;
                            coordXState = 1;                            
                        }                   
                       break;
                    
                case SPACE_CHAR_DELIMITER:                     
                        
                    spaceDelimiters++;      
                    
                    if(coordXState == 1 && coordYState == 1 && spaceDelimiters == 1)
                        {               
                            holdForConversionToInteger += String.valueOf(commandString.charAt(lastCommandIndex));                            
                        }
                   
                   else if(coordYState == 1 && spaceDelimiters == 2)
                        { 
                            roverY = Integer.parseInt(holdForConversionToInteger);
                            holdForConversionToInteger = ""; // reset anyways for good measure;                                
                            //System.out.println("command " + lastCommandIndex + "> " + " Rover " + numberOfRoverCommandSetsProcessed + ": Starting y coord = " + roverY); //  comment this testing code line out later                            
                            coordXState = 0; // reset for next rover command set coords
                            coordYState = 0; // reset for next rover command set coords
                        }                       

                    if(spaceDelimiters == 4)
                    {
                        numberOfRoverCommandSetsProcessed++;                    
                        spaceDelimiters = 0;
                        posBearing += "\n" + roverX + " " + roverY + " " + bearing + " \n";
                        roverX = 0;
                        roverY = 0;                        
                    }
                    
                    //System.out.println("command " + lastCommandIndex + "> " + " Rover " + numberOfRoverCommandSetsProcessed + ": spaceDelimiter = " + spaceDelimiters); //  comment this testing code line out later                    
                    break;
                    
                case 'N':
                case 'n':
                    bearing = 'N';
                    //System.out.println("command " + lastCommandIndex + "> " + " Rover " + numberOfRoverCommandSetsProcessed + ": Initial bearing set to " + String.valueOf(bearing)); //  comment this testing code line out later
                    break;
                    
                case 'E':
                case 'e':
                    bearing = 'E';
                    //System.out.println("command " + lastCommandIndex + "> " + " Rover " + numberOfRoverCommandSetsProcessed + ": Initial bearing set to " + String.valueOf(bearing)); //  comment this testing code line out later
                    break;
                    
                case 'W':
                case 'w':
                    bearing = 'W';
                    //System.out.println("command " + lastCommandIndex + "> " + " Rover " + numberOfRoverCommandSetsProcessed + ": Initial bearing set to " + String.valueOf(bearing)); //  comment this testing code line out later
                    break;
                
                case 'S':                    
                case 's':
                    bearing = 'S';
                    //System.out.println("command " + lastCommandIndex + "> " + " Rover " + numberOfRoverCommandSetsProcessed + ": Initial bearing set to " + String.valueOf(bearing)); //  comment this testing code line out later
                    break;
                
                case 'L':                    
                case 'l':                    
                    
                    switch(bearing)
                    {
                        case 'N':
                        case 'n':
                            bearing = 'W';                        
                            break;

                        case 'E':
                        case 'e':
                            bearing = 'N';                            
                            break;

                        case 'W':
                        case 'w':
                            bearing = 'S';                            
                            break;

                        case 'S':                    
                        case 's':
                            bearing = 'E';                            
                            break;
                    }                    
                    //System.out.println("command " + lastCommandIndex + "> " + " Rover " + numberOfRoverCommandSetsProcessed + ": Turned 90 degrees left - now bearing " + String.valueOf(bearing)); //  comment this testing code line out later
                    break;                    

                case 'R':    
                case 'r':
                                        
                    switch(bearing)
                    {
                        case 'N':
                        case 'n':
                            bearing = 'E';                        
                            break;

                        case 'E':
                        case 'e':
                            bearing = 'S';                            
                            break;

                        case 'W':
                        case 'w':
                            bearing = 'N';                            
                            break;

                        case 'S':                    
                        case 's':
                            bearing = 'W';                            
                            break;
                    }                    
                    //System.out.println("command " + lastCommandIndex + "> " + " Rover " + numberOfRoverCommandSetsProcessed + ": Turned 90 degrees right - now bearing " + String.valueOf(bearing)); //  comment this testing code line out later
                    break;
                    
                case 'M':                    
                case 'm':
                    switch(bearing)
                    {// also testing for falling off plateau and correct if needed by backing up one grid space and spinning 180 degrees - part of assumptions
                        case 'N':
                        case 'n':
                            roverY++;
                            if(roverY > plateauUpperRightYCoord)
                                {
                                    roverY--;
                                    bearing = 'S';
                                }
                            break;

                        case 'E':
                        case 'e':
                            roverX++;
                            if(roverX > plateauUpperRightXCoord)
                                {
                                    roverX--;
                                    bearing = 'W';
                                }
                            break;

                        case 'W':
                        case 'w':
                            roverX--;
                            if(roverX < X_ORIG)
                                {
                                    roverX++;
                                    bearing = 'E';
                                }
                            break;

                        case 'S':                    
                        case 's':
                            roverY--;
                            if(roverY < Y_ORIG)
                                {
                                    roverY++;
                                    bearing = 'N';
                                }
                            break;
                    }
                    //System.out.println("command " + lastCommandIndex + "> " + " Rover " + numberOfRoverCommandSetsProcessed + ": moving " + String.valueOf(bearing) + " one grid location to (" + roverX + "," + roverY + ")" ); //  comment this testing code line out later                    
                    break;                    
                    
                default:                    
                    break;// just ignore bad commands for now
            }
        }        
        return posBearing;
    }
    
} // end class RoverCommandSetProcessor
