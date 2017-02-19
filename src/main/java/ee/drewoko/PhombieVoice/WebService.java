package ee.drewoko.PhombieVoice;

import com.amazonaws.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static spark.Spark.*;

class WebService {

    private final static Logger log = LoggerFactory.getLogger(WebService.class);

    WebService(final OptionsParser optionsParser) {

        port(Integer.parseInt(optionsParser.getWebPort()));
        staticFiles.location("/public");

        webSocket("/ws", WebSocketHandler.class);

        get("/request", (req, res) -> {

            String text = req.queryParams("text");

            if(text == null) {
                res.status(400);
                return "Please specify request text";
            }

            String voice = req.queryParams("voice");

            if(voice == null) {
                voice = optionsParser.getIvonaVoice();
            }

            String playbackSpeed = req.queryParams("speed");

            if(playbackSpeed == null) {
                playbackSpeed = optionsParser.getAudioPlayBackSpeed();
            }

            String playImmediatelyStr = req.queryParams("immediately");
            Boolean playImmediately = optionsParser.getPlayImmediately();

            if(playImmediatelyStr != null) {
                playImmediately = Boolean.valueOf(playImmediatelyStr);
            }

            log.info(String.format("Sending request to play with %s voice and %s speed: %s\n", voice, playbackSpeed, text));

            Play playReq = new Play(text, voice, playbackSpeed, playImmediately);

            WebSocketHandler.getSessions().forEach(session -> {
                try {
                    session.getRemote().sendString(playReq.toJson());
                } catch (IOException e) {
                    log.error("Failed to send request to WS session, closing it", e);
                    session.close();
                }
            });

            return "TTS requested";
        });

        get("/play", (req, res) -> {

            log.info("Play request received");

            res.raw().addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            res.raw().addHeader("Pragma", "no-cache");
            res.raw().addHeader("Expires", "0");

            String text = req.queryParams("text");
            if (text == null) {
                res.status(400);
                return "text get parameter not presented";
            }
            String voice = req.queryParams("voice");
            if (voice == null) {
                res.status(400);
                return "voice get parameter not presented";
            }
            processTTS(res, text, voice);

            return res.raw();
        });
    }

    private void processTTS(Response res, String text, String voice) {

        res.raw().setContentType("audio/ogg");
        InputStream inputStream = null;
        try {
            inputStream = new URL(IvonaService.getInstance().getTTSUrl(text, voice)).openStream();
            IOUtils.copy(inputStream, res.raw().getOutputStream());
        } catch (Exception e) {
            log.error("failed to get TTS", e);
            res.status(503);
            res.body("failed to get TTS");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("failed to close stream", e);
                }
            }
        }
    }
}
