<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<div class="container" style="margin-top: 80px;">
	<h2>Activate Retailers</h2>
</div>

<script type="text/javascript">
   function approveMandate(button,mandateId){
	   
	   
	   var url = "approve_copmlete_mandate";	  
	   var data = {"mandateId":mandateId};
	 
	$.ajax({
	    type: "POST",
	    url: url,
	    dataType: "json",
	    data: data,
	    success: function(response){
	    	
	    	setData(response,button);
	       
	    }
	});
   }
   
function setData(response,button){
	
		alert(response.msg);
	   	if(response.status){
	   		alert($(button).html());
	   		$(button).parent().html("Approved");
	   		$(button).remove();
	   	}
	   
   }
   
   </script>

