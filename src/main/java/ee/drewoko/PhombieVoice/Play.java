package ee.drewoko.PhombieVoice;

import org.json.JSONObject;

public class Play {

    private String text;
    private String voice;
    private String speed;
    private Boolean immediately;

    Play(String text, String voice, String speed, Boolean immediately) {
        this.text = text;
        this.voice = voice;
        this.speed = speed;
        this.immediately = immediately;
    }

    public String getText() {
        return text;
    }

    public String getVoice() {
        return voice;
    }

    public String getSpeed() {
        return speed;
    }

    public Boolean getImmediately() {
        return immediately;
    }

    @Override
    public String toString() {
        return "Play{" +
                "text='" + text + '\'' +
                ", voice='" + voice + '\'' +
                ", speed='" + speed + '\'' +
                ", immediately='" + immediately + '\'' +
                '}';
    }

    String toJson() {
        return new JSONObject(this).toString();
    }
}
