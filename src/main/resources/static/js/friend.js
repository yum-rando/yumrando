($ => {
    "use strict"
    $(document).ready(() => {
        // A Select that changes the list view for user
        $("#currentList").change(() => {
            const listNum = $("#currentList").val()
            const friendId = $("#friend-label").attr('data-id');
            window.location.assign(`/friend/${friendId}/list/${listNum}`);
        })

        $('.user-restaurants').click(function () {
            let restId = $(this).attr("id").substring(1);
            apiShow(restId, "restaurant/show/").then(response => {
                console.log(response);
                $('#show-modal-label').empty().append(`<h5 class="modal-title">${response.name}</h5>`);
                $('#show-modal-review').empty().append(`<a href="/review/${response.id}">Review</a>`)
            });
        });

    })
})(jQuery);



