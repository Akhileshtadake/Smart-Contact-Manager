console.log("this is script file");

function checkAgreement() {
	  var checkBox = document.getElementById("agreement");
	  if (checkBox.checked == true) {
	    confirm("Registered Succesfully");
	  } else {
	    alert("Please agree to the terms and conditions before proceeding.");
	  }
}




const toggleSidebar =() =>{
	
	if($(".sidebar").is(":visible")){
		
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left","0%");
	}else{
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left","20%");
	}
	
};


const search=()=>{
	
	let query=$("#search-input").val();
	console.log(query);
	
	if(query==''){
		$(".search-result").hide();
	}else{
		console.log(query);
		
		//sending request to server
		
		let url= `http://localhost:8080/search/${query}`;
			
        fetch(url).then((response)=>{
        	return response.json();
        }).then((data) => {
        	
        	//data....
        	//console.log(data);
        	
        	let text =`<div class='list-group'>`
        	
        	
        	data.forEach((contact) =>{
        		text+=`<a href='/user/${contact.cId}/contact' class='list-group-item list-group-item-action'>${contact.name}</a>`
        		
        		$(".search-result").html(text);
        		$(".search-result").show();
        	})
        	
        	text+=`</div>`;
        	
        })
		
		
		$(".search-result").show();
	}
}

