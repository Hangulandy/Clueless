package edu.jhu.teamundecided.clueless.notebook;

import javax.swing.*;
import java.awt.*;

public class NoteBook extends JFrame {

    private StatusGroup[] _panels;
    private static String[][] _defaultCardInitializer =
            {
                    {"Rooms", "Study", "Hall", "Lounge", "Library", "Billiard Room",
                            "Dining Room", "Conservatory", "Ball Room", "Kitchen", ""},
                    {"Suspects", "Colonel Mustard", "Miss Scarlet", "Professor Plum",
                            "Mr. Green", "Mrs. White", "Mrs. Peacock", ""},
                    {"Weapons", "Rope", "Lead Pipe", "Knife", "Wrench", "Candlestick",
                            "Revolver", ""}
            };

    NoteBook(String args[]) {
        String[][] _initializer;
        _initializer = _defaultCardInitializer;
        this.setLayout(new GridBagLayout());
        this.setSize(400,700);
        GridBagConstraints placer = new GridBagConstraints();
        placer.gridy=0;
        placer.gridx=0;
        placer.weightx=10;
        placer.weighty=10;
        placer.fill=GridBagConstraints.BOTH;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        for (String[] group : _initializer) {
            int browser = 0;
            StatusGroup sgrp = new StatusGroup(this,group[browser++],placer);
            placer.weighty=5;
            while (!group[browser].equals("")) {
                placer.gridy++;
                placer.gridx=0;
                placer.weightx=10;
                sgrp.add(new StatusItem(this, group[browser++],placer));
            }
            placer.gridy++;
            placer.gridx=0;
            placer.weightx=10;
            placer.weighty=10;
        }
    }

    public static void main(String args[]) {
        new NoteBook(args).setVisible(true);
    }
}
