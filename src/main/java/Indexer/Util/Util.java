package Indexer.Util;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import java.io.*;
import java.io.FileReader;
import java.text.SimpleDateFormat;

public class Util {

    /**
     * Map from enum to string
     * @param fieldEnum enum
     * @return string
     */
    public static String getFieldMapping(FieldEnum fieldEnum){

        switch (fieldEnum){
            case CONTENTS:
                return LuceneConstants.CONTENTS;

            case MODIFIED_DATE:
                return LuceneConstants.MODIFIED_DATE;

            case PATH:
                return LuceneConstants.PATH;
        }
        return null;
    }

    /**
     * create a lucene document from file
     * @param file file to be stored
     * @return lucene document
     * @throws IOException failed to create document
     */
    public static Document createDocument(File file) throws IOException {
        Reader reader = new FileReader(file);

        Document document = new Document();

        document.add(new StringField(LuceneConstants.PATH, file.getCanonicalPath(), Field.Store.YES));
        document.add(new StringField(LuceneConstants.MODIFIED_DATE, new SimpleDateFormat(Config.DATE_FORMAT).format(file.lastModified()), Field.Store.YES));
        document.add(new TextField(LuceneConstants.CONTENTS, reader));

        return document;
    }


}
