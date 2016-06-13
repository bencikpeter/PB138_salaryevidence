<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns="http://docbook.org/ns/docbook">

  <xsl:output method="xml"
              doctype-public="-//OASIS//DTD DocBook XML V4.1.2//EN"
              doctype-system="http://www.oasis-open.org/docbook/xml/4.1.2/docbookx.dtd"
              encoding="UTF-8"
              indent="yes" />

  <xsl:template match="invoce">
    <article>
      <articleinfo>
        <title>
          Invoce
        </title>
      </articleinfo>
      <xsl:apply-templates select="days/day" mode="list" />
      <xsl:apply-templates select="days" mode="sum" />
    </article>
  </xsl:template>

  <xsl:template name="dayList" match="day" mode="list">
    <section>
      <title>
        Day: <xsl:value-of select='xs:dateTime("1970-01-01T00:00:00") + @date * xs:dayTimeDuration("PT0.001S")'/>
      </title>
      <para>
        Job: <xsl:value-of select="job" />
      </para>
      <para>
        Working hours: <xsl:value-of select="hours" />
      </para>
      <para>
        Per hour: <xsl:value-of select="perhour" />
      </para>
    </section>
  </xsl:template>

  <xsl:template name="salarySum" match="days" mode="sum">
    <section>
      <title>
        Total
      </title>
      <para>
        Total working hours: <xsl:value-of select="//sum" />
      </para>
      <para>
        Total salary: <xsl:value-of select="//salarysum" />
      </para>
    </section>
  </xsl:template>

</xsl:stylesheet>
