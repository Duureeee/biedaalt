package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FlashcardApp {
    private static final String HELP_MESSAGE = "Usage: flashcard <cards-file> [options]\n" +
            "Options:\n" +
            "--help                     Show help message\n" +
            "--order <order>            Order type: [random, worst-first, recent-mistakes-first] (default: random)\n" +
            "--repetitions <num>        Number of correct repetitions required (default: 1)\n" +
            "--invertCards              Swap question and answer\n";

            public static void main(String[] args) {
                if (args.length == 0 || args[0].equals("--help")) {
                    System.out.println(HELP_MESSAGE);
                    return;
                }
            
                String filePath = (args.length > 0 && !args[0].equals("--help")) ? args[0] : "C:\\Users\\ebo\\biedaalt\\src\\main\\java\\com\\example\\Card.txt";  //"C:\\Users\\ebo\\biedaalt\\src\\main\\java\\com\\example\\Card.txt"
                String order = "random";
                int repetitions = 1;
                boolean invertCards = false;
            
               
            
                List<Card> cards = loadFlashcards(filePath);
                if (cards == null) return;
            
                switch (order) {
                    case "random":
                        Collections.shuffle(cards);
                        break;
                    case "recent-mistakes-first":
                        CardOrganizer organizer = new RecentMistakesFirstSorter();
                        cards = organizer.sortCards(cards);
                        break;
                    default:
                        System.out.println("Invalid order type: " + order);
                        return;
                }
            
                runFlashcards(cards, repetitions, invertCards);
            }
    
        // Change from private to public
    public static List<Card> loadFlashcards(String filePath){
            System.out.println("Loading flashcards from: " + filePath);
            List<Card> cards = new ArrayList<>();
            try {
                List<String> lines = Files.readAllLines(Paths.get(filePath));
                for (String line : lines) {
                    String[] parts = line.split("::");
                    if (parts.length == 3) {
                        int mistakes = Integer.parseInt(parts[2]);
                        cards.add(new Card(parts[0], parts[1], mistakes));
                    } else {
                        System.out.println("Skipping invalid line: " + line);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading file: " + e.getMessage());
                return null;
            }
            return cards;
        }
    
        private static void runFlashcards(List<Card> cards, int repetitions, boolean invertCards) {
            Scanner scanner = new Scanner(System.in);
            Map<String, Integer> correctCount = new HashMap<>();
            Map<String, Integer> attemptCount = new HashMap<>();
            boolean allCorrect = true;
        
            for (Card card : cards) {
                correctCount.put(card.getQuestion(), 0);
                attemptCount.put(card.getQuestion(), 0);
            }
        
            while (true) {
                boolean allLearned = true;
                for (Card card : cards) {
                    String question = invertCards ? card.getAnswer() : card.getQuestion();
                    String answer = invertCards ? card.getQuestion() : card.getAnswer();
        
                    attemptCount.put(card.getQuestion(), attemptCount.get(card.getQuestion()) + 1);
        
                    if (correctCount.get(card.getQuestion()) >= repetitions) continue;
                    allLearned = false;
        
                    System.out.println("Question: " + question);
                    String userAnswer = scanner.nextLine();
                    
        
                    // Allow the user to exit the game by typing "exit"
                    if (userAnswer.equalsIgnoreCase("exit")) {
                        System.out.println("Exiting the game...");
                        return; // Exit the method and stop the game
                    }
        
                    if (userAnswer.equalsIgnoreCase(answer)) {
                        correctCount.put(card.getQuestion(), correctCount.get(card.getQuestion()) + 1);
                        System.out.println("Correct!");
                    } else {
                        allCorrect = false;
                        System.out.println("Wrong! Correct answer: " + answer);
                    }
                }
                if (allLearned) break; // If all cards are learned, exit the loop.
            }
        
            System.out.println("All cards learned!");
        
            // Check achievements
            System.out.println("Achievements Unlocked:");
            if (allCorrect) System.out.println("🏆 CORRECT: All cards were answered correctly in the last round!");
            for (String card : attemptCount.keySet()) {
                if (attemptCount.get(card) > 5) {
                    System.out.println("🔄 REPEAT: The card '" + card + "' was answered more than 5 times.");
                }
                if (correctCount.get(card) >= 3) {
                    System.out.println("💡 CONFIDENT: The card '" + card + "' was answered correctly at least 3 times.");
                }
            }
        }
}

//java -cp out com.example.FlashcardApp "C:\Users\ebo\biedaalt\src\main\java\com\example\Card.txt"
