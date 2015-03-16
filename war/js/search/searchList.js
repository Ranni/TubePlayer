/*var m = angular.module("myService", []);
m.service('serviceSample', function () {
    this.run = function () {
        return "Hello World! <br />This text is rendered by angularjs service.";
    };
});*/

var searchListModule = angular.module('searchModule', []);
/*searchListModule.service('serviceSample', function () {
    this.run = function () {
        return "search la!!!!";
    };
});*/
/*var m = angular.module("myService", []);
m.service('serviceSample', function () {
    this.run = function () {
        return "Hello World! <br />This text is rendered by angularjs service.";
    };
});*/
/*
searchListModule.factory('serviceSample', function() {
	 var savedData = {}
	 function set(data) {
	   savedData = data;
	 }
	 function get() {
	  return savedData;
	 }

	 return {
	  set: set,
	  get: get
	 }

});*/
function SearchListController($scope, $http, $element, $window){
	$scope.queryTextFuzzy=[];
	//$window.alert($window.sessionStorage);
    //console.log(  $window.innerHeight*100/$window.outerHeight);
    
    $scope.searchTextChanged = function (text){
  	  if(text.length==0){
  		 clearSuggestList();
  	  }else{
  		$http.jsonp('http://suggestqueries.google.com/complete/search?callback=?ds=yt&q='+text+'&client=firefox&jsonp=suggestCallBack').
	  	  error(function(data, status, headers, config) {
	  	});
  	  }
    };
    
//    $scope.fuzzySuggestListHasBorder = function(){return ($scope.queryTextFuzzy.length>0)?true:false};
//    $scope.tabShoudHide = function(){return ( $window.innerHeight*100/$window.outerHeight<82 )?true:false};
//    $scope.fuzzySuggestListItemNone      = function(itemText){return (itemText.length==0)?true:false};
    
    suggestCallBack = function (data) {
    	$scope.queryTextFuzzy=data[1];
//    	$scope.queryTextFuzzy.push('');
    };
    
    clearSuggestList = function(){
  		 $scope.queryTextFuzzy=[];
    };
    
    
    $scope.searchYoutubeListForText  =  function(text){
    	//console.log('hihi---'+text);
    	clearSuggestList();
    	
    	$scope.textSearch = text;
    	$window.sessionStorage.searchYTText=text;
    	$window.history.go(-1);
    	//serviceSample.set(text+'  search la!!!');
    };
    
    
};
