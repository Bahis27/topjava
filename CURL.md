`GetAll:`<br>
curl http://localhost:8080/topjava/rest/profile/meals/

`Get:`<br>
curl http://localhost:8080/topjava/rest/profile/meals/100002

`GetBetween:`<br>
curl "http://localhost:8080/topjava/rest/profile/meals/between?sd=2015-05-31&st=18:00&ed=&et="

`Create:`<br>
curl -d '{"dateTime":"2018-04-02T17:00:00","description":"VeryLightBurger","calories":1500,"user":null}' -H "Content-Type: application/json"  http://localhost:8080/topjava/rest/profile/meals/

`Update:`<br>
curl -H "Content-Type: application/json" -X PUT -d '{"id":100002,"dateTime":"2015-05-30T10:00:00","description":"VeryLightPizza","calories":2500,"user":null}' http://localhost:8080/topjava/rest/profile/meals/100002

`Delete:`<br>
curl -X DELETE http://localhost:8080/topjava/rest/profile/meals/100002