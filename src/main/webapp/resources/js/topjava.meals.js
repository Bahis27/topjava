$(function () {
    makeEditable({
        ajaxUrl: "ajax/profile/meals/",
        datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    0,
                    "asc"
                ]
            }
        )
    });
});

function setFilter() {
    $.ajax({
        type: "POST",
        url: context.ajaxUrl + "filter",
        data: $("#filtersForm").serialize()
    }).done(function () {
        successNoty("Filtered");
    });
}

function clearFilter() {
    $('#filtersForm').find(":input").val("");
    successNoty("Unfiltered");
}