# nagarro-task


use java -jar interview-task-0.0.1-SNAPSHOT.jar to run jar

server is running into fixed port 9080 to change it you have to open project and change server.port value into application.properties file.

you can access swagger ui to test all api from the below url 
http://localhost:9080/swagger-ui.html#/

the authentication api will return token which should be a header for all other apis.

add this word "Bearer " before adding the token to the headers for other apis.

sonarqube report attached as photo because pdf only avaliable on enterprise edition

use "mvn test" to run test cases 


I made 2 change values into DB file to get statment withen 2021 to test apis


unit test coverage pic is attached

you should have redis up and running into port 6379 and localhost to run the application

use "mvn clean package" to package the jar before run it