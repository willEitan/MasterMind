import java.util.Scanner;

public class Mastermind {
    public static void main(String[] args) {
        Scanner codeMasterResponder = new Scanner(System.in);
        GameConfigurations gameConfiguration = new GameConfigurations();
        gameConfiguration.welcome(codeMasterResponder);
        CodeBreaker codeBreaker = new CodeBreaker(gameConfiguration);
        playGame(codeMasterResponder, codeBreaker);
        codeMasterResponder.close();
    }

    protected static void playGame(Scanner codeMasterResponder, CodeBreaker codeBreaker) {
        GameConfigurations gc = codeBreaker.getGameConfiguration();
        boolean isValidGame = true;
        boolean isQuit = false;
        boolean isWon = false;
        int guesses = 0;
        String guess = gc.getFirstGuess();

        do {
            System.out.println();
            CodeMasterResponse response = codeBreaker.guessAndRespond(guess, codeMasterResponder);

            if (!response.isQuit()) {
                if (response.getWhitePegs() == gc.getPegcount()) {
                    isWon = true;
                }
                else {
                    codeBreaker.trimPossibilities(response, guess);
                    if (codeBreaker.getAvailableOptions() == 0) {
                        isValidGame = false;
                    }
                    else {
                        guess = codeBreaker.nextBestGuess();
                    }
                }
            }
            else {
                isQuit = true;
            }
            guesses++;

        } while (isValidGame && !isQuit && !isWon && guesses < gc.getGuessCount());
        
        endGame(isValidGame, isQuit, isWon, guesses, gc);
    }


    protected static void endGame(boolean isValidGame, boolean isQuit, boolean isWon, int guesses, GameConfigurations gameConfigurations) {
        if (isQuit) {
            System.out.println("Game exited after " + guesses + " guesses. Play again soon.");
        }
        else {
            if (isValidGame) {
                if (isWon) {
                    System.out.println("The code breaker won this game in " + guesses + " guesses. Better luck next time!");
                }
                else {
                    System.out.println("Congratulations! You, the code master, won this game. The code breaker was unable to guess your secret code.");
                }
            }
            else {
                System.out.print("There are no possible solutions. The code master's code must have " + gameConfigurations.getPegcount() + " pegs using only these colors:");
                gameConfigurations.displayPegColors();
            }
        }
    }
}