package edu.jhu.teamundecided.clueless.deck;

import edu.jhu.teamundecided.clueless.player.Player;

import java.util.*;

public class DeckController
{
    // Decks
    private Deck _suspectDeck;
    private Deck _roomDeck;
    private Deck _weaponDeck;
    private ArrayList<Deck> _decks;
    private Deck _FullDeck;

    // Card Lists
    private ArrayList<String> _Suspects;
    private ArrayList<String> _Rooms;
    private ArrayList<String> _Weapons;
    private ArrayList<Card> _CaseFile;

    // Players (temp?)
    private ArrayList<Player> _players;

    public DeckController()
    {
        // create the card name lists - used to create the cards
        _Suspects = new ArrayList<>(Arrays.asList("Mr. Green", "Colonel Mustard", "Mrs. Peacock", "Professor Plum", "Miss Scarlett", "Mrs. White"));
        _Rooms = new ArrayList<>(Arrays.asList("Ballroom", "Kitchen", "Dining Room", "Lounge", "Hall", "Study", "Library", "Conservatory", "Billiard Room"));
        _Weapons = new ArrayList<>(Arrays.asList("Candle Stick", "Dagger", "Lead Pipe", "Revolver", "Rope", "Wrench"));

        setupDecks();
        shuffleAllDecks();
        selectCaseFile();
        combineDecks();
    }

    private void setupDecks()
    {
        _suspectDeck = new Deck(_Suspects, Card.CardType.Suspect);
        _roomDeck = new Deck(_Rooms, Card.CardType.Room);
        _weaponDeck = new Deck(_Weapons, Card.CardType.Weapon);

        _decks = new ArrayList<>();
        _decks.add(_suspectDeck);
        _decks.add(_roomDeck);
        _decks.add(_weaponDeck);
    }

    public void combineDecks()
    {
        ArrayList<Card> tempList = new ArrayList<>();

        for (Deck deck : _decks)
        {
            tempList.addAll(deck.getCards());
        }

        _FullDeck = new Deck(tempList);

        // shuffle full deck
        _FullDeck.shuffleCards();
    }

    public void selectCaseFile()
    {
        _CaseFile = new ArrayList<>();

        for (Deck deck : _decks)
        {
            Card temp = deck.getCards().get(0);
            _CaseFile.add(temp);
            deck.removeCard(temp);
        }
    }

    public void shuffleAllDecks()
    {
        _decks.forEach(Deck::shuffleCards);
    }

    public boolean dealCards(ArrayList<Player> players)
    {
        // This method will deal all the cards to all the players
        if (players == null)
        {
            System.out.println("Need players...");
            return false;
        }

        int playerId = 0;
        for (Card card : _FullDeck.getCards())
        {
            try
            {
                players.get(playerId++).getPlayerHand().addCard(card);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                return false;
            }

            // loop playerId
            if (playerId == _players.size())
            {
                playerId = 0;
            }
        }

        // Deal successful
        return true;
    }

    // Method to check an accusation
    public boolean checkAccusation(Suggestion suggestion)
    {
        for (Card card : _CaseFile)
        {
            if(!card.getCardName().equals(suggestion.getCard(card.getType()).getCardName()))
            {
                return false;
            }
        }

        handleMessage("Your accusation is Correct!");
        return true;
    }

    public void handleMessage(String msg)
    {
        System.out.println(msg);
        // future use for this method may be to pass message to a player or broadcast to all players
        // will need access to Server's ServerWorker list
        // or a Player class will hold a reference to its ServerWorker then this will just reference
        // the Player object and access the ServerWorker that way.
    }

    // METHODS SHOULD BE MOVED FROM DECKCONTROLLER
    public String getLocation(Player player)
    {
        return "";
    }
    // METHODS SHOULD BE MOVED FROM DECKCONTROLLER
    public boolean isRoom(String location){
        return true;
    }

    ////// DEBUG Methods ///////////////////////
//    public void selectStackCaseFile(String suspect, String room, String weapon)
//    {
//        _CaseFile = new ArrayList<>();
//
//        Card cf_Suspect = _suspectDeck.getCard(suspect);
//        Card cf_Room =_roomDeck.getCard(room);
//        Card cf_Weapon = _weaponDeck.getCard(weapon);
//
//        _CaseFile.add(cf_Suspect);
//        _CaseFile.add(cf_Room);
//        _CaseFile.add(cf_Weapon);
//
//        _suspectDeck.removeCard(cf_Suspect);
//        _roomDeck.removeCard(cf_Room);
//        _weaponDeck.removeCard(cf_Weapon);
//    }

//    public void setupPlayers()
//    {
//        _players = new ArrayList<>();
//        int totalPlayers = 6;
//
//        for (int playerId = 0; playerId < totalPlayers; playerId++)
//        {
//            _players.add(new Player(playerId));
//        }
//    }

//    public ArrayList<Player> getPlayers()
//    {
//        return _players;
//    }
//
//    public void printDecks()
//    {
//        System.out.println("Suspects:");
//        _suspectDeck.getCards().forEach(System.out::println);
//        System.out.println("\nRooms:");
//        _roomDeck.getCards().forEach(System.out::println);
//        System.out.println("\nWeapons:");
//        _weaponDeck.getCards().forEach(System.out::println);
//    }
//
//    public void printCaseFile()
//    {
//        System.out.println("Case File:");
//        for (Card card : _CaseFile)
//        {
//            System.out.println(card);
//        }
//    }
//
//    public void printFullDeck()
//    {
//        System.out.println("Full Deck:");
//        for (Card card : _FullDeck.getCards())
//        {
//            System.out.println(card);
//        }
//    }
}
