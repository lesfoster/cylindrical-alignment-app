<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<!-- XSLT: to construct xml from GeneMachine output. -->

<xsl:output indent="no" method="xml" omit-xml-declaration="no"/>
<xsl:strip-space elements="*"/>

<xsl:template match="/">
  <xsl:apply-templates select="INSDSet" />
</xsl:template>


<xsl:template match="INSDSeq">
  <xsl:variable name="id" select="INSDSeq_locus" />
  <xsl:variable name="length" select="INSDSeq_length" />
  <xsl:variable name="defline" select="INSDSeq_other-seqids/INSDSeqid" />
  <cylinder version="1.0" anchor_length="{$length}" anchor_name="{$id}"><xsl:text>&#xa;</xsl:text>
    <entity><xsl:text>&#xa;</xsl:text>
      <subentity qstart="1" qend="{$length}" sstart="1" send="{$length}" strand="1"><xsl:text>&#xa;</xsl:text>
        <p name="subEntityType" value="Anchor" /><xsl:text>&#xa;</xsl:text>
        <p name="name" value="{$defline}" /><xsl:text>&#xa;</xsl:text>
      </subentity><xsl:text>&#xa;</xsl:text>
    </entity><xsl:text>&#xa;</xsl:text>
    <xsl:apply-templates select="INSDSeq_feature-table" /><xsl:text>&#xa;</xsl:text>

  </cylinder><xsl:text>&#xa;</xsl:text>

</xsl:template>


<xsl:template match="INSDSeq/INSDSeq_feature-table">
  <xsl:apply-templates select="INSDFeature" />
</xsl:template>

<xsl:template match="INSDSeq_feature-table/INSDFeature">
  <xsl:if test="INSDFeature_intervals">
  <entity><xsl:text>&#xa;</xsl:text>
    <xsl:apply-templates select="INSDFeature_intervals" />
  </entity><xsl:text>&#xa;</xsl:text>
  </xsl:if>

</xsl:template>

<xsl:template match="INSDFeature_intervals/INSDInterval">
  <xsl:variable name="type" select="ancestor::INSDFeature/INSDFeature_key" />
  <xsl:variable name="subtype" select="ancestor::INSDFeature/INSDFeature_quals/INSDQualifier/INSDQualifier_value" />
  <xsl:variable name="pname" select="ancestor::INSDFeature/INSDFeature_quals/INSDQualifier/INSDQualifier_name" />

  <xsl:variable name="ifrom" select="INSDInterval_from" />
  <xsl:variable name="ito" select="INSDInterval_to" />

  <xsl:if test="INSDInterval_to &gt; INSDInterval_from" >
    <subentity strand="1" sstart="{$ifrom}" send="{$ito}" qstart="{$ifrom}" qend="{$ito}">
      <p name="subEntityType" value="{$type}:{$subtype}" />
      <p name="{$pname}" value="{$subtype}" />
    </subentity>
  </xsl:if>
  <xsl:if test="INSDInterval_to &lt;= INSDInterval_from" >
    <subentity strand="-1" sstart="{$ito}" send="{$ifrom}" qstart="{$ito}" qend="{$ifrom}">
      <p name="subEntityType" value="{$type}:{$subtype}" />
      <p name="{$pname}" value="{$subtype}" />
    </subentity>
  </xsl:if>
</xsl:template>

</xsl:stylesheet>
