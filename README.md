# Indexer

The Indexer library interface allows you to index multiple directories and query them. The library is configurable with the following variables:

 - String[] directories : List of directories to index
 - String indexLocation: Location to store the index
 - AnalyzerEnum analyzerType:
	 - STANDARD: It removes stop words and lower cases the generated tokens. Recognizes URLs and emails.
	 - STOP: It splits text by non-letter characters and removes stop words from the token list.
	 - SIMPLE: doesn't remove stop words. It also doesn’t recognize URLs.
	 - WHITESPACE: It splits text by whitespace characters.
	 - KEYWORD: It tokenizes input into a single token, useful for matching full IDs.
 - long initialDelay: Delay before first reindexing is done
 - long recurrentDelay: Subsequent delay after first reindexing
 - TimeUnit unit: Unit of time for the two delays/
  
The library supports multi-threaded access to the index, as well as tracks changes in files and the composition of directories on the disk.

## Creating an instance of Indexer

All publicly useable functions are available in package Indexer. Creating an instance of the Indexer class with the above given configurations will create an indexer and will automatically index the directories.

## Searching

Use searchIndex(String query) function for querying. It will return List<Document> of type lucene.Documents.

## Shutdown

When you are done using the indexer, call closeIndexer() to kill all threads running for reindexing.
