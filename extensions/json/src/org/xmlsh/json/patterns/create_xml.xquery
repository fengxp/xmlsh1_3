declare namespace json='http://www.xmlsh.org/jsonxml';
declare namespace xsl='http://www.w3.org/1999/XSL/Transform';
import module namespace common = "http://www.xmlsh.org/jsonxml/common"  at "common.xquery" ;
import module namespace full = "http://www.xmlsh.org/jsonxml/full" at "full.xquery" ;
import module namespace simple = "http://www.xmlsh.org/jsonxml/simple" at "simple.xquery" ;




(: Dynamic dispatch  :)
declare function local:toxml( $es as element()* )
{
	for $e in $es
	return (
		let $json := common:getjson( $e ) , 
			 $pattern := $json/@pattern/string()
		return
		if( $pattern eq 'full' ) then
			full:toxml( $e )
		else
		if( $pattern eq 'simple' ) then
			simple:toxml( $e )
		else 
			(),
		local:toxml( $e/element | $e/attribute )
	)
};

document {
	<xsl:stylesheet version="2.0" >
	{
		local:toxml( /document )

	}
	</xsl:stylesheet>
}



(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="tojson" userelativepaths="yes" externalpreview="no" useresolver="yes" url="..\..\..\..\..\..\..\..\jsonxml\playing\all.xml" outputurl="" processortype="saxon" tcpport="0" profilemode="0" profiledepth=""
		          profilelength="" urlprofilexml="" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" host="" port="538976288" user=""
		          password="" validateoutput="no" validator="internal" customvalidator="">
			<advancedProperties name="DocumentURIResolver" value=""/>
			<advancedProperties name="bSchemaAware" value="false"/>
			<advancedProperties name="bXml11" value="false"/>
			<advancedProperties name="CollectionURIResolver" value=""/>
			<advancedProperties name="iValidation" value="0"/>
			<advancedProperties name="bExtensions" value="true"/>
			<advancedProperties name="iWhitespace" value="0"/>
			<advancedProperties name="bTinyTree" value="false"/>
			<advancedProperties name="bUseDTD" value="false"/>
			<advancedProperties name="bWarnings" value="true"/>
			<advancedProperties name="ModuleURIResolver" value=""/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
		<MapperBlockPosition></MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)