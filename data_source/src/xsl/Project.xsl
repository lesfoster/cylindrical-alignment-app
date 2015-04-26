<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:project="http://schemas.microsoft.com/project">

<xsl:output indent="no" method="xml" omit-xml-declaration="no"/>
<xsl:strip-space elements="*"/>

<xsl:template match="/project:Project">
<xsl:variable name="anchor_name" select="project:Name" />
<xsl:variable name="raw_start" select="translate(substring(project:StartDate, 1, 10), '-', '/')" />
<xsl:variable name="raw_end" select="translate(substring(project:FinishDate, 1, 10), '-', '/')" />

<!-- Note: there is no real anchor_start or anchor end.  anchor_start will be preserved, and used as an offset to
     be subtracted from all positional attributes. -->
<cylinder version="1.0" anchor_start="{$raw_start}" anchor_end="{$raw_end}" anchor_name="{$anchor_name}" order="ascending"><xsl:text>&#xa;</xsl:text>
  <!-- Add an entity for the anchor itself. -->
  <entity><xsl:text>&#xa;</xsl:text>
    <subentity qstart="{$raw_start}" qend="{$raw_end}" sstart="{$raw_start}" send="{$raw_end}" strand="0"><xsl:text>&#xa;</xsl:text>
      <p value="Anchor" name="subEntityType"/><xsl:text>&#xa;</xsl:text>
      <p name="name" value="{$anchor_name}"/><xsl:text>&#xa;</xsl:text>
    </subentity><xsl:text>&#xa;</xsl:text>
  </entity><xsl:text>&#xa;</xsl:text>
  <xsl:apply-templates select="project:Tasks" />
</cylinder><xsl:text>&#xa;</xsl:text>
</xsl:template>

<xsl:template match="project:Project/project:Tasks">
  <xsl:apply-templates select="project:Task" />
</xsl:template>

<xsl:template match="project:Tasks/project:Task">
  <xsl:variable name="raw_start" select="translate(substring(ancestor::project:Project/project:StartDate, 1, 10), '-', '')" />
  <entity><xsl:text>&#xa;</xsl:text>
  <xsl:variable name="accession" select="project:Name"/>
  <xsl:variable name="qstart" select="translate(substring(project:EarlyStart, 1, 10), '-', '/')" />
  <xsl:variable name="qend" select="translate(substring(project:EarlyFinish, 1, 10), '-', '/')" />
  <xsl:variable name="start" select="project:Start" />
  <xsl:variable name="end" select="project:Finish" />
  <xsl:variable name="early_start" select="project:EarlyStart" />
  <xsl:variable name="early_end" select="project:EarlyFinish" />
  <xsl:variable name="late_start" select="project:LateStart" />
  <xsl:variable name="late_end" select="project:LateFinish" />
  <xsl:variable name="create_date" select="project:CreateDate" />
  <xsl:variable name="name" select="project:Name" />
  <xsl:variable name="notes" select="project:Notes" />
  <xsl:variable name="type" select="project:Type" />
  <xsl:variable name="priority" select="project:Priority" />
  <xsl:variable name="final_cost_accrual" select="project:FinalCostAccrual" />
  <xsl:variable name="critical" select="project:Critical" />
  <xsl:variable name="work_variance" select="project:WorkVariance" />
  <xsl:variable name="summary" select="project:Summary" />
  <xsl:variable name="work" select="project:Work" />
  <xsl:variable name="sstart" select="translate(substring(project:LateStart, 1, 10), '-', '/')" />
  <xsl:variable name="send" select="translate(substring(project:LateFinish, 1, 10), '-', '/')" />
    <subentity qstart="{$qstart}" qend="{$qend}" sstart="{$sstart}" send="{$send}" strand="0"><xsl:text>&#xa;</xsl:text>
      <p name="subEntityType" value="Task+[score]" /><xsl:text>&#xa;</xsl:text>
      <p name="task type" value="{$type}" /><xsl:text>&#xa;</xsl:text>
      <p name="order by" value="{$sstart}" /><xsl:text>&#xa;</xsl:text>
      <p name="order by tiebreaker" value="{$send}" /><xsl:text>&#xa;</xsl:text>
      <p name="name" value="{$name}" /><xsl:text>&#xa;</xsl:text>
      <p name="notes" value="{$notes}" /><xsl:text>&#xa;</xsl:text>
      <p name="critical" value="{$critical}" /><xsl:text>&#xa;</xsl:text>
      <p name="final cost accrual" value="{$final_cost_accrual}" /><xsl:text>&#xa;</xsl:text>
      <p name="work variance" value="{$work_variance}" /><xsl:text>&#xa;</xsl:text>
      <p name="creation date" value="{$create_date}" /><xsl:text>&#xa;</xsl:text>
      <p name="priority" value="{$priority}" /><xsl:text>&#xa;</xsl:text>
      <p name="start" value="{$start}" /><xsl:text>&#xa;</xsl:text>
      <p name="finish" value="{$end}" /><xsl:text>&#xa;</xsl:text>
      <p name="early start" value="{$early_start}" /><xsl:text>&#xa;</xsl:text>
      <p name="early finish" value="{$early_end}" /><xsl:text>&#xa;</xsl:text>
      <p name="late start" value="{$late_start}" /><xsl:text>&#xa;</xsl:text>
      <p name="late finish" value="{$late_end}" /><xsl:text>&#xa;</xsl:text>
      <p name="work" value="{$work}" /><xsl:text>&#xa;</xsl:text>
      <p name="summary" value="{$summary}" /><xsl:text>&#xa;</xsl:text>

    </subentity><xsl:text>&#xa;</xsl:text>
  </entity><xsl:text>&#xa;</xsl:text>
</xsl:template>

</xsl:stylesheet>
