(function ($) {
    "use strict";

    function showStatic() {
        $(".dynamicBehavior").hide();
        $(".staticBehavior").show();
    }

    function showDynamic() {
        $(".staticBehavior").hide();
        $(".dynamicBehavior").show();
    }

    function checkMultifield() {
        if($(".js-coral-Multifield-list").children().length >= $("[name='./number']").prop("value")) {
            $(".js-coral-Multifield-add").prop("disabled", true);
        } else {
			$(".js-coral-Multifield-add").prop("disabled", false);
        }
    }

    function hideAll() {
        $(".staticBehavior").hide();
		$(".dynamicBehavior").hide();
    }

    $(document).on("dialog-ready", function () {

		hideAll();

        $(".staticBehavior").click(function () {
            checkMultifield();
        });

        if($(".static").attr("checked")) {
            showStatic();
        }
		if($(".dynamic").attr("checked")) {
            showDynamic();
        }
        $(".static").click(function () {
            showStatic();
        });
        $(".dynamic").click(function () {
            showDynamic();
        });
    });

})($);