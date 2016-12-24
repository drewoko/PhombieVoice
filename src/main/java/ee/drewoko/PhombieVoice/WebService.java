package ee.drewoko.PhombieVoice;

import com.amazonaws.util.IOUtils;
import org.json.JSONObject;
import spark.Response;
import spark.ResponseTransformer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ConcurrentLinkedQueue;

import static spark.Spark.*;

class WebService {

    private ConcurrentLinkedQueue<String> simpleQueue = new ConcurrentLinkedQueue<>();

    WebService(final OptionsParser optionsParser) {

        port(Integer.parseInt(optionsParser.getWebPort()));
        staticFiles.location("/public");

        if(optionsParser.isWs()) {
            webSocket("/ws", WebSocketHandler.class);
        }
        get("/settings", (req, res) -> new SettingsResponse(optionsParser.isWs(), optionsParser.getAudioPlayBackSpeed()), new JsonConverter());

        get("/request", (req, res) -> {

            String text = req.queryParams("text");

            if(text == null) {
                res.status(400);
                return "Please specify request text";
            }

            System.out.printf("Sending request to play: %s\n", text);

            if(optionsParser.isWs()) {
                WebSocketHandler.getSessions().forEach(session -> {
                    try {
                        session.getRemote().sendString(text);
                    } catch (IOException e) {
                        System.err.println("Failed to send request to WS session, closing it");
                        session.close();
                        e.printStackTrace();
                    }
                });
            } else {
                simpleQueue.add(text);
            }

            return "TTS requested";
        });

        get("/play", (req, res) -> {

            System.out.println("Play request received");

            res.raw().addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            res.raw().addHeader("Pragma", "no-cache");
            res.raw().addHeader("Expires", "0");

            if(optionsParser.isWs()) {
                String text = req.queryParams("text");
                if (text == null) {
                    res.status(400);
                    return "text get parameter not presented";
                }
                processTTS(res, text);
            } else {
                if(simpleQueue.size() == 0) {
                    res.status(204);
                } else {
                    processTTS(res, simpleQueue.poll());
                }
            }
            return res.raw();
        });
    }

    private void processTTS(Response res, String text) {

        res.raw().setContentType("audio/ogg");
        InputStream inputStream = null;
        try {
            inputStream = new URL(IvonaService.getInstance().getTTSUrl(text)).openStream();
            IOUtils.copy(inputStream, res.raw().getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            res.status(503);
            res.body("failed to get TTS");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class JsonConverter implements ResponseTransformer {
        @Override
        public String render(Object model) {
            return new JSONObject(model).toString();
        }
    }

}
