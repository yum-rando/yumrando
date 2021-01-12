package com.yumrando.app;

import com.yumrando.app.models.Restaurant;
import com.yumrando.app.models.Review;
import com.yumrando.app.models.User;
import com.yumrando.app.repos.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApplication.class)
@AutoConfigureMockMvc
public class AppApplicationTests {

    private User testUser;
    private HttpSession httpSession;

    @Autowired
    private MockMvc mvc;

    @Autowired
    UserRepository userDao;

    @Autowired
    ListRestaurantRepository listDao;

    @Autowired
    RestaurantRepository restaurantDao;

    @Autowired
    TagRepository tagDao;

    @Autowired
    ReviewRepository reviewDao;

    @Autowired
    UserTagRepository userTagDao;

    @Autowired
    PhotoRepository photoDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @Autowired
//    private TestRestTemplate restTemplate;


    @Before
    public void setup() throws Exception {

        testUser = userDao.findByUsername("testUser");

        //Creates the testUser if not exists
        if (testUser == null){
            User newUser = new User();
            newUser.setUsername("testUser");
            newUser.setPassword(passwordEncoder.encode("pass"));
            newUser.setEmail("testUser@yumrando.com");
            testUser = userDao.save(newUser);
        }

        // Throws a Post request to /login and expect a redirection to the yumRando homepage after being logged in
        httpSession = this.mvc.perform(post("/login").with(csrf())
                .param("username", "testUser")
                .param("password", "pass"))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("/"))
                .andReturn()
                .getRequest()
                .getSession();
    }

    @Test
    public void contextLoads() {
        // Sanity Test, just to make sure the MVC bean is working
        assertNotNull(mvc);
    }

    @Test
    public void testIfUserSessionIsActive() throws Exception {
        //It makes sure the returned session is not null
        assertNotNull(httpSession);
    }

    //Editing a User --> User Controller
    @Test
    public void testEditUserInfo() throws Exception{
        //Looks for the testUser
        User existingTestUser = userDao.findByUsername("testUser");
        //Makes a POST request to /profile and expect redirection to the /profile page
        this.mvc.perform(
                post("/profile").with(csrf())
                    .session((MockHttpSession) httpSession)
                    .param("firstName", "testDude")
                    .param("lastName", "McGee")
                    .param("email", "testUser1@yumrando.com")
                    .param("phoneNumber", "123456789")
                    .param("zipcode", "33333"))
                .andExpect(status().is3xxRedirection());
    }

    //Creating a List --> RestListRestaurant Controller
//    @Test
//    public void testCreatingList() throws Exception {
//        User existingTestUser = userDao.findByUsername("testUser");
//        //Makes a POST request to /list# and expect ListRestaurant Object
//        this.mvc.perform(
//                post("/restaurants/lists/create").with(csrf())
//                    .session((MockHttpSession) httpSession)
//                    .param("name", "testUser list #1"))
//                .andExpect(status().is3xxRedirection());
//    }

    //Adding a Restaurant --> RestRestaurant Controller

    //Creating a Review --> Review Controller

    //Edit a Review --> Review Controller

    //Deleting a Photo --> Photo Review Controller

    //Deleting a Restaurant from a User's Lists --> RestRestaurant Controller

    //Deleting a User's Lists --> User Controller

    //Adding Restaurant Tags --> RestRestaurantTag Controller

    //Adding Fav Restaurant Tags --> RestRestaurantTag Controller

    //Removing Fav Restaurant Tag --> RestRestaurantTag Controller

    //Adding User Custom Tag --> RestUserTag Controller

    //Removing User Custom Tag --> RestUserTag Controller





}
