import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CodeBreaker {
    private final GameConfigurations gameConfiguration;
    private final List<String> masterList;
    private List<String> codeList;
    private List<String> guessList;

    public CodeBreaker() {
        this.gameConfiguration = new GameConfigurations();
        masterList = generateMasterList();
        codeList = masterList;
        guessList = new ArrayList<>();
    }

    public CodeBreaker(GameConfigurations gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
        masterList = generateMasterList();
        codeList = masterList;
        guessList = new ArrayList<>();
    }

    public List<String> generateMasterList() {
        List<String> masterList = new ArrayList<>();
        char[] letters = new char[gameConfiguration.getNumberOfColors()];

        for(int i = 65; i < 65 + gameConfiguration.getNumberOfColors(); i++) {
            char letter = (char)i;
            letters[i - 65] = letter;
        }

        permuteAllCodeCombinations(masterList, letters, "", gameConfiguration.getPegcount());
        return masterList;
    }
    
    static void permuteAllCodeCombinations(List<String> resultList, char[] letters, String codePrefixed, int codeLength) {
        if (codeLength == 0) {
            resultList.add(codePrefixed);
            return;
        }

        for(int i = 0; i < letters.length; i++) {
            String newPrefix = codePrefixed + Character.toString(letters[i]);
            permuteAllCodeCombinations(resultList, letters, newPrefix, codeLength - 1);
        }
    }

    public CodeMasterResponse guessAndRespond(String guess, Scanner inputScanner) {
        int whitePegResponse = -1;
        int coloredPegResponse = -1;
        
        System.out.println("The code breaker guesses: " + convertGuessStringToColors(guess));
        guessList.add(guess);

        do {
            System.out.println("How many code breaker pegs are in the right position (small white pegs)?");
            System.out.print("> ");
            if (inputScanner.hasNext("q") || inputScanner.hasNext("quit") || inputScanner.hasNext("exit")) {
                return new CodeMasterResponse(0, 0, true);
            }
            try {
                whitePegResponse = inputScanner.nextInt();
            }
            catch(Exception e) {
                inputScanner.nextLine();
                whitePegResponse = -1;
                System.out.println("Invalid input");
            }
        } while (whitePegResponse < 0 || whitePegResponse > guess.length());
        
        if (whitePegResponse != guess.length()) {
            do {
                System.out.println("How many code breaker pegs are the right color but wrong position (small colored pegs)?");
                System.out.print("> ");
                if (inputScanner.hasNext("q") || inputScanner.hasNext("quit") || inputScanner.hasNext("exit")) {
                    return new CodeMasterResponse(0, 0, true);
                }
                try {
                    coloredPegResponse = inputScanner.nextInt();
                }
                catch(Exception e) {
                    inputScanner.nextLine();
                    coloredPegResponse = -1;
                    System.out.println("Invalid input");
                }
            } while (coloredPegResponse < 0 || coloredPegResponse > guess.length() || coloredPegResponse + whitePegResponse > guess.length());
        }
        
        return new CodeMasterResponse(whitePegResponse, coloredPegResponse);
    }

    public void trimPossibilities(CodeMasterResponse codeMasterResponse, String guess) {
        List<String> toRemove = new ArrayList<>();
        for (int i = 0; i < codeList.size(); i++) {
            if (!isPossible(codeList.get(i), codeMasterResponse, guess)) {
                toRemove.add(codeList.get(i));
            }
        }

        for(int i = 0; i < toRemove.size(); i++) {
            codeList.remove(toRemove.get(i));
        }

        //codeList.forEach(code -> System.out.print(code + " "));
    }

    private boolean isPossible(String entry, CodeMasterResponse codeMasterResponse, String guess) {
        int rightPosition = codeMasterResponse.getWhitePegs();
        int rightColor = codeMasterResponse.getColoredPegs();
        int wrongPeg = guess.length() - rightPosition - rightColor;
        StringBuilder subGuess = new StringBuilder(guess);

        for (int i = 0; i < guess.length(); i++) {
            if (guess.charAt(i) == entry.charAt(i)) {
                if (rightPosition > 0) {
                    rightPosition--;
                }
                subGuess.setCharAt(i, '*');
            }
        }

        for (int i = 0; i < subGuess.length(); i++) {
            if (subGuess.toString().contains(entry.charAt(i) + "")) {
                if(rightColor > 0) {
                    rightColor--;
                }
                subGuess.setCharAt(subGuess.indexOf(Character.toString(entry.charAt(i))), '*');
            }
            else {
                wrongPeg--;
            }
        }

        if (rightPosition > 0 || rightColor > 0 || wrongPeg > 0) {
            return false;
        }
        else {
            return true;
        }
    }

    public String nextBestGuess() {
        return minMax();
    }

    private String minMax() {
        Integer min = Integer.MIN_VALUE;
        Integer max = Integer.MIN_VALUE;
        String bestGuess = new String();

        for (String guess : masterList) {
            if(!guessList.contains(guess)) {
                int[][] minMaxRadix = new int[guess.length() + 1][guess.length() + 1];
            
                for (String possibleSolution : codeList) {
                    CodeMasterResponse conjected = theoraticalResponse(possibleSolution, guess);
                    minMaxRadix[conjected.getWhitePegs()][conjected.getColoredPegs()]++;
                }

                max = Integer.MIN_VALUE;
                
                for (int[] row : minMaxRadix) {
                    for (int i : row) {
                        max = Integer.max(i, max);
                    }
                }

                int score = codeList.size() - max;
                if (score > min) {
                    min = score;
                    bestGuess = guess;
                }
            }
        }

        return bestGuess;
    }

    private CodeMasterResponse theoraticalResponse(String secert, String guess) {
        int whitePegs = 0;
        int coloredPegs = 0;

        for (int i = 0; i < secert.length(); i++) {
            String subSecert = secert.substring(0, i + 1);
            if (secert.charAt(i) == guess.charAt(i)) {
                whitePegs++;
            }
            else if (subSecert.contains(guess.charAt(i) + "")) {
                coloredPegs++;
            }
        }

        return new CodeMasterResponse(whitePegs, coloredPegs);
    }

    private String convertGuessStringToColors(String alphGuess) {
        String colors = new String();
        for(int i = 0; i < alphGuess.length(); i++) {
            colors += gameConfiguration.getColor(alphGuess.charAt(i)) + " ";
        }
        return colors;
    }

    public int getAvailableOptions() {
        return codeList.size();
    }

    public GameConfigurations getGameConfiguration() {
        return gameConfiguration;
    }

}