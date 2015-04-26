This file was created as Microsoft(tm) Project(r) data, and saved as XML.

Here is how to view the file in the Cylindrical BLAST Viewer, with proper classpath setup.

	java -Xmx256m self.lesfoster.cylindrical_viewer.viewer.CylindricalBlastViewer samples\ms_project\ProjectExercise.Project.xml 0

Or, using the distributed jar file:

	java -jar dist\cylindrical_viewer-V.R-rlse.jar samples\ms_project\ProjectExercise.Project.xml 0

(Here, V.R is the version and revision numbers of the release).
Please remember, that in all cases, you need to first install Java3d 1.3.1 prior to attempting to run any sample.