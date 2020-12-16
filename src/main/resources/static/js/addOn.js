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
                                 <button id="${num}" type="button" class="btn btn-primary" data-bs-dismiss="modal">Add to List</button>
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
                    let coordInput = JSON.parse(localStorage.getItem("yumCoord"));
                    apiSearch(searchLocal(coordInput.latitude, coordInput.longitude)).then(data => {
                        listResult(data.nearby_restaurants)
                    });
                    break;
                default:
                    return;
            }
        })

        const inputSearch = () => {
            let coordInput = JSON.parse(localStorage.getItem("yumCoord"));
            apiSearch(searchName($('#nameSearch').val(), coordInput.latitude, coordInput.longitude)).then(data=> {
                listResult(data.restaurants);
            });


        }

        $(document).on('change', '#nameSearch',()=>{
            if(typeof $('#nameSearch').val() !== 'undefined')
                inputSearch();
        })
        listBasic(arrayConstructor());
    })
})(jQuery);



