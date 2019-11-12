package edu.jhu.teamundecided.clueless.deck;

import java.util.ArrayList;

public class Suggestion
{
    private ArrayList<Card> _suggestionList;
    private String _suspect;
    private String _weapon;
    private String _room;

    public Suggestion(String suspect, String weapon, String room)
    {

        _suspect = suspect;
        _weapon = weapon;
        _room = room;

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

    public String toString()
    {
        return _suspect + " did it with the " + _weapon + " in the " + _room;

    }

    public String getSuspect()
    {

        return _suspect;

    }
}
