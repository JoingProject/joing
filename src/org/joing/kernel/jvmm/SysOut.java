/*
 * Copyright (C) 2007, 2008 Join'g Team Members. All Rights Reserved.
 * Join'g Team Members are listed at project's home page. By the time of 
 * writting this at: https://joing.dev.java.net/servlets/ProjectMemberList.
 *
 * This file is part of Join'g project: www.joing.org
 *
 * GNU Classpath is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the free
 * Software Foundation; either version 3, or (at your option) any later version.
 * 
 * GNU Classpath is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * GNU Classpath; see the file COPYING.  If not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.joing.kernel.jvmm;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import org.joing.kernel.api.kernel.jvmm.JThreadGroup;

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
