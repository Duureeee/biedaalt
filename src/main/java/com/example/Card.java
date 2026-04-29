package com.example;

public class Card {
    private final String question;
    private final String answer;
    private int mistakes;
    private int lastMistakeOrder;

    public Card(String question, String answer, int mistakes) {
        this.question = question;
        this.answer = answer;
        this.mistakes = mistakes;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public int getMistakes() {
        return mistakes;
    }

    public int getLastMistakeOrder() {
        return lastMistakeOrder;
    }

    public void incrementMistakes() {
        this.mistakes++;
    }

    public void markMistake(int mistakeOrder) {
        incrementMistakes();
        this.lastMistakeOrder = mistakeOrder;
    }
}
