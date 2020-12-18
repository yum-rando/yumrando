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



        const obtainRestaurant = num => updateLocal(resultSet[num]);

        const listResult = array => {
            let parent = $('#search-results');
            parent.empty();
            resultSet = [];
            array.map(({restaurant}, num) => {
                resultSet.push(restaurant);
                let newId = num;
                $(parent).append(
                    `<div class="container">
                        <div class="row">
                            <div class="col-9">
                                <h5>${restaurant.name}</h5>
                                <p>${restaurant.location.address}</p>
                             </div>
                             <div class="col-3">
                                <div sec:authorize = "isAnonymous()">
                                 <button id="${num}" type="button" class="btn btn-primary" data-bs-dismiss="modal">Add to List</button>
                                </div>
                             </div>
                         </div>
                    </div>
                        `
                );
                $(`#${newId}`).click(()=> {
                    obtainRestaurant(newId);
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

        const modalBody = '#search-body';

        $(selectRest).change(()=>{
            $(modalBody).empty();
            $('#search-results').empty();
            switch ($(selectRest).val()){
                case "name":
                    $(modalBody).append(`<input placeholder="Search by Name" id="nameSearch"/>`)
                    break;
                case "near":
                    // Attach loader to $('#search-results')
                    let coordInput = JSON.parse(localStorage.getItem("yumCoord"));
                    apiSearch(searchLocal(coordInput.latitude, coordInput.longitude)).then(data => {
                        // Clear loader from $('#search-results) (.empty() works well for that)
                        listResult(data.nearby_restaurants)
                    });
                    break;
                default:
                    return;
            }
        })

        const inputSearch = () => {
            // Attach loader to $('#search-results')
            let coordInput = JSON.parse(localStorage.getItem("yumCoord"));
            apiSearch(searchName($('#nameSearch').val(), coordInput.latitude, coordInput.longitude)).then(data=> {
                // Clear loader from $('#search-results) (.empty() works well for that)
                listResult(data.restaurants);
            });


        }

        $(document).on('change', '#nameSearch',()=>{
            if(typeof $('#nameSearch').val() !== 'undefined')
                inputSearch();
        })

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
                    console.log(data);
                }).catch(()=>{
                    console.log("We are not champions : (")
                });
            })
        })

        $("#currentList").change(() => {
            console.log($("#currentList").val())
        })

        $("#add-basic-user").click(() => {
            const restaurantName = {
                name: $("#simple-name").val()
            }
            const listNumber = $("#currentList").val();
            const url = `/restaurants/lists/${listNumber}`;
            apiAddList(restaurantName, url).then(data=>{console.log(data)}).catch(()=>{console.error("Nope!")});

        })

        listBasic(arrayConstructor());
    })
})(jQuery);



