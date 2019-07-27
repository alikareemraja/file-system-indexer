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
import Runnable.UpdateIndex;


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


    public void createIndex() {
        try {
            _indexCreator.createIndex(_analyzer);
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(new UpdateIndex(), 1, 1, TimeUnit.MINUTES);
        } catch (Exception e) {
            System.out.println("Failed to create Index");
        }
    }

    public void updateDocument(File file){

        try{
            _indexUpdater.updateDocument(file, _analyzer);
        }
        catch (Exception e){
            System.out.println("Failed to update index");
        }

    }

    public void deleteDocument(String filePath){

        try{
            _indexUpdater.deleteDocument(filePath, _analyzer);
        }
        catch (Exception e){
            System.out.println("Failed to update index");
        }

    }

    public void addDocuments(ArrayList<File> files){

        try{
            _indexUpdater.addDocuments(files, _analyzer);
        }
        catch (Exception e){
            System.out.println("Failed to update index");
        }

    }


    public List<Document> searchIndex(String queryString, FieldEnum Field) {
        try {
            return _indexSearcher.searchIndex(queryString, Field, _analyzer);
        } catch (Exception e) {
            System.out.println("Failed to search");
        }
        return null;
    }
}
