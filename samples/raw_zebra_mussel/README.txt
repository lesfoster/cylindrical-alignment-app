This file was created using the sequence at NCBI, after which the file was named.  The output was converted using XSLT, to the
Cylinder XML format.  The sequence was BLASTP'ed from the http://www.ncbi.nih.gov/blast page, with an output setting of XML at "format"
time.

	java CylindricalBlastViewer samples\zebra_mussel_byssal\AF265353_1.BlastOutput.xml 1

or, from the executable jar in dist, first change to the Cylindrical_Blast_Viewer directory, and:

	java -jar dist\cylindrical_viewer-V.R-rlse.jar samples\zebra_mussel_byssal\AF265353_1.BlastOutput.xml 1

(Here, V.R is the version number and the revision number of the release.  For example, 1.1).
Please remember, that in all cases, you need to first install Java3d 1.3.1 prior to attempting to run any sample.