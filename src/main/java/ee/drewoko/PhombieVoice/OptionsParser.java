package ee.drewoko.PhombieVoice;

import org.apache.commons.cli.*;

public class OptionsParser {

    private static OptionsParser instance;

    private String[] args;
    private Options options;

    private boolean help = false;
    private boolean ws = false;
    private String webPort;
    private String ivonaKeysFile;
    private String ivonaVoice;
    private String ivonaEntryPoint;
    private String audioPlayBackSpeed;

    OptionsParser(String[] args) {
        this.args = args;
        parse();

        instance = this;
    }

    private void parse() {

        options = new Options();

        options.addOption("h", "help", false, "Print instruction");
        options.addOption("ws", false, "Enables WebSocket Transport");
        options.addOption("p", "web-port", true, "Http service port");
        options.addOption("ik", "ivona-keys", true, "File with ivonna clound API keys");
        options.addOption("iv", "ivona-voice", true, "Ivona voice. https://goo.gl/1vk7xq");
        options.addOption("ie", "ivona-entry", true, "Ivona Cloud Entry-point. https://tts.us-east-1.ivonacloud.com");
        options.addOption("ps", "play-speed", true, "Audio playback speed");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse( options, args);

            if(cmd.hasOption("h")) {
                help = true;
            }

            if(cmd.hasOption("ws")) {
                ws = true;
            }

            webPort = cmd.getOptionValue("p", "8080");
            ivonaKeysFile = cmd.getOptionValue("ik", "ivonakeys.txt");
            ivonaVoice = cmd.getOptionValue("iv", "Maxim");
            ivonaEntryPoint = cmd.getOptionValue("ie", "https://tts.us-east-1.ivonacloud.com");
            audioPlayBackSpeed = cmd.getOptionValue("ps", "1");

        } catch (ParseException e) {
            System.err.println("Failed to parse options");
            e.printStackTrace();
        }
    }

    void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "PhombieVoice.jar", options );
    }

    boolean isHelp() {
        return help;
    }

    String getWebPort() {
        return webPort;
    }

    String getIvonaKeysFile() {
        return ivonaKeysFile;
    }

    String getIvonaVoice() {
        return ivonaVoice;
    }

    String getIvonaEntryPoint() {
        return ivonaEntryPoint;
    }

    boolean isWs() {
        return ws;
    }

    public String getAudioPlayBackSpeed() {
        return audioPlayBackSpeed;
    }

    static OptionsParser getInstance() {
        return instance;
    }

}
