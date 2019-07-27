package Index;

import Util.Config;
import Util.LuceneConstants;
import Util.Util;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IndexCreator {

    private IndexWriter _writer;

    public void createIndex(StandardAnalyzer analyzer) throws Exception {


            if(Config.INDEX_LOCATION.isEmpty()){
                throw new Exception("Index location not specified");
            }

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

                            _writer.addDocument(Util.createDocument(file));

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
