#!bin/sh
rm -r $CATALINA_BASE/webapps/eBay
rm $CATALINA_BASE/webapps/eBay.war
rm -r build
ant -f build.xml
cp build/eBay.war $CATALINA_BASE/webapps
