package com.luc.bluetooth;

/**
 * Created by aziz on 4/11/15.
 */
public class SlaveCommand {

    String command;

    void setCommand(String com)
    {
        command=com;
    }

    String getCommand()
    {
        if(command.length()>1) {
            if (!command.substring(1,2).isEmpty()) {
                return command.trim().substring(0, 1) + "\n"+command.trim().substring(1,2)+"\n";
            }
            return command.trim().substring(0, 1) + "\n";
        }
        else if(command.length()==1)
            return command.trim().substring(0, 1);
        else
            return "";
    }

}
