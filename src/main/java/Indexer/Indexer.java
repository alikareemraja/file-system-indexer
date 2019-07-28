package Indexer;

import Indexer.Index.IndexController;
import Indexer.Util.AnalyzerEnum;
import Indexer.Util.FieldEnum;
import org.apache.lucene.document.Document;

import java.util.List;
import java.util.concurrent.TimeUnit;

import Indexer.Util.Config;

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

    public List<Document> searchIndex(String queryString) {
        try {
            return _index.searchIndex(queryString, FieldEnum.CONTENTS);
        } catch (Exception e) {
            System.out.println("Failed to search");
        }
        return null;
    }

    public boolean closeIndexer(){
        return _index.shutDown();
    }

    /*public static void main(String[] args ){

        String[] directories = {"/home/alikareemraja/TUM/dump", "/home/alikareemraja/TUM/dump 2"};
        Config.FILES_DIRECTORIES.add("/home/alikareemraja/TUM/dump");
        Config.FILES_DIRECTORIES.add("/home/alikareemraja/TUM/dump 2");
        Config.INDEX_LOCATION = "/home/alikareemraja/TUM/index";

        Config.INITIAL_DELAY = 1;
        Config.RECURRENT_DELAY = 1;
        Config.TIME_UNIT = TimeUnit.MINUTES;

        Config.ANALYZER_TYPE = AnalyzerEnum.STANDARD;

                
        try{
            IndexController index = new IndexController();
            index.createIndex();
            List<Document> hits =  index.searchIndex("Gyldendal", FieldEnum.CONTENTS);
            String field = hits.get(0).getField("modified").stringValue();

            System.out.println("So far so good");
        }
        catch (Exception e){
            System.out.println("BRUH ...");
        }

    }*/


}
