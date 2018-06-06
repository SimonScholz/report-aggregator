<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="html" />

	<xsl:template match="/">
		<html>
			<head>
				<title>Analysis Summary</title>
			</head>
			<body>
				<xsl:apply-templates />
			</body>
		</html>
	</xsl:template>

	<xsl:template match="BugCollection">
		<xsl:choose>
			<xsl:when test="count(BugInstance) > 0">
				<xsl:apply-templates select="Project" />
				<p>
					Amount of bugs:
					<xsl:value-of select="count(BugInstance)" />
				</p>
				<ul>
					<xsl:apply-templates select="BugInstance" />
				</ul>
			</xsl:when>
			<xsl:otherwise>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="Project">
		<h2>
			<xsl:value-of select="@projectName" />
		</h2>
	</xsl:template>

	<xsl:template match="BugInstance">
		<xsl:apply-templates select="ShortMessage" />
	</xsl:template>

	<xsl:template match="ShortMessage">
		<li>
			<xsl:value-of select="." />
		</li>
	</xsl:template>


</xsl:stylesheet>
