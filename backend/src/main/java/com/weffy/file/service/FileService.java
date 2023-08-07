package com.weffy.file.service;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface FileService {
    String uploadInputStream(BufferedImage image, String fileName) throws IOException;
}
