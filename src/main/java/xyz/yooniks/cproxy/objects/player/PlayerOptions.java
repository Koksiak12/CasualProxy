package xyz.yooniks.cproxy.objects.player;

public class PlayerOptions {

    public int timeOutConnect;
    public int timeOutPing;

    public boolean chatFromBots;
    public boolean chatFromServer =true;
    public boolean autoReconnect=true;
    public boolean autoCaptcha=true;

    public boolean autoLogin=true;

    public int autoReconnectTime = 3;
}
