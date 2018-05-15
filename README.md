# http-distribution-server

Like proxy server, but vice versa. The idea is to send the same HTTP POST or PUT request to bunch of recipients, without changing request sender. Http-distribution-server retranslates request body chunks to several locations. Even though http is commonly session-based protocol, distribution-server can not support sessions in this meaning, instead it supports different response policies, depending on how many retranslations succeded.

## Usage    
Distribution-server is a standalone microservice implemented as [Vert.x 3](http://vertx.io/) component. Generally, there is no need to include library in your dependencies, though it is possible.

### Simple Java process
*Note:* vert.x runner should be in your system PATH, please follow [vert.x installation procedure](http://vertx.io/vertx2/install.html).
```
wget https://bintray.com/artifact/download/alex-rnv-ru/maven/com/alexrnv/http-distribution-server/1.0.0/http-distribution-server-1.0.0.jar    
java -jar http-distribution-server-1.0.0.jar -conf <conf_file>
```
Configuration file is described further.    

### Vert.x service    
TBD    

### Maven 
- Update your maven settings:    
```xml
<?xml version='1.0' encoding='UTF-8'?>
<settings xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd' xmlns='http://maven.apache.org/SETTINGS/1.0.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>
<profiles>
	<profile>
		<repositories>
			<repository>
				<snapshots>
					<enabled>false</enabled>
				</snapshots>
				<id>bintray-alex-rnv-ru-maven</id>
				<name>bintray</name>
				<url>http://dl.bintray.com/alex-rnv-ru/maven</url>
			</repository>
		</repositories>
		<pluginRepositories>
			<pluginRepository>
				<snapshots>
					<enabled>false</enabled>
				</snapshots>
				<id>bintray-alex-rnv-ru-maven</id>
				<name>bintray-plugins</name>
				<url>http://dl.bintray.com/alex-rnv-ru/maven</url>
			</pluginRepository>
		</pluginRepositories>
		<id>bintray</id>
	</profile>
</profiles>
<activeProfiles>
	<activeProfile>bintray</activeProfile>
</activeProfiles>
</settings>
```
- Add dependency    
```xml
<dependency>
        <groupId>com.alexrnv</groupId>
        <artifactId>http-distribution-server</artifactId>
        <version>1.0.0</version>
</dependency>
```
### Gradle
- Add repository    
```
repositories {
    maven {
        url  "http://dl.bintray.com/alex-rnv-ru/maven" 
    }
}
```
- Add dependency    
```
compile(group: 'com.alexrnv', name: 'http-distribution-server', version: '1.0.0')
```

## Configuration    
Configuration file example    
```json
{
  "upstream": {
    "host": "localhost",
    "port": 8888
  },
  "downstreams" : [
    {
        "host": "localhost",
        "port": 8880
    },
    {
      "host": "localhost",
      "port": 8881
    },
    {
      "host": "localhost",
      "port": 8882,
      "uriMappings": [
        {
          "from": "/ok",
          "to": "/200"
        },
        {
          "from": "/redirect",
          "to": "/300"
        },
        {
          "from": "/error",
          "to": "/500"
        }
      ]
    }
  ],
  "eventPolicy": "NO_WAIT"
}
```
* **upstream** - host and port to listen for distribution requests
* **downstreams** - hosts and ports of services to distirbute to 
* **uriMappings** - allows to change request URI for individual recipients
* **eventPolicy** - defines success conditions, allowed policies are NO_WAIT (answers 200 immediately), WAIT_FIRST (send 200 when first 200 response received from downstream), WAIT_ALL (TBD). 

## TODO
normal logs
support GET (take from random downstream)
