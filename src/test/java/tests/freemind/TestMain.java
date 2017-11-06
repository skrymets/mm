/*
 * Copyright (C) 2017 skrymets
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package tests.freemind;

import freemind.main.FreeMindStarter;
import org.junit.Test;
import org.medal.clear.flow.agent.SequenceLogger;

/**
 *
 * @author skrymets
 */
public class TestMain {

    @Test
    public void testMain() {
        try {
            FreeMindStarter.main(new String[]{});
        } catch (Exception e) {
        } finally {
            System.out.println(SequenceLogger.getReport());
        }
    }
}
