import commands posix
import module j=json

OUT=$(mktemp -d)

cd ../../samples


j:jsonxslt -o $OUT -v -xsd books_none.xsd > $OUT/all.xml 
xslt -f $OUT/tojson.xsl < books.xml > $OUT/books.jxml
xsdvalidate "http://www.xmlsh.org/jxml ../schemas/jxml.xsd" $OUT/books.jxml && xml2json -p < $OUT/books.jxml 
#echo $OUT
rm -r -f $OUT