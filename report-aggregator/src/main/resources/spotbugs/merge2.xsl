<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" />

	<xsl:param name="xmlFiles"></xsl:param>

	<xsl:template name="test">
		<BugCollection sequence='0' release=''
			analysisTimestamp='1527773135425' version='3.1.2'
			timestamp='1527773134000'>
			<xsl:for-each select="$xmlFiles">
				<xsl:copy-of select="/*/node()"></xsl:copy-of>
			</xsl:for-each>
		</BugCollection>
	</xsl:template>
</xsl:stylesheet>