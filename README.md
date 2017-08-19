Carmelo is a fast, scalable Java server framework designed for online games. It uses [Netty](http://netty.io/) and [Fastjson](https://github.com/alibaba/fastjson) for highly efficient network transmission and supports both TCP/HTTP protocols. It also uses [Spring](https://spring.io/) for business logic and [Hibernate](http://hibernate.org/orm/) for data persistence. This framework implements its own servlet to handle client requests, so you can easily extend it to build your own server.



Start from here
----------------------------
**Pre-requisites**: Please have Maven 3.x, Eclipse and MySQL installed. 

Build
-----
1.  git clone https://github.com/needmorecode/carmelo.git
2.  cd carmelo
3.  mvn eclipse:eclipse
4.  Eclipse -> file -> import -> git -> select repository and import carmelo project
5.  carmelo project in Eclipse -> right click on pom.xml -> run as -> maven build

Test
-----
1.  cd src/main/java/carmelo/examples
2.  execute /server/my_test_user.sql in MySQL
3.  run or debug /server/ServerMain.java in Eclipse
4.  run or debug /client/TcpClientMain.java or /client/HttpClientMain.java in Eclipse
