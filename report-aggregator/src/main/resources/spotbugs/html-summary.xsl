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
				<style type="text/css">
					.tablerow0 {
					background: #EEEEEE;
					}

					.tablerow1 {
					background: white;
					}

					.detailrow0 {
					background: #EEEEEE;
					}

					.detailrow1 {
					background: white;
					}

					.tableheader {
					background: #b9b9fe;
					font-size: larger;
					}

					.tablerow0:hover, .tablerow1:hover {
					background: #aaffaa;
					}

					.priority-1 {
					color: red;
					font-weight: bold;
					}
					.priority-2 {
					color: orange;
					font-weight: bold;
					}
					.priority-3 {
					color: green;
					font-weight: bold;
					}
					.priority-4 {
					color: blue;
					font-weight: bold;
					}
				</style>
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
				<h3>Metrics</h3>
				<xsl:apply-templates select="FindBugsSummary" />
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

	<xsl:template match="FindBugsSummary">
		<xsl:variable name="kloc" select="@total_size div 1000.0" />
		<xsl:variable name="format" select="'#######0.00'" />

		<p>
			<xsl:value-of select="@total_size" />
			lines of code analyzed,
			in
			<xsl:value-of select="@total_classes" />
			classes,
			in
			<xsl:value-of select="@num_packages" />
			packages.
		</p>
		<table width="500" cellpadding="5" cellspacing="2">
			<tr class="tableheader">
				<th align="left">Metric</th>
				<th align="right">Total</th>
				<th align="right">Density*</th>
			</tr>
			<tr class="tablerow0">
				<td>High Priority Warnings</td>
				<td align="right">
					<xsl:value-of select="@priority_1" />
				</td>
				<td align="right">
					<xsl:choose>
						<xsl:when
							test="number($kloc) &gt; 0.0 and number(@priority_1) &gt; 0.0">
							<xsl:value-of
								select="format-number(@priority_1 div $kloc, $format)" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="format-number(0.0, $format)" />
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr class="tablerow1">
				<td>Medium Priority Warnings</td>
				<td align="right">
					<xsl:value-of select="@priority_2" />
				</td>
				<td align="right">
					<xsl:choose>
						<xsl:when
							test="number($kloc) &gt; 0.0 and number(@priority_2) &gt; 0.0">
							<xsl:value-of
								select="format-number(@priority_2 div $kloc, $format)" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="format-number(0.0, $format)" />
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>

			<xsl:choose>
				<xsl:when test="@priority_3">
					<tr class="tablerow1">
						<td>Low Priority Warnings</td>
						<td align="right">
							<xsl:value-of select="@priority_3" />
						</td>
						<td align="right">
							<xsl:choose>
								<xsl:when
									test="number($kloc) &gt; 0.0 and number(@priority_3) &gt; 0.0">
									<xsl:value-of
										select="format-number(@priority_3 div $kloc, $format)" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="format-number(0.0, $format)" />
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>
					<xsl:variable name="totalClass" select="tablerow0" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:variable name="totalClass" select="tablerow1" />
				</xsl:otherwise>
			</xsl:choose>

			<tr class="$totalClass">
				<td>
					<b>Total Warnings</b>
				</td>
				<td align="right">
					<b>
						<xsl:value-of select="@total_bugs" />
					</b>
				</td>
				<xsl:choose>
					<xsl:when test="number($kloc) &gt; 0.0">
						<td align="right">
							<b>
								<xsl:value-of
									select="format-number(@total_bugs div $kloc, $format)" />
							</b>
						</td>
					</xsl:when>
					<xsl:otherwise>
						<td align="right">
							<b>
								<xsl:value-of select="format-number(0.0, $format)" />
							</b>
						</td>
					</xsl:otherwise>
				</xsl:choose>
			</tr>
		</table>
		<p>
			<i>(* Defects per Thousand lines of non-commenting source statements)</i>
		</p>
		<p>
			<br />
			<br />
		</p>

	</xsl:template>


</xsl:stylesheet>