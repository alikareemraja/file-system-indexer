package Runnable;

import Index.IndexController;
import Util.Config;
import Util.FieldEnum;
import Util.LuceneConstants;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Recurrent job to update index
 */
public class UpdateIndexJob implements Runnable {

    private IndexController _index;
    private ArrayList<File> _toAdd;

    public UpdateIndexJob(){
        _index = new IndexController();
    }

    /**
     * runnable function called when a new thread is made
     */
    @Override
    public void run() {

        System.out.println("Running updater!");


        for (String directory : Config.FILES_DIRECTORIES)
        {
            _toAdd = new ArrayList<>();
            try {

                walkDirectoryForUpdates(directory);

                if(_toAdd.size() > 0){
                    System.out.println("Adding files!");
                    _index.addDocuments(_toAdd);
                }

                walkDirectoryForDeletions();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Walk the directory and find files to delete
     * @throws IOException if file cannot be deleted from index
     */
    private void walkDirectoryForDeletions() throws IOException {
        Directory index_directory = FSDirectory.open(Paths.get(Config.INDEX_LOCATION));
        IndexReader reader = DirectoryReader.open(index_directory);
        for (int i=0; i<reader.maxDoc(); i++) {

            Document doc = reader.document(i);
            String docPath = doc.get(LuceneConstants.PATH);

            if(!(new File(docPath).exists())){
                System.out.println("Deleting file!");
                _index.deleteDocument(docPath);
            }
        }
    }

    /**
     * Walk the directory and find files to be updated
     * @param directory directory to walk
     * @throws IOException if file cannot be deleted from index
     */
    private void walkDirectoryForUpdates(String directory) throws IOException {
        Files.find(Paths.get(directory),
                Integer.MAX_VALUE,
                (filePath, fileAttr) -> fileAttr.isRegularFile())
                .forEach((iterator) -> {

                    try {
                        File file = iterator.toFile();

                        List<Document> hits= _index.searchIndex(file.getCanonicalPath(), FieldEnum.PATH);

                        if(hits.isEmpty()){
                            _toAdd.add(file);

                        }
                        else if(!isDocumentModified(hits.get(0), file)){

                            System.out.println("Updating file");
                            _index.updateDocument(file);

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });
    }

    /**
     * check if document has been modified since being added to the index
     * @param document document in index
     * @param file file on disk
     * @return boolean
     */
    private boolean isDocumentModified(Document document, File file){
        String recordDate = document.getField(LuceneConstants.MODIFIED_DATE).stringValue();
        String dateNow = new SimpleDateFormat(Config.DATE_FORMAT).format(file.lastModified());
        return recordDate.equals(dateNow);
    }
}
