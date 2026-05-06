package com.example;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RecentMistakesFirstSorter implements CardOrganizer {
    @Override
    public List<Card> sortCards(List<Card> cards) {
        return cards.stream()
                .sorted(Comparator.comparing((Card card) -> card.getLastMistakeOrder() > 0)
                        .reversed()
                        .thenComparing(Comparator.comparingInt(Card::getLastMistakeOrder)
                                .reversed())
                        .thenComparing(Comparator.comparingInt(Card::getMistakes).reversed()))
                .collect(Collectors.toList());
    }
}
