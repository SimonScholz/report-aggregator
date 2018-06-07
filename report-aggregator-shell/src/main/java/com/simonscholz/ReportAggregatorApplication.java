package com.simonscholz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReportAggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportAggregatorApplication.class, args);
	}

//	@Bean
//	CommandLineRunner run(XsltService xsltService) {
//		return args -> {
//			ClassLoader classLoader = ReportAggregatorApplication.class.getClassLoader();
////			String xslFile = classLoader.getResource("spotbugs/fancy-hist.xsl").getFile();
//			String xslFile = classLoader.getResource("spotbugs/summary.xsl").getFile();
//
//			String xmlFile = "/home/simon/git/vogella/zak-code-analysis/ch.zakag.zak3.rcp.invoice/target/spotbugsXml.xml";
//
//			String output = "/home/simon/Desktop/report.html";
//
//			try {
//				xsltService.runXslt(xslFile, xmlFile, output);
//			} catch (SaxonApiException e) {
//				e.printStackTrace();
//			}
//		};
//	}
}
