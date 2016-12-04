package ee.drewoko.PhombieVoice;

import java.io.IOException;

public class Starter {

    public static void main(String[] args) {

        OptionsParser optionsParser = new OptionsParser(args);

        if(optionsParser.isHelp()) {
            optionsParser.printHelp();
            return;
        }

        System.out.printf("Starting with Web-port: %s, ivona-keys: %s, ivona-voice: %s\n",
                optionsParser.getWebPort(), optionsParser.getIvonaKeysFile(), optionsParser.getIvonaVoice());

        System.out.printf("Open address in browser http://localhost:%s \n", optionsParser.getWebPort());
        System.out.printf("Send requests to http://localhost:%s/request?text=Tere \n", optionsParser.getWebPort());

        try {
            new IvonaService();
        } catch (IOException e) {
            System.err.println("Failed to start IvonaService. Looks like unnable to parse IvonaKeys");
            e.printStackTrace();
            System.exit(0);
        }
        new WebService(optionsParser);
    }
}
