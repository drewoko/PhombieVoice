package ee.drewoko.PhombieVoice;

import org.apache.commons.cli.*;

class OptionsParser {

    private static OptionsParser instance;

    private String[] args;
    private Options options;

    private boolean help = false;
    private String webPort;

    private String amazonAccessKey;
    private String amazonSecretKey;

    private String voice;

    private String audioPlayBackSpeed;
    private Boolean playImmediately;

    OptionsParser(String[] args) {
        this.args = args;
        parse();

        instance = this;
    }

    private void parse() {

        options = new Options();

        options.addOption("h", "help", false, "Print instruction");
        options.addOption("p", "web-port", true, "Http service port");
        options.addOption("ak", "aws-accesskey", true, "Amazon AWS access key");
        options.addOption("as", "aws-secretkey", true, "Amazon AWS secret key");
        options.addOption("v", "voice", true, "Playback voice");
        options.addOption("ps", "play-speed", true, "Audio playback speed");
        options.addOption("pi", "play-immediately", true, "Play immediately or wait till last will be played");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse( options, args);

            if(cmd.hasOption("h")) {
                help = true;
            }

            webPort = cmd.getOptionValue("p", "8080");
            amazonAccessKey = cmd.getOptionValue("ak", "");
            amazonSecretKey = cmd.getOptionValue("as", "");
            voice = cmd.getOptionValue("v", "Justin");
            audioPlayBackSpeed = cmd.getOptionValue("ps", "1");
            playImmediately = Boolean.valueOf(cmd.getOptionValue("pi", "true"));

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

    public String getAmazonAccessKey() {
        return amazonAccessKey;
    }

    public String getAmazonSecretKey() {
        return amazonSecretKey;
    }

    public String getVoice() {
        return voice;
    }

    String getAudioPlayBackSpeed() {
        return audioPlayBackSpeed;
    }

    Boolean getPlayImmediately() {
        return playImmediately;
    }

    static OptionsParser getInstance() {
        return instance;
    }

}
