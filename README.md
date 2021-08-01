# Recipes

## About 
A multi-user web service based on Spring Boot that allows storing, retrieving, updating, and deleting recipes.
### *`(server.port=8881)`*  
First step running the program is to register a user account or login into existing one to interact with the endpoints.  

The application provides several endpoints for request:
* `POST /api/register` - to register a user with a JSON request body. This endpoint is free of authentication.  
Request body:
```
{
   "email": "Cook_Programmer@somewhere.com",
   "password": "RecipeInBinary"
}
```
Sending a valid email end lengthy enough password the user gets successfully registered in case the email has not already been registered. 

* `POST /api/recipe/new` - the user can create a recipe and store it in the database. To reach this endpoint the user need to be authenticated.  
Request body:  
```
{
   "name": "Mint Tea",
   "category": "beverage",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}
``` 
As the recipe is created, the user that created it is recorded as the author.  
The response of the request is the ID:  
```
{
   "id": 1
}
```
* `GET /api/recipe/{id}` - path variable request to get a recipe by its ID. The endpoint requires basic authentication, so any authenticated user can view the recipes.  
The response of the request also contains date of the last modification: 
```
{
   "name": "Fresh Mint Tea",
   "category": "beverage",
   "date": "2020-01-02T12:11:25.034734",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}
```
In case the recipe under particular id does not exist the program responds with 404 code response.
* `GET /api/recipe/search` takes one of the two mutually exclusive query parameters:
  * **category** – if this parameter is specified, it returns a JSON array of all recipes of the specified category. Search is case-insensitive, sort the recipes by date (newer first);
  * **name** – if this parameter is specified, it returns a JSON array of all recipes with the names that contain the specified parameter. Search is case-insensitive, sort the recipes by date (newer first).  
  
If no recipes are found, the program returns an empty JSON array. If 0 parameters were passed, or more than 1, the server returns 400 (***BAD_REQUEST***). The same response follows if the specified parameters are not valid. If everything is correct, it returns 200 (**_OK_**).

* `PUT /api/recipe/1` - receives a recipe as a JSON object and updates a recipe with a specified ID. To update a recipe user should be authenticated and authorized. Also, updates the date field too. If update is successful the server returns the 204 (***NO_CONTENT***) status code. If a recipe with a specified id does not exist, the server returns 404 (**_NOT_FOUND_**). The server responds with 400 (***BAD_REQUEST***) if a recipe doesn't follow the restrictions. 403 (Forbidden) - if not authorized.
Status code:  
`204 (No Content)`

* `DELETE /api/recipe/{id}` deletes a recipe with a specified ID. To delete a recipe user should be authenticated and authorized. If a recipe with a specified id does not exist, the server returns 404 (**_NOT_FOUND_**). 403 (**_FORBIDDEN_**) - if not authorized. If recipe specified is found and the user is the author the response is:  
`204 (No Content)`

## Technical Obsevations  
*Spring Boot project with JSON, REST API, Spring Boot Security, H2 database, LocalDateTime, Project Lombok, and other concepts.*  
The program is built on Layer Architecture design pattern. It provides a way to encapsulate software and functionality, allowing to easily change single layers, without affecting the others. In general, smaller applications with many layers will have worse performance and require more complex maintenance with a layered architecture. This, it is best to use layered architectures with larger applications to get their full benefits.  
**To test the api you can use [Postman](https://www.postman.com/)**.

