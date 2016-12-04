package ee.drewoko.PhombieVoice;

import com.amazonaws.auth.AWSCredentials;

public class IvonaCredential implements AWSCredentials {

    private String AWSAccessKeyId;
    private String AWSSecretKey;

    IvonaCredential(String AWSAccessKeyId, String AWSSecretKey) {
        this.AWSAccessKeyId = AWSAccessKeyId;
        this.AWSSecretKey = AWSSecretKey;
    }

    public String getAWSAccessKeyId() {
        return AWSAccessKeyId;
    }

    public String getAWSSecretKey() {
        return AWSSecretKey;
    }
}
