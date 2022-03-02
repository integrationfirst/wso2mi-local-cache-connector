## Avaiable Features
---
 - putObject
 - getObject
## Compatability
---
Connector Version | Minio Version | AWS S3 SDK Version
----|----|----
1.0.4 | 8.3.4 |
1.0.5 | 8.3.4 |

## Get Started
---
To be able to use the connector, you have to download ZIP package and import to WSO2 EI Studio
#### Download and install connector
---
1. Download the **minio-connector-{version}.zip** from the asset list of the release ([latest release](https://github.com/spsvnhub/wso2mi-minio-connector/releases/latest))
2. You can then follow this [documentation](https://docs.wso2.com/display/EI650/Working+with+Connectors+via+the+Management+Console) to add the connector to your WSO2 EI instance and to enable it (via the management console).
3. For more information on using connectors and their operations in your WSO2 EI configurations, see [Using a Connector](https://docs.wso2.com/display/EI650/Using+a+Connector).
4. If you want to work with connectors via WSO2 EI Tooling, see [Working with Connectors via Tooling](https://docs.wso2.com/display/EI650/Working+with+Connectors+via+Tooling).
#### Configuring the connector
---
To get started with Kafka connector and their operations, see [Configuring Kafka Operations](docs/config.md).

## Build from Source Code
---
Follow the steps given below to build the Kafka connector from the source code:
1. Check out source code from [github](https://github.com/spsvnhub/wso2mi-minio-connector.git) master branch
2. Build the source code by execute ```mvn clean package -Dmaven.test.skip=true```
