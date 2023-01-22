## How to run app
1. Download ActiveMQ Artemis https://activemq.apache.org/components/artemis/download/
2. Add its bin folder to PATH  
3. Create broker: `artemis create mybroker`
4. Start broker instance from its bin folder: `artemis run`
5. Start PostgreSql server
6. mvn spring-boot:run

## Test app using Postman
1. Send POST request to http://localhost:8080/booking/save with the body like:
`{
   "event": {
   "id": 2
   },
   "user": {
   "id": 2
   },
   "place": 6
   }`

## How to run test
1. Start broker instance
2. Install and start docker
3. Stop app if it is running
3. mvn test
