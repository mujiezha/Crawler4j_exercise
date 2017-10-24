//package com.favccxx.favsoft.favcrawler;
 
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;


public class Controller {
	private static final Logger logger =
	        LoggerFactory.getLogger(Controller.class);

	public static void main(String[] args) throws Exception  {
		// TODO Auto-generated method stub
		/////3 files
		PrintWriter p1 = new PrintWriter(new  FileWriter("fetch_ New_York_Daily_News.csv",true));
		PrintWriter p2 = new PrintWriter(new  FileWriter("visit_ New_York_Daily_News.csv",true));
		PrintWriter p3 = new PrintWriter(new  FileWriter("urls_ New_York_Daily_News.csv",true));
		///////
		String crawlStorageFolder = "data/crawl";
        int numberOfCrawlers = 7;
        ////
        int maxDepthOfCrawling=16;
        int maxPagesToFetch=20000;
       ////
        int politenessDelay=500;
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        
        //Instantiate the controller for this crawl.
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        robotstxtConfig.setEnabled(false);
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        //For each crawl, you need to add some seed urls. 
        controller.addSeed("http://www.nydailynews.com/");///http://www.nydailynews.com/
        ////add depth
        config.setMaxDepthOfCrawling(maxDepthOfCrawling);
        ////add maximum
        config.setMaxPagesToFetch(maxPagesToFetch);
        ////set polite
        config.setPolitenessDelay(politenessDelay);
        //Start the crawl
        controller.start(MyCrawler.class, numberOfCrawlers);
        
        System.out.println("Aggregated Statistics:");
        ///write the data
        final List<Object> crawlersLocalData = controller.getCrawlersLocalData();
        long totalLinks = 0;
        long totalTextSize = 0;
        int totalProcessedPages = 0;
        for (Object localData : crawlersLocalData) {
            CrawlStat stat = (CrawlStat) localData;
            totalLinks += stat.getTotalLinks();
            totalTextSize += stat.getTotalTextSize();
            totalProcessedPages += stat.getTotalProcessedPages();
            p1.write(stat.s1.toString());
            p2.write(stat.s2.toString());
            p3.write(stat.s3.toString());
        }

        System.out.println("Aggregated Statistics:");
        System.out.println("\tProcessed Pages: {}" + totalProcessedPages);
        System.out.println("\tTotal Links found: {}" + totalLinks);
        System.out.println("\tTotal Text Size: {}" + totalTextSize);
        
        p1.close();
        p2.close();
        p3.close();
        logger.info("Aggregated Statistics:");
        logger.info("\tProcessed Pages: {}", totalProcessedPages);
        logger.info("\tTotal Links found: {}", totalLinks);
        logger.info("\tTotal Text Size: {}", totalTextSize);
	}

}
