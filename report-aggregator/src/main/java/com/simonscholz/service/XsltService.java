package com.simonscholz.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.stream.StreamSource;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

@Service
public class XsltService {

	public void runXslt(String xslFile, File xmlFile, File outputFile) throws SaxonApiException, IOException {
		Processor proc = new Processor(false);
		XsltCompiler comp = proc.newXsltCompiler();

		InputStream xsl = null;
		if ("default".equals(xslFile)) {
			xsl = ResourceUtils.getURL("classpath:spotbugs/html-summary.xsl").openStream();
		} else {
			xsl = new FileInputStream(xslFile);
		}
		XsltExecutable exp = comp.compile(new StreamSource(xsl));
		XdmNode source = proc.newDocumentBuilder().build(new StreamSource(xmlFile));
		Serializer out = proc.newSerializer(outputFile);
		out.setOutputProperty(Serializer.Property.METHOD, "html");
		out.setOutputProperty(Serializer.Property.INDENT, "yes");
		XsltTransformer trans = exp.load();
		trans.setInitialContextNode(source);
		trans.setDestination(out);
		trans.transform();
	}
	
	public static void main(String[] args) {
		Processor proc = new Processor(false);
		XsltCompiler comp = proc.newXsltCompiler();
		
		ClassLoader classLoader = XsltService.class.getClassLoader();
		// load xsl file from java project
		InputStream xsl = classLoader.getResourceAsStream("transformation.xsl");
		// load xml file from java project
		InputStream xmlInput = classLoader.getResourceAsStream("input.xml");
		try {
			XsltExecutable exp = comp.compile(new StreamSource(xsl));
			
			XdmNode source = proc.newDocumentBuilder().build(new StreamSource(xmlInput));
			
			String outputDir = System.getProperty("user.home");
			// create a report.html file as result in the user home directory
			Serializer out = proc.newSerializer(new File(outputDir, "report.html"));
			out.setOutputProperty(Serializer.Property.METHOD, "html");
			out.setOutputProperty(Serializer.Property.INDENT, "yes");
			XsltTransformer trans = exp.load();
			trans.setInitialContextNode(source);
			trans.setDestination(out);
			trans.transform();
		} catch (SaxonApiException e) {
			e.printStackTrace();
		}
	}
}
