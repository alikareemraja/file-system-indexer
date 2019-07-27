import Index.IndexController;
import Util.FieldEnum;
import org.apache.lucene.document.Document;

import java.util.List;
import java.util.concurrent.TimeUnit;

import Util.Config;

public class Indexer {

    private IndexController _index;

    public Indexer(String[] directories, String indexLocation, long initialDelay, long recurrentDelay, TimeUnit unit) {

        for (String directory : directories){
            Config.FILES_DIRECTORIES.add(directory);
        }

        Config.INDEX_LOCATION = indexLocation;

        Config.INITIAL_DELAY = initialDelay;
        Config.RECURRENT_DELAY = recurrentDelay;
        Config.TIME_UNIT = unit;
        _index = new IndexController();
    }

    public List<Document> searchIndex(String queryString) {
        try {
            return _index.searchIndex(queryString, FieldEnum.CONTENTS);
        } catch (Exception e) {
            System.out.println("Failed to search");
        }
        return null;
    }

    public static void main(String[] args ){

        String[] directories = {"/home/alikareemraja/TUM/dump", "/home/alikareemraja/TUM/dump 2"};
        Config.FILES_DIRECTORIES.add("/home/alikareemraja/TUM/dump");
        Config.FILES_DIRECTORIES.add("/home/alikareemraja/TUM/dump 2");
        Config.INDEX_LOCATION = "/home/alikareemraja/TUM/index";


                
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

    }


}
