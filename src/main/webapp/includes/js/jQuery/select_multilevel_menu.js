/*
 * Support functions for multilevel menus
 * 
 */

var selectedId;
var clickedButtonId;

var map;

// build a map with key / pairs {id:text} from the list items
function asMap(menuSelector) {
	var myMap = new Object();
	$(menuSelector).each(function(index) {
		myMap[$(this).attr("id")] = $(this).text();
	});
	myMap[""] = "-?-"; // return empty text for undefined
	return myMap;
}

// issue selection button clicked
// open the flyout for selection and move it into position
function selectIssue(event) {
	event.preventDefault();		// skip the anchor
	event.stopImmediatePropagation();	// important for chrome and safari
	clickedButtonId = $(this).attr("id");
	var idx = clickedButtonId.lastIndexOf("_Button");
	var idPath = clickedButtonId.substring(0, idx); // + "_issueId";
	selectedId = idPath + "_issueId";
	$(".sheptMultiLevelMenu")
		.width(200)
		.show()
		.offset($(this).offset())
		.focus();
	// all anchors in the menu are substituted by a handler
	$(".sheptMultiLevelMenu a").click(issueSelected);
}

// substitute all links in the menu with handlers to set the selected id
function issueSelected(event) {
	event.preventDefault();
	var selId = $(this).attr("id");
	if (!isNaN(selId)) {
		$("#" + selectedId).val(selId);
		$("#" + clickedButtonId).button("option", "label", $(this).text());
		$(".sheptMultiLevelMenu").hide();
	}
}

function inspect(event) {
	alert("Target: " + event.target + " Related target: " + event.relatedTarget
			+ " Current target " + event.currentTarget);
}

