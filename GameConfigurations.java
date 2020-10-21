import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class GameConfigurations {
    private static final List<String> fourPegColors = new ArrayList<>(List.of("BLACK", "BLUE", "GREEN", "RED", "WHITE", "YELLOW"));
    private static final List<String> fivePegColors = new ArrayList<>(List.of("BLACK", "BLUE", "GREEN", "ORANGE", "PURPLE", "RED", "WHITE", "YELLOW"));
    private List<String> pegs;
    private int pegCount;
    private int guessCount;
    private String firstGuess;
    private HashMap<String, String> colorLookup;

    public GameConfigurations () {
        this.setPegs(fivePegColors);
        this.setPegcount(5);
        this.setGuessCount(12);
        this.populateColorLookup();
        this.firstGuess = "ABCDC";
    }

    public void welcome(Scanner inputScanner) {
        System.out.println("\nWelcome to your Mastermind code breaker assitent!");
        System.out.println("For game rules enter 'R' or enter 'P' to play");
        System.out.print("> ");        

        String selection = inputScanner.nextLine();
        
        if (selection.startsWith("R") || selection.startsWith("r")) {
            printRules();
        }

        promptForGameType(inputScanner);
    }

    private static void printRules() {
        System.out.println("For rule description, visit https://en.wikipedia.org/wiki/Mastermind_(board_game)#Gameplay_and_rules");
    }

    public void promptForGameType(Scanner inputScanner) {
        int selection = -1;
        do {
            System.out.println("Will you be playing a 4 peg (6 color) or a 5 peg (8 color) game?");
            System.out.print("> ");
            
            try {
                selection = inputScanner.nextInt();
            }
            catch(Exception e) {
                inputScanner.nextLine();
                selection = -1;
                System.out.println("Invalid input");
            }
        } while (selection != 4 && selection != 5);
        
        if (selection == 4) {
            this.setPegcount(4);
            this.setPegs(fourPegColors);
            this.setGuessCount(10);
            this.setFirstGuess("AABB");
        } else if (selection == 5){
            this.setPegcount(5);
            this.setPegs(fivePegColors);
            this.setGuessCount(12);
            this.setFirstGuess("ABCDC");
        }
        else {
            System.out.println("Unrecognized input. Defaulting to 5 peg game.");
            this.setPegcount(5);
            this.setPegs(fivePegColors);
            this.setGuessCount(12);
            this.setFirstGuess("ABCDC");
        }
        this.populateColorLookup();

        System.out.print("The colors for this " + getPegcount() + " peg game are:");
        pegs.forEach(color -> System.out.print(color + " "));
        System.out.println("\nBlank pegs are not allowed.\n\nThe code breaker will have " + getGuessCount() + " tries to guess the code master's secret code. Enter 'q' to quit anytime.");
    }

    private void populateColorLookup() {
        colorLookup = new HashMap<>();
        for(int i = 65; i < 65 + getNumberOfColors(); i++) {
            char letter = (char)i;
            colorLookup.put(Character.toString(letter), pegs.get(i - 65));
        }
    }

    public void displayPegColors() {
        pegs.forEach(color -> System.out.print(color + " "));
        System.out.println();
    }

    public int getNumberOfColors() {
        return pegs.size();
    }

    public List<String> getPegs() {
        return pegs;
    }

    public void setPegs(List<String> pegs) {
        this.pegs = pegs;
    }

    public int getPegcount() {
        return pegCount;
    }

    public void setPegcount(int pegcount) {
        this.pegCount = pegcount;
    }

    public int getGuessCount() {
        return guessCount;
    }

    public void setGuessCount(int guessCount) {
        this.guessCount = guessCount;
    }

    public String getColor(char hashIndex) {
        return colorLookup.get(Character.toString(hashIndex));
    }

    public String getFirstGuess() {
        return firstGuess;
    }

    public void setFirstGuess(String firstGuess) {
        this.firstGuess = firstGuess;
    }
}