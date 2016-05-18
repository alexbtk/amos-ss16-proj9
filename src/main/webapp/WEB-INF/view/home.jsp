<!DOCTYPE HTML>
<!--

    Copyright 2016 The Open Source Research Group,
                   University of Erlangen-N�rnberg

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
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery-ui.css">
    <script src="${pageContext.request.contextPath}/js/jquery-2.2.3.js"></script>
    <script src="${pageContext.request.contextPath}/js/jquery-ui.js"></script>
    <script src="${pageContext.request.contextPath}/js/script.js"></script>

	<!--[if lt IE 9]>
	<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
</head>

<body>
  
	<form method="GET" action="process" >
	<fieldset>
		<legend>Write company name</legend>
		<input id="companyInput" type="text" name="companyName" maxlength="100" required>
		<!--<input type="submit" value="Go" id="submitName">-->
	</fieldset>
	<fieldset>
		<legend>Ask a question</legend>
		<input required type="radio" name="question" value="1" />What is the company industry? <br />
		<input required type="radio" name="question" value="2" />What are the company's products?<br />
		<input type="submit" value="Ask" id="submitQuestion" />
	</fieldset>
	
	</form>

	<form method="POST" action="getSentiment" >
		<fieldset>
			<legend>Write text to analyze sentiment</legend>
			<input type="text" name="Text" maxlength="1000" required>
			<input type="submit" value="submit" id="submitText" />
			<div id="answer"><br />Average Twitter Posts sentiment value: <br />
			${textSentiment}<br />
			</div>
		</fieldset>
	</form>
	
	<form method="POST" action="getTone" >
		<fieldset>
			<legend>Write text to analyze tone</legend>
			Text:
			<input type="text" name="Text" required><br />
			Username:
			<input type="text" name="Username" maxlength="100" required><br />
			Password:
			<input type="text" name="Password" maxlength="100" required><br />
			<input type="submit" value="submit" id="submitText" />
			<div id="answer"><br />Tone results:<br />
			${textTone}<br />
			</div>
		</fieldset>
	</form>
	
	<!--<script type="text/javascript" src="js/myscript.js" ></script>-->
</body>
</html>
