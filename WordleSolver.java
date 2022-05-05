///////////////////////////////// FILE HEADER /////////////////////////////////
//
// Title: HomeMade Wordle - WordleSolver.java
//
// Author: Bahulya Tandon
// Email: btandon2@wisc.edu
//
///////////////////////////////////////////////////////////////////////////////

import java.util.ArrayList;
import java.util.Random;

public class WordleSolver extends Wordle {
  static ArrayList<String> possibleGuesses;
  static Random randGen = new Random();

  public static String guess(ArrayList<String> guesses) {
    return guesses.get(randGen.nextInt(guesses.size())).toLowerCase();
  }

  public static ArrayList<String> modifyPossibleGuesses(ArrayList<String> guesses, String guess,
      String answer) {
    int[] guessCorrectness = Wordle.checkGuess(guess, answer);
    
    for (int i = 0; i < guessCorrectness.length; i++) {
      if (guessCorrectness[i] == 2) {
        for (int j = 0; j < guesses.size(); j++) {
          if (guesses.get(j).indexOf(String.valueOf(guess.charAt(i))) == i) {
          } else {
            guesses.remove(j);
            j--;
          }
        }
      }

      else if (guessCorrectness[i] == 1) {
        for (int j = 0; j < guesses.size(); j++) {
          if (guesses.get(j).indexOf(String.valueOf(guess.charAt(i))) != i
              && guesses.get(j).indexOf(String.valueOf(guess.charAt(i))) != -1) {
          } else {
            guesses.remove(j);
            j--;
          }
        }
      }

      /**
       * if there is a uncolored character, search through the guess to see if there is the same
       * character in a color
       */

      if (guessCorrectness[i] == 0) {
        int countLetterInGuess = 0;
        char letter = guess.charAt(i);
        int countLetterInAnswer = 0;
        for (int j = 0; j < guess.length(); j++) {
          if (letter == guess.charAt(j)) {
            countLetterInGuess++;
          }

          if (letter == guess.charAt(j) && guessCorrectness[j] != 0) {
            countLetterInAnswer++;
          }
        }
        if (countLetterInGuess == 1 || countLetterInAnswer == 0) {
          for (int k = 0; k < guesses.size(); k++) {
            if (guesses.get(k).indexOf(String.valueOf(letter)) != -1) {
              guesses.remove(k);
              k--;
            }
          }
        } else {
          for (int k = 0; k < guesses.size(); k++) {
            int indexOfLetter = -1;
            for (int l = 0; l < countLetterInAnswer + 1; l++) {
              indexOfLetter =
                  guesses.get(k).substring(indexOfLetter + 1).indexOf(String.valueOf(letter));
            }
            if (indexOfLetter != -1) {
              guesses.remove(k);
              k--;
            }
          }
        }
      }
    }
    return guesses;
  }

  public static void main(String[] args) {
    Wordle.createLists();
    String wordleAnswer = Wordle.chooseAnswer().toLowerCase();
    possibleGuesses = Wordle.wordleGuesses;
    String wordleGuess = "";
    System.out.println("Answer: " + wordleAnswer);
    for (int i = 1; i < 7; i++) {
      wordleGuess = guess(possibleGuesses);
      Wordle.display(wordleGuess, wordleAnswer);
      if (Wordle.win == true) {
        break;
      }
      possibleGuesses = modifyPossibleGuesses(possibleGuesses, wordleGuess, wordleAnswer);
      System.out.print(possibleGuesses.size());
      System.out.println(" possible guesses left.");
    }
  }
}
