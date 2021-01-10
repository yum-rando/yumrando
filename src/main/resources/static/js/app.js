($ => {
    "use strict"
    $(document).ready(() => {

        // let tagSelection = [];
        let randomSearchResult = {};
        let initialList = [];
        const searchResultBody = "#search-results, #search-results-user";
        const connectErrMessage =
            `
                <div class="alert alert-danger" role="alert">
                    Connection Error...
                </div>
        `

        const searchRandomEvent = () => {
            $('#random-name, #random-name-user').click(function () {
                $("#show-modal-review, #show-modal-body").empty();
                let nameValue = $('#random-search-input').val();
                let coordInput = JSON.parse(localStorage.getItem("yumCoord"));
                let modalLabel = "#show-modal-label";
                $(modalLabel).empty();

                apiSearch(searchName(nameValue, coordInput.latitude, coordInput.longitude)).then(data => {
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
                    if ($(this).attr("id") === "random-name") {
                        $(modalLabel).append(
                            `
                                <h5 class="modal-title">${randomSearchResult.name}</h5>
                                <a id="add-random-rest" data-bs-dismiss="modal">Add To List</a>
                            `
                        );
                        $("#add-random-rest").click(() => {
                            updateLocal(chosenRestaurant);
                        })
                    } else {
                        let addListAnchor = "";
                        if($("#currentList").val() !== 'default') {
                            addListAnchor = `<a id="add-random-restUser">Add To List</a>`;
                        }
                        $(modalLabel).append(
                            `
                            <h5 class="modal-title">${randomSearchResult.name}</h5>
                            ${addListAnchor}
                        `
                        );
                        $("#add-random-restUser").click(() => {
                            updateCurrentList(randomSearchResult);
                        })
                    }

                }).catch(() => {
                    $("#show-modal-body").empty().append(connectErrMessage);
                })
            })
        }

        const geoHandler = ({coords}) => {
            localStorage.setItem("yumCoord", JSON.stringify({latitude: coords.latitude, longitude: coords.longitude}));
            $(".geo-disabled").remove();
            $("#guest-add-buttons").append(
                `
                    <button type="button" class="btn btn-primary activate-search" data-bs-toggle="modal" data-bs-target="#searchModal">
                        Search Restaurant
                    </button>
                    `
            )
            $("#user-add-buttons").append(
                `
                <button type="button" class="btn btn-primary activate-search" data-bs-toggle="modal" data-bs-target="#searchModal">
                    Search Restaurant
                </button>
                `
            )
            $("#guest-random-search").append(
                `
                   <button id="random-name" class="btn btn-primary activate-search register" data-bs-toggle="modal" data-bs-target="#showModal">Presto Yum-o</button>
                `
            )
            $("#user-random-search").append(
                `
                   <button id="random-name-user" class="btn btn-primary activate-search register" data-bs-toggle="modal" data-bs-target="#showModal">Presto Yum-o</button>
                `
            )
            searchRandomEvent();
        }
        const arrayConstructor = () => {
            let listDisplayItems = localStorage.getItem("yumList");
            if (listDisplayItems === null) {
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
            array.map((item, num) => {
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
                $(`#delete${num}`).click(() => {
                    deleteLocal(num)
                });

                $(`#r${num}`).click(() => {
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
        const updateCurrentList = rest => {
            const listNumber = $("#currentList").val();
            const url = `/restaurants/lists/${listNumber}`;
            apiCreate(rest, url).then(() => {
                window.location.assign(`/${listNumber}`)
            }).catch(() => {
                $("#error-message").empty().removeClass("d-none").append(`Connection Error. Could not add restaurant to list.`)
            });
        }

        const obtainRestaurant = num => {
            if (num.includes('u')) {
                let restaurant = resultSet[parseInt(num.substring(1))]
                let cuisines = restaurant.cuisines.split(", ")
                let postObject = {
                    address: restaurant.location.address,
                    apiId: restaurant.id,
                    name: restaurant.name,
                    website: restaurant.url,
                    city: restaurant.location.city,
                    zipcode: restaurant.location.zipcode,
                    tags: cuisines
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

                $(`#${type + num}`).click(() => {
                    obtainRestaurant(type + num);
                });
            });
            if (resultSet.length === 0) {
                $(parent).append(
                    `
                    <div class="container">
                        <div class="row">
                            No Results Found
                        </div>
                    </div>
                    `
                )
            }
        }

        $('#add-basic').click(() => {
            let basicInput = $('#simple-name').val();
            if(basicInput !== "") {
                let objectConvert = {name: basicInput};
                updateLocal(objectConvert);
                listBasic(arrayConstructor());
                // tagSelection = [];
                $("#tag-choices, #tag-addon").empty();
                $("#tag-choice").toggleClass('d-none');
                $("#simple-name, #simple-address, #simple-zipcode").val("");
            }
        })

        const selectRest = '#search-select';
        const selectRestUser = '#search-select-user'
        const modalBody = '#search-body';

        const selectEvent = (selector, type) => {
            $(selector).change(() => {
                $(modalBody).empty();

                $(searchResultBody).empty();
                switch ($(selector).val()) {
                    case "name":

                        if (type === "") {
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
                        }).catch(() => {
                            $(searchResultBody).append(
                                connectErrMessage
                            )
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
            apiSearch(searchName($(selector).val(), coordInput.latitude, coordInput.longitude)).then(data => {
                // Clear loader from $('#search-results) (.empty() works well for that)
                listResult(data.restaurants, type);
            }).catch(() => {
                $(searchResultBody).append(
                    connectErrMessage
                );

            });
        }
        let timer;
        const inputSearchSetup = (selector, type) => {
            $(document).on('keyup', selector, () => {
                if (typeof $(selector).val() !== 'undefined'){
                    clearTimeout(timer);
                   timer = setTimeout(()=> {
                       inputSearch(selector, type);
                   },350);
                }

            })
        }

        $('#new-list').click(() => {
            let userListBody = "#user-list-items, #user-list-initial";
            $(userListBody).addClass('d-none');
            $('#add-list-form').empty().append(
                `<form>
                    <div class="mb-3">
                    <label for="name" class="form-label">Enter a name for your list:</label>
                    <input name="name" type="text" class="form-control" id="name">
                    </div>
                     <button id="submit-list" type="button" class="btn btn-primary">Submit</button>
                     <button id="submit-list-cancel" type="button" class="btn btn-secondary">Cancel</button>
                 </form>
                `
            )
            $('#submit-list').click(() => {

                let listObject = {
                    name: $('#name').val()
                }
                apiCreate(listObject, "/restaurants/lists/create").then(data => {

                    window.location.assign(`/${data.id}`)

                }).catch(() => {
                    $("#add-list-form").empty();
                    $(userListBody).removeClass('d-none');
                    $("#error-message").empty().removeClass("d-none").append(`Error connection. Could not add on new list.`)
                });
            });
            $("#submit-list-cancel").click(()=>{
                $(userListBody).removeClass("d-none");
                $("#add-list-form").empty();
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
                name: $("#simple-name").val().trim(),
                address: $("#simple-address").val().trim(),
                zipcode: $("#simple-zipcode").val().trim()
            }
            updateCurrentList(restaurantName);
        })

//         $("#tag-choice").click(function () {
//             $(this).toggleClass('d-none')
//             $("#tag-addon").append(
//                 `
//                 <div>
//                    <input id="tag-type" type="text">
//                    <button id="tag-submit" class="btn btn-primary">Add Tag</button>
//                 </div>
// `
//             )
//             $("#tag-submit").click(() => {
//                 let tagInput = $("#tag-type").val();
//                 tagSelection.push(tagInput);
//                 $("#tag-choices").empty();
//                 tagSelection.map(tag => {
//                     $("#tag-choices").append(
//                         `
//                         <li>${tag}</li>
//                     `
//                     );
//                     $("#tag-type").val("");
//                 })
//             })
//         })

        $('.user-restaurants').click(function () {
            let restId = $(this).attr("id").substring(1);
            apiShow(restId, "restaurant/show/").then(response => {
                console.log(response);
                let listId = window.location.pathname.substring(1);
                $('#show-modal-label').empty().append(`<h5 class="modal-title">${response.name}</h5>`);
                $('#show-modal-review').empty().append(`<a href="/list/${listId}/restaurant/${response.id}/review?friend=0">Review</a>`);

            }).catch(() => {
                $("#show-modal-label").empty();
                $("#show-modal-review").empty();
                $("#show-modal-body").empty().append(connectErrMessage);
            })
        });



        const randomizerChoice = size => Math.floor(Math.random() * Math.floor(size));

        const randomizerDelay = () => Math.floor(Math.random() * Math.floor(10) + 1) * 75;

        const randomizerLoop = () => Math.floor(Math.random() * Math.floor(8) + 12);

        const userInitialList = () => {
            if (localStorage.getItem("yumCoord") !== null) {
                if (window.location.pathname === "/") {
                    $("#user-add-buttons").addClass('d-none');
                    let coordInput = JSON.parse(localStorage.getItem("yumCoord"));
                    apiSearch(searchLocal(coordInput.latitude, coordInput.longitude)).then(data => {
                        let adjustableArr = data.nearby_restaurants;
                        for (let i = 1; i <= 5; i++) {
                            let chosenIndex = randomizerChoice(adjustableArr.length);
                            initialList.push(adjustableArr[chosenIndex]);
                            adjustableArr = adjustableArr.filter((rest, index) => index !== chosenIndex);
                        }
                        initialList.map(({restaurant}, num) => {
                            $("#user-list-initial").append(
                                `
                            <div class="container">
                                <div class="row">
                                    <div class="user-restaurants" id="r${num}" data-bs-toggle="modal" data-bs-target="#showModal">
                                        <h6>${restaurant.name}</h6>
                                    </div>
                                </div>
                            </div>
                        `
                            );

                            $(`#r${num}`).click(() => {
                                $('#show-modal-label').empty().append(
                                    `
                <h5 class="modal-title">${restaurant.name}</h5>
                `
                                )
                            })
                        })
                    }).catch(() => {
                        console.log("Error connecting to obtain initial list.")
                    })
                }
            }
        }

        const guestRandomizer = () => {
            let chosenIndex = randomizerChoice(arrayConstructor().length);
            $(`#r${chosenIndex}`).css('background-color', 'cyan');
            return `#r${chosenIndex}`;
        }

        const userRandomizer = () => {
            let chosenIndex = randomizerChoice($('.user-restaurants').length);
            let chosenElement = "";
            $(".user-restaurants").css('background-color', "").each(function (index) {
                if (index === chosenIndex) {
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
            if (confirm) {
                let finalSelection = userRandomizer();
                let chosenRestId = finalSelection.substring(2);
                if (window.location.pathname !== "/") {
                    let rest = {id: chosenRestId};
                    const url = `restaurants/reviews`;
                    apiCreate(rest, url).then(() => {
                        $(finalSelection).click();
                    })
                }
                $('#user-random').attr("disabled", false);
            } else {
                userRandomizer();
            }
        }

        const loopFunc = (limit, loop, user) => {
            if (loop === limit) {
                setTimeout(() => {
                    if (user === 'guest') {
                        guestFinalInterface(true);
                    } else {
                        userFinalInterface(true);
                    }
                }, randomizerDelay());
            } else {
                setTimeout(() => {
                    if (user === 'guest') {
                        guestFinalInterface(false);
                    } else {
                        userFinalInterface(false);
                    }
                    return loopFunc(limit, loop + 1, user);
                }, randomizerDelay());
            }
        }

        $('#guest-random').click(function () {
            $(this).attr("disabled", true);
            let loopLimit = randomizerLoop();
            if (arrayConstructor().length <= 2){
                loopLimit = 3;
            }
            loopFunc(loopLimit, 0, 'guest');
        })

        $('#user-random').click(function () {
            $(this).attr("disabled", true);
            let loopLimit = randomizerLoop();
            if($('.user-restaurants').length <=2){
                loopLimit = 3;
            }
            loopFunc(loopLimit, 0, 'user');
        })


        geoLocation(geoHandler);
        listBasic(arrayConstructor());
        selectEvent(selectRest, "");
        selectEvent(selectRestUser, 'u');

        inputSearchSetup('#nameSearch', "");
        inputSearchSetup('#nameSearchUser', 'u');

        userInitialList();
        searchRandomEvent();


    })
})(jQuery);



