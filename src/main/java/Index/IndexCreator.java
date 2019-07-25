package Index;

import Util.Config;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

public class IndexCreator {

    IndexWriter _writer;

    public void createIndex(StandardAnalyzer analyzer) throws IOException {

        Directory index_directory = FSDirectory.open(Paths.get(Config.INDEX_LOCATION));

        if(DirectoryReader.indexExists(index_directory)){
            return;
        }

        _writer = new IndexWriter(index_directory, new IndexWriterConfig(analyzer));

        for (String directory : Config.FILES_DIRECTORIES)
        {
            indexDirectory(directory);

        }

        _writer.close();
    }

    private void indexDirectory(String directory){
        try{
            Files.find(Paths.get(directory),
                    Integer.MAX_VALUE,
                    (filePath, fileAttr) -> fileAttr.isRegularFile())
                    .forEach((iterator) -> {

                        try {

                            File file = iterator.toFile();
                            Document document = new Document();

                            String path = file.getCanonicalPath();
                            document.add(new TextField(Config.FIELD_PATH, path, Field.Store.YES));
                            document.add(new TextField(Config.FIELD_MODIFIED_DATE, new SimpleDateFormat(Config.DATE_FORMAT).format(file.lastModified()), Field.Store.YES));
                            Reader reader = new FileReader(file);
                            document.add(new TextField(Config.FIELD_CONTENTS, reader));

                            _writer.addDocument(document);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });
        }
        catch (Exception e){
            System.out.println("Failed to index directory: " + directory);
        }

    }
}
