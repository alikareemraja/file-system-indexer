package Indexer.Index;

import Indexer.Util.Config;
import Indexer.Util.FieldEnum;
import Indexer.Util.Util;
import org.apache.lucene.analysis.Analyzer;
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


/**
 * Perform searches on the index
 */
class IndexSearcher {


    /**
     * Perform searches on the index
     * @param queryString query string
     * @param Field field to be queried
     * @param analyzer analyzer for tokenization
     * @return list of matching documents
     * @throws IOException if the search fails
     * @throws ParseException if the query fails to be parsed
     */
     List<Document> searchIndex(String queryString, FieldEnum Field, Analyzer analyzer) throws IOException, ParseException {

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

    /**
     * Depending on the Field to be searched, generate a query
     * @param queryString query string
     * @param Field field to be queried
     * @param analyzer analyzer for tokenization
     * @return query
     * @throws ParseException if the querystring fails to parse
     */
    private Query generateQuery(String queryString, FieldEnum Field, Analyzer analyzer) throws ParseException {

        if(Field == FieldEnum.CONTENTS){
            return new QueryParser(Util.getFieldMapping(Field), analyzer).parse(QueryParser.escape(queryString));
        }
        else return new TermQuery(new Term(Util.getFieldMapping(Field), queryString));

    }
}
