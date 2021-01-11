($ => {
    "use strict"
    let userTag = []

    $('#add-list').click(()=>{
        $('.list-items').addClass('d-none');
        $('#list-form').empty().append(
            `<form>
                    <div class="mb-3">
                    <label for="name" class="form-label">Enter a name for your list:</label>
                    <input name="name" type="text" class="form-control deny-submit" id="name">
                    </div>
                     <button id="submit-list" type="button" class="btn btn-primary">Submit</button>
                     <button id="submit-list-cancel" type="button" class="btn btn-secondary">Cancel</button>
                 </form>
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
                    <input name="name" value="${listName}" type="text" class="form-control deny-submit" id="nameEdit">
                    </div>
                     <button id="submit-listEdit" type="button" class="btn btn-primary">Submit</button>
                 </form>
                `
        )
        cancelInputSubmit();
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
            `<form>
                    <div class="mb-3">
                    <label for="name" class="form-label">Type the username you would like to send a friend request:</label>
                    <input name="name" type="text" class="form-control deny-submit" id="friend-username">
                    <div class="invalid-feedback">
                    Invalid username entered.
                    </div>
                    </div>
                     <button id="submit-friend-request" type="button" class="btn btn-primary">Submit</button>
                     <button id="submit-friend-request-cancel" type="button" class="btn btn-secondary">Cancel</button>
                 </form>
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
        let inputValue = $(this).val()
        let tagList = filterArray.filter(({name}) => name.includes(inputValue))
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

<<<<<<< HEAD
let filterArray = []
  const cancelInputSubmit = ()=>{
      $('.deny-submit:not([type="submit"])').keydown(e => {
          if (e.keyCode == 13) {
              e.preventDefault();
              return false;
          }
      });
  }
=======

let filterArray = []
>>>>>>> a5a7bf363dd32925f0bd58eda80c73dd8d5cabe0



    $("#edit-tag-list").click(() => {
        apiTagSearch("/tags").then(data=>{
            $("#check-tags").empty()
            filterArray = [...data]
            filterArray.map(faveTags =>{
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
    })
apiTagSearch("/tags").then((data)=>{console.log(data)})


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
        apiCreate(userTag, url).then(data =>{
            window.location.assign("/profile");
        })
    })

<<<<<<< HEAD
=======

  const cancelInputSubmit = ()=>{
      $('.deny-submit:not([type="submit"])').keydown(e => {
          if (e.keyCode == 13) {
              e.preventDefault();
              return false;
          }
      });
  }

>>>>>>> a5a7bf363dd32925f0bd58eda80c73dd8d5cabe0


})(jQuery);