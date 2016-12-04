package ee.drewoko.PhombieVoice;

import com.ivona.services.tts.IvonaSpeechCloudClient;
import com.ivona.services.tts.model.CreateSpeechRequest;
import com.ivona.services.tts.model.Input;
import com.ivona.services.tts.model.OutputFormat;
import com.ivona.services.tts.model.Voice;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

class IvonaService {

    private static IvonaService instance;

    private List<IvonaCredential> ivonaCredentials = new LinkedList<>();

    IvonaService() throws IOException {

        parseKeys();

        instance = this;
    }

    private void parseKeys() throws IOException {
        String ivonaKeysFileContent = IOUtils.toString(
                new FileInputStream(OptionsParser.getInstance().getIvonaKeysFile()), "utf-8");

        JSONArray objects = new JSONArray(ivonaKeysFileContent);

        objects.forEach(object -> {
            JSONObject jsonObject = ((JSONObject)object);
            ivonaCredentials.add(new IvonaCredential(
                    jsonObject.getString("AWSAccessKeyId"),
                    jsonObject.getString("AWSSecretKey")
            ));
        });
    }

    private IvonaCredential getIvonaCredential() {
        return ivonaCredentials.get(ThreadLocalRandom.current().nextInt(0, ivonaCredentials.size() + 1));
    }

    String getTTSUrl(String text) throws UnsupportedEncodingException {
        IvonaSpeechCloudClient speechCloud = new IvonaSpeechCloudClient(
                IvonaService.getInstance().getIvonaCredential()
        );

        speechCloud.setEndpoint(OptionsParser.getInstance().getIvonaEntryPoint());

        CreateSpeechRequest createSpeechRequest = new CreateSpeechRequest();
        Input input = new Input();
        Voice voice = new Voice();
        OutputFormat outputFormat = new OutputFormat();

        voice.setName(OptionsParser.getInstance().getIvonaVoice());

        input.setData(text);

        outputFormat.setCodec("OGG");

        createSpeechRequest.setInput(input);
        createSpeechRequest.setVoice(voice);
        createSpeechRequest.setOutputFormat(outputFormat);

        return speechCloud.getCreateSpeechUrl(createSpeechRequest).toString();
    }

    static IvonaService getInstance() {
        return instance;
    }
}
