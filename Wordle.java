///////////////////////////////// FILE HEADER /////////////////////////////////
//
// Title: HomeMade Wordle - Wordle.java
//
// Author: Bahulya Tandon
// Email: btandon2@wisc.edu
//
///////////////////////////////////////////////////////////////////////////////

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class creates an in-console Wordle game by generating a random 5 letter word out of a list
 * of words and allows the user to guess the word. If a letter exists in the selected word and is in
 * the correct place is displayed by the color green. If a letter exists in the word but is in an
 * incorrect place it is displayed by the color yellow.
 * 
 * @author Bahulya Tandon
 */
public class Wordle {
  // List of words that can be answers in the wordle
  static ArrayList<String> wordleAnswers = new ArrayList<String>();

  // List of words that can be guesses in the wordle
  static ArrayList<String> wordleGuesses = new ArrayList<String>();

  // Declaring ANSI_RESET so that we can reset the color
  static final String ANSI_RESET = "\u001B[0m";

  // Declaring the color yellow
  static final String ANSI_YELLOW = "\u001B[33m";

  // Declaring the color green
  static final String ANSI_GREEN = "\u001B[32m";

  // Condition for winning the game
  static boolean win = false;

  /**
   * Reads the files WordleAnswers.txt and WordleGuesses.txt to create two array lists of words that
   * can be the answers or the guesses for the Wordle.
   */
  public static void createLists() {
    File answers = new File("/Users/bahul/eclipse-workspace/Project Wordle/src/WordleAnswers.txt");
    File guesses = new File("/Users/bahul/eclipse-workspace/Project Wordle/src/WordleGuesses.txt");

    try {
      Scanner scnr = new Scanner(answers);
      while (scnr.hasNextLine()) {
        wordleAnswers.add(scnr.nextLine());
      }
      scnr.close();
    } catch (Exception e) {
      System.out.println(e.getStackTrace());
    }

    try {
      Scanner scnr = new Scanner(guesses);
      while (scnr.hasNextLine()) {
        wordleGuesses.add(scnr.nextLine());
      }
      scnr.close();
    } catch (Exception e) {
      System.out.println(e.getStackTrace());
    }

  }

  /**
   * This method chooses a random word from the wordleAnswers array list as the answer to the Wordle
   * 
   * @return A random string object from the array list wordleAnswers
   */
  public static String chooseAnswer() {
    Random randGen = new Random();
    int answerIndex = randGen.nextInt(wordleAnswers.size());
    return wordleAnswers.get(answerIndex);
  }

  /**
   * This method displays the word with the colored letters to show which letters are present in 
   * the selected word and which ones are not.
   * 
   * @param guess The word that the user has guessed
   * @param answer The word that has been selected to be the answer
   */
  public static void display(String guess, String answer) {
    int[] guessColors = checkGuess(guess, answer);
    for (int i = 0; i < guessColors.length; i++) {
      if (guessColors[i] == 1) {
        System.out.print(ANSI_YELLOW + String.valueOf(guess.charAt(i)) + ANSI_RESET);
      } else if (guessColors[i] == 2) {
        System.out.print(ANSI_GREEN + String.valueOf(guess.charAt(i)) + ANSI_RESET);
      } else {
        System.out.print(String.valueOf(guess.charAt(i)));
      }
    }
    System.out.println();
    System.out.println();

    for (int i = 0; i < guessColors.length; i++) {
      if (guessColors[i] != 2) {
        break;
      }
      if (i == guessColors.length - 1) {
        System.out.println("Congratulations! You won!");
        win = true;
        return;
      }
    }

  }

  /**
   * This method checks the guessed word by the user and returns an array of 5 integers which
   * range from 0 to 2, where 0 suggests the letter is not present in the answer, 1 suggests the
   * letter is present in the answer but at an incorrect index, and 2 suggests that the letter
   * is present in the answer and at the correct index.
   * 
   * @param guess The word that the user has guessed
   * @param answer The word that has been selected as the answer
   * @return An array of integers suggesting the correctness of each letter in the guess
   */
  public static int[] checkGuess(String guess, String answer) {
    char[] answerLetters = new char[5];
    for (int i = 0; i < 5; i++) {
      answerLetters[i] = answer.charAt(i);
    }

    char[] guessLetters = new char[5];
    for (int i = 0; i < 5; i++) {
      guessLetters[i] = guess.charAt(i);
    }

    int[] guessCorrectness = new int[5];

    for (int i = 0; i < guessLetters.length; i++) {
      if (guessLetters[i] == answerLetters[i]) {
        guessCorrectness[i] = 2;
      }
    }

    for (int i = 0; i < guessLetters.length; i++) {
      for (int j = 0; j < answerLetters.length; j++) {
        if (guessCorrectness[i] != 2 && guessCorrectness[j] != 2) {
          if (guessLetters[i] == answerLetters[j]) {
            guessCorrectness[i] = 1;
            answerLetters[j] = '.';
          }
        }
      }
    }
    return guessCorrectness;
  }

  /**
   * Main Method
   * 
   * @param args
   */
  public static void main(String[] args) {
    Scanner scnr = new Scanner(System.in);
    createLists();
    String wordleAnswer = (chooseAnswer().toLowerCase());
    for (int i = 1; i <= 6; i++) {
      String wordleGuess = (scnr.next()).toLowerCase();
      int num = 0;
      while (num < wordleGuesses.size()) {
        if ((wordleGuesses.get(num).toLowerCase()).equals(wordleGuess)) {
          break;
        }
        if (num == wordleGuesses.size() - 1) {
          System.out.println("Word does not exist in list of guesses. Try another word\n");
          wordleGuess = (scnr.next().toLowerCase());
          num = -1;
        }
        num++;
      }
      display(wordleGuess, wordleAnswer);
    }
    scnr.close();
    if (win == false) {
      System.out.println("You lost! Better luck next time.");
      System.out.println("The word was: " + wordleAnswer);
    }
  }
}
