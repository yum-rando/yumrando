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
        `;
        const modalBody = "#show-modal-body";
        const userListInitial = "#user-list-initial";
        const addList = "#add-list-option";
        const imgCol = "#show-img-col";
        const modBodyCont = "#modal-body-container";
        const modalLabel = "#show-modal-label";
        const currList = "#currentList";

        const searchRandomEvent = () => {
            $('#random-name, #random-name-user').click(function () {
                $("#show-modal-review, #show-modal-body").empty();
                $(imgCol).addClass("d-none");
                $(modBodyCont).append(loader());
                let nameValue = $('#random-search-input').val();
                let coordInput = JSON.parse(localStorage.getItem("yumCoord"));


                $(modalLabel + ", " + addList).empty();

                apiSearch(searchName(nameValue, coordInput.latitude, coordInput.longitude)).then(data => {
                    let chosenIndex = randomizerChoice(data.restaurants.length);
                    let chosenRestaurant = data.restaurants[chosenIndex].restaurant;
                    randomSearchResult = {
                        address: chosenRestaurant.location.address,
                        apiId: chosenRestaurant.id,
                        name: chosenRestaurant.name,
                        website: chosenRestaurant.url,
                        city: chosenRestaurant.location.city,
                        zipcode: chosenRestaurant.location.zipcode,
                    }

                        $("#loader").remove();
                        $(modalLabel).empty().append(`<h5 class="modal-title">${randomSearchResult.name}</h5>`);
                    if ($(this).attr("id") === "random-name") {
                        $(addList).empty().append(`<h5 id="add-random-rest" data-bs-dismiss="modal" data-bs-toggle="tooltip" data-bs-placement="left" title="Add To List" class="modal-add py-3">+</h5>`);
                        $("#add-random-rest").click(() => {
                            updateLocal(chosenRestaurant);
                        })
                    } else {
                        if ($("#currentList").val() !== 'default') {
                            $(addList).empty().append(`<h5 id="add-random-restUser" data-bs-toggle="tooltip" data-bs-placement="left" title="Add To List" class="modal-add py-3">+</h5>`);
                            $("#add-random-restUser").click(() => {
                                updateCurrentList(randomSearchResult);
                            })
                        }
                    }
                    $(modalBody).empty().append(
                        `
                           <div class="col-12">
                               <p class="modal-address">${randomSearchResult.address}</p>
                           </div>
                            <div class="col-12">
                                <p>
                                    <a class="modal-address" href="${randomSearchResult.website}" target="_blank">More Info</a>
                                </p>
                            </div>
                            `
                        );
                        $(imgCol).removeClass("d-none");
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
                    <button type="button" class="btn activate-search login btn-white" data-bs-toggle="modal" data-bs-target="#searchModal">
                        <i class="fas fa-search"></i>
                    </button>
                    `
            )
            $("#user-add-buttons").append(
                `
                <button type="button" class="btn activate-search btn-white login" data-bs-toggle="modal" data-bs-target="#searchModal">
                    <i class="fas fa-search"></i>
                </button>
                `
            )
            $("#guest-random-search").append(
                `
                   <button id="random-name" class="btn activate-search btn-pink" data-bs-toggle="modal" data-bs-target="#showModal">Presto Yum-o</button>
                `
            )
            $("#user-random-search").append(
                `
                   <button id="random-name-user" class="btn activate-search btn-pink" data-bs-toggle="modal" data-bs-target="#showModal">Presto Yum-o</button>
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
                      <div class="container search-show ">
                      <div class="row">
                      <div id="r${num}" class="col-9 search-name" data-bs-toggle="modal" data-bs-target="#showModal">
                      <h6 >${item.name}</h6>
                      </div>
                     <div class="col-3">
                     <button id="delete${num}" type="button" class="btn btn-pink">
                     <i class="fas fa-minus"></i>
</button>
                    </div>
                      </div>
                        </div>`
                );
                $(`#delete${num}`).click(() => {
                    deleteLocal(num)
                });

                $(`#r${num}`).click(() => {
                    $("#show-modal-review, #show-modal-body, #show-modal-label, #add-list-option").empty();
                    $(modalLabel).append(`<h5 class="modal-title">${item.name}</h5>`);
                    $(modalBody).append(
                        `
                           <div class="col-12">
                               <p class="modal-address">${item.location.address}</p>
                           </div>
                            <div class="col-12">
                                ${nullWebCheck(item.url)}
                            </div>
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

        $("#yummies").click(()=>{
            const listNumber = $("#currentList").val();
            const url= `/${listNumber}/filter`;
            window.location.assign(url);
        })

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
                    tags: cuisines,
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
                    `
                        <div class="row align-items-center my-3 py-2">
                            <div class="col-8">
                                <div class="row">
                                    <h5 class="modal-address green-secondary">${restaurant.name}</h5>
                                </div>
                                <div class="row">
                                    <p class="modal-address green-secondary">${restaurant.location.address}</p>
                                </div>
                             </div>
                             <div class="col-4 d-flex align-items-center">
                                 <button id="${type + num}" type="button" class="btn btn-green green-secondary" data-bs-dismiss="modal">Add to List</button> 
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
                    <div class="container d-flex justify-content-center align-items-center">
                        <div class="row">
                            <h4 class="modal-address">No Search Results Found</h4>
                        </div>
                    </div>
                    `
                )
            }
        }

        $('#add-basic').click(() => {
            let basicInput = $('#simple-name').val();
            let basicAddress = $("#simple-address").val();
            let basicZipcode = $("#simple-zipcode").val();
            if(basicInput !== "") {
                let objectConvert = {
                    name: basicInput,
                    location: {
                        address: basicAddress,
                        zipcode: basicZipcode
                    }
                };
                updateLocal(objectConvert);
                listBasic(arrayConstructor());
                $("#tag-choices, #tag-addon").empty();
                $("#tag-choice").toggleClass('d-none');
                $("#simple-name, #simple-address, #simple-zipcode").val("");
            }
        })

        const selectRest = '#search-select';
        const selectRestUser = '#search-select-user'
        const modalSearchBody = '#search-body';

        const selectEvent = (selector, type) => {
            const inputStructure = id => {
                return`
                <div class="row g-2 align-items-center my-3">
                    <div class="col-12">
                        <input class="user-info-input form-control" placeholder="Type to search for restaurants." id="${id}"/>
                    </div>
                </div>
                       `

            }
            $(selector).change(() => {
                $(modalSearchBody).empty();
                $(searchResultBody).empty();
                switch ($(selector).val()) {
                    case "name":
                        if (type === "") {
                            $(modalSearchBody).append(inputStructure("nameSearch"));
                        } else {
                            $(modalSearchBody).append(inputStructure("nameSearchUser"));
                        }
                        break;
                    case "near":
                        $(searchResultBody).append(loader());
                        let coordInput = JSON.parse(localStorage.getItem("yumCoord"));
                        apiSearch(searchLocal(coordInput.latitude, coordInput.longitude)).then(data => {
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
            $(searchResultBody).empty().append(loader());
            let coordInput = JSON.parse(localStorage.getItem("yumCoord"));
            apiSearch(searchName($(selector).val(), coordInput.latitude, coordInput.longitude)).then(data => {
                listResult(data.restaurants, type);
            }).catch(() => {
                $(searchResultBody).append(
                    connectErrMessage
                );

            });
        }

        let timer;

        const inputSearchSetup = (selector, type) => {
            $(document).on('keyup', selector, (e) => {
                e.preventDefault();
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
                    <input name="name" type="text" class="form-control deny-submit" id="name">
                    </div>
                     <button id="submit-list" type="button" class="btn btn-green">Submit</button>
                     <button id="submit-list-cancel" type="button" class="btn btn-red">Cancel</button>
                 </form>
                `
            )

            $('.deny-submit:not([type="submit"])').keydown(e => {
                if (e.keyCode == 13) {
                    e.preventDefault();
                    return false;
                }
            });

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

        const nullWebCheck = response =>{
            if(typeof response === "string" && response !== ""){
                return  `<div class="col-12">
                    <p>
                    <a class="modal-address" href="${response}" target="_blank">More Info</a>
                </p>
                </div>`
            }
            return "";
        }

        $('.user-restaurants').click(function (e) {
            e.stopPropagation();
            let restId = $(this).attr("id").substring(1);
            let listId = $("#currentList").val();
            $("#show-modal-review, #show-modal-body, #show-modal-label, #add-list-option").empty();
            $(imgCol).addClass("d-none");
            $(modBodyCont).append(loader());
            apiShow(restId, "/restaurant/show/").then(response => {
                $("#loader").remove();
                $(modalLabel).append(`<h5 class="modal-title">${response.name}</h5>`);
                $(modalBody).append(
                    `
                           <div class="col-12">
                               <p class="modal-address">${response.address}</p>
                           </div>
                           ${nullWebCheck(response.website)}
                            <div class="col-12">
                                <p>
                                    <a class="modal-address" href="/list/${listId}/restaurant/${response.id}/review?friend=0">Review</a>
                                </p>
                            </div>
                            `
                )


                $(imgCol).removeClass("d-none");
            }).catch(() => {
                $("#show-modal-label").empty();
                $("#show-modal-review").empty();
                $(modalBody).empty().append(connectErrMessage);
            })
        });



        const randomizerChoice = size => Math.floor(Math.random() * Math.floor(size));

        const randomizerDelay = () => Math.floor(Math.random() * Math.floor(10) + 1) * 75;

        const randomizerLoop = () => Math.floor(Math.random() * Math.floor(8) + 12);

        const userInitialList = () => {
            if (localStorage.getItem("yumCoord") !== null) {
                if ($(currList).length > 0 && ($(currList).val() === "default")) {
                    $("#user-add-buttons, #yummies").addClass('d-none');
                    $(userListInitial).empty().append(loader());
                    let coordInput = JSON.parse(localStorage.getItem("yumCoord"));
                    apiSearch(searchLocal(coordInput.latitude, coordInput.longitude)).then(data => {
                        let adjustableArr = data.nearby_restaurants;
                        for (let i = 1; i <= 5; i++) {
                            let chosenIndex = randomizerChoice(adjustableArr.length);
                            initialList.push(adjustableArr[chosenIndex]);
                            adjustableArr = adjustableArr.filter((rest, index) => index !== chosenIndex);
                        }
                        $(userListInitial).empty();
                        initialList.map(({restaurant}, num) => {
                            $(userListInitial).append(
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
                                $("#show-modal-review, #show-modal-body, #show-modal-label, #add-list-option").empty();

                                $(modalLabel).append(`<h5 class="modal-title">${restaurant.name}</h5>`);
                                $(modalBody).append(
                                    `
                                        <div class="col-12">
                                            <p class="modal-address">${restaurant.location.address}</p>
                                        </div>
                                        <div class="col-12">
                                            <p>
                                                <a class="modal-address" href="${restaurant.url}" target="_blank">More Info</a>
                                            </p>
                                        </div>
                                    `
                                );
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
                } else{
                    $(finalSelection).click();
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
        const preventInputDef = ()=> {
            $(document).on("keyup", ".submit-need", e => {
                e.preventDefault();
            })
        }

        geoLocation(geoHandler);
        if($(currList).length === 0) {
            listBasic(arrayConstructor());
        }
        selectEvent(selectRest, "");
        selectEvent(selectRestUser, 'u');

        inputSearchSetup('#nameSearch', "");
        inputSearchSetup('#nameSearchUser', 'u');

        userInitialList();
        searchRandomEvent();
        preventInputDef();

    })
})(jQuery);



