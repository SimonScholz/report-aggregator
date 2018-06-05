package com.simonscholz.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

	public List<File> getSpotBugsFiles(File rootDir) {

		File[] directories = rootDir.listFiles(File::isDirectory);
		List<File> spotBugsFiles = Arrays.asList(directories).stream().map(f -> {
			return new File(f, "target/spotbugsXml.xml");
		}).filter(File::exists).collect(Collectors.toList());

		return spotBugsFiles;
	}
	
	public void mergeSpotBugsFiles(File rootDir, File outputDir) throws FileNotFoundException, IOException, SaxonApiException {
		List<File> spotBugsFiles = getSpotBugsFiles(rootDir);
		
		Processor proc = new Processor(false);
		XsltCompiler comp = proc.newXsltCompiler();

		InputStream	xsl = ResourceUtils.getURL("classpath:spotbugs/merge2.xsl").openStream();
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
