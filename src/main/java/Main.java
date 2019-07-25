import Index.Index;
import Util.FieldEnum;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.util.List;
import Util.Config;

public class Main {



    public static void main(String[] args ){

        Config.FILES_DIRECTORIES.add("/home/alikareemraja/TUM/dump");
        Config.FILES_DIRECTORIES.add("/home/alikareemraja/TUM/dump 2");
        Config.INDEX_LOCATION = "/home/alikareemraja/TUM/index";
        Index index = new Index();
                
        try{
            index.createIndex();
            List<Document> hits =  index.searchIndex("Gyldendal", FieldEnum.FIELD_CONTENTS);
            System.out.println("So far so good");
        }
        catch (Exception e){
            System.out.println("BRUH ...");
        }

    }


}
