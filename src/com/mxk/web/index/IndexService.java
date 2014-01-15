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
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.mxk.dao.WebResourceMapper;
import com.mxk.model.WebResource;
import com.mxk.model.WebResourceCriteria;
import com.mxk.web.search.SearchRespone;
/**
 * 全文检索服务 创建索引
 * 
 * @author Administrator
 * 
 */
@Service
public class IndexService {

	public static final Logger logger = LoggerFactory
			.getLogger(IndexService.class);

	@Resource
	private WebResourceMapper webResourceMapper;

	@PostConstruct
	public void execute() throws Exception {
		WebResourceCriteria criteria = new WebResourceCriteria();
		criteria.createCriteria().andIdGreaterThan(0);
		List<WebResource> list = webResourceMapper.selectByExample(criteria);
		System.out.println(list.size());

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
			String content = res.getInfo();
			String img = res.getImage();
			String txt = res.getOwnername() + " | " + res.getMultiinfo();
			// 创建文档
			Document doc = new Document();
			// 加入url域
			doc.add(new Field("url", url, Field.Store.YES,
					Field.Index.NOT_ANALYZED));
			// 加入标题域
			doc.add(new Field("title", title, Field.Store.YES,
					Field.Index.ANALYZED));
			// 加入内容域
			doc.add(new Field("content", content, Field.Store.YES,
					Field.Index.ANALYZED));
			
			// 加入内容域
			doc.add(new Field("img", img, Field.Store.YES,
					Field.Index.ANALYZED));
			
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
//		String keyWord = "IKAnalyzer的分词效果到底怎么样呢，我们来看一下吧";
//		// 创建IKAnalyzer中文分词对象
//		IKAnalyzer analyzer = new IKAnalyzer();
//		// 使用智能分词
//		analyzer.setUseSmart(true);
//		// 打印分词结果
//		try {
//			printAnalysisResult(analyzer, keyWord);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		searcher("坦克","title");
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
			IndexReader reader = IndexReader.open(dir);
			// 搜索对象创建
			IndexSearcher searcher = new IndexSearcher(reader);
			// IKAnalyzer中文分词
			Analyzer analyzer = new IKAnalyzer();
			// 创建查询解析对象
			QueryParser parser = new QueryParser(Version.LUCENE_36, field, analyzer);
			parser.setDefaultOperator(QueryParser.AND_OPERATOR);
			// 根据域和目标搜索文本创建查询器
			Query query = parser.parse(words);
			System.out.println("Searching for: " + query.toString(field));
			// 对结果进行相似度打分排序
			TopScoreDocCollector collector = TopScoreDocCollector.create(10,
					false);
			searcher.search(query, collector);
			
			// 获取结果
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			for (int i = 0; i < hits.length; i++) {
				Document doc = searcher.doc(hits[i].doc);
				SearchRespone sr = new SearchRespone();
				sr.setImg(doc.get("img"));
				sr.setInfo(doc.get("content"));
				sr.setSubtext(doc.get("subtext"));
				sr.setTitle(highlight(words,doc.get("title")));
				sr.setUrl(doc.get("url"));
				list.add(sr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	private static void searcher(String words, String field)
			throws CorruptIndexException, IOException, ParseException {
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
		parser.setDefaultOperator(QueryParser.AND_OPERATOR);
		// 根据域和目标搜索文本创建查询器
		Query query = parser.parse(words);
		
		
		System.out.println("Searching for: " + query.toString(field));
		// 对结果进行相似度打分排序
		TopScoreDocCollector collector = TopScoreDocCollector.create(10,
				false);
		searcher.search(query, collector);
		// 获取结果
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		int numTotalHits = collector.getTotalHits();

		System.out.println(numTotalHits + " total matching pages");
		// 显示搜索结果
		for (int i = 0; i < hits.length; i++) {
			Document doc = searcher.doc(hits[i].doc);
			String url = doc.get("url");
			String title = highlight(words,doc.get("title"));
			String content = highlight(words,doc.get("content"));
			String img = doc.get("img");
			String txt = doc.get("subtext");
			System.out.println((i + 1) + "." + title);
			System.out.println("-----------------------------------");
			System.out.println(content);
			System.out.println("-----------------------------------");
			System.out.println(url);
			System.out.println();
		}
	}

	private static String highlight(String key,String str){
		return str.replace(key, "<font color='red'>"+ key +"</font>");
	}
	
	
}
