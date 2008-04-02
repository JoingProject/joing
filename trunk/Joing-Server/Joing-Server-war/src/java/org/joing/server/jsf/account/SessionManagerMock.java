package org.joing.server.jsf.account;

import ejb.Constant;
import ejb.session.SessionManagerLocal;
import org.joing.common.dto.session.LoginResult;
import org.joing.common.exception.JoingServerSessionException;

class SessionManagerMock implements SessionManagerLocal {

    public boolean isAccountAvailable(String sAccount) throws JoingServerSessionException {

	return !("not_available@" + Constant.getSystemName()).equals(sAccount);
    }

    public String getUserAccount(String sSessionId) throws JoingServerSessionException {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    public LoginResult login(String sAccount, String sPassword) throws JoingServerSessionException {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    public void logout(String sSessionId) throws JoingServerSessionException {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    public String composeAccount(String sAccount) {
	return String.format("%s@%s", sAccount, Constant.getSystemName());
    }
}
