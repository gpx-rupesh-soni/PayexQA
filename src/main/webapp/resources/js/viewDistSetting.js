/**
 * 
 */
$('.pca').each(function() {
	var allowed = $(this).html();
	if (allowed == 1){
		$(this).html('Yes')
	}
	if (allowed == 0){
		$(this).html('No')
	}
});
