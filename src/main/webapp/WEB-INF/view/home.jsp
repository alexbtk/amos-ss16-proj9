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

<!-- Latest compiled and minified Bootstrap CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
	integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7"
	crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css"
	integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r"
	crossorigin="anonymous">

<!-- Latest compiled and minified Bootstrap JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"
	integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS"
	crossorigin="anonymous"></script>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/jquery-ui.css">
<script src="${pageContext.request.contextPath}/js/jquery-2.2.3.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery-ui.js"></script>
<script src="${pageContext.request.contextPath}/js/script.js"></script>

<!--[if lt IE 9]>
	<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
</head>

<body>

	<form method="GET" action="process" id="principalForm">
		<fieldset>
			<legend>Company Profile</legend>
			<label for="companyInput">Company Name</label> <br /> <input
				required id="companyInput" type="text" name="companyName"
				maxlength="100" required><br /> <input type="checkbox"
				name="question1" id="question1" /> <label for="question1">What
				is the company industry(Alchemy/Home page/Taxonomies)?</label> <br /> <input
				type="checkbox" name="question2" id="question2" /> <label
				for="question2">What is the company main
				product(Alchemy/Home page/Entities)?</label><br /> <input type="checkbox"
				name="question3a" id="question3a" /> <label for="question3a">What
				are the company competitors(DBpedia/Industry)?</label><br /> <input
				type="checkbox" name="question3b" id="question3b" /> <label
				for="question3b">What are the company
				competitors(Alchemy/News)?</label><br /> <input type="checkbox"
				name="question4" id="question4" /> <label for="question3">What
				are the company news sentiment(Alchemy/News)?</label><br /> <input
				type="checkbox" name="question5a" id="question5a" /> <label
				for="question5a">What are the company
				products(DBpedia/Category)?</label><br /> <input type="checkbox"
				name="question5b" id="question5b" /> <label for="question5b">What
				are the company products(Alchemy/Home page)?</label><br /> <input
				type="checkbox" name="question5c" id="question5c" /> <label
				for="question5c">What are the company competitors
				products(DBpedia/Category)?</label><br /> <input type="checkbox"
				name="question5d" id="question5d" /> <label for="question5d">What
				are the company competitors products Sentiment(DBpedia/Category)?</label><br />

			<input type="submit" value="Ask" id="submitQuestion" /> <input
				type="submit" value="Advanced options" id="submitAdvancedQuestion" />
		</fieldset>

	</form>

	<div id="displayAnswers"></div>
	<!-- ui-dialog -->
	<div id="dialog" title="Advanced Options"></div>

	<form method="POST" action="getSentiment">
		<fieldset>
			<legend>Write company name to analyze twitter posts
				sentiment</legend>
			<input type="text" name="Text" maxlength="1000" required> <input
				type="submit" value="submit" id="submitText" />
			<div id="answer">
				<br />Average Twitter Posts sentiment value: <br />
				${textSentiment}<br />
			</div>
		</fieldset>
	</form>

	<form method="POST" action="getTone">
		<fieldset>
			<legend>Write text to analyze tone</legend>
			Text: <input type="text" name="Text" required><br />
			Username: <input type="text" name="Username" maxlength="100" required><br />
			Password: <input type="text" name="Password" maxlength="100" required><br />
			<input type="submit" value="submit" id="submitText" />
			<div id="answer">
				<br />Tone results:<br /> ${textTone}<br />
			</div>
		</fieldset>
	</form>

	<!--<script type="text/javascript" src="js/myscript.js" ></script>-->
</body>
</html>
