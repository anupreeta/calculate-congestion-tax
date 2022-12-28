# calculate-congestion-tax
A Springboot application to calculate congestion tax for vehicles in a city for a specific year. 
The application is easily configurable to cater for different cities, vehicles and years using postgresql database. 
The tax rules are also configurable per city.

## REST API ENDPOINT
http://localhost:8080/api/v1/tax-calculate

## Local Setup

* Build and run tests -> mvn clean install
* Run application -> java -jar {FOLDER_LOCATION}/target/calculator-0.0.1-SNAPSHOT.jar or mvn spring-boot:run

## How to run the application in dockerized container using docker-compose
* Pre-requisite:
    * Docker
* To build and run application with `postgresql` database
``````
  * docker-compose up
``````

* To shutdown application before a fresh start (Note: it will loose state):
````
  * docker-compose down
  * docker rmi docker-tax-calculator-postgres:latest
````

## Testing

To calculate congestion tax for a vehicle for a list of date entries in a city(Eg: Gothenburg), send a POST 
request as below:

http://localhost:8080/api/v1/tax-calculate

### Request Body:

````
{
    "vehicle": {
    "type": "Car"
},
"arrivalTimes": [
                "2013-01-14 06:00:00","2013-01-14 07:30:00",
                "2013-01-14 15:33:27","2013-01-14 16:40:00",
                "2013-02-08 06:27:00","2013-02-08 06:20:27",
                "2013-02-08 14:35:00","2013-02-08 15:29:00",
                "2013-02-08 15:47:00","2013-02-08 16:01:00",
                "2013-02-08 16:48:00","2013-02-08 17:49:00",
                "2013-02-08 18:29:00","2013-02-08 18:35:00",
                "2013-03-26 14:25:00","2013-03-28 14:07:27"
                ]
}
````

### Response Body

````````
{
    "taxAmount": 128.00,
    "datewiseTaxCharges": {
        "2013-01-14": 60,
        "2013-03-26": 8.00,
        "2013-02-08": 60
    }
}
````````

## Connect to PostGreSQL Database
``````
docker exec -it db /bin/bash
bash-5.1# psql -h localhost -p 5432 -d calculator -U myuser
psql (13.1)
Type "help" for help.

calculator=# \dt
                List of relations
 Schema |         Name          | Type  | Owner  
--------+-----------------------+-------+--------
 public | city                  | table | myuser
 public | city_holiday_months   | table | myuser
 public | city_holidays         | table | myuser
 public | city_tax_charges      | table | myuser
 public | city_tax_days         | table | myuser
 public | city_tax_rules        | table | myuser
 public | city_vehicle          | table | myuser
 public | flyway_schema_history | table | myuser
 public | vehicle               | table | myuser
(9 rows)

``````````

