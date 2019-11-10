package edu.jhu.teamundecided.clueless.clientApp;/*
The MessageListener Interface is used to

This implementation is borrowed from a tutorial at https://fullstackmastery.com/ep4 written by Jim Liao.
 */

public interface MessageListener {
    public void onMessage(String fromLogin, String msgBody);
}
