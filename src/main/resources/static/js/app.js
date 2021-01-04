($ => {
    "use strict"
    $(document).ready(()=>{

        let tagSelection = [];
        let randomSearchResult = {};

        const arrayConstructor = () => {
            let listDisplayItems = localStorage.getItem("yumList");
            if (listDisplayItems === null){
                return [];
            } else {
                return JSON.parse(listDisplayItems);
            }
        }
        const deleteLocal = num => {
            let array = arrayConstructor().filter((rest, index) => index !== num);
            localStorage.setItem("yumList", JSON.stringify(array));
            listBasic(array);
        }

        const listBasic = array => {
            let parent = $('#restaurant-items');
            parent.empty();
            array.map((item, num) =>{
                $(parent).append(
                    `
                      <div class="container">
                      <div class="row">
                      <div id="r${num}" class="col-9" data-bs-toggle="modal" data-bs-target="#showModal">
                      <h6>${item.name}</h6>
                      </div>
                     <div class="col-3">
                     <button id="delete${num}" type="button" class="btn btn-danger">-</button>
                    </div>
                      </div>
                        </div>`
                );
                $(`#delete${num}`).click(()=>{
                    deleteLocal(num)
                });

                $(`#r${num}`).click(()=>{
                $('#show-modal-label').empty().append(
                `
                <h5 class="modal-title">${item.name}</h5>
                `
                )
                })
            })

        }

        const updateLocal = object => {
            localStorage.setItem("yumList", JSON.stringify(
                [...arrayConstructor(), object]
            ))
            listBasic(arrayConstructor());
        }
        const updateCurrentList = rest =>{
            const listNumber = $("#currentList").val();
            const url = `/restaurants/lists/${listNumber}`;
            apiCreate(rest, url).then(()=>{window.location.assign(`/${listNumber}`)}).catch(()=>{console.error("Nope!")});
        }

        const obtainRestaurant = num => {
            if (num.includes('u')){
                let restaurant = resultSet[parseInt(num.substring(1))]
                let postObject = {
                    address: restaurant.location.address,
                    apiId: restaurant.id,
                    name: restaurant.name,
                    website: restaurant.url,
                    city: restaurant.location.city,
                    zipcode: restaurant.location.zipcode
                }
                updateCurrentList(postObject);
            } else {
                updateLocal(resultSet[parseInt(num)]);
            }
        }

        const listResult = (array, type) => {
            let parent = $('#search-results, #search-results-user');

            parent.empty();
            resultSet = [];
            array.map(({restaurant}, num) => {
                resultSet.push(restaurant);

                $(parent).append(
                    `<div class="container">
                        <div class="row">
                            <div class="col-9">
                                <h5>${restaurant.name}</h5>
                                <p>${restaurant.location.address}</p>
                             </div>
                             <div class="col-3">


                                 <button id="${type + num}" type="button" class="btn btn-primary" data-bs-dismiss="modal">Add to List</button>


                             </div>
                         </div>
                    </div>
                        `
                );


                $(`#${type + num}`).click(()=> {
                    obtainRestaurant(type + num);


                })
            });
        }

        $('#add-basic').click(()=>{
            let basicInput = $('#simple-name');
            let objectConvert = {name: basicInput.val()};
           updateLocal(objectConvert);
            basicInput.val("");
            listBasic(arrayConstructor());
            tagSelection=[];
            $("#tag-choices, #tag-addon").empty();
            $("#tag-choice").toggleClass('d-none')
        })

        const selectRest = '#search-select';
        const selectRestUser = '#search-select-user'
        const modalBody = '#search-body';

        const selectEvent = (selector, type) => {
            $(selector).change(() => {
                $(modalBody).empty();
                $('#search-results, #search-results-user').empty();
                switch ($(selector).val()) {
                    case "name":

                        if(type === "") {
                            $(modalBody).append(`<input placeholder="Search by Name" id="nameSearch"/>`)
                        } else {
                            $(modalBody).append(`<input placeholder="Search by Name" id="nameSearchUser"/>`)
                        }
                        break;
                    case "near":
                        // Attach loader to $('#search-results')
                        let coordInput = JSON.parse(localStorage.getItem("yumCoord"));
                        apiSearch(searchLocal(coordInput.latitude, coordInput.longitude)).then(data => {
                        // Clear loader from $('#search-results) (.empty() works well for that)
                        listResult(data.nearby_restaurants, type)
                    });
                        break;
                    default:
                        return;
                }
            })
        }

        const inputSearch = (selector, type) => {
            // Attach loader to $('#search-results')
            let coordInput = JSON.parse(localStorage.getItem("yumCoord"));
            apiSearch(searchName($(selector).val(), coordInput.latitude, coordInput.longitude)).then(data=> {
                // Clear loader from $('#search-results) (.empty() works well for that)
                listResult(data.restaurants, type);
            });
        }

        const inputSearchSetup = (selector, type) => {
            $(document).on('change', selector,()=>{
             if(typeof $(selector).val() !== 'undefined')
                    inputSearch(selector, type);
            })
        }

        $('#new-list').click(()=>{
            $('#user-list-items').addClass('d-none');
            $('#add-list-form').empty().append(
                `<form>
                    <div class="mb-3">
                    <label for="name" class="form-label">Enter a name for your list:</label>
                    <input name="name" type="text" class="form-control" id="name">
                    </div>
                     <button id="submit-list" type="button" class="btn btn-primary">Submit</button>
                 </form>
                `
            )
            $('#submit-list').click(()=>{

                let listObject = {
                    name: $('#name').val()
                }
                apiCreate(listObject, "/restaurants/lists/create").then(data=>{

                    window.location.assign(`/${data.id}`)

                }).catch(()=>{
                    console.log("We are not champions : (")
                });
            })
        })


        // A Select that changes the list view for user
        $("#currentList").change(() => {
           const listNum = $("#currentList").val()
            if (listNum !== 'default') {
                window.location.assign(`/${listNum}`)
            }

        })


        $("#add-basic-user").click(() => {
            const restaurantName = {
                name: $("#simple-name").val()
            }
            updateCurrentList(restaurantName);
        })

        $("#tag-choice").click(function(){
            $(this).toggleClass('d-none')
            $("#tag-addon").append(
                `
                <div>
                   <input id="tag-type" type="text">
                   <button id="tag-submit" class="btn btn-primary">Add Tag</button> 
                </div>
`
            )
            $("#tag-submit").click(()=>{
                let tagInput = $("#tag-type").val();
                tagSelection.push(tagInput);
                $("#tag-choices").empty();
                tagSelection.map(tag => {
                    $("#tag-choices").append(
                        `
                        <li>${tag}</li>
                    `
                    );
                    $("#tag-type").val("");
                })
            })
        })

        $('.user-restaurants').click(function(){
            let restId = $(this).attr("id").substring(1);
            apiShow(restId, "restaurant/show/").then(response => {
                console.log(response);
                $('#show-modal-label').empty().append(`<h5 class="modal-title">${response.name}</h5>`);
                $('#show-modal-review').empty().append(`<a href="/review/${response.id}">Review</a>`)
            });
        });

        const randomizerChoice = size => Math.floor(Math.random() * Math.floor(size));

        const randomizerDelay = ()=> Math.floor(Math.random() * Math.floor(10) + 1) * 75;

        const randomizerLoop = ()=> Math.floor(Math.random() * Math.floor(8) + 12);


        const guestRandomizer = () => {
           let chosenIndex = randomizerChoice(arrayConstructor().length);
            $(`#r${chosenIndex}`).css('background-color', 'cyan');
            return `#r${chosenIndex}`;
        }

        const userRandomizer = () => {
            let chosenIndex = randomizerChoice($('.user-restaurants').length);
            let chosenElement = "";
            $(".user-restaurants").css('background-color', "").each(function(index){
                if (index === chosenIndex){
                    $(this).css('background-color', 'cyan');
                 chosenElement = '#' + $(this).attr('id');
                }
            })
            return chosenElement;
        }

        const guestFinalInterface = confirm => {
            listBasic(arrayConstructor());
            if (confirm) {
                let finalSelection = guestRandomizer();
                $(finalSelection).click();
                $('#guest-random').attr("disabled", false);
            } else {
                guestRandomizer();
            }
        }

        const userFinalInterface = confirm => {
            if (confirm){
                let finalSelection = userRandomizer();
                let chosenRestId = finalSelection.substring(2);
                let rest = {id: chosenRestId};
                // POST REST REQUEST SET UP WITH URL
                // apiCreate(rest, URL).then(()=>{
                //     $(finalSelection).click();
                // })
                // TODO: DELETE SINGLE LINE BELOW ONCE URL IS SET UP ABOVE ON APICREATE
                $(finalSelection).click();
                $('#user-random').attr("disabled", false);
            } else {
                userRandomizer();
            }
        }

        const loopFunc = (limit, loop, user) => {
            if (loop === limit){
                setTimeout(()=>{
              if (user === 'guest'){
                  guestFinalInterface(true);
              } else {
                  userFinalInterface(true);
              }
                }, randomizerDelay());
            } else {
                setTimeout(()=>{
                    if (user === 'guest'){
                        guestFinalInterface(false);
                    } else {
                        userFinalInterface(false);
                    }
                    return loopFunc(limit, loop + 1, user);
                }, randomizerDelay());
            }

        }
        $('#guest-random').click(function(){
            $(this).attr("disabled", true);
            let loopLimit = randomizerLoop();
            loopFunc(loopLimit, 0, 'guest');
        })

        $('#user-random').click(function(){
            $(this).attr("disabled", true);
            let loopLimit = randomizerLoop();
            loopFunc(loopLimit, 0, 'user');
        })

        $('#random-name, #random-name-user').click(function(){
            let nameValue = $('#random-search-input').val();
            let coordInput = JSON.parse(localStorage.getItem("yumCoord"));
            let modalLabel = "#show-modal-label";
            $(modalLabel).empty();

            apiSearch(searchName(nameValue,coordInput.latitude, coordInput.longitude)).then(data => {
                let chosenIndex = randomizerChoice(data.restaurants.length);
                let chosenRestaurant = data.restaurants[chosenIndex].restaurant;
                randomSearchResult = {
                    address: chosenRestaurant.location.address,
                    apiId: chosenRestaurant.id,
                    name: chosenRestaurant.name,
                    website: chosenRestaurant.url,
                    city: chosenRestaurant.location.city,
                    zipcode: chosenRestaurant.location.zipcode
                }
                if($(this).attr("id") === "random-name"){
                    $(modalLabel).append(
                        `
                            <h5 class="modal-title">${randomSearchResult.name}</h5>
                            <a id="add-random-rest" data-bs-dismiss="modal">Add To List</a>
                        `
                    );
                    $("#add-random-rest").click(()=>{
                        updateLocal(chosenRestaurant);
                    })
                } else {
                    $(modalLabel).append(
                        `
                            <h5 class="modal-title">${randomSearchResult.name}</h5>
                            <a id="add-random-restUser">Add To List</a>
                        `
                    );
                    $("#add-random-restUser").click(()=>{
                       updateCurrentList(randomSearchResult);
                    })
                }

            });
        })

        listBasic(arrayConstructor());
        selectEvent(selectRest, "")
        selectEvent(selectRestUser, 'u')

        inputSearchSetup('#nameSearch', "")
        inputSearchSetup('#nameSearchUser','u')

    })
})(jQuery);



