<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<!-- XSLT: to construct anchor cylinder xml. -->

<xsl:output indent="no" method="xml" omit-xml-declaration="yes"/>
<xsl:strip-space elements="*"/>

<xsl:template match="/BlastOutput">
<xsl:variable name="anchor_name" select="BlastOutput_query-def" />
<xsl:variable name="anchor_length" select="BlastOutput_query-len" />
<xsl:variable name="prog" select="BlastOutput_program" />
<!-- seqid source type start end score strand phase attributes tags -->
    <!-- Add an entity for the anchor itself. -->
    <xsl:text><xsl:value-of select="$anchor_name" /></xsl:text>
    <xsl:text>&#x9;</xsl:text><xsl:text>BLAST Run</xsl:text>
    <xsl:text>&#x9;</xsl:text><xsl:text><xsl:value-of select="$prog" /></xsl:text>
    <xsl:text>&#x9;</xsl:text><xsl:text>1</xsl:text>
    <xsl:text>&#x9;</xsl:text><xsl:text><xsl:value-of select="$anchor_length" /></xsl:text>
    <xsl:text>&#x9;</xsl:text><xsl:text>.</xsl:text>
    <xsl:text>&#x9;</xsl:text><xsl:text>.</xsl:text>
    <xsl:text>&#x9;</xsl:text><xsl:text>.</xsl:text>
    <xsl:text>&#x9;</xsl:text><xsl:text>ID=<xsl:value-of select="$anchor_name" /></xsl:text>
    <xsl:text>&#xa;</xsl:text>

  <xsl:apply-templates select="BlastOutput_iterations" />
</xsl:template>

<xsl:template match="BlastOutput/BlastOutput_iterations">
  <xsl:apply-templates select="Iteration" />
</xsl:template>

<xsl:template match="BlastOutput_iterations/Iteration">
  <xsl:apply-templates select="Iteration_hits" />
</xsl:template>

<xsl:template match="Iteration/Iteration_hits">
  <xsl:apply-templates select="Hit" />
</xsl:template>

<xsl:template match="Iteration_hits/Hit">
    <xsl:apply-templates select="Hit_hsps" />
</xsl:template>

<xsl:template match="Hit/Hit_hsps">
  <xsl:apply-templates select="Hsp" />
</xsl:template>

<xsl:template match="Hit_hsps/Hsp">
  <!-- Making an entity and sub entity out of every HSP, to avoid having to deal with overlapping HSPs, and
       because HSPs can be thought of as competing alignments to each other, between this subject and the query -->
  <xsl:variable name="anchor_name" select="../../../../../../BlastOutput_query-def" />
  <xsl:variable name="accession" select="../../Hit_accession"/>
  <xsl:variable name="defline_info" select="../../Hit_id"/>
  <xsl:variable name="prog" select="../../../../../../BlastOutput_program" />
  <xsl:variable name="qstart" select="Hsp_query-from" />
  <xsl:variable name="qend" select="Hsp_query-to" />
  <xsl:variable name="sstart" select="Hsp_hit-from" />
  <xsl:variable name="send" select="Hsp_hit-to" />
  <xsl:variable name="score" select="Hsp_score" />
  <xsl:variable name="evalue" select="Hsp_evalue" />
  <xsl:variable name="bitscore" select="Hsp_bit-score" />
  <xsl:variable name="query_align" select="Hsp_qseq" />
  <xsl:variable name="subject_align" select="Hsp_hseq" />
  <xsl:variable name="midline" select="Hsp_midline" />
    <xsl:if test="$prog='blastp'" >
    <!-- TODO call function -->
        <xsl:text><xsl:value-of select="$anchor_name" /></xsl:text>
        <xsl:text>&#x9;</xsl:text><xsl:text>NCBI BLASTP</xsl:text>
        <xsl:text>&#x9;</xsl:text><xsl:text><xsl:value-of select="$prog" /></xsl:text>
        <xsl:text>&#x9;</xsl:text><xsl:text><xsl:value-of select="$qstart" /></xsl:text>
        <xsl:text>&#x9;</xsl:text><xsl:text><xsl:value-of select="$qend" /></xsl:text>
        <xsl:text>&#x9;</xsl:text><xsl:text><xsl:value-of select="$score" /></xsl:text>
        <xsl:text>&#x9;</xsl:text><xsl:text>.</xsl:text><!-- neutral strand -->
        <xsl:text>&#x9;</xsl:text><xsl:text>.</xsl:text>
        <xsl:text>&#x9;</xsl:text>ID=<xsl:value-of select="$accession" />_<xsl:value-of select="$sstart" />_<xsl:value-of select="$send" />;sstart=<xsl:value-of select="$sstart" />;send=<xsl:value-of select="$send" />;evalue=<xsl:value-of select="$evalue" />;bitscore=<xsl:value-of select="$bitscore" />;query_align=<xsl:value-of select="$query_align" />;subject_align=<xsl:value-of select="$subject_align" />;order by=<xsl:value-of select="$bitscore" />;order by tiebreaker=<xsl:value-of select="$accession" />;accession=<xsl:value-of select="$accession" />;midline=<xsl:value-of select="$midline" />
    </xsl:if>
    <xsl:if test="$prog='blastn'" >
    <!-- Strand is positive if sstart greater than send -->
    <xsl:if test="$qend &gt; $qstart" >
        <xsl:text><xsl:value-of select="$anchor_name" /></xsl:text>
        <xsl:text>&#x9;</xsl:text><xsl:text>NCBI BLASTN</xsl:text>
        <xsl:text>&#x9;</xsl:text><xsl:text><xsl:value-of select="$prog" /></xsl:text>
        <xsl:text>&#x9;</xsl:text><xsl:text><xsl:value-of select="$qstart" /></xsl:text>
        <xsl:text>&#x9;</xsl:text><xsl:text><xsl:value-of select="$qend" /></xsl:text>
        <xsl:text>&#x9;</xsl:text><xsl:text><xsl:value-of select="$score" /></xsl:text>
        <xsl:text>&#x9;</xsl:text><xsl:text>&#43;</xsl:text> <!-- plus strand -->
        <xsl:text>&#x9;</xsl:text><xsl:text>.</xsl:text>
        <xsl:text>&#x9;</xsl:text>ID=<xsl:value-of select="$accession" />_<xsl:value-of select="$sstart" />_<xsl:value-of select="$send" />;sstart=<xsl:value-of select="$sstart" />;send=<xsl:value-of select="$send" />;evalue=<xsl:value-of select="$evalue" />;bitscore=<xsl:value-of select="$bitscore" />;query_align=<xsl:value-of select="$query_align" />;subject_align=<xsl:value-of select="$subject_align" />;order by=<xsl:value-of select="$bitscore" />;order by tiebreaker=<xsl:value-of select="$accession" />;accession=<xsl:value-of select="$accession" />;midline=<xsl:value-of select="$midline" />
    </xsl:if>
    <xsl:if test="$qend &lt; $qstart" >
        <xsl:text><xsl:value-of select="$anchor_name" /></xsl:text>
        <xsl:text>&#x9;</xsl:text><xsl:text>NCBI BLASTN</xsl:text>
        <xsl:text>&#x9;</xsl:text><xsl:text><xsl:value-of select="$prog" /></xsl:text>
        <xsl:text>&#x9;</xsl:text><xsl:text><xsl:value-of select="$qend" /></xsl:text>
        <xsl:text>&#x9;</xsl:text><xsl:text><xsl:value-of select="$qstart" /></xsl:text>
        <xsl:text>&#x9;</xsl:text><xsl:text><xsl:value-of select="$score" /></xsl:text>
        <xsl:text>&#x9;</xsl:text><xsl:text>&#45;</xsl:text><!-- minus Strand -->
        <xsl:text>&#x9;</xsl:text><xsl:text>.</xsl:text>
        <xsl:text>&#x9;</xsl:text>ID=<xsl:value-of select="$accession" />_<xsl:value-of select="$sstart" />_<xsl:value-of select="$send" />;sstart=<xsl:value-of select="$sstart" />;send=<xsl:value-of select="$send" />;evalue=<xsl:value-of select="$evalue" />;bitscore=<xsl:value-of select="$bitscore" />;query_align=<xsl:value-of select="$query_align" />;subject_align=<xsl:value-of select="$subject_align" />;order by=<xsl:value-of select="$bitscore" />;order by tiebreaker=<xsl:value-of select="$accession" />;accession=<xsl:value-of select="$accession" />;midline=<xsl:value-of select="$midline" />
    </xsl:if>
    </xsl:if>
    <xsl:text>&#xa;</xsl:text>
</xsl:template>

</xsl:stylesheet>