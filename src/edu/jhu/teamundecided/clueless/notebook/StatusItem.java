package edu.jhu.teamundecided.clueless.notebook;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StatusItem {
    private String _label;
    private int _tickBoxCount=4;
    private JLabel _labelBox;
    private ArrayList<JRadioButton> _ticks;
    private ButtonGroup mutex;

    StatusItem(JFrame w, String name, GridBagConstraints placer)
    {
        boolean selected=true;
        _label=name;
        _labelBox=new JLabel(_label);
        w.add(_labelBox,placer);
        _ticks = new ArrayList<JRadioButton>();
        for (int i = 0; i < _tickBoxCount; i++) _ticks.add(new JRadioButton(""));
        mutex = new ButtonGroup();
        placer.weightx=2;
        for(JRadioButton btn: _ticks) {
            btn.setSelected(selected);
            selected=false;
            placer.gridx++;
            w.add(btn,placer);
            mutex.add(btn);
        }
    }

    JLabel getLabelBox()
    {
        return(_labelBox);
    }
}