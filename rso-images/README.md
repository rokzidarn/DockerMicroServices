RSO DOCUMENTATION:
------------------

DEPLOY:

	http://159.122.186.58:31169/v1/images
	http://159.122.186.58:31010/v1/comments
	http://159.122.186.58:31443/v1/catalogs

PROJECT:

	IntelliJ configuration:
		Main class: EeApplication -> com.kumuluz.ee.EeApplication
		Working directory: <project name>
		Classpath module <project-name>-api
	
	api dir:
		<entity>Application.java - defines application path (version /v1)
		<entity>Resource.java - REST CRUD operations, [@Path, @GET], (business logic calls + responses)
	models dir:
		init-<entity>.sql - database seeder
		<entity>.java - ORM entity class, [@Entity, @NamedQueries, @Id, @Column], get/set methods
	services dir:
		<entity>Bean.java - needs entity manager (@Inject from PersistenceManager.java), performs transactions
		on database (begin(), commit(), rollback()), business logic, receives calls from api dir
		
	mvn clean package  // builds all modules, creates jar -> \api\target\
	java -jar ./api/target/images-api-1.0.0-SNAPSHOT.jar  // runs app
		
DOCKER:

    docker rm -f pg-images
	docker run -d --name pg-images -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=image -p 5432:5432 postgres:10.5	
		
	config.yaml:
		datasource connection url = localhost
		
POSTMAN:
	 
	 http://localhost:<exposed port number>/<version>/<endpoint>
    
TRAVIS:

    1. mvn clean package
    2. Dockerfile configuration
    3. docker build -t rokzidarn/rso-images:0.1 .
    4. docker push rokzidarn/rso-images:0.1
    
DOCKER CONFIG:

    environment variables in docker: docker run -e ENVIRONMENT_VAR=value
    docker-compose.yml:
	
	local config.yaml
    kumuluzee:
      name: rso-comment
      env:
        name: dev
      version: 1.0.0
      server:
        base-url: http://localhost:8080
        http:
          port: 8080
      datasources:
      - jndi-name: jdbc/CommentsDS
        connection-url: jdbc:postgresql://localhost:5432/comment
        username: dbuser
        password: postgres
        max-pool-size: 20
      discovery:
        etcd:
          hosts: http://localhost:2379
    
    docker network create rso  # creates network between DB and my service
    docker run -d --name pg-comment --network rso -e \
        POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=image -p 5432:5432 postgres:10.5
    docker run -d --name rso-comment --network rso -e KUMULUZEE_DATASOURCES0_CONNECTIONURL=jdbc:postgresql://postgres:5432/comment -e \
		 KUMULUZEE_DATASOURCES0_USERNAME=dbuser -e KUMULUZEE_DATASOURCES0_PASSWROD=postgres -p 8080:8080 rokzidarn/rso-comment:0.2
        
SERVICE DISCOVERY:
    
    docker run -d -p 2379:2379 \
                 --name etcd \
                 quay.io/coreos/etcd:latest \
                 usr/local/bin/etcd \
                 --name my-etcd-1 \
                 --data-dir /etcd-data \
                 --listen-client-urls http://0.0.0.0:2379 \
                 --advertise-client-urls http://0.0.0.0:2379 \
                 --listen-peer-urls http://0.0.0.0:2380 \
                 --initial-advertise-peer-urls http://0.0.0.0:2380 \
                 --initial-cluster my-etcd-1=http://0.0.0.0:2380 \
                 --initial-cluster-token my-etcd-token \
                 --initial-cluster-state new \
                 --auto-compaction-retention 1 \
                 -cors="*"                    
    
    docker-compose up -d 
    
    docker logs <service-image>
        ETCD2Configuration -- Using namespace: 
            environments/dev/services/rso-images/1.0.0/config/app-properties/external-services/enabled
            
    http://henszey.github.io/etcd-browser/
        http://localhost:2379
		
KUBERNETES:

    DASHBOARD:
        Deployments
        Pods
        Replica Sets
        Services
    
    IBM (only 1 node, use NodePort)
	
LOGS AND FAULT TOLERANCE:

	Kibana search: marker.name: ENTRY || marker.name: EXIT
		
	Fault tolerance: connection refused error on service discovery (comment) with /images
		rso-comment service: config.yaml
			base-url: http://localhost:8079
				http:
				  port: 8080
				  
GRAPHQL:

	http://localhost:8081/graphiql