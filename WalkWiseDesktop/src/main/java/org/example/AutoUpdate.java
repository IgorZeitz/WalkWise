package org.example;

public class AutoUpdate {

    public static String localVersion = "0.0.1";

    public static String checkVersion(){
        //TO DO: real version comparing (localVersion vs latestVersion published)
        return localVersion;
    }

    public String getLocalMCUVersion(){
    //TO DO: request actual firmware version of mcu
    return null;
    }

    public String checkLatestMCUVersion(String url){
        //TO DO: real mcu version comparing (localMCUVersion vs latestMCUVersion published)
        return null;
    }

}
