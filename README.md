![header](src/main/resources/static/img/yumLong.png)

<h1 align="center">⭐️ A little bit about Yum-Rando ⭐️</h3>
<br>
<p>Yum-Rando is a game-changing web application built to answer the age-old question of “what’s for dinner?” Built using geolocation data from the Zomato restaurant API, Yum-Rando can suggest a randomized restaurant for you that is near your location. You are also able to build a list using local storage to randomize through. If you chose to become a registered user you will have the ability to further customize your experience through self-generated lists that are saved to our database with a unique title of your choosing, as well as the ability to review a restaurant and adding photos through the Filestack API, and share your experience with your friends.</p>

### Learn all about us!
1. [Try our site out!](https://github.com/yum-rando/yumrando#Try-us-out)
2. [Technology and Software](https://github.com/yum-rando/yumrando#Technology-and-Software<)
3. [Developer Favorites!]
4. [Contact Us]

### Try us out!
[yumrando.com](https://yumrando.com/)

### Technology and Software:

<h5>Frontend</h5>
- Html 5
- JavaScript/EcmaScript 6
- [Bootstrap](https://getbootstrap.com/)
- jQuery [link](https://jquery.com/)

<h5>Backend</h5>
- Spring Boot 
- Hibernate ORM + JPA
- Thymeleaf
- MySQL
- Java 

<h5>Resources</h5>
- Zomato Restaurant API [link](https://developers.zomato.com/)
<hr>

### Developer Favorites:

<h2 align="center">Favorite Aspects and Challenges From Our Developers</h2>

Chelsea:

Favorite aspect & a challenge =>

<p>To add a restaurant to a list required the server to return a Rest Controller. In order to add or update the restaurant to the database, it required checking the restaurant to be added via its name and zip code. If it was not in the system, it would then create an empty set of lists for the restaurant then save the new restaurant. If the check was verified, then you would simply update the restaurant to be saved by adding the new list its set of lists. The challenge here was updating a many-to-many relationship between the lists and restaurants and letting the server know if the chosen restaurant was in the DB to save it. Creating methods that specifically update this type of relationship only, benefited keeping the database clean by not creating a new list every time a user added a restaurant.</p>

Code Snippet :

