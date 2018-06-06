package com.simonscholz.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.transform.stream.StreamSource;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

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

@Service
public class SpotBugsService {

	private XsltService xsltService;

	public SpotBugsService(XsltService xsltService) {
		this.xsltService = xsltService;
	}

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

		InputStream xsl = ResourceUtils.getURL("classpath:spotbugs/merge.xsl").openStream();
		XsltExecutable exp = comp.compile(new StreamSource(xsl));
		Serializer out = proc.newSerializer(new File(outputDir, "merge.xml"));
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

	public void generateOriginalAll(File xmlFile, File outputDir) {
		InputStream xsl;
		try {
			xsl = ResourceUtils.getURL("classpath:spotbugs/orig/color.xsl").openStream();
			xsltService.runXslt(xsl, xmlFile, new File(outputDir, "color.html"));
		} catch (IOException | SaxonApiException e) {
			e.printStackTrace();
		}

		try {
			xsl = ResourceUtils.getURL("classpath:spotbugs/orig/default.xsl").openStream();
			xsltService.runXslt(xsl, xmlFile, new File(outputDir, "default.html"));
		} catch (IOException | SaxonApiException e) {
			e.printStackTrace();
		}

		try {
			xsl = ResourceUtils.getURL("classpath:spotbugs/orig/fancy-hist.xsl").openStream();
			xsltService.runXslt(xsl, xmlFile, new File(outputDir, "fancy-hist.html"));
		} catch (IOException | SaxonApiException e) {
			e.printStackTrace();
		}

		try {
			xsl = ResourceUtils.getURL("classpath:spotbugs/orig/fancy.xsl").openStream();
			xsltService.runXslt(xsl, xmlFile, new File(outputDir, "fancy.html"));
		} catch (IOException | SaxonApiException e) {
			e.printStackTrace();
		}

		try {
			xsl = ResourceUtils.getURL("classpath:spotbugs/orig/plain.xsl").openStream();
			xsltService.runXslt(xsl, xmlFile, new File(outputDir, "plain.html"));
		} catch (IOException | SaxonApiException e) {
			e.printStackTrace();
		}

		try {
			xsl = ResourceUtils.getURL("classpath:spotbugs/orig/summary.xsl").openStream();
			xsltService.runXslt(xsl, xmlFile, new File(outputDir, "summary.html"));
		} catch (IOException | SaxonApiException e) {
			e.printStackTrace();
		}
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
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());
	}
}
