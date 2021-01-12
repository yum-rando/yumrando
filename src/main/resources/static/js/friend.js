($ => {
    "use strict"
    $(document).ready(() => {
        const modalBody = "#show-modal-body";
        const modalLabel = "#show-modal-label";
        const connectErrMessage =
            `
                <div class="alert alert-danger" role="alert">
                    Connection Error...
                </div>
        `

        // A Select that changes the list view for user
        $("#currentList").change(() => {
            const listNum = $("#currentList").val()
            const friendId = $("#friend-label").attr('data-id');
            window.location.assign(`/friend/${friendId}/list/${listNum}`);
        })

        $('.user-restaurants').click(function () {
            let restId = $(this).attr("id").substring(1);
            const friendId = $("#friend-label").attr('data-id');
            let listId = $("#currentList").val();
            $(modalLabel).empty();
            $(modalBody).append(loader());
            apiShow(restId, "/restaurant/show/").then(response => {
                console.log(response);
                $(modalLabel).empty().append(`<h5 class="modal-title">${response.name}</h5>`);
                $('#show-modal-review').empty().append(`<a href="/list/${listId}/restaurant/${response.id}/review?friend=${friendId}">Review</a>`);
                $(modalBody).empty();
            }).catch(()=>{
                $(modalLabel).empty();
                $("#show-modal-review").empty();
                $(modalBody).empty().append(connectErrMessage);
            })
        });

    })
})(jQuery);



