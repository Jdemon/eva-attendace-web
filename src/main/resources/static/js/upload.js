function bs_input_file() {
	$(".input-file")
			.before(
					function() {
						if (!$(this).prev().hasClass('input-ghost')) {
							var element = $("<input type='file' id='file' name='file' class='input-ghost' style='visibility:hidden; height:0'>");
							element.attr("name", $(this).attr("name"));
							element.change(function() {
								element.next(element).find('input').val(
										(element.val()).split('\\').pop());
								if (!(null === $("#file-text").val())
										&& !('' === $("#file-text").val())) {
									$("#btnupload").prop('disabled', false);
								} else {
									$("#btnupload").prop('disabled', true);
								}
							});
							$(this).find("button.btn-choose").click(function() {
								element.click();
							});
							$(this).find("button.btn-reset").click(
									function() {
										element.val(null);
										$(this).parents(".input-file").find(
												'input').val('');
									});
							$(this).find('input').css("cursor", "pointer");
							$(this).find('input').mousedown(function() {
								$(this).parents('.input-file').prev().click();
								return false;
							});
							return element;
						}
					});

	$("#btnupload,#login-submit").click(function() {
		document.getElementById("loader").style.display = "block";
		document.getElementById("mainDiv").style.display = "none";
	});

	$("#downloadLink").click(function() {
		$("#filedwn").remove();
	});

	$("#holiadd").click(function() {
		
		if($("#addholidate").val() === null || $("#addholidate").val() === ''){
			alert('Date required.')
		}else{
			
			$.ajax({
			    type: "GET",
			    url: "holiday/add",
			    data: {
					"date": $("#addholidate").val(),
					"desc": $("#addholides").val(),
					"descTh": $("#addholidesth").val()
			    },
			    success: function(data){
			    	var htmlObject = $(data);
			    	if(data.includes("Error")){
			    		alert(htmlObject.html());
			    	}else{
			    		window.location.href = location.protocol + '//' 
			    		+ location.host + location.pathname + "?year="+htmlObject.html();
			    	}
		        },
		        error: function(data){
		        	var htmlObject = $(data);
		        	alert(htmlObject);
		        }
			});
		}
		
	});

	$(".nav a").click(function() {
		$(".nav").find(".active").removeClass("active");
		$(this).parent().addClass("active");
	});
}

function deldate(index){
	
	if(confirm("แน่ใจว่าต้องการลบ?")){
		$.ajax({
		    type: "GET",
		    url: "holiday/del",
		    data: {
				"date": $("#"+index+"date").text(),
		    },
		    success: function(data){
		    	var htmlObject = $(data);
		    	if(data.includes("Error")){
		    		alert(htmlObject.html());
		    	}else{
		    		window.location.href = location.protocol + '//' 
		    		+ location.host + location.pathname + "?year="+htmlObject.html();
		    	}
	        },
	        error: function(data){
	        	var htmlObject = $(data);
	        	alert(htmlObject);
	        }
		});
	}
}

$(function() {
	bs_input_file();
});