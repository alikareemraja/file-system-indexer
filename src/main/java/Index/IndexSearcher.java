package Index;

import Util.Config;
import Util.FieldEnum;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class IndexSearcher {



    public List<Document> searchIndex(String queryString, FieldEnum Field, StandardAnalyzer analyzer) throws IOException, ParseException {

        Query query = generateQuery(queryString, Field, analyzer);

        Directory index_directory = FSDirectory.open(Paths.get(Config.INDEX_LOCATION));
        IndexReader indexReader = DirectoryReader.open(index_directory);
        org.apache.lucene.search.IndexSearcher searcher = new org.apache.lucene.search.IndexSearcher(indexReader);
        TopDocs topDocs = searcher.search(query, 10);
        List<Document> documents = new ArrayList<>();
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            documents.add(searcher.doc(scoreDoc.doc));
        }

        return documents;

    }

    private Query generateQuery(String queryString, FieldEnum Field, StandardAnalyzer analyzer) throws ParseException {

        if(Field == FieldEnum.CONTENTS){
            return new QueryParser(Util.Util.getFieldMapping(Field), analyzer).parse(QueryParser.escape(queryString));
        }
        else return new TermQuery(new Term(Util.Util.getFieldMapping(Field), queryString));

    }
}
