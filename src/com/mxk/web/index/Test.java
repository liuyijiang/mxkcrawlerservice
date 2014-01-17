package com.mxk.web.index;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.DuplicateFilter;
import org.apache.lucene.search.FilteredQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.mxk.dao.WebResourceMapper;
import com.mxk.model.WebResource;
import com.mxk.model.WebResourceCriteria;
import com.mxk.web.search.SearchRespone;

public class Test {

	public static final Logger logger = LoggerFactory
			.getLogger(IndexService.class);

	@Resource
	private WebResourceMapper webResourceMapper;

	@PostConstruct
	public void execute() throws Exception {
		WebResourceCriteria criteria = new WebResourceCriteria();
		criteria.createCriteria().andIdGreaterThan(0);
		List<WebResource> list = webResourceMapper.selectByExample(criteria);
		//System.out.println(list.size());

		// Indexer indexer = new Indexe

		// IndexWriter writer = new IndexWriter("D:\\index",getAnalyzer(),true);
		// Directory dir = FSDirectory.open(new File("D:\\index"));
		// IndexWriter writer = new
		// IndexWriter(dir,getAnalyzer(),true,IndexWriter.MAX_TERM_LENGTH);

		// 存储索引的目录
		File indexDir = new File("D:\\index");
		// 索引目录
		Directory dir = FSDirectory.open(indexDir);
		// 创建IKAnalyzer中文分词对象
		Analyzer analyzer = new IKAnalyzer();
		// 配置IndexWriterConfig
		IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_36,
				analyzer);
		iwConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
		// 创建写索引对象
		IndexWriter writer = new IndexWriter(dir, iwConfig);

		for (WebResource res : list) {
			String url = res.getUrl();
			String title = res.getTitle();
			String content = res.getTitle()+" | "+res.getInfo();
			String img = res.getImage();
			String txt = res.getOwnername() + " | " + res.getMultiinfo();
			// 创建文档
			Document doc = new Document();
			// 加入url域
			doc.add(new Field("url", url, Field.Store.YES,
					Field.Index.NOT_ANALYZED));  //Field.Index.NOT_ANALYZED 要分词
			// 加入标题域
			doc.add(new Field("title", title, Field.Store.YES,
					Field.Index.ANALYZED));
			// 加入内容域
			doc.add(new Field("content", content, Field.Store.YES,
					Field.Index.ANALYZED));
			
			// 加入内容域
			doc.add(new Field("img", img, Field.Store.YES,
					Field.Index.NOT_ANALYZED));
			
			doc.add(new Field("subtext", txt, Field.Store.YES,
					Field.Index.ANALYZED));
			
			// 写入文档
			writer.addDocument(doc);
		}
		// 关闭
		writer.close();
		logger.info("索引创建完成");
	}

	public Analyzer getAnalyzer() {
		IKAnalyzer analyzer = new IKAnalyzer();
		analyzer.setUseSmart(true);
		return analyzer;
	}

	public static void main(String[] args) throws Exception{
//		String keyWord = "胡德号战舰";
//		// 创建IKAnalyzer中文分词对象
//		IKAnalyzer analyzer = new IKAnalyzer();
//		// 使用智能分词
//		//analyzer.setUseSmart(false);
//		// 打印分词结果 
//		try {
//			printAnalysisResult(analyzer, keyWord);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		searcher("大和","content");
	}

	private static void printAnalysisResult(Analyzer analyzer, String keyWord)
			throws Exception {
		System.out.println("[" + keyWord + "]分词效果如下");
		TokenStream tokenStream = analyzer.tokenStream("content",
				new StringReader(keyWord));
		tokenStream.addAttribute(CharTermAttribute.class);
		while (tokenStream.incrementToken()) {
			CharTermAttribute charTermAttribute = tokenStream
					.getAttribute(CharTermAttribute.class);
			System.out.println(charTermAttribute.toString());

		}
	}
	
	public List<SearchRespone> searchIndex(String words, String field){
		List<SearchRespone> list = new ArrayList<SearchRespone>();
		try {
			File indexDir = new File("D:\\index");
			// 索引目录
			Directory dir = FSDirectory.open(indexDir);
			// 根据索引目录创建读索引对象
			IndexReader reader = IndexReader.open(dir); //使用一个
			// 搜索对象创建
			IndexSearcher searcher = new IndexSearcher(reader);
			// IKAnalyzer中文分词
			Analyzer analyzer = new IKAnalyzer();
			// 创建查询解析对象
			QueryParser parser = new QueryParser(Version.LUCENE_36, field, analyzer);
			parser.setDefaultOperator(QueryParser.OR_OPERATOR);//设置表达式 与 或
			// 根据域和目标搜索文本创建查询器
			Query query = parser.parse(words);
			System.out.println("Searching for: " + query.toString(field));
			// 对结果进行相似度打分排序
//			TopScoreDocCollector collector = TopScoreDocCollector.create(10,
//					false);
//			searcher.search(query, collector);
			TopDocs topDocs = searcher.search(query, 10);
			
			
			// Sort sort=new Sort(new SortField("birthdays", 
           // new com.ljq.comparator.DateValComparatorSource("yyyy-MM-dd"), false));
			
			//高亮
			SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
			Highlighter highlighter = new Highlighter(simpleHTMLFormatter,new QueryScorer(query));   
	        highlighter.setTextFragmenter(new SimpleFragmenter(1024));       
			// 获取结果
	        ScoreDoc[] hits = topDocs.scoreDocs;
//			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			for (int i = 0; i < hits.length; i++) {
				Document doc = searcher.doc(hits[i].doc);
				SearchRespone sr = new SearchRespone();
				sr.setImg(doc.get("img"));
				String title = doc.get("title");
				String info = doc.get("content");
				TokenStream tokenStreamTitle = analyzer.tokenStream("title",new StringReader(title));  
				String highLightTitle = highlighter.getBestFragment(tokenStreamTitle, title);
				if(highLightTitle == null){
					highLightTitle = title;
				}
				TokenStream tokenStreamInfo = analyzer.tokenStream("content",new StringReader(info));  
				String highLightInfo = highlighter.getBestFragment(tokenStreamInfo, info);
				if(highLightInfo == null){
					highLightInfo = title;
				}
				sr.setInfo(highLightInfo);
				sr.setSubtext(doc.get("subtext"));
				sr.setTitle(highLightTitle);
				sr.setUrl(doc.get("url"));
				list.add(sr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	private static void searcher(String words, String field)
			throws CorruptIndexException, IOException, ParseException, InvalidTokenOffsetsException {
		File indexDir = new File("D:\\index");
		// 索引目录
		Directory dir = FSDirectory.open(indexDir);
		// 根据索引目录创建读索引对象
		IndexReader reader = IndexReader.open(dir);
		// 搜索对象创建
		IndexSearcher searcher = new IndexSearcher(reader);
		// IKAnalyzer中文分词
		Analyzer analyzer = new IKAnalyzer();
		// 创建查询解析对象
		QueryParser parser = new QueryParser(Version.LUCENE_36, field, analyzer);
		parser.setDefaultOperator(QueryParser.OR_OPERATOR);
		// 根据域和目标搜索文本创建查询器
		Query query = parser.parse(words);
		
		DuplicateFilter filter = new DuplicateFilter("content");
		FilteredQuery fquery = new FilteredQuery(query, filter);
		
		
		System.out.println("Searching for: " + query.toString(field));
		// 对结果进行相似度打分排序
//		TopScoreDocCollector collector = TopScoreDocCollector.create(100,
//				false);
		
		TopDocs docs = searcher.search(query, 50); 
		//searcher.search(fquery, collector);
		//searcher.se
		// 获取结果
		ScoreDoc[] hits =  docs.scoreDocs;;

		int numTotalHits = docs.totalHits;

		System.out.println(numTotalHits + " total matching pages");
		// 显示搜索结果
		SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
		 Highlighter highlighter = new Highlighter(simpleHTMLFormatter,new QueryScorer(query));   
         highlighter.setTextFragmenter(new SimpleFragmenter(1024));       
         
		for (int i = 0; i < hits.length; i++) {
			Document doc = searcher.doc(hits[i].doc);
			String url = doc.get("url");
			
			String title = doc.get("title");
			TokenStream tokenStream = analyzer.tokenStream("title",new StringReader(title));  
			String highLightText = highlighter.getBestFragment(tokenStream, title);
			if(highLightText == null){
				highLightText = title;
			}
	        //highlighter.getBestFragment(analyzer, "content", d.get("content"))
			
			String content = doc.get("content");
			TokenStream tokenStream2 = analyzer.tokenStream("content",new StringReader(content));   
			String highLightText2 = highlighter.getBestFragment(tokenStream2, content);  
			
			String img = doc.get("img");
			String txt = doc.get("subtext");
			System.out.println((i + 1) + "." + highLightText);
			System.out.println("-----------------------------------");
			System.out.println(highLightText2);
			System.out.println("-----------------------------------");
			System.out.println(url);
			System.out.println();
		}
	}
   
	//public static  
	
//	private List processHits(Hits hits,int startIndex,int endIndex)throws Exception{
//		   if(endIndex>=hits.length())
//		    endIndex=hits.length()-1;
//		   List docs=new ArrayList();
//		   for(int i=startIndex;i<=endIndex;i++){
//		    Document doc=hits.doc(i);
//		    Map docMap=new HashMap();
//		    docMap.put("id",doc.getField("id").stringValue());
//		    docMap.put("name",doc.getField("name").stringValue());
//		    docMap.put("price",doc.getField("price").stringValue());
//		    docs.add(docMap);
//		   }
//		   return docs;
//		}
	
}
