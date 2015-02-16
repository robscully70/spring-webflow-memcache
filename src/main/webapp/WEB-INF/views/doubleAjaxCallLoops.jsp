<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>spring-mvc-showcase</title>
<link href="<c:url value="/resources/form.css" />" rel="stylesheet" type="text/css" />
<link href="<c:url value="/resources/jqueryui/1.8/themes/base/jquery.ui.core.css" />" rel="stylesheet" type="text/css" />
<link href="<c:url value="/resources/jqueryui/1.8/themes/base/jquery.ui.theme.css" />" rel="stylesheet" type="text/css" />
<link href="<c:url value="/resources/jqueryui/1.8/themes/base/jquery.ui.tabs.css" />" rel="stylesheet" type="text/css" />
<style>
.right {
	float: right;
}

.left {
	float: left;
}

.clear {
	clear: both;
}
</style>
<script type="text/javascript" src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
<%-- <script type="text/javascript" src="<c:url value="/resources/jqueryform/2.8/jquery.form.js" />"></script> --%>
<%-- <script type="text/javascript" src="<c:url value="/resources/jqueryui/1.8/jquery.ui.core.js" />"></script> --%>
<%-- <script type="text/javascript" src="<c:url value="/resources/jqueryui/1.8/jquery.ui.widget.js" />"></script> --%>
<%-- <script type="text/javascript" src="<c:url value="/resources/jqueryui/1.8/jquery.ui.tabs.js" />"></script> --%>
<%-- <script type="text/javascript" src="<c:url value="/resources/json2.js" />"></script> --%>
<%-- <script type="text/javascript" src="<c:url value="/resources/multithread/multithread.js" />"></script> --%>

<!--
		Used for including CSRF token in JSON requests
		Also see bottom of this file for adding CSRF token to JQuery AJAX requests
	-->
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<script type="text/javascript">
  var GLOBAL = {
    callSpeed : 1000,
    interrupt : false
  };
  var growMapLoop = function(mapKey, idOfElement, lastEntryKey) {
    console.log(lastEntryKey);
    var elementToUpdate = $(idOfElement);
    var error = false;
    var count = 0;
    elementToUpdate.text("Calling: ");
    responseText = growMapFunction(mapKey, lastEntryKey);
    if (responseText === "ERROR_NO_LAST") {
      error = true;
      elementToUpdate.text("Could Not find last value");
    } else if (responseText === "ERROR_REQUEST") {
      error = true;
      elementToUpdate.text("System Error! something went wrong with the request");
    } else {
      nextEntryKey = responseText;
      elementToUpdate.append(responseText);
    }
    if (!error && !GLOBAL.interrupt) {
      setTimeout(function() {
        growMapLoop(mapKey, idOfElement, nextEntryKey);
      }, GLOBAL.callSpeed);
    }
  };
  var growMapFunction = function(mapKey, entryKey) {
    var returnText;
    var ajaxOutcome = $.ajax({
      url : '/spring-mvc-showcase/session-edit/grow-map?sessionMap=' + mapKey + '&entryKey=' + entryKey,
      async : false,
      dataType : "text",
      success : function(text) {
        returnText = text;
      },
      error : function(xhr) {
        returnText = "ERROR_REQUEST";//elementToChange.text("ERROR!");
      }
    });
    return returnText;
  };

  $(document).ready(function() {
    $("#thread1").click(function() {
      GLOBAL.interrupt = false;
      growMapLoop("mapKey-thread1", "#thread1-output", "");
      return false;
    });
    $("#thread2").click(function() {
      GLOBAL.interrupt = false;
      growMapLoop("mapKey-thread2", "#thread2-output", "");
      return false;
    });
    $("#stop").click(function() {
      GLOBAL.interrupt = true;
      return false;
    });
  });
</script>
</head>
<body>
	<h1>
		<a href="<c:url value="/doubleAjaxCallLoops" />">spring-mvc-memcache Ajax Example</a>
	</h1>
	<p>Recommended: Using a Web Developer tool such a Firebug to inspect the client/server interaction</p>
	<div>
		<a id="thread1" href="#">Test ajax</a>
		<div id="thread1-output">Thread not yet started.</div>
	</div>
	<div>
		<a id="thread2" href="#">Test ajax 2</a>
		<div id="thread2-output">Thread not yet started.</div>
	</div>
	<div>
		<a id="stop" href="#">Click to stop</a>
	</div>
	<!-- 	
web -> store param1 -> web
web -> (needs map, and value)store param2 recal param1 -> (needs new stored value)web


callAjax - call ajax with previously stored var
check return - of ajax call
store new var from ajax for next call


 -->
</body>
</html>