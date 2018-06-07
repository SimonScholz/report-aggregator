<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" />

	<xsl:param name="xmlFiles" />

	<xsl:template name="test">
		<!-- TODO count xml files and add timestamp -->
		<MergedSpotBugs timestamp="TODO"
			spotbugsFilesCount="TODO">
			<xsl:for-each select="$xmlFiles">
				<xsl:copy-of select="/" />
			</xsl:for-each>
		</MergedSpotBugs>
	</xsl:template>
</xsl:stylesheet>