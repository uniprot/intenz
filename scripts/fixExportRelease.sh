#!/bin/bash
#
# Fixes the release date in exported files, leaving everything else unchanged.
# Parameters:
# $1 - Wrong release date present in the files
# $2 - Corrected release date

if [ $# -lt 2 ]
then
    echo "Usage: $0 <wrongReleaseDate> <rightReleaseDate>"
    exit 1
fi

cd $(dirname $0)/../dist

echo Fixing sitemap...
mv sitemap.xml.gz sitemap.xml.gz.bak
zcat sitemap.xml.gz.bak | sed "s/$1/$2/" | gzip -c > sitemap.xml.gz
echo Fixed!

cd xml
echo Fixing XML...
mv IntEnzXML.zip IntEnzXML.zip.bak
for XML in $(find ASCII XCHARS -name *.xml)
do
     sed -i "s/$1/$2/" $XML
done
zip -r IntEnzXML.zip *.xsd *.txt ASCII XCHARS
echo Fixed!
