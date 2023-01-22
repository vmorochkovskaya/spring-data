## How to run app
1. Install MongoDb server
2. mvn spring-boot:run

## Test app using Postman
- To add ticket:
Send POST request to http://localhost:8080/booking/save?email=harry@gmail.coom with the body like:
`{
   "event": {
   "title": "Event-X",
   "date": "2023-06-12"
   },
   "place": 17
   }`
- To delete ticket:
Send DELETE request to http://localhost:8080/booking?email=harry@gmail.coom with the body like:
  `{
  "event": {
  "title": "Event-X",
  "date": "2023-06-12"
  },
  "place": 17
  }`
- To show grouped results:
Send GET request to http://localhost:8080/booking
