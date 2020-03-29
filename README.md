* I saw the below issue at ES forum.
* [how-to-convert-avro-schema-to-elastic-search-mapping](https://discuss.elastic.co/t/how-to-convert-avro-schema-to-elastic-search-mapping/114749)
* So I made this project for solving it.

It has some features.
First, you should load Avro schema using Registry API or modify text on left panel
Second, you should click "convert" button on right panel
Finally, you can get converting result that Avro schema to Elasticsearch Mapping simplify

![Demo Image](docs/readme.png?raw=true "readme")
![Demo Image](docs/passed_test.png?raw=true "passed_test")

How to use
* gradle build
* java -jar target/gs-serving-web-content-0.1.0.jar
* https://spring.io/guides/gs/serving-web-content/
* docker build -t gs-serving-web-content-0.1.0:v1 .
* docker push gs-serving-web-content-0.1.0:v1

Thank you!
