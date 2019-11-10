/*
The MessageListener Interface is used to

This implementation is borrowed from a tutorial at https://fullstackmastery.com/ep4 written by Jim Liao.
 */
package edu.jhu.teamundecided.clueless.clientApp;

public interface MessageListener {
    void onMessage(String fromLogin, String msgBody);
}
