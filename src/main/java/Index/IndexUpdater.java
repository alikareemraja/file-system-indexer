package Index;

import Util.Config;
import Util.LuceneConstants;
import Util.Util;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Update index
 */
class IndexUpdater {

    private IndexWriter _writer;

    /**
     * Update the document in index
     * @param file file on disk
     * @param analyzer analyzer for tokenization
     * @throws IOException index fails to update document
     */
    void updateDocument(File file, StandardAnalyzer analyzer) throws IOException {


        Directory index_directory = FSDirectory.open(Paths.get(Config.INDEX_LOCATION));

        if(DirectoryReader.indexExists(index_directory)){
            return;
        }

        _writer = new IndexWriter(index_directory, new IndexWriterConfig(analyzer));

        Document document = Util.createDocument(file);

        //update indexes for file contents
        _writer.updateDocument(new Term (LuceneConstants.PATH, file.getCanonicalPath()),document);
        _writer.close();

    }

    /**
     * delete document from index
     * @param filePath path of file to be deleted from index
     * @param analyzer analyzer for tokenization
     * @throws IOException file fails to be deleted from index
     */
    void deleteDocument(String filePath, StandardAnalyzer analyzer) throws IOException {


        Directory index_directory = FSDirectory.open(Paths.get(Config.INDEX_LOCATION));

        if(DirectoryReader.indexExists(index_directory)){
            return;
        }

        _writer = new IndexWriter(index_directory, new IndexWriterConfig(analyzer));

        //update indexes for file contents
        _writer.deleteDocuments(new Term (LuceneConstants.PATH, filePath));
        _writer.close();

    }

    /**
     * add documents to the index
     * @param files list of files to be added to index
     * @param analyzer analyzer for tokenization
     * @throws IOException files failed to be added to the index
     */
    void addDocuments(ArrayList<File> files, StandardAnalyzer analyzer) throws IOException {


        Directory index_directory = FSDirectory.open(Paths.get(Config.INDEX_LOCATION));

        if(DirectoryReader.indexExists(index_directory)){
            return;
        }

        _writer = new IndexWriter(index_directory, new IndexWriterConfig(analyzer));

        ArrayList<Document> documents = new ArrayList<>();
        for (File file : files){
            documents.add(Util.createDocument(file));

        }
        //update indexes for file contents
        _writer.addDocuments(documents);
        _writer.close();

    }
}
