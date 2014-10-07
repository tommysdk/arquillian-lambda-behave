package org.jboss.arquillian.lambdabehave;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.Calendar;

/**
 * @author Tommy Tynj&auml;
 */
@Stateless
@LocalBean
public class WeekService {

    public int getWeekNumber() {
        return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
    }
}
