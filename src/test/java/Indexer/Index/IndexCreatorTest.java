package Indexer.Index;

import Indexer.Util.AnalyzerEnum;
import Indexer.Util.Config;
import Indexer.Util.FieldEnum;
import org.apache.lucene.document.Document;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class IndexCreatorTest {

    private IndexController _index;
    public IndexCreatorTest() throws IOException {
        Config.TIME_UNIT = TimeUnit.MINUTES;
        Config.RECURRENT_DELAY = 1;
        Config.INITIAL_DELAY = 1;
        Config.ANALYZER_TYPE = AnalyzerEnum.STANDARD;

        Config.FILES_DIRECTORIES.add("src/test/resources/dir1");
        Config.FILES_DIRECTORIES.add("src/test/resources/dir2");


        Config.INDEX_LOCATION = "src/test/resources/index";

        _index = new IndexController();

    }

    @Test
    public void createIndex() {
        _index.createIndex();
        assertEquals(_index.is_indexCreated(), true);
    }

    @Test
    public void searchIndex() {
        List<Document> hits =_index.searchIndex("exquisite", FieldEnum.CONTENTS);
        assertEquals(hits.size(), 2);
    }


    @Test
    public void addDocuments(){

        ArrayList<File> files = new ArrayList<>();
        files.add(new File("src/test/resources/additional/add1.txt"));
        _index.addDocuments(files);
        List<Document> hits =_index.searchIndex("middleton", FieldEnum.CONTENTS);
        assertEquals(hits.size(), 3);

    }


    @Test
    public void deleteDocument() throws IOException {

        File resourcesDirectory = new File("src/test/resources");
        _index.deleteDocument(resourcesDirectory.getCanonicalPath() + "/additional/add1.txt");
        List<Document> hits =_index.searchIndex("middleton", FieldEnum.CONTENTS);
        assertEquals(hits.size(), 2);

    }

    @Test
    public void shutDown() {
        _index.createIndex();

        assertEquals(_index.shutDown(), true);
    }

    @Test
    public void shutDown_Fails() {

        assertEquals(_index.shutDown(), false);
    }
}