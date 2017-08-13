package ee.drewoko.PhombieVoice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Starter {

    private final static Logger log = LoggerFactory.getLogger(Starter.class);

    public static void main(String[] args) throws UnsupportedEncodingException {

        final OptionsParser optionsParser = new OptionsParser(args);

        if(optionsParser.isHelp()) {
            optionsParser.printHelp();
            return;
        }

        if(optionsParser.getAmazonAccessKey() == null || optionsParser.getAmazonSecretKey() == null) {
            log.error("Fatal error, Amazon Access or Secret key is not defined");
            return;
        }

        log.info(String.format("Starting with Web-port: %s, Amazon AWS Access Key: %s, Amazon AWS Secret Key: %s\n",
                optionsParser.getWebPort(), optionsParser.getAmazonAccessKey(), optionsParser.getAmazonSecretKey()));

        log.info(String.format("Open address in browser http://localhost:%s", optionsParser.getWebPort()));
        log.info(String.format("Send requests to http://localhost:%s/request", optionsParser.getWebPort()));

        log.info("GET request parameters:");
        log.info("text - basic parameter with text, that will be played (required)");
        log.info("voice - parameter with voice which will be requested from Ivona (https://goo.gl/1vk7xq). If it not presented in request then it will be taken from configuration");
        log.info("speed - audio playback speed (1.0). If it not presented in request then it will be taken from configuration");
        log.info("immediately - plays audio immediately or uses a queue (true/false). If it not presented in request then it will be taken from configuration");
        log.info(String.format("Full example: http://localhost:%s/request?text=Hi, There! How are you&voice=Joey&speed=0.9&immediately=false", optionsParser.getWebPort()));

        try {
            new SoundService();
        } catch (IOException e) {
            log.error("Failed to start AudioService.", e);
            System.exit(0);
        }

        new WebService(optionsParser);
    }
}
