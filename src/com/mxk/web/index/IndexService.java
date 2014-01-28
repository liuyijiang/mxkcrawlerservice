package com.mxk.web.index;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.mxk.dao.WebResourceMapper;
import com.mxk.model.WebResource;
import com.mxk.model.WebResourceCriteria;
import com.mxk.web.resource.WebResourceMapperPlus;
import com.mxk.web.search.PageModel;
import com.mxk.web.search.SearchRespone;
/**
 * 全文检索服务 创建索引
 * 
 * @author Administrator
 * 
 */
@Service
public class IndexService {
	
	@Value("${index.create}")
	private boolean execute;
	
	@Value("${index.path}")
	private String path;
	
	@Value("${index.pagesize}")
	private int pageSize;
	
	public static final Logger logger = LoggerFactory
			.getLogger(IndexService.class);

	@Resource
	private WebResourceMapper webResourceMapper;
	
	@Resource
	private WebResourceMapperPlus webResourceMapperPlus;
	
	private IndexSearcher searcher;
	/** IKAnalyzer中文分词 */
	private Analyzer analyzer = new IKAnalyzer();
	/**
	 * 系统启动的时候创建索引
	 * @throws Exception
	 */
	@PostConstruct
	public void execute() throws Exception {
	   if(execute){	
		   new Thread(new Runnable() {
				@Override
				public void run() {
					try{
					   createIndex();
					}catch(Exception e){
						logger.error("创建索引异常{},索引创建失败",e);
					}
				}
			}).start();
	    }
	}	
		
	/**
	 * 创建索引	
	 */
	public void createIndex() throws Exception{
		WebResourceCriteria criteria = new WebResourceCriteria();
		criteria.createCriteria().andIdGreaterThan(0);
//		List<WebResource> list = webResourceMapper.selectByExample(criteria);
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
		indexDataLoad(0,writer);
	}
	
	private void indexDataLoad(int startid,IndexWriter writer) throws Exception {
		List<WebResource> list = webResourceMapperPlus.selectlimit(startid);
		if(list.size() != 0){
			int lastid=0;
			for (WebResource res:list) {
				lastid = res.getId();
				String id = String.valueOf(lastid);
				String url = res.getUrl();
				String title = res.getTitle();
				String content = res.getTitle()+" | "+ res.getInfo();
				String img = res.getImage();
				String txt = "作者："+res.getOwnername() + " " + res.getMultiinfo();
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
						Field.Index.NOT_ANALYZED));
				
				doc.add(new Field("id", id, Field.Store.YES,
						Field.Index.NOT_ANALYZED));
				// 写入文档
				writer.addDocument(doc);
			}
			indexDataLoad(lastid,writer);
		}else{
			writer.close();
			logger.info("索引创建完成");
		}
	}
	
	/**
	 * 索引查询
	 * @param words
	 * @param field
	 * @return
	 */
	public PageModel searchIndex(String words, String field, int currentPage){
		PageModel model = new PageModel();
		long total = 0;//总页数
		try{
			IndexSearcher searcher = createIndexSearcher();
			// 创建查询解析对象
			QueryParser parser = new QueryParser(Version.LUCENE_36, field, analyzer);
			parser.setDefaultOperator(QueryParser.OR_OPERATOR);//设置表达式 与 或
			// 根据域和目标搜索文本创建查询器
			Query query = parser.parse(words);
			logger.debug("Searching for: " + query.toString(field));
			TopDocs topDocs = searcher.search(query, searcher.maxDoc());
			//高亮
			SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
			Highlighter highlighter = new Highlighter(simpleHTMLFormatter,new QueryScorer(query));   
	        highlighter.setTextFragmenter(new SimpleFragmenter(300));
	        // 获取结果
	        ScoreDoc[] hits = topDocs.scoreDocs;
	        total = topDocs.totalHits;//总记录
	        int start= (currentPage - 1) * pageSize;  
	        int end= start + pageSize -1;  
	        List<SearchRespone> list = getSearchRespone(hits,searcher,highlighter,start,end);
	        model.setTotal(total);
	        model.setCurrentPage(currentPage);
	        model.setPage( (total + pageSize - 1) / pageSize);
	        model.setData(list);
		}catch(Exception e){
			logger.error("获取搜索Top失败{},keyword+filed:{}",e,words+"+"+field);
		}
		return model;
	}
	
	private List<SearchRespone> getSearchRespone(ScoreDoc[] hits,IndexSearcher searcher,Highlighter highlighter,int start,int end){
		List<SearchRespone> list = new ArrayList<SearchRespone>();
		for (int i = start; i <= end; i++) {
			try{
				Document doc = searcher.doc(hits[i].doc);
				SearchRespone sr = new SearchRespone();
				sr.setImg(doc.get("img"));
				String title = doc.get("title");
				String info = doc.get("content");
				if(title != null){
					TokenStream tokenStreamTitle = analyzer.tokenStream("title",new StringReader(title));  
					String highLightTitle = highlighter.getBestFragment(tokenStreamTitle, title);
					if(highLightTitle == null){
						highLightTitle = title;
					}
					sr.setTitle(highLightTitle);
				}
				if(info != null){
					TokenStream tokenStreamInfo = analyzer.tokenStream("content",new StringReader(info));  
					String highLightInfo = highlighter.getBestFragment(tokenStreamInfo, info);
					if(highLightInfo == null){
						highLightInfo = title;
					}
					sr.setInfo(highLightInfo);
				}
				sr.setSubtext(doc.get("subtext"));
				sr.setUrl(doc.get("url"));
				sr.setId(doc.get("id"));
				list.add(sr);
			}catch(Exception e){
				logger.error("获取搜索数据异常{}",e);
			}
		}
		return list;
	}
	
	/**
	 * 创建
	 * @return
	 * @throws Exception
	 */
	private IndexSearcher createIndexSearcher() throws Exception{
		if(searcher == null){
			File indexDir = new File("D:\\index");
			// 索引目录
			Directory dir = FSDirectory.open(indexDir);
			// 根据索引目录创建读索引对象
			IndexReader reader = IndexReader.open(dir); //使用一个
			// 搜索对象创建
			searcher = new IndexSearcher(reader);
		}
		return searcher;
	}
	
}
