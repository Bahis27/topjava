**MealRestController:**

`GetAll:`<br>
curl http://localhost:8080/topjava/rest/profile/meals/

`Get:`<br>
curl http://localhost:8080/topjava/rest/profile/meals/100002

`GetBetween:`<br>
curl "http://localhost:8080/topjava/rest/profile/meals/between?startDate=2015-05-31&startTime=18:00&endDate=&endTime="

`Create:`<br>
curl -d '{"dateTime":"2018-04-02T17:00:00","description":"VeryLightBurger","calories":1500,"user":null}' -H "Content-Type: application/json"  http://localhost:8080/topjava/rest/profile/meals/

`Update:`<br>
curl -H "Content-Type: application/json" -X PUT -d '{"id":100002,"dateTime":"2015-05-30T10:00:00","description":"VeryLightPizza","calories":2500,"user":null}' http://localhost:8080/topjava/rest/profile/meals/100002

`Delete:`<br>
curl -X DELETE http://localhost:8080/topjava/rest/profile/meals/100002



**AdminRestController**

`GetAll:`<br>
curl http://localhost:8080/topjava/rest/admin/users/

`Get:`<br>
curl http://localhost:8080/topjava/rest/admin/users/100001

`GetByEmail:`<br>
curl "http://localhost:8080/topjava/rest/admin/users/by?email=user@yandex.ru"

`Create:`<br>
curl -d '{"name":"New2","email":"new2@yandex.ru","password":"passwordNew","roles":["ROLE_USER"]}' -H "Content-Type: application/json"  http://localhost:8080/topjava/rest/admin/users/

`Update:`<br>
curl -H "Content-Type: application/json" -X PUT -d '{"name":"UserUpdated","email":"user@yandex.ru","password":"passwordNew","roles":["ROLE_USER"]}' http://localhost:8080/topjava/rest/admin/users/100000

`Delete:`<br>
curl -X DELETE http://localhost:8080/topjava/rest/admin/users/100000



**ProfileRestController:**

`Get:`<br>
curl http://localhost:8080/topjava/rest/profile

`Update:`<br>
curl -H "Content-Type: application/json" -X PUT -d '{"name":"New777","email":"new777@yandex.ru","password":"passwordNew","roles":["ROLE_USER"]}' http://localhost:8080/topjava/rest/profile

`Delete:`<br>
curl -X DELETE http://localhost:8080/topjava/rest/profile

