package com.vogella.gradleplugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class AnalysisPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		MergeSpotBugsTask mergeSpotBugsTask = project.getTasks().create("mergeSpotBugsFiles", MergeSpotBugsTask.class, t -> {
			t.setRootDir(project.getRootDir());
			t.setOutputFile(project.getRootDir());
			t.setLevel(2);
		});
		mergeSpotBugsTask.setGroup("SpotBugs");
		mergeSpotBugsTask.setDescription("Merges all SpotBugs xml files into one SpotBugsMerged.xml file");
		
		GenerateMergedReportTask generateMergedReportTask = project.getTasks().create("generateMergedReport", GenerateMergedReportTask.class, t -> {
			t.setRootDir(project.getRootDir());
			t.setOutputFile(project.getRootDir());
			t.setLevel(2);
		});
		generateMergedReportTask.setGroup("SpotBugs");
		generateMergedReportTask.setDescription("Generate HTML report from merged SpotBugs xml files");
	}
}
