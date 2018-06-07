package com.simonscholz.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import com.simonscholz.report.service.SpotBugsService;

import net.sf.saxon.s9api.SaxonApiException;

public class MergeSpotBugsTask extends DefaultTask {

	private SpotBugsService spotBugsService;

	private File rootDir;

	private File outputFile;

	private int level;

	public MergeSpotBugsTask() {
		spotBugsService = new SpotBugsService();
	}

	@TaskAction
	public void mergeSpotBugsFiles() throws FileNotFoundException, IOException, SaxonApiException {
		spotBugsService.mergeSpotBugsFiles(rootDir, level, outputFile);
		getProject().getLogger().info("Wrote merged SpotBugs xml files to " + outputFile);
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

	@Override
	public String toString() {
		return "MergeSpotBugsTask [rootDir=" + rootDir + ", outputFile=" + outputFile + ", level=" + level + "]";
	}
}
