package model;

import java.util.*;

public class Hand {
    private final List<Card> cards = new ArrayList<>();

    public void add(Card c) {
        cards.add(c);
    }

    public List<Card> getCards() {
        return cards;
    }

    public void clear() {
        cards.clear();
    }

    // valor da mao atual
    public int bestValue() {
        int total = 0;
        int aces = 0;

        for (Card c : cards) {
            if ("A".equals(c.getRank())) {
                aces++; total += 11;
            } else total += c.getValue();
        }

        // As vale 11 por padrao, mas se a soma do as com as demais cartas der acima de 21, o As passa a valer 1
        while (total > 21 && aces > 0) {
            total -= 10; aces--;
        }
        return total;
    }

    // As + carta de valor 10
    public boolean isBlackjack() {
        if (cards.size() != 2) return false;

        boolean hasAce = cards.stream().anyMatch(c -> "A".equals(c.getRank()));
        boolean hasTen = cards.stream().anyMatch(c -> {
            String r = c.getRank();
            return "10".equals(r) || "J".equals(r) || "Q".equals(r) || "K".equals(r);
        });

        return hasAce && hasTen;
    }

    //checar se perdeu
    public boolean isBust() {
        return bestValue() > 21;
    }
}
