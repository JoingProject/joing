<%@page contentType="text/html"
%><%@page pageEncoding="UTF-8"
%><%@taglib prefix="f" uri="http://java.sun.com/jsf/core"
%><%@taglib prefix="h" uri="http://java.sun.com/jsf/html"
%><html>
<head>
  <title>Join'g - Your desktop across the net</title>
<style type="text/css">
@import url(css/css.css);
</style>
</head>
<body>
<f:view>
<h:form id="joing">
<div class="top">
  <div class="content_top">
    <div class="content_top_div">
	<h:commandLink action="home" title="Join'g home" styleClass="logo"/>
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
      <p>Cras bibendum. Aenean purus enim, cursus ac, scelerisque sed, tempor in, lorem. Pellentesque metus enim, tempus sed, rutrum sit amet, egestas id, lacus. Sed auctor hendrerit nisi. Quisque nulla justo, commodo vel, lacinia a, nonummy vel, eros. Maecenas quam risus, cursus et, fringilla vel, viverra eu, mauris. Mauris aliquet arcu ut nunc. Morbi sit amet orci sit amet dolor sagittis mollis. Donec volutpat faucibus nulla. Donec et eros. In ipsum sem, commodo sit amet, pellentesque quis, eleifend id, nulla. Duis at nisi. Sed varius elementum sem. Curabitur nibh nisl, scelerisque non, cursus sit amet, tincidunt in, orci. Fusce diam felis, rhoncus non, blandit et, tincidunt vitae, massa.</p>
      <p>Ut ac nunc in nunc sodales consequat. In cursus tortor vel sem. Fusce nec massa in tellus vehicula venenatis. Sed dignissim eleifend magna. Aliquam lacinia, massa non auctor pharetra, ante orci semper lacus, in pulvinar arcu velit non est. Ut ipsum dui, fermentum ut, dictum ut, tristique euismod, nulla. Aliquam justo. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Maecenas nonummy metus quis odio. Quisque metus ligula, bibendum vel, lacinia a, aliquam et, urna. Ut vel arcu.</p>
      <p>Donec in nunc. Curabitur blandit augue nec metus. Nam congue purus et turpis ultrices viverra. In et tellus. Integer euismod feugiat velit. Sed ultricies molestie turpis. Sed condimentum. Duis at ligula. Fusce aliquet augue sit amet elit. Sed diam. Phasellus risus eros, dignissim a, aliquam vitae, tempus non, ipsum. Ut pretium posuere orci. Duis condimentum purus vitae nulla.</p>
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
<div class="footer_text">Sed nonummy euismod magna. Vestibulum lacinia nisi nec neque. Suspendisse porttitor. </div>
</h:form>
</f:view>
</body>
</html>