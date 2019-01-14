package cn.chenmixuexi;

import static org.junit.Assert.assertTrue;

import cn.chenmixuexi.util.EmailUtils;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        EmailUtils.sendEmail("1483104508@qq.com","qweqweqwe");
    }
}
