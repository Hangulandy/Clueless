package edu.jhu.teamundecided.clueless.deck;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Suggestion
{
    private ArrayList<Card> _suggestionList;

    public Suggestion(String suspect, String room, String weapon)
    {
        _suggestionList = new ArrayList<>();
        _suggestionList.add(new Card(suspect, Card.CardType.Suspect));
        _suggestionList.add(new Card(room, Card.CardType.Room));
        _suggestionList.add(new Card(weapon, Card.CardType.Weapon));
    }

    public Card getCard(Card.CardType cardType)
    {
        for (Card card : _suggestionList)
        {
            if (card.getType().equals(cardType))
            {
                return card;
            }
        }

        return null;
    }

    public ArrayList<Card> getSuggestedCards()
    {
        return _suggestionList;
    }
}
