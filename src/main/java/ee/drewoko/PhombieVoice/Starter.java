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

        log.info(String.format("Starting with Web-port: %s, ivona-keys: %s, ivona-voice: %s\n",
                optionsParser.getWebPort(), optionsParser.getIvonaKeysFile(), optionsParser.getIvonaVoice()));

        System.out.printf("Open address in browser http://localhost:%s \n", optionsParser.getWebPort());
        System.out.printf("Send requests to http://localhost:%s/request \n", optionsParser.getWebPort());

        System.out.println("GET request parameters:");
        System.out.println("text - basic parameter with text, that will be played (required)");
        System.out.println("voice - parameter with voice which will be requested from Ivona (https://goo.gl/1vk7xq). If it not presented in request then it will be taken from configuration");
        System.out.println("speed - audio playback speed (1.0). If it not presented in request then it will be taken from configuration");
        System.out.println("immediately - plays audio immediately or uses a queue (true/false). If it not presented in request then it will be taken from configuration");
        System.out.println();
        System.out.printf("Full example: http://localhost:%s/request?text=Hi, There! How are you&voice=Joey&speed=0.9&immediately=false", optionsParser.getWebPort());
        System.out.println();

        try {
            new IvonaService();
        } catch (IOException e) {
            log.error("Failed to start IvonaService. Looks like unable to parse IvonaKeys", e);
            System.exit(0);
        }

        new WebService(optionsParser);
    }
}
