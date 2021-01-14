($ => {
    "use strict";

    $('#review-comment-edit').click(() => {
        $('#add-photo').toggleClass('d-none');
        $('#exampleFormControlTextarea1').attr("disabled", false);
    })
    $('#photo-edit').click(() => {
        $('.delete-photo-list').toggleClass('d-none');
    })

})(jQuery)