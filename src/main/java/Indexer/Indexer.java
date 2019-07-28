package Indexer;

import Indexer.Index.IndexController;
import Indexer.Util.AnalyzerEnum;
import Indexer.Util.FieldEnum;
import org.apache.lucene.document.Document;

import java.util.List;
import java.util.concurrent.TimeUnit;

import Indexer.Util.Config;

/**
 * public entry point for the library
 */
public class Indexer {

    private IndexController _index;

    public Indexer(String[] directories, String indexLocation, AnalyzerEnum analyzerType, long initialDelay, long recurrentDelay, TimeUnit unit) {

        for (String directory : directories){
            Config.FILES_DIRECTORIES.add(directory);
        }

        Config.INDEX_LOCATION = indexLocation;

        Config.INITIAL_DELAY = initialDelay;
        Config.RECURRENT_DELAY = recurrentDelay;
        Config.TIME_UNIT = unit;

        Config.ANALYZER_TYPE = analyzerType;

        _index = new IndexController();
        _index.createIndex();
    }

    /**
     * public method to query the index
     * @param queryString
     * @return
     */
    public List<Document> searchIndex(String queryString) {
        try {
            return _index.searchIndex(queryString, FieldEnum.CONTENTS);
        } catch (Exception e) {
            System.out.println("Failed to search");
        }
        return null;
    }

    /**
     * call to shutdown use of index
     * @return
     */
    public boolean closeIndexer(){
        return _index.shutDown();
    }


}
