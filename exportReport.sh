#!/bin/sh

function jsonval {
    prop=$1
    temp=`echo $result | sed 's/\\\\\//\//g' | sed 's/[{}]//g' | awk -v k="text" '{n=split($0,a,","); for (i=1; i<=n; i++) print a[i]}' | sed 's/\"\:\"/\|/g' | sed 's/[\,]/ /g' | sed 's/\"//g' | grep -w $prop`
    echo ${temp##*|}
}

cliente=$1
saveZipDir=$2
saveMD5Dir=$3

echo "Cliente: "$cliente
echo "Dir Zip: "$saveZipDir
echo "Dir MD5: "$saveMD5Dir

mkdir -p $saveZipDir
mkdir -p $saveMD5Dir

if [ "$cliente" == "" ]  || [ "$saveZipDir" == "" ] || [ "$saveMD5Dir" == "" ] ; then
    echo "Parametro cliente, saveZipDir o saveMD5Dir non presenti"
    exit 1
fi

# export report cliente
result=`curl -H "Accept: application/json" -H "Content-Type: application/json" -X POST http://localhost:8080/jasperserver/rest_v2/export -u jasperadmin:jasperadmin -d '{"uris":["/'${cliente}'","/Standard","/themes"]}'`

exportid=`jsonval id`

#check di fine export finchè non e in progress
phase="inprogress"

while  [ "$phase" == "inprogress" ] 
do
    sleep 5
    
    result=`curl -H "Accept: application/json" -H "Content-Type: application/json" http://localhost:8080/jasperserver/rest_v2/export/${exportid}/state -u jasperadmin:jasperadmin`

    phase=`jsonval phase`

    echo $phase
    echo `jsonval message`
done

if [ "$phase" == "failure" ] ; then
    echo "errore durante l'export dei report per il cliente '${cliente}'"
    exit 1
fi

# export output
curl http://localhost:8080/jasperserver/rest_v2/export/${exportid}/${cliente}".zip" -u jasperadmin:jasperadmin > ${saveZipDir}"/"${cliente}".zip"

# MD5 dello zip
md5=`md5sum ${saveZipDir}"/"${cliente}".zip" | awk '{ print $1 }'`
echo $md5 > ${saveMD5Dir}"/"${cliente}".zip.MD5"
