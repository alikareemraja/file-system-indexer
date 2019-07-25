package Index;

import Util.FieldEnum;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.List;

public class Index {

    private StandardAnalyzer _analyzer;
    private IndexCreator _indexCreator;
    private IndexSearcher _indexSearcher;

    public Index() {
        _analyzer = new StandardAnalyzer();
        _indexCreator = new IndexCreator();
        _indexSearcher = new IndexSearcher();
    }

    public void createIndex() {
        try {
            _indexCreator.createIndex(_analyzer);
        } catch (Exception e) {
            System.out.println("Failed to create Index");
        }

    }

    public List<Document> searchIndex(String queryString, FieldEnum Field) throws IOException, ParseException {
        try {
            return _indexSearcher.searchIndex(queryString, Field, _analyzer);
        } catch (Exception e) {
            System.out.println("Failed to search");
        }
        return null;
    }
}
