package edu.jhu.teamundecided.clueless.notebook;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 *
 */
public class StatusGroup
{
    private String GroupLabel;
    private ArrayList<StatusItem> members;
    private String[] _tickLabels = {"Unknown", "In Hand", "In Other's Hand", "In Case File"};
    JFrame w;
    BevelBorder thickBorder= new BevelBorder(BevelBorder.LOWERED);

    StatusGroup(JFrame toplevel, String name, GridBagConstraints placer) {
        GroupLabel = name;
        members = new ArrayList<StatusItem>();
        w = toplevel;
        JLabel label = new JLabel(GroupLabel);
        w.add(label, placer);
        label.setBorder(thickBorder);
        placer.weightx=2;
        for (String tickLabel : _tickLabels) {
            placer.gridx++;
            label = new JLabel(tickLabel);
            label.setBorder(thickBorder);
            w.add(label, placer);
        }
    }

    public void add(StatusItem item)
    {
        members.add(item);
    }
}
