/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2007  Christian Foltin <christianfoltin@users.sourceforge.net>
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
/*$Id: IFreeMindSplash.java,v 1.1.2.1 2007/02/03 23:19:26 christianfoltin Exp $*/

package freemind.main;

import javax.swing.*;

public interface IFreeMindSplash {

    public abstract FeedBack getFeedBack();

    public abstract void close();

    public abstract void setVisible(boolean pB);

    public abstract ImageIcon getWindowIcon();

}
// private static org.slf4j.Logger logger =
// freemind.main.Resources.getInstance().getLogger(IFreeMindSplash.class.getName());
