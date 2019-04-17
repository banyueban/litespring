package org.litespring.test.v5;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;
import org.litespring.aop.MethodMatcher;
import org.litespring.aop.aspectj.AspectJExpressionPointcut;
import org.litespring.service.v5.PetStoreService;

public class PointcutTest {

	@Test
	public void testPointcut() throws Exception {
		String expression = "execution(* org.litespring.service.v5.*.placeOrder(..))";
		AspectJExpressionPointcut pc = new AspectJExpressionPointcut();
		pc.setExpression(expression);
		
		MethodMatcher mm = pc.getMethodMatcher();
		{
			Class<?> target = PetStoreService.class;
			Method method1 = target.getMethod("placeOrder");
			assertTrue(mm.matches(method1));
			
			Method method2 = target.getMethod("getAccountDao");
			assertFalse(mm.matches(method2));
		}
		{
			Class<?> target = org.litespring.service.v4.PetStoreService.class;
			Method method1 = target.getMethod("getAccountDao");
			assertFalse(mm.matches(method1));
		}
	}
}
