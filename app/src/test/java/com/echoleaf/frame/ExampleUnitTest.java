package com.echoleaf.frame;

import com.echoleaf.frame.recyle.TrashMonitor;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        TestC testC = new TestC("1");
        getB(testC);
    }


    private static void getB(Object target) {
        Constructor<?>[] fields = target.getClass().getConstructors();
        for (Constructor field : fields) {
            field.setAccessible(true);
            Annotation annotation = field.getAnnotation(TrashMonitor.class);
            if (annotation instanceof TrashMonitor) {
                TrashMonitor trashMonitor = (TrashMonitor) annotation;
                System.out.print(trashMonitor);
            }
        }
    }


    static class TestC {
        @TrashMonitor
        String s;

        public TestC(String s) {
            this.s = s;
        }

        @TrashMonitor
        public TestC(String s, Integer d) {
            this.s = s;
        }

        public String getS() {
            return s;
        }
    }
}