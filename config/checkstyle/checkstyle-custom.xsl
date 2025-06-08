<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" indent="yes"/>
    <xsl:decimal-format decimal-separator="." grouping-separator=","/>

    <xsl:template match="/">
        <html>
            <head>
                <style type="text/css">
                    .severityError {color: red}
                    .severityWarning {color: orange}
                    .file {margin-bottom: 20px}
                    .fileName {font-weight: bold; font-size: 16px; margin-bottom: 10px}
                    .violation {margin-left: 20px; margin-bottom: 5px}
                </style>
                <title>Отчет Checkstyle</title>
            </head>
            <body>
                <h1>Отчет проверки стиля кода</h1>
                <xsl:apply-templates select="checkstyle/file">
                    <xsl:sort select="@name"/>
                </xsl:apply-templates>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="file">
        <xsl:if test="error">
            <div class="file">
                <div class="fileName">
                    <xsl:value-of select="@name"/>
                </div>
                <xsl:apply-templates select="error"/>
            </div>
        </xsl:if>
    </xsl:template>

    <xsl:template match="error">
        <div class="violation">
            <span>
                <xsl:attribute name="class">
                    <xsl:text>severity</xsl:text>
                    <xsl:value-of select="@severity"/>
                </xsl:attribute>
                Строка <xsl:value-of select="@line"/>:
                <xsl:value-of select="@message"/>
            </span>
        </div>
    </xsl:template>
</xsl:stylesheet> 