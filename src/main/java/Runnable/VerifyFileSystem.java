package Runnable;

import Util.Config;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

public class VerifyFileSystem implements Runnable {
    @Override
    public void run() {

        for (String directory : Config.FILES_DIRECTORIES)
        {
            try {
                Files.find(Paths.get(directory),
                        Integer.MAX_VALUE,
                        (filePath, fileAttr) -> fileAttr.isRegularFile())
                        .forEach((iterator) -> {

                            try {
                                File file = iterator.toFile();


                                Document document = new Document();

                                String path = file.getCanonicalPath();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
