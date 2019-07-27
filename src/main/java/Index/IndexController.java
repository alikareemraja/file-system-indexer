package Index;


import Util.Config;
import Util.FieldEnum;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import Runnable.UpdateIndexJob;


/**
 * Internal controller responsible for all things Lucene
 */
public class IndexController {

    private StandardAnalyzer _analyzer;
    private IndexCreator _indexCreator;
    private IndexSearcher _indexSearcher;
    private IndexUpdater _indexUpdater;


    public IndexController() {
        _analyzer = new StandardAnalyzer();
        _indexCreator = new IndexCreator();
        _indexSearcher = new IndexSearcher();
        _indexUpdater = new IndexUpdater();
    }


    /**
     * Create and store lucene index using the directories and storage path in config. Delete old index folder if
     * wanting to recreate index
     */
    public void createIndex() {
        try {
            _indexCreator.createIndex(_analyzer);

            // Need only one thread
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(new UpdateIndexJob(), Config.INITIAL_DELAY, Config.RECURRENT_DELAY, Config.TIME_UNIT);
        } catch (Exception e) {
            System.out.println("Failed to create Index");
        }
    }

    /**
     * update document in index
     * @param file file to be updated
     */
    public void updateDocument(File file){

        try{
            _indexUpdater.updateDocument(file, _analyzer);
        }
        catch (Exception e){
            System.out.println("Failed to update index");
        }

    }

    /**
     * delete document from index
     * @param filePath path of file to me deleted
     */
    public void deleteDocument(String filePath){

        try{
            _indexUpdater.deleteDocument(filePath, _analyzer);
        }
        catch (Exception e){
            System.out.println("Failed to update index");
        }

    }


    /**
     * add multiple new documents to the lucene index
     * @param files files to up added
     */
    public void addDocuments(ArrayList<File> files){

        try{
            _indexUpdater.addDocuments(files, _analyzer);
        }
        catch (Exception e){
            System.out.println("Failed to update index");
        }

    }


    /**
     * perform search on the index. Field parameter specifie which field in the index to query. For end user it will
     * always be CONTENTS
     * @param queryString string to be queried
     * @param Field field to be searched
     * @return all matching documents
     */
    public List<Document> searchIndex(String queryString, FieldEnum Field) {
        try {
            return _indexSearcher.searchIndex(queryString, Field, _analyzer);
        } catch (Exception e) {
            System.out.println("Failed to search");
        }
        return null;
    }
}
