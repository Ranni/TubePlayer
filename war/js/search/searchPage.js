
var searchModule = angular.module('searchModule', []);
//var searchModule = angular.module('searchModule', ['myService']);
/*searchModule.service('serviceSample', function () {
    this.run = function () {
        return "search la!!!!";
    };
});*/
function SearchController($scope, $http, $element, $window){
	var hintSearchYTText= 'search on YouTube';
	console.log(hintSearchYTText);
	
	//console.log(serviceSample.run());
	//console.log($window.sessionStorage.searchYTText);
	$scope.searchYTText= ( $window.sessionStorage.searchYTText)?$window.sessionStorage.searchYTText:  hintSearchYTText;
	//console.log($scope.searchYTText);
	//console.log($scope.searchYTText.replace(hintSearchYTText, ''));
	/*setTimeout(function(){
		$window.scrollTo(0, 1);
    }, 100);*/
	
	
	$scope.queryYTResultList=[];
    //$scope.queryTextFuzzy=[];
    //console.log(  $window.innerHeight*100/$window.outerHeight);
    
    /*$scope.searchTextChanged = function (text){
  	  //console.log('text= '+text);
    	//$scope.element('#ulSearchFuzzy').classList.remove();
    	
  	  if(text.length==0){
  		 clearSuggestList();
  	  }else{
  		$http.jsonp('http://gdata.youtube.com/feeds/api/videos?q='+text+'&jsonp=queryCallBack&max-results=50&start-index=1&alt=json').
	  	  error(function(data, status, headers, config) {
	  	});
  	  }
    };*/
	
	$scope.musicPlay = function (index){
		var videoUrl = $scope.queryYTResultList[index].media$group.media$player[0].url.split("&")[0];
		auderioMp3Service(videoUrl);
	};
	
	if($scope.searchYTText.replace(hintSearchYTText, '').length>0){
		$http({
	        method: 'GET',
	        url: 'http://gdata.youtube.com/feeds/api/videos?q='+$scope.searchYTText+'&jsonp=queryCallBack&max-results=50&start-index=1&alt=json',
	     }).success(function(data){
	        // With the data succesfully returned, call our callback
	    	 queryCallBack(data);
	    }).error(function(){
	        alert("error");
	    });
		/*$http.get('http://gdata.youtube.com/feeds/api/videos?q='+$scope.searchYTText+'&jsonp=queryCallBack&max-results=50&start-index=1&alt=json')
		  .done(data, status, headers, config){
			queryCallBack(data);
		  }
	  	  .error(function(data, status, headers, config) {
	  	  });*/
	}
    
    $scope.searchResultListGone = function(){return ($scope.searchYTText.replace(hintSearchYTText, '').length==0  || $scope.queryYTResultList.length==0)};
    $scope.searchEmptyResultList = function(){return ( $scope.searchYTText.toLowerCase()!=hintSearchYTText.toLowerCase() && $scope.queryYTResultList.length==0)};
    $scope.fuzzySuggestListHasBorder = function(){return ($scope.queryTextFuzzy.length>0)?true:false};
    $scope.tabShoudHide = function(){return ( $window.innerHeight*100/$window.outerHeight<82 )?true:false};
    //$scope.fuzzySuggestItemHighLight = function(){return ($scope.queryTextFuzzy.length>0)?true:false};
    
    
    queryCallBack = function (data) {
    	//console.log(data);
    	$scope.queryYTResultList=data.feed.entry;
    	//console.log($scope.queryYTResultList);
    	
    	//$scope.element('#ulSearchFuzzy').classList.add('border');
    	//$scope.queryYTResultList=data[1];
    	/*if(data[1].length>0){
    		$element.find('#ulSearchFuzzy').addClass('border');
    	}else{
    		$element.find('#ulSearchFuzzy').removeClass('border');
    	}*/
    };
    
    clearSuggestList = function(){
    	//$element.find('#ulSearchFuzzy').removeClass('border');
  		//$scope.element('#ulSearchFuzzy').classList.add('non-border');
  		 $scope.queryTextFuzzy=[];
    };
    
    
    
    //mp3 auderio
    auderioMp3Service	= function(videoUrl){
    	var jsonData;
    	$http({
	        method: 'POST',
	        url: 'https://auderio.p.mashape.com/download',
	        headers: {
	        	   'X-Mashape-Key': 'b5qYQxsqJMmshawEEYmwwrr3M5Ttp1WOPeljsnMPaFgghKCmpM',
	        	   'Content-Type':'application/x-www-form-urlencoded',
	        	   'Accept':'application/json',
	        	 },
	        data: "url="+videoUrl,
	     }).success(function(data){
	    	 console.log(data.download_link);
//	    	 jsonData=data;
//	    	 console.log(jsonData);
//	    	 console.log(jsonData.download_link);
	        // With the data succesfully returned, call our callback
	    	 //queryCallBack(data);
	    }).error(function(){
	        alert("error");
	    });
    }
    
   /* $scope.searchYoutubeListForText  =  function(text){
    	//console.log('hihi---'+text);
    	//clearSuggestList();
    	
    	//$scope.textSearch = text;
    	var qUrl='http://gdata.youtube.com/feeds/api/videos?q='+text+'&jsonp=queryCallBack&max-results=50&start-index=1&alt=json';
    	qUrl
    };*/
    
    $(window).resize(function(){
        console.log(($window.innerHeight*100/$window.outerHeight<60)?'<':'');

        $scope.$apply(function(){
           //do something to update current scope based on the new innerWidth and let angular update the view.
        });
    });
    
    
};

/*window.onload = function(){
    setTimeout(function(){
        window.scrollTo(0, 1);
    }, 100);
}*/
/*angular.forEach(element.find('ulSearchFuzzy'), function(node)
{
		  if(node.id == 'ulSearchFuzzy'){
			  console.log('helo');
		    node.class='non-border';
		  }
});*/
/*$(window).resize(function(){
    console.log(window.innerWidth);

    $scope.$apply(function(){
       //do something to update current scope based on the new innerWidth and let angular update the view.
    });
});*/
/*
$(document).on("pageinit", "#PageYoutube", function () {


  $('#inputSearchYoutube').on('keyup', function () {
    fuzzyListContent($(this).val());
  });


  $('#YsearchBtn').on('click', function () {
    var queryText = $('#inputSearchYoutube').val();
    $('#labelTmpt').text(queryText + '成績單');
  });


  function fuzzyListContent(queryTextFuzzy) {
      $("#YsearchFuzzyList").gk("model", []);
      $.getJSON("http://suggestqueries.google.com/complete/search?callback=?",
      {
        "ds":"yt", // Restrict lookup to youtube
        "q":queryTextFuzzy, // query term
        "client":"firefox" // force youtube style response, i.e. jsonp
      },
        function(queryFuzzys) {
          $("#YsearchFuzzyList").gk("model", queryFuzzys[1]);
        }
      );
  }

});*/