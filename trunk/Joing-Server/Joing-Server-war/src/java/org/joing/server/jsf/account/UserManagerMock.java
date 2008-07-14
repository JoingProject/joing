package org.joing.server.jsf.account;

import ejb.user.UserManagerLocal;
import java.util.List;
import java.util.Locale;
import org.joing.common.dto.user.Local;
import org.joing.common.dto.user.User;
import org.joing.common.exception.JoingServerUserException;

class UserManagerMock implements UserManagerLocal {

    public User createUser(String sAccount, String sPassword, String sEmail, String sFirstName, String sSecondName, boolean bIsMale, Locale locale, int nQuota) throws JoingServerUserException {
	User user = new User();

	user.setAccount(sAccount);
	user.setEmail(sEmail);
	user.setFirstName(sFirstName);
	user.setMale(bIsMale);
	user.setSecondName(sSecondName);
	user.setTotalSpace(nQuota);
	user.setUsedSpace(0);

	return user;
    }

    public void removeUser(User user) throws JoingServerUserException {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isValidAccount(String sAccount) {
	return sAccount.matches("[a-z][a-z1-9\\.\\_]{5,31}");
    }

    public boolean isValidPassword(String sPassword) {
	return sPassword.matches(".{6,32}");
    }

    public User getUser(String sSessionId) throws JoingServerUserException {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    public User updateUser(String sSessionId, User user) throws JoingServerUserException {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Local> getAvailableLocales(String sSessionId) throws JoingServerUserException {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean areLinked( String sSessionId, String sPassword )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}
