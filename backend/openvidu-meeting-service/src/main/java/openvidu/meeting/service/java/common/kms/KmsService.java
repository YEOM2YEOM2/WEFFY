package openvidu.meeting.service.java.common.kms;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.EncryptionAlgorithmSpec;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

@Service
public class KmsService {

    @Value("${aws.kms.key-id}")
    private String KEY_ID;

    public String decryptData(String encryptedData) {
        try {
            AWSKMS kmsClient = AWSKMSClientBuilder.standard()
                    .withRegion(Regions.AP_NORTHEAST_2)
                    .build();

            DecryptRequest request = new DecryptRequest();
            request.withCiphertextBlob(ByteBuffer.wrap(Base64.decodeBase64(encryptedData)));
            request.withKeyId(KEY_ID);
            request.withEncryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256);
            ByteBuffer plainText = kmsClient.decrypt(request).getPlaintext();
            return new String(plainText.array());
        } catch (Exception e) {
            System.out.println("encrypt fail: " + e.getMessage());
        }
        return null;
    }
}
