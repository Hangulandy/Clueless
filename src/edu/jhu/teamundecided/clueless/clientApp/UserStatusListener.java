package edu.jhu.teamundecided.clueless.clientApp;

public interface UserStatusListener {
    void online(String login);
    void offline(String login);
}
