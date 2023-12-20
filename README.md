README.md


Git link:
All the projects are uploaded in a single git repository
https://github.com/abhishekkashyapsudo/gRPC_RabbitMq

It will have following four projects:
a. Notifications1
b. Notifications2
c. Order
d. Products

For running these projects:
1. Download the source code as Zip file.
2. Import all the four projects as Existing Maven projects 
3. Run maven build for all these projects.
4. If you get a duplicate class:  for Products and Order proto classes, please follow the folowing steps:
    4.1 Delete OrderServiceGrpc.java and Products.java under com.nagp.products.proto
    4.2 Again run maven install
    4.3 Copy OrderServiceGrpc.java from target -> generated-sources -> protobuf -> grpc-java -> com ->     nagp -> products -> proto and paste it under com.nagp.products.proto
    4.4 Copy Products.java from target -> generated-sources -> protobuf -> java -> com ->     nagp -> products -> proto and paste it under com.nagp.products.proto
5. For running Order project, run OrderApplication as java Application.
6. For running Products project, run ProductsApplication as java Application.
7. For running Notifications1 project, run OrderCreationConsumer as java Application.
8. For running Notifications2 project, run OrderCreationAndUpdationConsumer as java Application.

Prequisites: Please make sure that RabbitMQ is installed and the required exchanges are created.