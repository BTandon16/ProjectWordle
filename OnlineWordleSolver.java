///////////////////////////////// FILE HEADER /////////////////////////////////
//
// Title: Project Wordle - OnlineWordleSolver.java
//
// Author: Bahulya Tandon
// Email: btandon2@wisc.edu
//
///////////////////////////////////////////////////////////////////////////////

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * An automatic wordle solver which provides the possible guesses of a word and accepts responses
 * in the correctness of the word. Responds until the correct answer is achieved
 * @author Bahulya Tandon
 *
 */
public class OnlineWordleSolver extends Wordle {
  static ArrayList<String> possibleGuesses;
  static Random randGen = new Random();
  static Scanner scnr = new Scanner(System.in);
  static char[] chars = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
      'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
  
  public static String guess(ArrayList<String> guesses, int[] charProb) {
    int[] wordProb = new int[guesses.size()];
    
    for (int i = 0; i < guesses.size(); i++) {
      for (int j = 0; j < 5; j++) {
        for (int k = 0; k < chars.length; k++) {
          if (chars[k] == guesses.get(i).charAt(j)) {
            wordProb[i] += charProb[k];
          }
        }
      }
    }
    int wordIndex = 0;
    int maxProb = wordProb[0];
    for (int i = 1; i < wordProb.length; i++) {
      if (maxProb < wordProb[i]) {
        maxProb = wordProb[i];
        wordIndex = i;
      }
    }
    return guesses.get(wordIndex).toLowerCase();
  }

  public static int[] characterProbability(ArrayList<String> guesses) {
    String guess = "";
    int[] charProb = new int[26];
    for (int i = 0; i < guesses.size(); i++) {
      for (int j = 0; j < 5; j++) {
        for (int k = 0; k < chars.length; k++) {
          if (chars[k] == guesses.get(i).charAt(j)) {
            charProb[k]++;
          }
        }
      }
    }
    return charProb;
  }
  
  public static ArrayList<String> modifyPossibleGuesses(ArrayList<String> guesses, String guess,
      int[] guessCorrectness) {

    ArrayList<String> newPossibleGuesses = new ArrayList<String>();
    for (int i = 0; i < guessCorrectness.length; i++) {
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
      
      if (guessCorrectness[i] == 2) {
        for (int j = 0; j < guesses.size(); j++) {
          if (countLetterInAnswer == 1) {
            if (guesses.get(j).indexOf(String.valueOf(guess.charAt(i))) == i) {
              newPossibleGuesses.add(guesses.get(j));
            } else {
              guesses.remove(j);
              j--;
            }
          } else {
            int indexOfLetter = -1;
            boolean toRemove = true;
            for (int l = 0; l < countLetterInAnswer; l++) {
              indexOfLetter =
                  guesses.get(j).indexOf(String.valueOf(letter), (indexOfLetter + 1));
              if (indexOfLetter == i) {
                toRemove = false;
                break;
              }
            }
            if (toRemove) {
              guesses.remove(j);
            }
          }
        }
      }

      else if (guessCorrectness[i] == 1) {
        
        for (int j = 0; j < guesses.size(); j++) {
          if (countLetterInAnswer == 1) {
            if (guesses.get(j).indexOf(String.valueOf(guess.charAt(i))) != i
                && guesses.get(j).indexOf(String.valueOf(guess.charAt(i))) != -1) {
              newPossibleGuesses.add(guesses.get(j));
            } else {
              guesses.remove(j);
              j--;
            }
          } else {
            int indexOfLetter = -1;
            boolean toRemove = false;
            for (int l = 0; l < countLetterInAnswer; l++) {
              indexOfLetter =
                  guesses.get(j).indexOf(String.valueOf(letter), (indexOfLetter + 1));
              if (indexOfLetter == i || indexOfLetter == -1) {
                toRemove = true;
                break;
              }
            }
            if (toRemove) {
              guesses.remove(j);
            }   
          }
        }
      }

      if (guessCorrectness[i] == 0) {
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
                  guesses.get(k).indexOf(String.valueOf(letter), (indexOfLetter + 1));
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
    possibleGuesses = Wordle.wordleGuesses;
    String wordleGuess = "";
    int[] probability = characterProbability(possibleGuesses);
    // An array on integers which shows how correct a letter is: 
    // 0 - does not exist in word; 1 - exists but incorrect place; 2 - exists and correctly placed
    int[] guessCorrectness = new int[5];
    while (win != true) {
      wordleGuess = guess(possibleGuesses, probability);
      System.out.println(wordleGuess);
      for (int j = 0; j < guessCorrectness.length; j++) {
        guessCorrectness[j] = scnr.nextInt();
      }
      System.out.println();

      for (int j = 0; j < guessCorrectness.length; j++) {
        if (guessCorrectness[j] != 2) {
          break;
        }
        if (j == guessCorrectness.length - 1) {
          System.out.println("Congratulations! You guessed it!");
          win = true;
          return;
        }
      }
      possibleGuesses = modifyPossibleGuesses(possibleGuesses, wordleGuess, guessCorrectness);
      System.out.print(possibleGuesses.size());
      System.out.println(" possible guesses left.");
    }
  }
}
