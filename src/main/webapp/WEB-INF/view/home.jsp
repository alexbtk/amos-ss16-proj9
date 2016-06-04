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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
	integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7"
	crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css"
	integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r"
	crossorigin="anonymous">

<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css"
	integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r"
	crossorigin="anonymous">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/jquery-ui.css">
<!-- Link to bootstrap slider css -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-slider/7.1.0/css/bootstrap-slider.css">

<style type="text/css">
#twitterslider .slider-selection {
	background: red;
}

#twitterslider .slider-track-high {
	background: green;
}
</style>



<script src="${pageContext.request.contextPath}/js/jquery-2.2.3.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery-ui.js"></script>
<script src="${pageContext.request.contextPath}/js/util.js"></script>
<script src="${pageContext.request.contextPath}/js/modernizr.js"></script>
<!-- Latest compiled and minified Bootstrap and Modernizr JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"
	integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS"
	crossorigin="anonymous"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-slider/7.1.0/bootstrap-slider.js"></script>
<script src="${pageContext.request.contextPath}/js/d3.min.js"></script>
<script src="${pageContext.request.contextPath}/js/graphs.js"></script>
<script src="${pageContext.request.contextPath}/js/AMOSAlchemy.js"></script>
<script src="${pageContext.request.contextPath}/js/script.js"></script>




<!--[if lt IE 9]>
	<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
</head>

<body>
	<div class="container">
		<div id="login">
			<legend>Login</legend>
			<fieldset>
				<table>
					<tbody>
						<tr>
							<td><label for="apiKey">Alchemy API-Key</label></td>
						</tr>
						<tr>
							<td><input id="apiKey" type="text" maxLength=100 /></td>
						</tr>
						<tr>
							<td style="min-width: 180px"><label
								for="toneAnalyzerUsername">Toneanalyzer Username</label></td>
							<td style="min-width: 180px"><label
								for="toneAnalyzerPassword">Toneanalyzer Password</label></td>
						</tr>
						<tr>
							<td><input id="toneAnalyzerUsername" type="text"
								maxLength=100 /></td>
							<td><input id="toneAnalyzerPassword" type="text"
								maxLength=100 /></td>
						</tr>
						<tr>
							<td style="min-width: 180px"><label for="twitterConsumerKey">Twitter
									Consumer Key</label></td>
							<td style="min-width: 180px"><label
								for="twitterConsumerSecret">Twitter Consumer Secret</label></td>
							<td style="min-width: 180px"><label for="twitterToken">Twitter
									Token</label></td>
							<td style="min-width: 180px"><label for="twitterTokenSecret">Twitter
									Token Secret</label></td>
						</tr>
						<tr>
							<td><input id="twitterConsumerKey" type="text" maxLength=100 /></td>
							<td><input id="twitterConsumerSecret" type="text"
								maxLength=100 /></td>
							<td><input id="twitterToken" type="text" maxLength=100 /></td>
							<td><input id="twitterTokenSecret" type="text" maxLength=100 /></td>
						</tr>
					</tbody>
				</table>

				<br /> <br />
				<button id="loginButton">Login</button>
			</fieldset>
		</div>
		<div id="content">
			<form method="GET" action="process" id="principalForm">
				<fieldset>
				<legend>Company Name</legend> <br /> <input
						required id="companyInput" type="text" name="companyName"
						maxlength="100" required><br /> 
					<legend>Timeframe</legend>
					  <input id="oneWeekButton" type="radio" name="timeframe" value="oneWeek" checked> 1 Week<br/>
					  <input id="twoWeekButton" type="radio" name="timeframe" value="twoWeek"> 2 Week<br />
					  <input id="fourWeekButton" type="radio" name="timeframe" value="fourWeek"> 4 Week <br />
					<legend>Company Profile</legend>
					<input type="checkbox"
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
						name="question4" id="question4" /> <label for="question4">What
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
					<input type="checkbox" name="question6" id="question6" /> <label
						for="question6">Twiter vs News Sentiment?</label><br /> 
						<input type="checkbox" name="avgNewsSentimentGraph" id="avgNewsSentimentGraphCheckbox" /> <label for="avgNewsSentimentGraphCheckbox">Average News Sentiment Graph</label><br />
						<input
						type="submit" value="Ask" id="submitQuestion" /> <input
						type="submit" value="Advanced options" id="submitAdvancedQuestion" />
						
				</fieldset>

			</form>
			<input type="submit" value="Dummy Graph" id="dummyGraphSubmit" />
			<div id="avgSentimentGraph"></div>
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
						<br />Average Twitter Posts sentiment value:<br /> <input
							id="twitterslider" type="text" data-slider-min="-1"
							data-slider-max="1" data-slider-step="0.1"
							data-slider-value="${textSentiment}" data-slider-enabled="false" />

					</div>

				</fieldset>
			</form>
			<br>
			<div class="row">
				<div class="tabbable">
					<ul class="nav nav-tabs">
						<li class="active"><a href="#negsent" data-toggle="tab">Negative
								Sentiment</a></li>
						<li><a href="#neutsent" data-toggle="tab">Neutral
								Sentiment</a></li>
						<li><a href="#possent" data-toggle="tab">Positive
								Sentiment</a></li>
					</ul>
					<div class="tab-content">
						<div class="tab-pane active" id="negsent">
							<c:choose>
								<c:when test="${fn:length(postsList) > 1}">
									<c:forEach begin="0" end="${fn:length(postsList) - 1}"
										var="index">
										<c:choose>
											<c:when
												test="${sentimentlist.get(postsList[index].getId()) < 0}">
												<p>
													<button type="button" class="btn btn-xs"
														data-toggle="collapse"
														data-target="#<c:out value="${postsList[index].getId()}" />">+</button>
													Tweet from User
													<c:out value="${postsList[index].getUser().getName()}" />
												</p>
												<div id="<c:out value="${postsList[index].getId()}" />"
													class="collapse">
													<c:out value="${postsList[index].getText()}" />
												</div>
											</c:when>
											<c:otherwise>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</c:when>
								<c:otherwise>
								</c:otherwise>
							</c:choose>
						</div>
						<div class="tab-pane" id="neutsent">
							<c:choose>
								<c:when test="${fn:length(postsList) > 1}">
									<c:forEach begin="0" end="${fn:length(postsList) - 1}"
										var="index">
										<c:choose>
											<c:when
												test="${(sentimentlist.get(postsList[index].getId()) >= 0) and (sentimentlist.get(postsList[index].getId()) <= 0.5)}">
												<p>
													<button type="button" class="btn btn-xs"
														data-toggle="collapse"
														data-target="#<c:out value="${postsList[index].getId()}" />">+</button>
													Tweet from User
													<c:out value="${postsList[index].getUser().getName()}" />
												</p>
												<div id="<c:out value="${postsList[index].getId()}" />"
													class="collapse">
													<c:out value="${postsList[index].getText()}" />
												</div>
											</c:when>
											<c:otherwise>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</c:when>
								<c:otherwise>
								</c:otherwise>
							</c:choose>
						</div>
						<div class="tab-pane" id="possent">
							<c:choose>
								<c:when test="${fn:length(postsList) > 1}">
									<c:forEach begin="0" end="${fn:length(postsList) - 1}"
										var="index">
										<c:choose>
										<c:when
											test="${sentimentlist.get(postsList[index].getId()) > 0.5}">
											<p>
												<button type="button" class="btn btn-xs"
													data-toggle="collapse"
													data-target="#<c:out value="${postsList[index].getId()}" />">+</button>
												Tweet from User
												<c:out value="${postsList[index].getUser().getName()}" />
											</p>
											<div id="<c:out value="${postsList[index].getId()}" />"
												class="collapse">
												<c:out value="${postsList[index].getText()}" />
											</div>
										</c:when>
										</c:choose>
									</c:forEach>
								</c:when>
								<c:otherwise>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>

			</div>
			<br>

			<form method="POST" action="getTone">
				<fieldset>
					<legend>Write text to analyze tone</legend>
					Text: <input type="text" name="Text" required><br /> <input
						type="submit" value="submit" id="submitText" />
					<div id="answer">
						<br />Tone results:<br /> ${textTone}<br />
					</div>
				</fieldset>
			</form>

			<br /> <br />
			<button id="logoutButton">Logout</button>
			<br /> <br />
			<!--<script type="text/javascript" src="js/myscript.js" ></script>-->
		</div>
	</div>
</body>
</html>
