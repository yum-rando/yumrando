($ => {
    $(document).ready(()=>{

        const listBasic = array => {
           let parent = $('#restaurant-items');
           parent.empty();
           array.map(item =>{
               $(parent).append(
                   `<h6 id="${item.name}">${item.name}</h6>`
               );
           })
       }

       const listResult = array => {
            let parent = $('#search-results');
            parent.empty();
            array.map(item => {
                $(parent).append(
                    `<div>
                        <h5>${item.name}</h5>
                        <p>${item.location.address}</p>
                        </div>
                        `
                )
            });
       }

        const arrayConstructor = () => {
            let listDisplayItems = localStorage.getItem("yumList");
            if (listDisplayItems === null){
                return [];
            } else {
               return JSON.parse(listDisplayItems);
            }
                }

       $('#add-basic').click(()=>{
           let basicInput = $('#simple-name');
        localStorage.setItem("yumList", JSON.stringify(
            [...arrayConstructor(), {name: basicInput.val()}]
        ))
         basicInput.val("");
        listBasic(arrayConstructor());
        })

        const selectRest = '#search-select';
        const modalBody = '#search-body';

        $(selectRest).change(()=>{
            $(modalBody).empty();
           switch ($(selectRest).val()){
               case "name":
                   $(modalBody).append(`<input placeholder="Search by Name" id="nameSearch"/>`)
                   break;
               default:
                   return;
           }
        })
        let delay = 3000;
        const inputSearch = () => {
            let coordInput = JSON.parse(localStorage.getItem("yumCoord"));
            apiSearch(searchName($('#nameSearch').val(), coordInput.latitude, coordInput.longitude));
            setInterval(()=>{
                if(data.info ===)
            })
                console.log(data.info);
               listResult(data.info);


        }

        $(document).on('keyup', '#nameSearch',()=>{
            setTimeout(()=>{}, delay)
            inputSearch();
        })
    listBasic(arrayConstructor());
    })
})(jQuery);