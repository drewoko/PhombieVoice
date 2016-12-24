package ee.drewoko.PhombieVoice;

public class SettingsResponse {
    private boolean isWs;
    private String playBackSpeed;

    SettingsResponse(boolean isWs, String playBackSpeed) {
        this.isWs = isWs;
        this.playBackSpeed = playBackSpeed;
    }

    public boolean isWs() {
        return isWs;
    }

    public String getPlayBackSpeed() {
        return playBackSpeed;
    }
}
