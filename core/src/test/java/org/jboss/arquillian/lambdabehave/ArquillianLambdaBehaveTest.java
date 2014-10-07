package org.jboss.arquillian.lambdabehave;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.Calendar;

import static com.insightfullogic.lambdabehave.Suite.describe;

/**
 * @author Tommy Tynj&auml;
 */
@RunWith(ArquillianLambdaBehave.class)
public class ArquillianLambdaBehaveTest {

    @Deployment
    public static Archive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
                .addClass(WeekService.class)
                .addAsResource(EmptyAsset.INSTANCE, "beans.xml")
                .setWebXML(new StringAsset("<web-app></web-app>"));
        war.writeTo(System.out, Formatters.VERBOSE);
        return war;
    }

    @EJB
    WeekService weekService;

    {
        describe("should always pass", it -> {
            it.should("return true", expect -> {
                expect.that(true).is(true);
            });
        });

        describe("should get week number", it -> {
            it.should("always return the current week number", expect -> {
                expect.that(weekService).isNotNull()
                        .and(weekService.getWeekNumber()).isNotNull()
                        .and(weekService.getWeekNumber()).isEqualTo(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
            });
        });
    }

}
