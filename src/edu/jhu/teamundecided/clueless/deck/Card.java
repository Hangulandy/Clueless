package edu.jhu.teamundecided.clueless.deck;

public class Card
{
    private String _cardName;
    private CardType _type;

    enum CardType
    {
        Suspect,
        Room,
        Weapon
    }

    Card(String name, CardType type)
    {
        _cardName = name;
        _type = type;
    }

    public String getCardName()
    {
        return _cardName;
    }

    public void setCardName(String cardName)
    {
        this._cardName = cardName;
    }

    public CardType getType()
    {
        return _type;
    }

    public void setType(CardType type)
    {
        this._type = type;
    }

    public String toString()
    {
        String out = getCardName() + " - Card Type: " + getType();
        return out;
    }

}
