package com.simonscholz;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.shell.standard.FileValueProvider;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.simonscholz.service.ConsoleService;
import com.simonscholz.service.SpotBugsService;
import com.simonscholz.service.XsltService;

import net.sf.saxon.s9api.SaxonApiException;

@ShellComponent
public class ReportShellComponent {

	private XsltService xsltService;
	private ConsoleService consoleService;
	private SpotBugsService spotBugsService;

	public ReportShellComponent(XsltService xsltService, ConsoleService consoleService,
			SpotBugsService spotBugsService) {
		this.xsltService = xsltService;
		this.consoleService = consoleService;
		this.spotBugsService = spotBugsService;
	}

	@ShellMethod("Transform xml to html")
	public void transform(@ShellOption(defaultValue = "default") String xslFile,
			@ShellOption(valueProvider = FileValueProvider.class) File xmlFile,
			@ShellOption(valueProvider = FileValueProvider.class) File output) throws SaxonApiException, IOException {
		xsltService.runXslt(xslFile, xmlFile, output);
		consoleService.write("Wrote report to %s", output);
	}

	@ShellMethod("Merge SpotBugs files")
	public void mergeSpotBugs(@ShellOption(valueProvider = FileValueProvider.class) File rootDir,
			@ShellOption(defaultValue = "2") int level,
			@ShellOption(valueProvider = FileValueProvider.class) File outputDir)
			throws SaxonApiException, IOException {
		spotBugsService.mergeSpotBugsFiles(rootDir, level, outputDir);
		consoleService.write("Wrote merged xml to %s", outputDir);
	}

	@ShellMethod("List SpotBugs files")
	public void listSpotBugsFiles(@ShellOption(valueProvider = FileValueProvider.class) File rootDir,
			@ShellOption(defaultValue = "2") int level) throws SaxonApiException, IOException {
		List<File> spotBugsFiles = spotBugsService.getSpotBugsFiles(rootDir, level);

		spotBugsFiles.forEach(f -> consoleService.write(f.toString()));

		consoleService.write("Found %s SpotBugs files.", spotBugsFiles.size());
	}
}
