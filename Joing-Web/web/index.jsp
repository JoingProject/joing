<%@page contentType="text/html"
%><%@page pageEncoding="UTF-8"
%><%@taglib prefix="f" uri="http://java.sun.com/jsf/core"
%><%@taglib prefix="h" uri="http://java.sun.com/jsf/html"
%><f:view><html>
<head>
  <title>Join'g - Your desktop across the net</title>
<style type="text/css">
@import url(<h:outputText value="#{Util.path['css/css.css']}"/>);
</style>
</head>
<body>
<h:form id="joing">
<div class="top">
  <div class="content_top">
    <div class="content_top_div">
	<a href="<h:outputText value="#{Util.path['/']}"/>" title="Join'g home" class="logo"></a>
      <div class="catchphrases">
        <ul>
          <li><h3>Join'g is about ubiquity and communication</h3></li>
          <li>Place your computer in the Internet and make it available from anywhere.</li>
          <li>Stay in touch with friends, make new ones and share your documents with them.</li>
        </ul>
      </div>
    </div>
  </div>
</div>
<div class="middle">
  <div class="content_middle">

    <div class="web_form">
      <div class="web_form_content">
	<h2>Create an account</h2>
	<div class="field">
	    <h:outputLabel for="user_name" value="User"/>
	  <div class="input_text"><span><h:inputText
		id="user_name"
		value="#{CreateAccount.username}"/></span></div>
	</div>
	<div class="field">
	    <h:outputLabel for="user_email" value="e-mail"/>
	  <div class="input_text"><span><h:inputText
		id="user_email"
		value="#{CreateAccount.email}"/></span></div>
	</div>
	<div class="field">
	    <h:outputLabel for="user_password" value="Password"/>
	  <div class="input_text"><span><h:inputSecret
		id="user_password"
		value="#{CreateAccount.password}"/></span></div>
	</div>
	<div class="field">
	    <h:outputLabel for="user_confirm_password" value="Confirm"/>
	  <div class="input_text"><span><h:inputSecret
		id="user_confirm_password"
		value="#{CreateAccount.confirmPassword}"/></span></div>
	</div>
	<div class="field">
	    <h:commandButton
		styleClass="submit"
		value="Submit"
		action="#{CreateAccount.createNewAccount}"/>
	</div>

	<h:messages
	    errorClass="error_msg"
	    infoClass="info_msg"
	    fatalClass="fatal_msg"
	    warnClass="warning_msg"

	    showSummary="true"
	    showDetail="false"

	    styleClass="messages"/>

          <p style="clear: both;">
            Registered users can launch Join'g
          </p>
          <div>
            <input type="button" class="web_start" value="Start" onclick="alert('Start Join\'g');"/>
            <br style="clear: both;"/>&nbsp;
          </div>
       </div>
      <div class="web_form_end"></div>
    </div>

    <div class="web_content">

	<h1><span><span>Welcome to Join'g</span></span></h1>
      
	<p>
	    Join'g is an ultra lightweight remote desktop and application
	    launcher where your applications and files seamlessly live in a
	    remote server.
	</p>

	<p>
	    <img src="<h:outputText value="#{Util.path['img/diagram.png']}"/>"
		class="diagram" title="Access Join'g from anywhere"/>

	    Join'g is about ubiquity, it takes advantage of the
	    <a href="http://java.sun.com" target="java">java technology</a>
	    multi platform features, making it possible to access Join'g client
	    using most computer operating systems and many other devices such as
	    palm PCs and even cell phones. Your data and applications will live
	    a remote server, they will be available from any device or any place
	    you connect from. You will no longer need to carry your files in a
	    data storage device.
	</p>

	<p>
	    Join'g needs no client set up. It's client is accessible through
	    <a href="http://java.sun.com/products/javawebstart/" target="java">Java Web Start</a>,
	    no client software install nor update is needed. Client-server
	    communication is performed through the HTTP and the secure HTTP
	    protocols, you can use Join'g from behind a firewall without any
	    special configuration.
	</p>

	<p>All you need to run a Join'g client is a
	    <a href="http://www.java.com/" target="java">Java Virtual Machine</a>
	    and an internet connection.</p>

	<p>You can download Join'g and join us at
	    <a href="https://joing.dev.java.net" target="joing">Join'g project page</a></p>

     </div>

  <br style="clear: both;"/>&nbsp;
  </div>
</div>
<div class="bottom">
  <div class="content_bottom">
    <div class="content_bottom_div">
    </div>
  </div>
</div>
<div class="footer_text">&copy; 2008 Join'g project</div>
</h:form>
</body>
</html>
</f:view>