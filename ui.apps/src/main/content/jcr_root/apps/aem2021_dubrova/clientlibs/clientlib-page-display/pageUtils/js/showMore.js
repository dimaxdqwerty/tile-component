(function () {
    "use strict";

    let hidden = 0;

    function showItems() {
        for (let i = 0; i < hidden+3; i++) {
           $(".staticComponent").children().eq(i).hide();
        }
        hidden += 3;
        for (let i = hidden; i < hidden+3; i++) {
            $(".staticComponent").children().eq(i).show();
        }
    }

    function hideExtraItems() {
    	for (let i = 3; i < $(".staticComponent").children().length; i++) {
        	$(".staticComponent").children().eq(i).hide();
        }
    }

    $(document).ready(function () {
        if ($(".staticComponent").children().length < 3) {
			$('#button').hide();
        } else {
            hideExtraItems();
        }
        let n = 0;
        $("#button").on("click", function () {
            n += 3;
            showItems();
            if (($(".staticComponent").children().length - n) <= 3) {
				$('#button').hide();
            }
        });
    });

})();