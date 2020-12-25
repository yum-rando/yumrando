($ => {
    "use strict"

    $('#add-list').click(()=>{
        $('.list-items').addClass('d-none');
        $('#list-form').empty().append(
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
                window.location.assign('/profile')
            }).catch(()=>{
                console.log("We are not champions : (")
            });
        })
    })

    $('#edit-list').click(()=> {
        $('#list-form').empty();
        $('.list-items').removeClass('d-none');
        $('.delete-list, .icon-container').toggleClass('d-none');
    })

    $('.icon-container').click(function(){
        let listName = $(this).attr('data-name');
        let listId = $(this).attr('data-id');
        $('.list-items').addClass('d-none');
        $('#list-form').empty().append(
            `<form>
                    <div class="mb-3">
                    <label for="name" class="form-label">Edit the name of your list:</label>
                    <input name="name" value="${listName}" type="text" class="form-control" id="nameEdit">
                    </div>
                     <button id="submit-listEdit" type="button" class="btn btn-primary">Submit</button>
                 </form>
                `
        )
        $('#submit-listEdit').click(()=>{

            let listObject = {
                name: $('#nameEdit').val()
            }
            apiEdit(listObject,"URL GOES HERE").then(data=>{
                window.location.assign('/profile')
            }).catch(()=>{
                console.log("We are not champions : (")
            });
        })

    })




})(jQuery);