---
title: 'Cylindrical Alignment App: A Java Application for Aligning Biomolecules
       in 3-D'
tags:
  - Java
  - bioinformatics
  - 3D
authors:
  - name: Leslie Foster^[author]
    orcid: 0000-0002-8909-3711
    affiliation: 1
affiliations:
 - name: Independent Researcher, USA
   index: 1
date: 04 June 2022

# Summary

Cylindrical Alignment App is a desktop application for examining Biomolecular
alignments such as would arise from running BLAST (Basic Local Alignment Search
Tool) written in the Java programming language.  It allows the user to open
variously formatted files of alignments to a common axis.  It presents the
alignments visually in 3-D as bar shapes arranged in a cylinder, such that
alignments are arranged along the "l" or long axis of the cylinder.  The result
is a barrel shape, which is animated by spinning around that axis.  The "axis of
alignment" (not to be confused with the axis of the cylinder) is depicted as a
separate, stationary shape in front of the cylinder and low enough not to
obstruct the view.  Above the spinning cylinder is a scale (ruler-like, with
lines telling unit counts from beginning to end of the axis of alignment.  The
cylinder is not solid, but rather has its alignments free-floating in space.

The shapes making up the cylinder outline start and end with respect to the axis
of alignment, and can be selected with mouse clicks.  When selected, properties
of those alignments may be seen in a subview.  An additional subview shows a
"legend" of color-vs-name.  Coloring is based on characteristics such as score
were applicable, or type where applicable.  In addition, the entire view may be
moved around with mouse drag gestures, and the spin animation may be stopped,
started, slowed or sped.

Since alignments may include deletions and insertions with respect to their axis
of alignment, those can be seen in alignments as either depressions in the
surface of the shape or additional floating shapes, respectively.  Additionally,
substitutions can be seen as smaller colored shapes alongside the overall
alignment shape.

The Cylindrical Alignment App is the successor of a previous viewer also written
by the author called the Cylindrical BLAST Viewer.

# Statement of Need

Using this code to present alignments allows (up to a certain limit) more
alignments to be seen on the screen at one time than viewing them flat normally
does.  Having the alignments spinning allows users to see them flow past the eye
so that variations among the alignments may be seen easily.  Selecting shapes
allows some level of interaction, and since properties may include web links,
drilling down still further into interesting alignments is also possible, as are
indels and substitutions.

# Related

https://en.wikipedia.org/wiki/List_of_alignment_visualization_software this
Wikipedia page has several examples of 
alignment software available at time of writing.

# Past Users

This project has been available on Source Forge at 
https://sourceforge.net/projects/cylindrical-alignment-app/ for the past three 
years and has been downloaded over 200 times.  Its predecessor was included in 
a larger project at Universitat Sans Malaysia in 2010.

# Acknowledgements

No financial support was given for this project.  Its predecessor's
functionality was reviewed by a group of three reviewers at Applied Biosystems
(now part of Thermo Fisher Scientific).  The predecessor "Cylindrical BLAST
Viewer" was created as a Master's degree project at Johns Hopkins
University, with supervision by Dr. Russell Turner.

# References