package edu.jhu.teamundecided.clueless.deck;

import java.util.ArrayList;
import java.util.Collections;

public class Deck
{
    private ArrayList<Card> _cards;

    // Creates a Deck out of a list of card names and a card type
    Deck(ArrayList<String> cardname, Card.CardType cardtype)
    {
        _cards = new ArrayList<>();
        cardname.forEach((c) -> _cards.add(new Card(c, cardtype)));
    }

    // Creates a Deck out of a list of Cards
    Deck(ArrayList<Card> card)
    {
        _cards = new ArrayList<>();
        _cards.addAll(card);
    }

    Deck()
    {
        _cards = new ArrayList<>();
    }

    void shuffleCards()
    {
        Collections.shuffle(_cards);
    }

    public ArrayList<Card> getCards()
    {
        return _cards;
    }

    public void setCards(ArrayList<Card> _cards)
    {
        this._cards = _cards;
    }

    void addCardsFromDeck(Deck deck)
    {
        this._cards.addAll(deck.getCards());
    }

    public Card getCard(String name)
    {
        for (Card card : _cards)
        {
            if (card.getCardName().equals(name))
            {
                return card;
            }
        }
        return null;
    }

    void removeCard(Card name)
    {
        _cards.remove(name);
    }
}
