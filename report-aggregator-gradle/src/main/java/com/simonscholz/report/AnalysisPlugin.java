package com.simonscholz.report;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class AnalysisPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		AggregateSpotBugsTask aggregateSpotBugsFilesTask = project.getTasks().create("aggregateSpotBugsFiles", AggregateSpotBugsTask.class, t -> {
			t.setRootDir(project.getRootDir());
			t.setOutputFile(project.getRootDir());
			t.setLevel(2);
		});
		aggregateSpotBugsFilesTask.setGroup("SpotBugs");
		aggregateSpotBugsFilesTask.setDescription("Aggregates all SpotBugs xml files into one SpotBugsAggregated.xml file");
		
		GenerateAggregatedReportTask generateAggregatedReportTask = project.getTasks().create("generateAggregatedReport", GenerateAggregatedReportTask.class, t -> {
			t.setRootDir(project.getRootDir());
			t.setOutputFile(project.getRootDir());
			t.setLevel(2);
		});
		generateAggregatedReportTask.setGroup("SpotBugs");
		generateAggregatedReportTask.setDescription("Generate HTML report from Aggregated SpotBugs xml files");
	}
}
