($ => {
    "use strict"
    $(document).ready(() => {
        const modalBody = "#show-modal-body";
        const modalLabel = "#show-modal-label";
        const imgCol = "#show-img-col";
        const modBodyCont = "#modal-body-container";
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

        const nullWebCheck = response =>{
            if(typeof response === "string" && response !== ""){
                return  `<div class="col-12">
                    <p>
                    <a class="modal-address" href="${response}" target="_blank">More Info</a>
                </p>
                </div>`
            }
            return "";
        }

        $('.user-restaurants').click(function () {
            let restId = $(this).attr("id").substring(1);
            const friendId = $("#friend-label").attr('data-id');
            let listId = $("#currentList").val();
            $(imgCol).addClass("d-none");
            $(modBodyCont).append(loader());
            $("#show-modal-review, #show-modal-body, #show-modal-label").empty();
            apiShow(restId, "/restaurant/show/").then(response => {
                $("#loader").remove();
                $(modalLabel).append(`<h5 class="modal-title">${response.name}</h5>`);
                $(modalBody).append(
                    `
                           <div class="col-12">
                               <p class="modal-address">${response.address}</p>
                           </div>
                               ${nullWebCheck(response.website)}
                            <div class="col-12">
                                <p>
                                    <a class="modal-address" href="/list/${listId}/restaurant/${response.id}/review?friend=${friendId}">Review</a>
                                </p>
                            </div>
                            `
                )
                $(imgCol).removeClass("d-none");
            }).catch(()=>{
                $(modalLabel).empty();
                $("#show-modal-review").empty();
                $(modalBody).empty().append(connectErrMessage);
            })
        });

    })
})(jQuery);



