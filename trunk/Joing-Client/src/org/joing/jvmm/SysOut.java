/*
 * SysOut.java
 *
 * Created on Aug 5, 2007, 4:57:38 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.jvmm;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import org.joing.common.clientAPI.jvmm.JThreadGroup;

/**
 * Esta clase está relacionada con el codigo encargado de ejecutar las
 * aplicaciones. Es necesario reorganizar esto para poder separa
 * la parte de GUI.
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class SysOut {
    
    private PrintStream mSysOut;
    private PrintStream mSysErr;
    private OutputStream mOut;
    private OutputStream mErr;
    
    public SysOut() {
        mSysOut = System.out;
        mSysErr = System.err;
        
        mOut = new OutputStream() {
            public void write(int b) throws IOException {
                JThreadGroup tg = RuntimeFactory.getPlatform().getJThreadGroup();  // getJThreadGroup debe pasarse a Platform
                if (tg != null) {
                    tg.getOut().write(b);
                } else {
                    mSysOut.write(b);
                }
            }
            public void flush() throws IOException {
                JThreadGroup tg = RuntimeFactory.getPlatform().getJThreadGroup();
                if (tg != null) {
                    tg.getOut().flush();
                } else {
                    mSysOut.flush();
                }
            }
        };
        
        mErr = new OutputStream() {
            public void write(int b) throws IOException {
                JThreadGroup tg = RuntimeFactory.getPlatform().getJThreadGroup();
                if (tg != null) {
                    tg.getErr().write(b);
                } else {
                    mSysErr.write(b); // no deberia ser mSysOut igual que el objeto mOut?
                }
            }
            public void flush() throws IOException {
                JThreadGroup tg = RuntimeFactory.getPlatform().getJThreadGroup();
                if (tg != null) {
                    tg.getErr().flush();
                } else {
                    mSysErr.flush();
                }
            }
        };
        
        System.setOut(new PrintStream(mOut));
        System.setErr(new PrintStream(mErr));
    }
    
}
