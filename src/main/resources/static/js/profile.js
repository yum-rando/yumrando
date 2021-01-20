($ => {
    "use strict"
    let userTag = []

    $('#add-list').click(()=>{
        $('.list-items').addClass('d-none');
        $('#list-form').empty().append(
                `
               <div class="container py-2">
                <form>
                    <div class="mb-3">
                    <label  for="name" class="form-label navy label-font">Enter a name for your list:</label>
                    <input name="name" type="text" class="form-control deny-submit" id="name">
                    </div>
                     <button id="submit-list" type="button" class="btn btn-white">Submit</button>
                     <button id="submit-list-cancel" type="button" class="btn btn-pink">Cancel</button>
                 </form>
                </div>
                `
        )
        cancelInputSubmit();
        $('#submit-list').click(()=>{

            let listObject = {
                name: $('#name').val()
            }
            apiCreate(listObject, "/restaurants/lists/create").then(()=>{
                window.location.assign('/profile')
            }).catch(()=>{
                $("#list-form").empty();
                $(".list-items").removeClass('d-none');
                $("#error-message").empty().removeClass("d-none").append(`Connection Error. Could not add on new list.`)
            });
        });

        $("#submit-list-cancel").click(()=>{
            $(".list-items").removeClass("d-none");
            $("#list-form").empty();
        })
    })

        $("#edit-list").click(() => {
            $('#list-form').empty();
            $('.list-items').removeClass('d-none');
            $('.delete-list, .icon-container').toggleClass('d-none');
        })

    $('.icon-container').click(function(){
        let listName = $(this).attr('data-name');
        let listId = $(this).attr('data-id');
        $('.list-items').addClass('d-none');
        $('#list-form').empty().append(
            `
            <div class="container py-2">
                <form>
                    <div class="mb-3">
                    <label for="name" class="form-label navy label-font">Edit the name of your list:</label>
                    <input name="name" value="${listName}" type="text" class="form-control deny-submit" id="nameEdit">
                    </div>
                     <button id="submit-listEdit" type="button" class="btn login">Submit</button>
                     <button id="submit-listEdit-cancel" type="button" class="btn btn-pink">Cancel</button>
                 </form>
            </div>
                 
                `
        )
        cancelInputSubmit();

        $("#submit-listEdit-cancel").click(()=>{
            $(".list-items").removeClass("d-none");
            $("#list-form").empty();
            $(".icon-container, .delete-list").addClass("d-none");
        })

        $('#submit-listEdit').click(()=>{

            let listObject = {
                name: $('#nameEdit').val()
            }
            apiEdit(listObject,`/lists/${listId}/edit`).then(()=>{
                window.location.assign('/profile')
            }).catch(()=>{
                $("#list-form").empty();
                $(".list-items").removeClass('d-none');
                $("#error-message").empty().removeClass("d-none").append(`Connection Error. Could not edit list.`)
            });
        })
    })

    $('#toggle-user-info').click(function(){
        $(this).toggleClass('d-none');
        $('.user-info-input').attr("disabled", false);
        $('#user-info-submit').removeClass('d-none');
    })
    $("#add-friend").click(()=> {
        $('#friend-list').addClass('d-none');
        $('#friend-form').empty().append(
            `
                <div class="container py-2">
                    <form>
                        <div class="mb-3">
                        <label for="name" class="form-label navy label-font">Type the username you would like to send a friend request:</label>
                        <input name="name" type="text" class="form-control deny-submit" id="friend-username">
                        <div class="invalid-feedback">
                            Invalid username entered.
                        </div>
                        </div>
                        <button id="submit-friend-request" type="button" class="btn login">Submit</button>
                        <button id="submit-friend-request-cancel" type="button" class="btn btn-pink">Cancel</button>
                    </form>
                </div>    
                `
        )
        cancelInputSubmit();
        $("#submit-friend-request").click(()=>{
            let inputValue = $("#friend-username").val()
            let friend = {username: inputValue }
            const url = "/profile/friends/create"
            apiCreate(friend, url)
                .then(()=>{
                    setTimeout(()=>{
                        window.location.assign('/profile')}, 750)
                    })
                .catch(()=> {
                $("#friend-username").addClass("is-invalid");
            })
        });
        $("#submit-friend-request-cancel").click(()=>{
            $("#friend-list").removeClass("d-none");
            $("#friend-form").empty();
        })
    });

    $("#tag-search").keyup(function() {
        let inputValue = $(this).val().toLowerCase();
        let tagList = filterArray.filter(({name}) => name.toLowerCase().includes(inputValue))
        $("#check-tags").empty()
        tagList.map(faveTags =>{
            $("#check-tags").append(
                `
                    <div class="col-3">
                        <input class="form-check-input tag-form-input" type="checkbox" value="" id="flexCheckDefault" data-tagID="${faveTags.id}" data-tagName="${faveTags.name}">
                        <label class="form-check-label" for="flexCheckDefault">
                            ${faveTags.name}
                         </label>
                    </div>
                    `
            )
        })
    })

    $(".friend-view").click(function(){
        let friendId = $(this).attr("id").substring(1);
        let url = `/friend/${friendId}`;
        window.location.assign(url);
    })

    let filterArray = [];
    let checkBody = "#check-tags";

    $("#edit-tag-list").click(() => {
        $(".tag-modal-info").addClass("d-none");
        $(checkBody).empty().append(loader());
        apiTagSearch("/tags").then(data=>{
            $(".tag-modal-info").removeClass("d-none");
            $(checkBody).empty();
            filterArray = [...data]
            filterArray.map(faveTags =>{
                $(checkBody).append(
                    `
                    <div class="col-sm-6 col-lg-4">
                        <input class="tag-form-input modal-address" type="checkbox" value="" id="flexCheckDefault" data-tagID="${faveTags.id}" data-tagName="${faveTags.name}">
                        <label class="form-check-label modal-address" for="flexCheckDefault">
                            ${faveTags.name}
                         </label>
                    </div>
                    `
                )
            })
        })
    })

    $("#save-tags").click(() => {
        $(".tag-form-input").each(function(){
            if($(this).prop("checked") === true){
                let tagID=$(this).attr("data-tagID")
                let tagName=$(this).attr("data-tagName")
                userTag.push(
                    {id:tagID, name:tagName}
                )
            }
        })
        const url= "/tags"
        apiCreate(userTag, url).then(()=>{
            window.location.assign("/profile");
        })
    })

  const cancelInputSubmit = ()=>{
      $('.deny-submit:not([type="submit"])').keydown(e => {
          if (e.keyCode == 13) {
              e.preventDefault();
              return false;
          }
      });
  }

})(jQuery);