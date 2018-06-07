package com.simonscholz.report.aggregator.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

public class SpotBugsService {

	private static final Logger LOG = LoggerFactory.getLogger(SpotBugsService.class);

	public List<File> getSpotBugsFiles(File rootDir, int level) {
		List<File> list = new ArrayList<>(50);
		findDirectories(rootDir, list, level);

		List<File> spotBugsFiles = list.stream().map(f -> new File(f, "target/spotbugsXml.xml")).filter(File::exists)
				.collect(Collectors.toList());

		return spotBugsFiles;
	}

	public void mergeSpotBugsFiles(File rootDir, int level, File outputDir)
			throws FileNotFoundException, IOException, SaxonApiException {
		List<File> spotBugsFiles = getSpotBugsFiles(rootDir, level);

		Processor proc = new Processor(false);
		XsltCompiler comp = proc.newXsltCompiler();

		InputStream xsl = getClass().getClassLoader().getResourceAsStream("spotbugs/merge.xsl");
		XsltExecutable exp = comp.compile(new StreamSource(xsl));
		Serializer out = proc.newSerializer(new File(outputDir, "SpotBugsMerged.xml"));
		out.setOutputProperty(Serializer.Property.METHOD, "xml");
		out.setOutputProperty(Serializer.Property.INDENT, "yes");
		XsltTransformer trans = exp.load();

		List<XdmNode> sources = createXdmNodes(spotBugsFiles, proc.newDocumentBuilder());
		QName xmlFiles = new QName("xmlFiles");
		XdmValue filesXdm = XdmValue.makeSequence(sources);
		trans.setParameter(xmlFiles, filesXdm);
		trans.setInitialTemplate(new QName("test"));
		trans.setDestination(out);
		trans.transform();
	}

	public void generateMergedReport(File rootDir, int level, File outputDir)
			throws FileNotFoundException, IOException, SaxonApiException {
		File spotBugsMergedXmlFile = new File(outputDir, "SpotBugsMerged.xml");

		// only generate SpotBugsMerged.xml file, if it does not already exist
		if (spotBugsMergedXmlFile.exists()) {
			mergeSpotBugsFiles(rootDir, level, outputDir);
		}

		generateMergedHtmlReport(spotBugsMergedXmlFile, outputDir);
	}

	private void generateMergedHtmlReport(File xmlFile, File outputDir) throws SaxonApiException {
		Processor proc = new Processor(false);
		XsltCompiler comp = proc.newXsltCompiler();

		InputStream xsl = getClass().getClassLoader().getResourceAsStream("spotbugs/html-summary.xsl");
		XsltExecutable exp = comp.compile(new StreamSource(xsl));
		XdmNode source = proc.newDocumentBuilder().build(new StreamSource(xmlFile));
		Serializer out = proc.newSerializer(new File(outputDir, "SpotBugsMerged.html"));
		out.setOutputProperty(Serializer.Property.METHOD, "html");
		out.setOutputProperty(Serializer.Property.INDENT, "yes");
		XsltTransformer trans = exp.load();
		trans.setInitialContextNode(source);
		trans.setDestination(out);
		trans.transform();
	}

	private void findDirectories(File rootDir, List<File> list, int level) {
		File[] directories = rootDir.listFiles(File::isDirectory);
		list.addAll(Arrays.asList(directories));
		if (level > 0) {
			for (File dir : directories) {
				findDirectories(dir, list, --level);
			}
		}
	}

	private List<XdmNode> createXdmNodes(List<File> spotBugsFiles, DocumentBuilder documentBuilder) {
		return spotBugsFiles.stream().map(t -> {
			try {
				return documentBuilder.build(t);
			} catch (SaxonApiException e) {
				LOG.error(e.getMessage(), e);
			}
			return null;
		}).collect(Collectors.toList());
	}
}
