<!DOCTYPE HTML>
<!--

    Copyright 2016 The Open Source Research Group,
                   University of Erlangen-Nürnberg

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->
<html lang="en">
<head>
	<meta charset="utf-8">

	<title>The Project 9</title>
	<meta name="description" content="The Project 9">
	<meta name="author" content="Project9">

	<!-- <link rel="stylesheet" href="css/mystyle.css?v=1.0">
	<script type="text/javascript" src="js/jquery-2.2.3.min.js"></script>
    -->

	<!--[if lt IE 9]>
	<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
</head>

<body>
  
	<form method="GET" action="process" >
	<fieldset>
		<legend>Write company name</legend>
		<input type="text" name="companyName" maxlength="100" required>
		<!--<input type="submit" value="Go" id="submitName">-->
	</fieldset>
	<fieldset>
		<legend>Ask a question</legend>
		<input required type="radio" name="question" value="1" />What is the company industry? <br />
		<input required type="radio" name="question" value="2" />What are the company's products?<br />
		<input type="submit" value="Ask" id="submitQuestion" />
	</fieldset>
	
	</form>
	
	<!--<script type="text/javascript" src="js/myscript.js" ></script>-->
</body>
</html>
