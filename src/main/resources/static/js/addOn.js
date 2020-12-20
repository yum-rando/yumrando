($ => {
    $(document).ready(()=>{

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
                      <div class="col-9">
                      <h6 id="${item.name}">${item.name}</h6>
                      </div>
                     <div class="col-3">
                     <button id="delete${num}" type="button" class="btn btn-danger">-</button>
                    </div>
                      </div>
                        </div>`
                );
                $(`#delete${num}`).click(()=>{
                    deleteLocal(num)
                })
            })

        }

        const updateLocal = object => {
            localStorage.setItem("yumList", JSON.stringify(
                [...arrayConstructor(), object]
            ))
            listBasic(arrayConstructor());
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
                const listNumber = $("#currentList").val();
                const url = `/restaurants/lists/${listNumber}`;
                apiAddList(postObject, url).then(() =>{window.location.assign(`/${listNumber}`)}).catch(()=>{console.error("Nope!")});
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
        // $(document).on('change', '#nameSearch',()=>{
        //     if(typeof $('#nameSearch').val() !== 'undefined')
        //         inputSearch("");
        // })

        $('#new-list').click(()=>{
            $('#user-list-items').toggleClass('d-none');
            $('#add-list-form').append(
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
                apiAddList(listObject, "/restaurants/lists/create").then(data=>{
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
            const listNumber = $("#currentList").val();
            const url = `/restaurants/lists/${listNumber}`;
            console.log(url);
            apiAddList(restaurantName, url).then(()=>{window.location.assign(`/${listNumber}`)}).catch(()=>{console.error("Nope!")});

        })

        listBasic(arrayConstructor());
        selectEvent(selectRest, "")
        selectEvent(selectRestUser, 'u')
        inputSearchSetup('#nameSearch', "")
        inputSearchSetup('#nameSearchUser','u')
    })
})(jQuery);



