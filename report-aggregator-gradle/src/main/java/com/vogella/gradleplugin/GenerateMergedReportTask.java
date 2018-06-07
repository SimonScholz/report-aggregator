package com.vogella.gradleplugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import com.vogella.gradleplugin.service.SpotBugsService;

import net.sf.saxon.s9api.SaxonApiException;

public class GenerateMergedReportTask extends DefaultTask {
	private SpotBugsService spotBugsService;

	private File rootDir;

	private File outputFile;

	private int level;

	public GenerateMergedReportTask() {
		spotBugsService = new SpotBugsService();
	}

	@TaskAction
	public void mergeSpotBugsFiles() throws FileNotFoundException, IOException, SaxonApiException {
		spotBugsService.generateMergedReport(rootDir, level, outputFile);
		getProject().getLogger().info("Generated merged SpotBugs html report: " + outputFile);
	}

	public File getRootDir() {
		return rootDir;
	}

	public void setRootDir(File rootDir) {
		this.rootDir = rootDir;
	}

	public File getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
