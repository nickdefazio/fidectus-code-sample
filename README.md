# fidectus-code-sample
This repository acts as a starting point for submitting a code sample for Fidectus. In this repo, you will find a basic 
Java application to get you started, and some instructions on what we are looking for you to implement as a sample. We ask
that you fork this repository, and modify your fork as needed.

# Coding Exercise

##  Problem Statement
The goal of this API is to provide a RESTful HTTP interface for other services to call, so that they may log events that happen
within an imaginary system. For this exercise, imagine you are consuming events from a system that deals primarily with user registration.

## Technology Stack
This application should use the following:
* Java 11 or greater
* Spring Boot
* Maven

## Events
Events are made up of the following fields, all of which are required:
* A unique event ID
* An event type
  * Event types should be limited to
    * User registration
    * User deleted
    * User deactivated
    * User updated registration information
* A user ID
* The time the event took place
* An event hash, generated from the user id, time, and event type


## API Specification
* The API should provide a way to get events by a given ID, a way to create new events, and a way to search them by a given user id.
* The API should not provide any means up updating or deleting events, as this log is meant to be immutable to consumers.
* The API should only support JSON
* The API should enforce required fields

## Storage Requirements
This application should be capable of storing data within memory. Note that it does not need to persist this data between restarts. 
We suggest one of the following, but you are welcome to implement storage however you see fit:

1. A simple Spring Data/In memory database solution
   - https://spring.io/guides/gs/accessing-data-jpa/ and https://spring.io/guides/gs/accessing-data-rest/ provide excellent samples of this
2. If you are not familiar with Spring Data/storage, feel free to implement your own. This can be as simple as an ArrayList or similar that
stores events, or something of your own design.

Keep in mind that the objective here is to simply persist events by any means necessary, but we are looking for well written code that specifically separates storage
responsibilities from REST concerns(https://en.wikipedia.org/wiki/SOLID).

## Testing requirements
We are looking for you to implement well tested code for this sample. Ideally this is made up of a fair amount of unit tests for testing
your individual components, and some integration tests to test the application as a whole.

To aid in this, we have provided the jacoco plugin, which can produce a unit test coverage report. Note that this does not mean we expect
100% coverage, but we do ask that you implement what you feel is reasonable.