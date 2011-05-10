function makeTimeEntry24(inputId) {
	$('#'+inputId).timeEntry({spinnerImage: './includes/js/jQuery/timeEntry/spinnerOrange.png',
			spinnerBigImage: './includes/js/jQuery/timeEntry/spinnerOrangeBig.png',
			show24Hours: true});
}
function makeTimeEntry12(inputId) {
	$('#'+inputId).timeEntry({spinnerImage: './includes/js/jQuery/timeEntry/spinnerOrange.png',
			spinnerBigImage: './includes/js/jQuery/timeEntry/spinnerOrangeBig.png',
			show24Hours: false});
}