package ee.drewoko.PhombieVoice;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.model.*;


import java.io.*;

class SoundService {

    private AmazonPollyClient polly;
    private static SoundService instance;

    SoundService() throws IOException {

        ClientConfiguration configuration = new ClientConfiguration();

        configuration.setMaxErrorRetry(3);
        configuration.setConnectionTimeout(5*1000);
        configuration.setSocketTimeout(5*1000);
        configuration.setProtocol(Protocol.HTTP);


        polly = new AmazonPollyClient(new AWSStaticCredentialsProvider(new BasicAWSCredentials(OptionsParser.getInstance().getAmazonAccessKey(), OptionsParser.getInstance().getAmazonSecretKey())),
                new ClientConfiguration());
        polly.setRegion(Region.getRegion(Regions.EU_WEST_1));

        instance = this;
    }


    InputStream getTTS(String text, String voiceID) throws UnsupportedEncodingException {
        SynthesizeSpeechRequest synthReq =
                new SynthesizeSpeechRequest().withText(text).withVoiceId(voiceID)
                        .withOutputFormat(OutputFormat.Ogg_vorbis);
        SynthesizeSpeechResult synthRes = polly.synthesizeSpeech(synthReq);

        return synthRes.getAudioStream();
    }

    static SoundService getInstance() {
        return instance;
    }
}
