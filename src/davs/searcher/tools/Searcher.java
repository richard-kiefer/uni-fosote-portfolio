package davs.searcher.tools;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

public class Searcher
	{
		
		public static Version currentVersion = Version.LUCENE_35;
		
		private Directory indexLocation;
		private IndexSearcher indexSearcher;
		private IndexReader indexReader;
		
		public Searcher(String indexLocation) throws IOException
			{
				String fullPath = IndexUtils.getValidIndexPath(indexLocation);
				
				setIndexLocation( new SimpleFSDirectory(new File(fullPath)) );
				setIndexReader( IndexReader.open(getIndexLocation(), true) );
				setIndexSearcher( new IndexSearcher(getIndexReader()) );
			}
		
		private Analyzer getAnalyzer()
			{
				//(same as indexer's analyzer) name1a.name2b_name3... => (to tokens) namea, nameb, name...
				return new StopAnalyzer(currentVersion);
			}
		
		
		
		/**
		 * Based on search term, return docsNumber's of potential hits
		 * 
		 * 
		 * @param term Term looked up in the index
		 * @param docsNumber Number of results returned
		 * @return Results return in specific order
		 * @throws CorruptIndexException
		 * @throws IOException
		 */
		public Queue<String> getSearchTerms(String term, int docsNumber) throws CorruptIndexException, IOException
		{
			Queue<String> results = new LinkedList<String>();
			
			Query query = null;
			
			String[] fields = {IndexField.filename , IndexField.fulltext};
			
			MultiFieldQueryParser queryParser = new MultiFieldQueryParser(currentVersion, fields, getAnalyzer());
			
			try
				{
					
					query = queryParser.parse(term);
					
				} catch (ParseException e)
				{
					return null;
				}

			TopDocs hits = getIndexSearcher().search(query, docsNumber);

			for (int i = 0; i < hits.scoreDocs.length; i++)
				{
					ScoreDoc scoreDoc = hits.scoreDocs[i];
					Document doc = getIndexSearcher().doc(scoreDoc.doc);
					results.add(doc.get(IndexField.filepath));
				}
			
			
			return results;
			
		}

	
		
		
		/**
		 * Close index searcher and search reader
		 * 
		 * @throws IOException
		 */
		public void close() throws IOException
			{
				getIndexReader().close();
				getIndexSearcher().close();
			}
		

		private Directory getIndexLocation()
			{
				return indexLocation;
			}

		private void setIndexLocation(Directory indexLocation)
			{
				this.indexLocation = indexLocation;
			}

		private IndexSearcher getIndexSearcher()
			{
				return indexSearcher;
			}

		private void setIndexSearcher(IndexSearcher indexSearcher)
			{
				this.indexSearcher = indexSearcher;
			}

		private IndexReader getIndexReader()
			{
				return indexReader;
			}

		private void setIndexReader(IndexReader indexReader)
			{
				this.indexReader = indexReader;
			}

	}
