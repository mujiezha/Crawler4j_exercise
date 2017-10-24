import edu.uci.ics.crawler4j.crawler.WebCrawler;
//package com.favccxx.favsoft.favcrawler;

import java.util.Set;
import java.util.regex.Pattern;
 
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.url.WebURL;


import java.io.FileWriter;
import java.io.IOException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;



//This class decides which URLs should be crawled,
//any filtering you may want to implement and handles the download pages
public class MyCrawler extends WebCrawler {
	private static final Logger logger = LoggerFactory.getLogger(MyCrawler.class);
	
	 private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|rss|js|bmp|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v" +
      "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	 CrawlStat myCrawlStat; // 定义爬虫状态对象，用户统计和分析
	 
     
	 
	  public MyCrawler()throws Exception {
	    myCrawlStat = new CrawlStat(); // 实例化爬虫状态对象
	  }
	  
	  @Override
	  protected void handlePageStatusCode(WebURL url, int statusCode, String statusDescription) {
		  	String weburl=url.getURL();
			int status=statusCode;
			 myCrawlStat.s1.append(weburl);
			 myCrawlStat.s1.append(',');
			 myCrawlStat.s1.append(status);
			 myCrawlStat.s1.append('\n');
			
		    //System.out.println("URL: " + url); ////the url we are crawlering. in the fetch.csv
		    //System.out.println(status);///the status we gonna get in fetch.csv
			logger.info("Visited: {}", url.getURL());
		    myCrawlStat.incProcessedPages(); // 处理页面加1
		    
		    if ((myCrawlStat.getTotalProcessedPages() % 50) == 0) {
	            dumpMyData(); /////50 dump once
	        }
	      
	  }
	  
	 @Override
	 public boolean shouldVisit(Page referringPage, WebURL url) {
		    String href = url.getURL().toLowerCase();
		    return !FILTERS.matcher(href).matches()
		           && href.startsWith("http://www.nydailynews.com/");}
		@Override
		public void visit(Page page)  {
			
			String url = page.getWebURL().getURL();
			int status=	page.getStatusCode();
			 /*
			 myCrawlStat.s1.append(url);
			 myCrawlStat.s1.append(',');
			 myCrawlStat.s1.append(status);
			 myCrawlStat.s1.append('\n');
			
		    //System.out.println("URL: " + url); ////the url we are crawlering. in the fetch.csv
		    //System.out.println(status);///the status we gonna get in fetch.csv
			logger.info("Visited: {}", page.getWebURL().getURL());
		    myCrawlStat.incProcessedPages(); // 处理页面加1
		    */
		    /////////////////////////////
		    
		    if(status>=200 && status<300)
		    {
		    	//System.out.println("URL: " + url); ////the url in succeeded fetched, in the visit_NewsSite.csv
		    	 myCrawlStat.s2.append(url);
		    	 myCrawlStat.s2.append(',');
		    	 
		    	 
		    	
			    	ParseData htmlParseData = page.getParseData();
			    		int length = page.getContentData().length;	
			    		//System.out.println("length: " +length);////the length for the page, in the visit_NewsSite.csv
			    		 myCrawlStat.s2.append(length);
			    		 myCrawlStat.s2.append(',');
			    	
			    
			        //String html = htmlParseData.getHtml();
			  
			        Set<WebURL> links = htmlParseData.getOutgoingUrls();// 获取输出链接
			        ///System.out.println("url find: " +links.size());///获取输出链接,visit_NewsSite.csv
			        if(links==null)
			        	 myCrawlStat.s2.append(0);
			        else
			        	myCrawlStat.s2.append(links.size());
			        myCrawlStat.s2.append(',');	
			        myCrawlStat.incTotalLinks(links.size()); // 总链接加link.size个
			        ////the three form
			        for (WebURL link : links)
			        {
			   
			        	 myCrawlStat.s3.append(link.getURL());/////System.out.println(link.getURL());
			        	 myCrawlStat.s3.append(',');
			        	String inside=link.getURL().toLowerCase();
			
			        	if(inside.startsWith("http://www.nydailynews.com/"))
			        		 myCrawlStat.s3.append(1);////System.out.println(1);
			        	else
			        		 myCrawlStat.s3.append(0);////System.out.println(0);
			        	 myCrawlStat.s3.append('\n');
			        	
			        }
			    }
		    
		    ///System.out.println(page.getContentType());///th constent type , in visit_NewsSite.csv
		    String []temp=page.getContentType().split(";");
		    myCrawlStat.s2.append(temp[0]);
		    myCrawlStat.s2.append('\n');
		    /*
		    if ((myCrawlStat.getTotalProcessedPages() % 50) == 0) {
	            dumpMyData(); /////50 dump once
	        }
		    */
		}
		
		@Override
	    public Object getMyLocalData() {
	        return myCrawlStat;
	    }
		
		@Override
	    public void onBeforeExit() {
	        dumpMyData();
	    }
		
		public void dumpMyData() {
	        int id = getMyId();
	        // You can configure the log to output to file
	        logger.info("Crawler {} > Processed Pages: {}", id, myCrawlStat.getTotalProcessedPages());
	        logger.info("Crawler {} > Total Links Found: {}", id, myCrawlStat.getTotalLinks());
	        logger.info("Crawler {} > Total Text Size: {}", id, myCrawlStat.getTotalTextSize());
	    }
	
}
