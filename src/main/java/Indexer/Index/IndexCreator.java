package Indexer.Index;

import Indexer.Util.Config;
import Indexer.Util.Util;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Create index from scratch
 */
class IndexCreator {

    private IndexWriter _writer;

    /**
     * Create index from scratch
     * @param analyzer analyzer for tokenization
     * @throws Exception if the index fails to be created
     */
    void createIndex(Analyzer analyzer) throws Exception {


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

    /**
     * Walk the directory and add document
     * @param directory directory to be walked
     */
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
