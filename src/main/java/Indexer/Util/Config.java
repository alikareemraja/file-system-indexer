package Indexer.Util;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Config {

    // DIRECTORIES TO INDEX
    public static ArrayList<String> FILES_DIRECTORIES = new ArrayList<String>();

    // LOCATION TO STORE INDEX
    public static String INDEX_LOCATION;

    // UNIT OF TIME FOR SCHEDULER
    public static TimeUnit TIME_UNIT;
    // INITIAL DELAY FOR SCHEDULER
    public static long INITIAL_DELAY;
    // SUBSEQUENT DELAY FOR SCHEDULER
    public static long RECURRENT_DELAY;

    // Type of analyzer to be created
    public static AnalyzerEnum ANALYZER_TYPE;

    // FORMAT TO STORE DATE IN
    public static final String DATE_FORMAT = "MM-dd-yyyy HH:mm:ss";
}
