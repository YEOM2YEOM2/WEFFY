package com.weffy;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.amazonaws.services.kms.model.EncryptResult;
import com.amazonaws.services.kms.model.EncryptionAlgorithmSpec;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@ActiveProfiles("test")
public class KmsTest {
    private static final String KEY_ID = "34e74f60-e450-43b1-ada9-a26be8e472fe";

    @Test
    void encrypt() {
        final String plaintext = "root";

        try {
            AWSKMS kmsClient = AWSKMSClientBuilder.standard()
                    .withRegion(Regions.AP_NORTHEAST_2)
                    .build();

            EncryptRequest request = new EncryptRequest();
            request.withKeyId(KEY_ID);
            request.withPlaintext(ByteBuffer.wrap(plaintext.getBytes(StandardCharsets.UTF_8)));
            request.withEncryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256);

            EncryptResult result = kmsClient.encrypt(request);
            ByteBuffer ciphertextBlob = result.getCiphertextBlob();

            System.out.println("ciphertextBlob: " + new String(Base64.encodeBase64(ciphertextBlob.array())));
        } catch (Exception e) {
            System.out.println("encrypt fail: " + e.getMessage());
        }
    }

    @Test
    void decrypt() {
        final String encriptedText = "N6tC7TFRXfHHm5kEY7xmcSXvgT+H0D/Fj855C12UeORa3o6BFC1Pj56xyL9i9+nDvwLUSTa1YRtkmgmyV7ozIAO4ymJLWHbJKOIBgPlpMShlb/g9vCcKw1T+8qgrWHtRSSxxKid0jbeRsgEy7XG5qPrh4OOj2RgOLYpFUgnsgXZTeKvMQMRA8RtbZscHvBHacwRGf9sHMb60SDTCjTn6qkMIFaqn2zzJGR08NOZa2xBip7IuB0MaY89+3RMJKPVjg+vPpFjW1HG5aWlEANc1HO7ETCWySeo0E7OCbaDTkxrgf6TWXruysRBdIBNZS5BXzzcTHk2yaiN3//SnScGP2YhLFwc/FwT8lca51wepj411O/8iaa8XrM9wjmsBrzZlKzIMY9qJetk4M7BSFofCPqaYZURsDLNhXEjASuc/OSWnGkvE677sYqB7UGFl6yX05tLWYDVC75SRiGIdG88j0EEncZVYcnmb0wACYeaIW+AK+EhqowIVo9MjGhNADR3Kq5AW5vI+X72T4TzMJ089SYgOqSJa4TsdDO0RPx3MufwrDP5ktToxErNev0ul6YoLsSlHcy/5ywC3w3Db0N0gER9H5xXHFtCiUPNrvaBxo8U2rTpmgpnu20YLWpwHT18pf9nguxlu3RWqbZh0ZspTXQVGk1WkR94K3zRlzpdiqrQ=";

        try {
            AWSKMS kmsClient = AWSKMSClientBuilder.standard()
                    .withRegion(Regions.AP_NORTHEAST_2)
                    .build();

            DecryptRequest request = new DecryptRequest();
            request.withCiphertextBlob(ByteBuffer.wrap(Base64.decodeBase64(encriptedText)));
            request.withKeyId(KEY_ID);
            request.withEncryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256);
            ByteBuffer plainText = kmsClient.decrypt(request).getPlaintext();

            System.out.println("plainText: " + new String(plainText.array()));
        } catch (Exception e) {
            System.out.println("decrypt fail: " + e.getMessage());
        }
    }
}