package com.weffy.mattermostcontentservice.file;

import com.weffy.mattermostcontentservice.file.service.FileService;
import com.weffy.mattermostcontentservice.file.service.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FileTest {

//    @Mock
//    private RestTemplate mockRestTemplate;
//
//    @Mock
//    private URL mockUrl;

    @Autowired
    private FileServiceImpl service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

//    @Test
//    public void testDownloadFileFromURL() throws IOException {
//        // mock the InputStream from URL.openStream()
//        when(mockUrl.openStream()).thenReturn(new ByteArrayInputStream("test".getBytes()));
//
//        // call the downloadFileFromURL method
//        Path path = service.downloadFileFromURL(mockUrl);
//
//        // assert the expected result
//        // ...
//    }

    @Test
    public void testUploadFileToMattermost() throws IOException {
//        when(mockRestTemplate.postForEntity(anyString(), any(), any(Class.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // call the uploadFileToMattermost method
//        service.uploadFileToMattermost(Paths.get("C:\\test.txt"), "https://meeting.ssafy.com/s09p10d1/channels/d107");

        // verify if postForEntity was called
//        verify(mockRestTemplate, times(1)).postForEntity(anyString(), any(), any(Class.class));
        Path filePath = Paths.get("C:\\test.txt");
        String mmURL = "https://meeting.ssafy.com/s09p10d1/channels/d107";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        //sessionToken
        headers.setBearerAuth("k8jrikoiftdbtdanr54j7d5ush");

        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(filePath)) {
            @Override
            public String getFilename() {
                return filePath.getFileName().toString();
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("files", resource);
        body.add("channel_id", "YOUR_CHANNEL_ID"); // replace with your channel id

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        restTemplate.postForEntity(mmURL, requestEntity, String.class);
    }
}