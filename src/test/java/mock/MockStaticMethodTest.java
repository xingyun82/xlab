package mock;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UtilityInstance.class)
public class MockStaticMethodTest {

    @Test
    public void test() throws Exception {
        CallerInstance inst = new CallerInstance();
        mockStatic(UtilityInstance.class);
        when(UtilityInstance.hello()).thenThrow(Exception.class);
        Assert.assertEquals("bad",inst.hello());
    }
}
