package org.openrepose.commons.config.parser.jaxb;

import org.apache.commons.pool.PoolableObjectFactory;
import org.openrepose.commons.utils.pooling.ResourceConstructionException;
import org.junit.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Enclosed.class)
public class UnmarshallerBasePoolableObjectFactoryTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    public static class WhenUsingUnmarshallerConstructionStrategy {

        @Test(expected = ResourceConstructionException.class)
        public void testMakeObject() throws Exception {
            JAXBContext jaxbContext = mock(JAXBContext.class);
            when(jaxbContext.createUnmarshaller()).thenThrow(new JAXBException("mock jaxb exception"));
                        
            PoolableObjectFactory<UnmarshallerValidator> poolableObjectFactory = new UnmarshallerPoolableObjectFactory(jaxbContext);

            poolableObjectFactory.makeObject();
        }
    }
}
