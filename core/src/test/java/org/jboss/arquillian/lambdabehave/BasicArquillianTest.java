package org.jboss.arquillian.lambdabehave;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.junit.Assert.assertNotNull;

/**
 * @author Tommy Tynj&auml;
 */
@RunWith(Arquillian.class)
public class BasicArquillianTest {

    @Deployment
    public static Archive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClass(WeekService.class)
                .addAsResource(EmptyAsset.INSTANCE, "beans.xml")
                .setWebXML(new StringAsset("<web-app></web-app>"));
    }

    @EJB
    WeekService weekService;

    @Test
    public void shouldExecuteTestRun() {
        assertNotNull(weekService);
        assertNotNull(weekService.getWeekNumber());
        System.out.println(weekService.getWeekNumber());
    }
}
