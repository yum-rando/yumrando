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

        const arrayConstructor = () => {
            let listDisplay= localStorage.getItem("yumList");
            if (listDisplay === null){
                return [];
            } else {
               return JSON.parse(listDisplay);
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

        $('#search-basic').click(()=>{
            let coordInput = JSON.parse(localStorage.getItem("yumCoord"));
            apiSearchLocal(coordInput.latitude, coordInput.longitude)
        })

    listBasic(arrayConstructor());

    })
})(jQuery);